package queryProcessor.validator;

import java.util.*;

public class MetadataRepository {

    private static final Map<String, Set<String>> TABLES = new HashMap<>();

    static {
        TABLES.put("CATEGORIA", Set.of("IDCATEGORIA", "DESCRICAO"));
        TABLES.put("PRODUTO", Set.of("IDPRODUTO", "NOME", "DESCRICAO", "PRECO", "QUANTESTOQUE", "CATEGORIA_IDCATEGORIA"));
        TABLES.put("TIPOCLIENTE", Set.of("IDTIPOCLIENTE", "DESCRICAO"));
        TABLES.put("CLIENTE", Set.of("IDCLIENTE", "NOME", "EMAIL", "NASCIMENTO", "SENHA", "TIPOCLIENTE_IDTIPOCLIENTE", "DATAREGISTRO"));
        TABLES.put("TIPOENDERECO", Set.of("IDTIPOENDERECO", "DESCRICAO"));
        TABLES.put("ENDERECO", Set.of("IDENDERECO", "ENDERECOPADRAO", "LOGRADOURO", "NUMERO", "COMPLEMENTO", "BAIRRO", "CIDADE", "UF", "CEP", "TIPOENDERECO_IDTIPOENDERECO", "CLIENTE_IDCLIENTE"));
        TABLES.put("TELEFONE", Set.of("NUMERO", "CLIENTE_IDCLIENTE"));
        TABLES.put("STATUS", Set.of("IDSTATUS", "DESCRICAO"));
        TABLES.put("PEDIDO", Set.of("IDPEDIDO", "STATUS_IDSTATUS", "DATAPEDIDO", "VALORTOTALPEDIDO", "CLIENTE_IDCLIENTE"));
        TABLES.put("PEDIDO_HAS_PRODUTO", Set.of("IDPEDIDOPRODUTO", "PEDIDO_IDPEDIDO", "PRODUTO_IDPRODUTO", "QUANTIDADE", "PRECOUNITARIO"));
    }

    public boolean tableExists(String tableName) {
        return TABLES.containsKey(tableName.toUpperCase());
    }

    public boolean columnExists(String tableName, String columnName) {
        Set<String> fields = TABLES.get(tableName.toUpperCase());
        return fields != null && fields.contains(columnName.toUpperCase());
    }

    public Map<String, Set<String>> getTables() {
        return Collections.unmodifiableMap(TABLES);
    }
}
