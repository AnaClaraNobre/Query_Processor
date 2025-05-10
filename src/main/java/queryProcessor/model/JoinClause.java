package queryProcessor.model;

/**
 * Representa uma cláusula JOIN de uma consulta SQL.
 * Exemplo: JOIN PEDIDO ON CLIENTE.IDCLIENTE = PEDIDO.CLIENTE_IDCLIENTE
 */
public class JoinClause {
    // Nome da tabela a ser unida
    private String table;
    // Condição do join (ex: CLIENTE.IDCLIENTE = PEDIDO.CLIENTE_IDCLIENTE)
    private String condition;

    /**
     * Cria uma cláusula JOIN com a tabela e condição especificadas.
     */
    public JoinClause(String table, String condition) {
        this.table = table;
        this.condition = condition;
    }

    /**
     * Retorna o nome da tabela do join.
     */
    public String getTable() {
        return table;
    }

    /**
     * Retorna a condição do join.
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Retorna a cláusula JOIN como string.
     */
    @Override
    public String toString() {
        return "JOIN " + table + " ON " + condition;
    }
}
