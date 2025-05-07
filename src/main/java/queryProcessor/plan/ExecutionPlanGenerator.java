package queryProcessor.plan;

import queryProcessor.graph.OperatorNode;

import java.util.ArrayList;
import java.util.List;

public class ExecutionPlanGenerator {

    public List<String> generatePlan(OperatorNode root) {
        List<String> steps = new ArrayList<>();
        walk(root, steps);
        return steps;
    }

    private void walk(OperatorNode node, List<String> steps) {
        for (OperatorNode child : node.getChildren()) {
            walk(child, steps);
        }

        String label = node.getLabel();

        if (label.startsWith("π")) {
            steps.add("PROJEÇÃO: " + label.substring(1).trim());
        } else if (label.startsWith("σ")) {
            steps.add("SELEÇÃO: " + label.substring(1).trim());
        } else if (label.startsWith("⨝")) {
            steps.add("JOIN " + label.substring(1).trim());
        } else {
            steps.add("Scan tabela " + label);
        }
    }
}
