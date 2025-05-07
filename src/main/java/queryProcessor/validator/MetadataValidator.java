package queryProcessor.validator;

import queryProcessor.model.SQLQuery;
import queryProcessor.model.Condition;
import queryProcessor.model.JoinClause;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        for (String field : query.getSelectFields()) {            if (!validateField(field)) {
                throw new IllegalArgumentException("Campo no SELECT inválido: " + field);
            }
        }

        for (Condition condition : query.getWhereConditions()) {
            if (!validateField(condition.getLeftOperand())) {
                throw new IllegalArgumentException("Campo na cláusula WHERE inválido: " + condition);
            }
        }
    }

    private String stripAlias(String input) {
        return input.trim().split("\\s+")[0];
    }

    private boolean validateField(String qualifiedField) {
        if (!qualifiedField.contains(".")) return false;

        String[] parts = qualifiedField.split("\\.");
        if (parts.length != 2) return false;

        String aliasOrTable = parts[0];
        String field = parts[1];

        for (String table : metadataRepository.getTables().keySet()) {
            if (metadataRepository.columnExists(table, field)) {
                return true;
            }
        }

        return false;
    }
}
