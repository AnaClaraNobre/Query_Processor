package queryProcessor.graph;

import queryProcessor.model.JoinClause;
import queryProcessor.model.SQLQuery;

public class OperatorGraphBuilder {

    public OperatorNode build(SQLQuery query) {
        OperatorNode base = new OperatorNode(query.getFromTables().get(0));

        for (JoinClause join : query.getJoins()) {
            OperatorNode right = new OperatorNode(join.getTable());
            OperatorNode joinNode = new OperatorNode("⨝ " + join.getCondition());

            joinNode.addChild(base);
            joinNode.addChild(right);

            base = joinNode;
        }

        if (!query.getWhereConditions().isEmpty()) {
            StringBuilder selection = new StringBuilder("σ ");
            for (int i = 0; i < query.getWhereConditions().size(); i++) {
                if (i > 0)
                    selection.append(" ∧ ");
                selection.append(query.getWhereConditions().get(i));
            }
            OperatorNode selectNode = new OperatorNode(selection.toString());
            selectNode.addChild(base);
            base = selectNode;
        }

        if (!query.getSelectFields().isEmpty()) {
            StringBuilder projection = new StringBuilder("π ");
            for (int i = 0; i < query.getSelectFields().size(); i++) {
                if (i > 0)
                    projection.append(", ");
                projection.append(query.getSelectFields().get(i));
            }
            OperatorNode projNode = new OperatorNode(projection.toString());
            projNode.addChild(base);
            base = projNode;
        }

        return base;
    }

    // Parser recursivo robusto para álgebra relacional
    public static OperatorNode fromRelationalAlgebra(String algebra) {
        algebra = algebra.trim();
        // Projeção
        if (algebra.startsWith("π ")) {
            int idx = findMainOperatorParen(algebra);
            String attrs = algebra.substring(2, idx).trim();
            String sub = algebra.substring(idx + 1, algebra.length() - 1).trim();
            OperatorNode proj = new OperatorNode("π " + attrs);
            proj.addChild(fromRelationalAlgebra(sub));
            return proj;
        }
        // Seleção
        if (algebra.startsWith("σ ")) {
            int idx = findMainOperatorParen(algebra);
            String cond = algebra.substring(2, idx).trim();
            String sub = algebra.substring(idx + 1, algebra.length() - 1).trim();
            OperatorNode sel = new OperatorNode("σ " + cond);
            sel.addChild(fromRelationalAlgebra(sub));
            return sel;
        }
        // Join
        if (algebra.startsWith("(")) {
            // Remove parênteses externos
            algebra = algebra.substring(1, algebra.length() - 1).trim();
        }
        int joinIdx = findMainJoin(algebra);
        if (joinIdx != -1) {
            String left = algebra.substring(0, joinIdx).trim();
            String right = algebra.substring(joinIdx + 1).trim();
            OperatorNode join = new OperatorNode("⨝");
            join.addChild(fromRelationalAlgebra(left));
            join.addChild(fromRelationalAlgebra(right));
            return join;
        }
        // Tabela base
        return new OperatorNode(algebra);
    }

    // Encontra o índice do parêntese principal após o operador (π ou σ)
    private static int findMainOperatorParen(String algebra) {
        for (int i = 0; i < algebra.length(); i++) {
            if (algebra.charAt(i) == '(') {
                return i;
            }
        }
        return -1;
    }

    // Encontra o índice do join principal (⨝) fora de parênteses
    private static int findMainJoin(String algebra) {
        int depth = 0;
        for (int i = 0; i < algebra.length(); i++) {
            char c = algebra.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (c == '⨝' && depth == 0) return i;
        }
        return -1;
    }
}
