package queryProcessor.ui;

import javax.swing.*;
import java.awt.*;
import queryProcessor.parser.Parser;
import queryProcessor.model.SQLQuery;
import queryProcessor.validator.MetadataValidator;
import queryProcessor.algebra.RelationalAlgebraConverter;
import queryProcessor.algebra.RelationalAlgebraOptimizer;
import queryProcessor.plan.ExecutionPlanGenerator;
import queryProcessor.graph.OperatorGraphBuilder;
import queryProcessor.graph.OperatorNode;
import java.util.List;



public class PrincipalScreen extends JFrame {

    private JTextArea sqlInput;
    private JTextArea relationalAlgebraOutput;
    private JTextArea relationalAlgebraOptimizedOutput;
    private JTextArea executionPlanOutput;
    private JTextArea executionPlanOptimizedOutput;
    private GraphPanel graphPanel;
    private GraphPanel graphOptimizedPanel;
    private final Parser parser = new Parser();
    private final MetadataValidator validator = new MetadataValidator();
    private final RelationalAlgebraConverter algebraConverter = new RelationalAlgebraConverter();
    private final RelationalAlgebraOptimizer algebraOptimizer = new RelationalAlgebraOptimizer();
    private final ExecutionPlanGenerator planGenerator = new ExecutionPlanGenerator();
    private final OperatorGraphBuilder graphBuilder = new OperatorGraphBuilder();

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
                // Otimização da álgebra relacional
                String algebraOptimized = algebraOptimizer.optimize(algebra);
                relationalAlgebraOptimizedOutput.setText(algebraOptimized);
                executionPlanOutput.setText("🔧 Plano de execução ainda não implementado.");
                executionPlanOptimizedOutput.setText("🔧 Plano de execução otimizado ainda não implementado.");
                OperatorNode root = graphBuilder.build(query);
                graphPanel.setOperatorTree(root);
                List<String> plan = planGenerator.generatePlan(root);
                StringBuilder planText = new StringBuilder();
                for (int i = 0; i < plan.size(); i++) {
                    planText.append((i + 1)).append(". ").append(plan.get(i)).append("\n");
                }
                executionPlanOutput.setText(planText.toString());
                // Gera o plano otimizado a partir da álgebra relacional otimizada
                OperatorNode optimizedAlgebraRoot = OperatorGraphBuilder.fromRelationalAlgebra(algebraOptimized);
                graphOptimizedPanel.setOperatorTree(optimizedAlgebraRoot);
                List<String> planOptimized = planGenerator.generatePlan(optimizedAlgebraRoot);
                StringBuilder planOptimizedText = new StringBuilder();
                for (int i = 0; i < planOptimized.size(); i++) {
                    planOptimizedText.append((i + 1)).append(". [OTIMIZADO] ").append(planOptimized.get(i)).append("\n");
                }
                executionPlanOptimizedOutput.setText(planOptimizedText.toString());
            } catch (Exception ex) {
                relationalAlgebraOutput.setText("Erro: " + ex.getMessage());
                relationalAlgebraOptimizedOutput.setText("");
                executionPlanOutput.setText("");
                executionPlanOptimizedOutput.setText("");
            }
        });
        clearButton.addActionListener(e -> {
            sqlInput.setText("");
            relationalAlgebraOutput.setText("");
            relationalAlgebraOptimizedOutput.setText("");
            executionPlanOutput.setText("");
            executionPlanOptimizedOutput.setText("");
            graphPanel.setOperatorTree(null);  
            graphOptimizedPanel.setOperatorTree(null);
        });

        // Painel de saída reorganizado
        JPanel outputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0.15;

        relationalAlgebraOutput = new JTextArea();
        relationalAlgebraOutput.setEditable(false);
        relationalAlgebraOutput.setBorder(BorderFactory.createTitledBorder("Álgebra Relacional"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        outputPanel.add(new JScrollPane(relationalAlgebraOutput), gbc);

        relationalAlgebraOptimizedOutput = new JTextArea();
        relationalAlgebraOptimizedOutput.setEditable(false);
        relationalAlgebraOptimizedOutput.setBorder(BorderFactory.createTitledBorder("Álgebra Relacional Otimizada"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        outputPanel.add(new JScrollPane(relationalAlgebraOptimizedOutput), gbc);

        executionPlanOutput = new JTextArea();
        executionPlanOutput.setEditable(false);
        executionPlanOutput.setBorder(BorderFactory.createTitledBorder("Plano de Execução"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        outputPanel.add(new JScrollPane(executionPlanOutput), gbc);

        executionPlanOptimizedOutput = new JTextArea();
        executionPlanOptimizedOutput.setEditable(false);
        executionPlanOptimizedOutput.setBorder(BorderFactory.createTitledBorder("Plano de Execução Otimizado"));
        gbc.gridx = 1;
        gbc.gridy = 2;
        outputPanel.add(new JScrollPane(executionPlanOptimizedOutput), gbc);

        graphPanel = new GraphPanel();
        graphOptimizedPanel = new GraphPanel();
        JScrollPane scrollPaneGraph = new JScrollPane(graphPanel);
        scrollPaneGraph.setBorder(BorderFactory.createTitledBorder("Grafo de Operadores"));
        JScrollPane scrollPaneGraphOptimized = new JScrollPane(graphOptimizedPanel);
        scrollPaneGraphOptimized.setBorder(BorderFactory.createTitledBorder("Grafo Otimizado"));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.55;
        outputPanel.add(scrollPaneGraph, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        outputPanel.add(scrollPaneGraphOptimized, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(outputPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrincipalScreen().setVisible(true));
    }
}
