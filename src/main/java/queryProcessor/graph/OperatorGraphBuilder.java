package queryProcessor.graph;

import queryProcessor.model.JoinClause;
import queryProcessor.model.SQLQuery;

public class OperatorGraphBuilder {

    /**
     * Constrói a árvore de operadores (grafo) a partir de um objeto SQLQuery.
     * Cria nós para joins, seleções e projeções, aninhando-os conforme a ordem lógica da consulta.
     */
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

    /**
     * Constrói a árvore de operadores a partir de uma string de álgebra relacional.
     * Identifica projeção, seleção, join e tabelas base de forma recursiva.
     */
    public static OperatorNode fromRelationalAlgebra(String algebra) {
        algebra = algebra.trim();
        if (algebra.startsWith("π ")) {
            int idx = findMainOperatorParen(algebra);
            String attrs = algebra.substring(2, idx).trim();
            String sub = algebra.substring(idx + 1, algebra.length() - 1).trim();
            OperatorNode proj = new OperatorNode("π " + attrs);
            proj.addChild(fromRelationalAlgebra(sub));
            return proj;
        }
        if (algebra.startsWith("σ ")) {
            int idx = findMainOperatorParen(algebra);
            String cond = algebra.substring(2, idx).trim();
            String sub = algebra.substring(idx + 1, algebra.length() - 1).trim();
            OperatorNode sel = new OperatorNode("σ " + cond);
            sel.addChild(fromRelationalAlgebra(sub));
            return sel;
        }
        if (algebra.startsWith("(")) {
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
        return new OperatorNode(algebra);
    }

    /**
     * Retorna o índice do primeiro parêntese após o operador principal (π ou σ).
     */
    private static int findMainOperatorParen(String algebra) {
        for (int i = 0; i < algebra.length(); i++) {
            if (algebra.charAt(i) == '(') {
                return i;
            }
        }
        return -1;
    }

    /**
     * Retorna o índice do operador de join (⨝) que está fora de parênteses.
     */
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
