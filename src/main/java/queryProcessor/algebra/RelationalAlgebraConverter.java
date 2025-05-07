package queryProcessor.algebra;

import queryProcessor.model.SQLQuery;
import queryProcessor.model.Condition;
import queryProcessor.model.JoinClause;

import java.util.List;
import java.util.StringJoiner;

public class RelationalAlgebraConverter {

    public String convert(SQLQuery query) {
        StringBuilder algebra = new StringBuilder();

        // 1. Seleção (σ)
        String selection = convertSelection(query.getWhereConditions());

        // 2. Junções (⨝)
        String joins = convertJoins(query);

        // 3. Projeção (π)
        String projection = convertProjection(query.getSelectFields());

        // Final: π atributos ( σ condição ( JOINs ) )
        if (!selection.isEmpty()) {
            algebra.append(projection)
                   .append("( ")
                   .append("σ ").append(selection)
                   .append(" ( ")
                   .append(joins)
                   .append(" )")
                   .append(" )");
        } else {
            algebra.append(projection)
                   .append("( ")
                   .append(joins)
                   .append(" )");
        }

        return algebra.toString();
    }

    private String convertProjection(List<String> fields) {
        StringJoiner joiner = new StringJoiner(", ");
        for (String f : fields) {
            joiner.add(f);
        }
        return "π " + joiner.toString() + " ";
    }

    private String convertSelection(List<Condition> conditions) {
        if (conditions.isEmpty()) return "";

        StringJoiner joiner = new StringJoiner(" ∧ ");
        for (Condition c : conditions) {
            joiner.add(c.toString());
        }
        return joiner.toString();
    }

    private String convertJoins(SQLQuery query) {
        List<String> tables = query.getFromTables();
        List<JoinClause> joins = query.getJoins();

        StringBuilder result = new StringBuilder();
        result.append(tables.get(0)); 

        for (JoinClause join : joins) {
            result.insert(0, "(");
            result.append(" ⨝ ").append(join.getCondition()).append(" ").append(join.getTable()).append(")");
        }

        return result.toString();
    }
}
