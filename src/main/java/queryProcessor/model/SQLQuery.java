package queryProcessor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma consulta SQL de forma estruturada, com campos, tabelas, joins e condições.
 */
public class SQLQuery {
    // Lista de campos selecionados (SELECT)
    private List<String> selectFields = new ArrayList<>();
    // Lista de tabelas principais (FROM)
    private List<String> fromTables = new ArrayList<>();
    // Lista de joins (JOIN ... ON ...)
    private List<JoinClause> joins = new ArrayList<>();
    // Lista de condições do WHERE
    private List<Condition> whereConditions = new ArrayList<>();
    
    /**
     * Retorna a lista de campos selecionados.
     */
    public List<String> getSelectFields() {
        return selectFields;
    }

    /**
     * Define a lista de campos selecionados.
     */
    public void setSelectFields(List<String> selectFields) {
        this.selectFields = selectFields;
    }

    /**
     * Retorna a lista de tabelas principais.
     */
    public List<String> getFromTables() {
        return fromTables;
    }

    /**
     * Define a lista de tabelas principais.
     */
    public void setFromTables(List<String> fromTables) {
        this.fromTables = fromTables;
    }

    /**
     * Retorna a lista de joins.
     */
    public List<JoinClause> getJoins() {
        return joins;
    }

    /**
     * Define a lista de joins.
     */
    public void setJoins(List<JoinClause> joins) {
        this.joins = joins;
    }

    /**
     * Retorna a lista de condições do WHERE.
     */
    public List<Condition> getWhereConditions() {
        return whereConditions;
    }

    /**
     * Define a lista de condições do WHERE.
     */
    public void setWhereConditions(List<Condition> whereConditions) {
        this.whereConditions = whereConditions;
    }
}
