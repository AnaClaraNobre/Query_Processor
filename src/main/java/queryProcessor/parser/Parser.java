package queryProcessor.parser;

import queryProcessor.model.SQLQuery;
import queryProcessor.model.Condition;
import queryProcessor.model.JoinClause;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public SQLQuery parse(String sql) throws IllegalArgumentException {
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("Consulta SQL está vazia.");
        }

        SQLQuery query = new SQLQuery();

        try {
            String cleanedSql = sql.trim().replaceAll("\\s+", " ").toUpperCase();

            Pattern selectPattern = Pattern.compile("SELECT (.+?) FROM");
            Matcher selectMatcher = selectPattern.matcher(cleanedSql);
            if (selectMatcher.find()) {
                String[] fields = selectMatcher.group(1).split(",");
                for (String field : fields) {
                    query.getSelectFields().add(field.trim());
                }
            } else {
                throw new IllegalArgumentException("Cláusula SELECT inválida.");
            }

            Pattern fromPattern = Pattern.compile("FROM ([^\\s]+)");
            Matcher fromMatcher = fromPattern.matcher(cleanedSql);
            if (fromMatcher.find()) {
                query.getFromTables().add(fromMatcher.group(1).trim());
            } else {
                throw new IllegalArgumentException("Cláusula FROM inválida.");
            }

            Pattern joinPattern = Pattern.compile("JOIN\\s+(\\w+)(?:\\s+\\w+)?\\s+ON\\s+([\\w\\.]+\\s*=\\s*[\\w\\.]+)", Pattern.CASE_INSENSITIVE);
            Matcher joinMatcher = joinPattern.matcher(cleanedSql);
            while (joinMatcher.find()) {
                String table = joinMatcher.group(1).trim();
                String condition = joinMatcher.group(2).trim();
                query.getJoins().add(new JoinClause(table, condition));
            }

            Pattern wherePattern = Pattern.compile("WHERE (.+)");
            Matcher whereMatcher = wherePattern.matcher(cleanedSql);
            if (whereMatcher.find()) {
            	String[] conditions = whereMatcher.group(1).split("(?i)\\s+AND\\s+");
            	for (String cond : conditions) {
            	    Condition parsed = parseCondition(cond);
            	    query.getWhereConditions().add(parsed);
            	}
            }

            return query;

        } catch (Exception ex) {
            throw new IllegalArgumentException("Erro ao analisar a consulta SQL: " + ex.getMessage(), ex);
        }
    }
    private Condition parseCondition(String cond) {
        String[] operators = {"<=", ">=", "<>", "=", "<", ">"};

        for (String op : operators) {
            int idx = cond.indexOf(op);
            if (idx != -1) {
                String left = cond.substring(0, idx);
                String right = cond.substring(idx + op.length());
                return new Condition(left, op, right);
            }
        }

        throw new IllegalArgumentException("Operador não suportado em condição: " + cond);
    }

}
