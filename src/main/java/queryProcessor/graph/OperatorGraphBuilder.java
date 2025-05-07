package queryProcessor.graph;

import queryProcessor.model.SQLQuery;
import queryProcessor.model.Condition;
import queryProcessor.model.JoinClause;

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
                if (i > 0) selection.append(" ∧ ");
                selection.append(query.getWhereConditions().get(i));
            }
            OperatorNode selectNode = new OperatorNode(selection.toString());
            selectNode.addChild(base);
            base = selectNode;
        }

        if (!query.getSelectFields().isEmpty()) {
            StringBuilder projection = new StringBuilder("π ");
            for (int i = 0; i < query.getSelectFields().size(); i++) {
                if (i > 0) projection.append(", ");
                projection.append(query.getSelectFields().get(i));
            }
            OperatorNode projNode = new OperatorNode(projection.toString());
            projNode.addChild(base);
            base = projNode;
        }

        return base;
    }
}
