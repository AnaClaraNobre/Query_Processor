package queryProcessor.model;

public class JoinClause {
    private String table;
    private String condition;

    public JoinClause(String table, String condition) {
        this.table = table;
        this.condition = condition;
    }

    public String getTable() {
        return table;
    }

    public String getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "JOIN " + table + " ON " + condition;
    }
}
