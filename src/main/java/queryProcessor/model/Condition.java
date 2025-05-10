package queryProcessor.model;

/**
 * Representa uma condição (predicado) de uma cláusula WHERE ou JOIN.
 * Exemplo: CLIENTE.ID = PEDIDO.CLIENTE_ID
 */
public class Condition {

    // Operando à esquerda do operador (ex: CLIENTE.ID)
    private String leftOperand;
    // Operador relacional (ex: =, >, <, >=, <=, <>)
    private String operator;
    // Operando à direita do operador (ex: PEDIDO.CLIENTE_ID ou valor)
    private String rightOperand;

    /**
     * Cria uma condição com os operandos e operador especificados.
     */
    public Condition(String leftOperand, String operator, String rightOperand) {
        this.leftOperand = leftOperand.trim();
        this.operator = operator.trim();
        this.rightOperand = rightOperand.trim();
    }

    /**
     * Retorna o operando à esquerda.
     */
    public String getLeftOperand() {
        return leftOperand;
    }

    /**
     * Retorna o operador relacional.
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Retorna o operando à direita.
     */
    public String getRightOperand() {
        return rightOperand;
    }

    /**
     * Retorna a condição como string (ex: CLIENTE.ID = PEDIDO.CLIENTE_ID).
     */
    @Override
    public String toString() {
        return leftOperand + " " + operator + " " + rightOperand;
    }
}
