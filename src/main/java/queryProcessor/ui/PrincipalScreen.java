package queryProcessor.ui;

import javax.swing.*;
import java.awt.*;
import queryProcessor.parser.Parser;
import queryProcessor.model.SQLQuery;
import queryProcessor.validator.MetadataValidator;
import queryProcessor.algebra.RelationalAlgebraConverter;
import queryProcessor.graph.OperatorGraphBuilder;
import queryProcessor.graph.OperatorNode;
import queryProcessor.plan.ExecutionPlanGenerator;
import java.util.List;



public class PrincipalScreen extends JFrame {

    private JTextArea sqlInput;
    private JTextArea relationalAlgebraOutput;
    private JTextArea executionPlanOutput;
    private final Parser parser = new Parser();
    private final MetadataValidator validator = new MetadataValidator();
    private final RelationalAlgebraConverter algebraConverter = new RelationalAlgebraConverter();
    private GraphPanel graphPanel = new GraphPanel();
    private final OperatorGraphBuilder graphBuilder = new OperatorGraphBuilder();
    private final ExecutionPlanGenerator planGenerator = new ExecutionPlanGenerator();


    public PrincipalScreen() {
        setTitle("Processador de Consultas SQL");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout());
        sqlInput = new JTextArea(4, 50);
        sqlInput.setBorder(BorderFactory.createTitledBorder("Digite a consulta SQL:"));

        JButton processButton = new JButton("Processar Consulta");
        JButton clearButton = new JButton("Limpar");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(processButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 5))); 
        buttonPanel.add(clearButton);

        inputPanel.add(new JScrollPane(sqlInput), BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        
        processButton.addActionListener(e -> {
            String sql = sqlInput.getText();

            try {
                SQLQuery query = parser.parse(sql);
                validator.validate(query);

                String algebra = algebraConverter.convert(query);
                relationalAlgebraOutput.setText(algebra);

                executionPlanOutput.setText("🔧 Plano de execução ainda não implementado.");
                
                OperatorNode root = graphBuilder.build(query);
                graphPanel.setOperatorTree(root);

                List<String> plan = planGenerator.generatePlan(root);
                StringBuilder planText = new StringBuilder();
                for (int i = 0; i < plan.size(); i++) {
                    planText.append((i + 1)).append(". ").append(plan.get(i)).append("\n");
                }
                executionPlanOutput.setText(planText.toString());


                
            } catch (Exception ex) {
                relationalAlgebraOutput.setText("Erro: " + ex.getMessage());
                executionPlanOutput.setText("");
            }
        });
        
        clearButton.addActionListener(e -> {
            sqlInput.setText("");
            relationalAlgebraOutput.setText("");
            executionPlanOutput.setText("");
            graphPanel.setOperatorTree(null);  
        });


        JPanel outputPanel = new JPanel(new GridLayout(1, 2));
        relationalAlgebraOutput = new JTextArea();
        relationalAlgebraOutput.setEditable(false);
        relationalAlgebraOutput.setBorder(BorderFactory.createTitledBorder("Álgebra Relacional"));

        executionPlanOutput = new JTextArea();
        executionPlanOutput.setEditable(false);
        executionPlanOutput.setBorder(BorderFactory.createTitledBorder("Plano de Execução"));
        graphPanel = new GraphPanel();
        outputPanel.add(new JScrollPane(relationalAlgebraOutput));
        outputPanel.add(new JScrollPane(executionPlanOutput));

        JScrollPane scrollPane = new JScrollPane(graphPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Grafo de Operadores"));
        scrollPane.setPreferredSize(new Dimension(1000, 400));
        
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(outputPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrincipalScreen().setVisible(true));
    }
}
