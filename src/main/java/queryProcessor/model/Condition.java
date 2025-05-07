package queryProcessor.model;

public class Condition {

    private String leftOperand;
    private String operator;
    private String rightOperand;

    public Condition(String leftOperand, String operator, String rightOperand) {
        this.leftOperand = leftOperand.trim();
        this.operator = operator.trim();
        this.rightOperand = rightOperand.trim();
    }

    public String getLeftOperand() {
        return leftOperand;
    }

    public String getOperator() {
        return operator;
    }

    public String getRightOperand() {
        return rightOperand;
    }

    @Override
    public String toString() {
        return leftOperand + " " + operator + " " + rightOperand;
    }
}
