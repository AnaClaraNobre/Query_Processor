package queryProcessor.test;

import queryProcessor.model.SQLQuery;
import queryProcessor.parser.Parser;
import queryProcessor.validator.MetadataValidator;
import queryProcessor.algebra.RelationalAlgebraConverter;
import queryProcessor.model.Condition;
import queryProcessor.model.JoinClause;

public class ParserTest {

    public static void main(String[] args) {
        Parser parser = new Parser();
        MetadataValidator validator = new MetadataValidator();
        RelationalAlgebraConverter converter = new RelationalAlgebraConverter();


        String sql = "SELECT c.Nome, p.DataPedido, pr.Nome FROM Cliente c " +
                "JOIN Pedido p ON c.idCliente = p.Cliente_idCliente " +
                "JOIN Pedido_has_Produto pp ON p.idPedido = pp.Pedido_idPedido " +
                "JOIN Produto pr ON pp.Produto_idProduto = pr.idProduto " +
                "WHERE c.Nome = 'Maria' AND p.ValorTotalPedido > 100";
        
        // Exemplo 2: Consulta com erro (campo errado)
//         String sql = "SELECT c.Nome, p.DataErrada, pr.Nome FROM Cliente c " +
//                      "JOIN Pedido p ON c.idCliente = p.Cliente_idCliente " +
//                      "JOIN Pedido_has_Produto pp ON p.idPedido = pp.Pedido_idPedido " +
//                      "JOIN Produto pr ON pp.Produto_idProduto = pr.idProduto " +
//                      "WHERE c.Nome = 'Maria' AND p.ValorTotalPedido > 100";

        try {
            SQLQuery query = parser.parse(sql);

            System.out.println("Campos selecionados:");
            for (String field : query.getSelectFields()) {
                System.out.println("  - " + field);
            }

            System.out.println("\nTabelas principais:");
            for (String table : query.getFromTables()) {
                System.out.println("  - " + table);
            }

            System.out.println("\nJOINs:");
            for (JoinClause join : query.getJoins()) {
                System.out.println("  - " + join);
            }

            System.out.println("\nCondições WHERE:");
            for (Condition cond : query.getWhereConditions()) {
                System.out.println("  - " + cond);
            }
            
            validator.validate(query);
            System.out.println("\nConsulta válida segundo os metadados.");
            
            validator.validate(query);

            System.out.println("Consulta válida. Gerando álgebra relacional...\n");

            String algebra = converter.convert(query);
            System.out.println("Álgebra Relacional:");
            System.out.println(algebra);


        } catch (Exception e) {
            System.err.println("Erro ao analisar SQL: " + e.getMessage());
        }
    }
}
