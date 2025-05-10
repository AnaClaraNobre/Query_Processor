package queryProcessor.plan;

import queryProcessor.graph.OperatorNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsável por gerar o plano de execução (lista de passos) a partir da árvore de operadores.
 */
public class ExecutionPlanGenerator {

    /**
     * Gera uma lista de strings representando os passos do plano de execução,
     * percorrendo a árvore de operadores em pós-ordem.
     * @param root Raiz da árvore de operadores
     * @return Lista de passos do plano de execução
     */
    public List<String> generatePlan(OperatorNode root) {
        List<String> steps = new ArrayList<>();
        walk(root, steps);
        return steps;
    }

    /**
     * Método recursivo auxiliar que percorre a árvore em pós-ordem,
     * adicionando cada operação (projeção, seleção, join, scan) à lista de passos.
     * @param node Nó atual
     * @param steps Lista de passos acumulada
     */
    private void walk(OperatorNode node, List<String> steps) {
        for (OperatorNode child : node.getChildren()) {
            walk(child, steps);
        }

        String label = node.getLabel();

        if (label.startsWith("π")) {
            steps.add("PROJEÇÃO: " + label.substring(1).trim());
        } else if (label.startsWith("σ")) {
            steps.add("SELEÇÃO: " + label.substring(1).trim());
        } else if (label.startsWith("⨝")) {
            steps.add("JOIN " + label.substring(1).trim());
        } else {
            steps.add("Scan tabela " + label);
        }
    }
}
