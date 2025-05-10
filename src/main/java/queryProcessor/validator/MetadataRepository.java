package queryProcessor.validator;

import java.util.*;

/**
 * Repositório de metadados simulado para tabelas e colunas do banco de dados.
 * Usado para validação de existência de tabelas e colunas.
 */
public class MetadataRepository {

    // Mapeamento de nomes de tabelas para conjuntos de nomes de colunas
    private static final Map<String, Set<String>> TABLES = new HashMap<>();

    // Inicializa o repositório com tabelas e colunas conhecidas
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

    /**
     * Verifica se uma tabela existe no repositório.
     * @param tableName Nome da tabela
     * @return true se existe, false caso contrário
     */
    public boolean tableExists(String tableName) {
        return TABLES.containsKey(tableName.toUpperCase());
    }

    /**
     * Verifica se uma coluna existe em uma tabela no repositório.
     * @param tableName Nome da tabela
     * @param columnName Nome da coluna
     * @return true se existe, false caso contrário
     */
    public boolean columnExists(String tableName, String columnName) {
        Set<String> fields = TABLES.get(tableName.toUpperCase());
        return fields != null && fields.contains(columnName.toUpperCase());
    }

    /**
     * Retorna um mapa imutável de todas as tabelas e colunas.
     */
    public Map<String, Set<String>> getTables() {
        return Collections.unmodifiableMap(TABLES);
    }
}
