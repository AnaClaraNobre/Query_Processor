package queryProcessor.model;

import java.util.ArrayList;
import java.util.List;

public class SQLQuery {
    private List<String> selectFields = new ArrayList<>();
    private List<String> fromTables = new ArrayList<>();
    private List<JoinClause> joins = new ArrayList<>();
    private List<Condition> whereConditions = new ArrayList<>();
    

    public List<String> getSelectFields() {
        return selectFields;
    }

    public void setSelectFields(List<String> selectFields) {
        this.selectFields = selectFields;
    }

    public List<String> getFromTables() {
        return fromTables;
    }

    public void setFromTables(List<String> fromTables) {
        this.fromTables = fromTables;
    }

    public List<JoinClause> getJoins() {
        return joins;
    }

    public void setJoins(List<JoinClause> joins) {
        this.joins = joins;
    }

    public List<Condition> getWhereConditions() {
        return whereConditions;
    }

    public void setWhereConditions(List<Condition> whereConditions) {
        this.whereConditions = whereConditions;
    }
}
