package queryProcessor.parser;

import queryProcessor.model.SQLQuery;
import queryProcessor.model.Condition;
import queryProcessor.model.JoinClause;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Responsável por fazer o parsing de uma string SQL simples para um objeto SQLQuery estruturado.
 */
public class Parser {

    /**
     * Faz o parsing de uma string SQL e retorna um objeto SQLQuery estruturado.
     * Suporta SELECT, FROM, JOIN e WHERE simples.
     * @param sql Consulta SQL em string
     * @return Objeto SQLQuery representando a consulta
     * @throws IllegalArgumentException se a consulta for inválida
     */
    public SQLQuery parse(String sql) throws IllegalArgumentException {
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("Consulta SQL está vazia.");
        }

        SQLQuery query = new SQLQuery();

        try {
            String cleanedSql = sql.trim().replaceAll("\\s+", " ").toUpperCase();

            // Extrai campos do SELECT
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

            // Extrai tabela principal do FROM
            Pattern fromPattern = Pattern.compile("FROM ([^\\s]+)");
            Matcher fromMatcher = fromPattern.matcher(cleanedSql);
            if (fromMatcher.find()) {
                query.getFromTables().add(fromMatcher.group(1).trim());
            } else {
                throw new IllegalArgumentException("Cláusula FROM inválida.");
            }

            // Extrai JOINs
            Pattern joinPattern = Pattern.compile("JOIN\\s+(\\w+)(?:\\s+\\w+)?\\s+ON\\s+([\\w\\.]+\\s*=\\s*[\\w\\.]+)", Pattern.CASE_INSENSITIVE);
            Matcher joinMatcher = joinPattern.matcher(cleanedSql);
            while (joinMatcher.find()) {
                String table = joinMatcher.group(1).trim();
                String condition = joinMatcher.group(2).trim();
                query.getJoins().add(new JoinClause(table, condition));
            }

            // Extrai condições do WHERE
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

    /**
     * Faz o parsing de uma condição simples (ex: A = B) para um objeto Condition.
     * @param cond String da condição
     * @return Condition estruturado
     */
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
