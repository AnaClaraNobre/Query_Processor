package queryProcessor.validator;

import queryProcessor.model.SQLQuery;
import queryProcessor.model.Condition;
import queryProcessor.model.JoinClause;

import java.util.ArrayList;
import java.util.List;

public class MetadataValidator {

    private final MetadataRepository metadataRepository = new MetadataRepository();

    public void validate(SQLQuery query) throws IllegalArgumentException {
        for (String table : query.getFromTables()) {
            if (!metadataRepository.tableExists(stripAlias(table))) {
                throw new IllegalArgumentException("Tabela não existe: " + table);
            }
        }

        for (JoinClause join : query.getJoins()) {
            if (!metadataRepository.tableExists(stripAlias(join.getTable()))) {
                throw new IllegalArgumentException("Tabela do JOIN não existe: " + join.getTable());
            }
        }

        for (String field : query.getSelectFields()) {
            if (!validateField(field, query)) {
                throw new IllegalArgumentException("Campo no SELECT inválido: " + field);
            }
        }

        for (Condition condition : query.getWhereConditions()) {
            if (!validateField(condition.getLeftOperand(), query)) {
                throw new IllegalArgumentException("Campo na cláusula WHERE inválido: " + condition);
            }
        }
    }

    private String stripAlias(String input) {
        return input.trim().split("\\s+")[0];
    }

    private boolean validateField(String qualifiedField, SQLQuery query) {

        if (qualifiedField.contains(".")) {

            String[] parts = qualifiedField.split("\\.");

            if (parts.length != 2)
                return false;

            String field = parts[1];

            List<String> allTables = new ArrayList<>();

            allTables.addAll(query.getFromTables());

            for (JoinClause join : query.getJoins()) {
                allTables.add(join.getTable());
            }

            for (String table : allTables) {
                if (metadataRepository.columnExists(stripAlias(table), field)) {
                    return true;
                }
            }
            return false;
        }

        String field = qualifiedField;

        List<String> allTables = new ArrayList<>();

        allTables.addAll(query.getFromTables());

        for (JoinClause join : query.getJoins()) {
            allTables.add(join.getTable());
        }

        int foundIn = 0;

        for (String table : allTables) {
            if (metadataRepository.columnExists(stripAlias(table), field)) {
                foundIn++;
            }
        }
        return foundIn == 1;
    }
}