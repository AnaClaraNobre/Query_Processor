package queryProcessor.graph;

import java.util.ArrayList;
import java.util.List;

public class OperatorNode {
    private String label;
    private List<OperatorNode> children = new ArrayList<>();

    public OperatorNode(String label) {
        this.label = label;
    }

    public void addChild(OperatorNode child) {
        children.add(child);
    }

    public String getLabel() {
        return label;
    }

    public List<OperatorNode> getChildren() {
        return children;
    }
}
