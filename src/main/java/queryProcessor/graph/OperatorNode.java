package queryProcessor.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um nó na árvore de operadores (grafo relacional).
 * Cada nó pode ser uma operação (projeção, seleção, join) ou uma tabela base.
 */
public class OperatorNode {
    private String label;
    private List<OperatorNode> children = new ArrayList<>();

    /**
     * Cria um novo nó com o rótulo especificado.
     * @param label Rótulo do nó (ex: "σ condição", "⨝ condição", "π campos", "TABELA")
     */
    public OperatorNode(String label) {
        this.label = label;
    }

    /**
     * Adiciona um nó filho a este nó.
     * @param child Nó filho a ser adicionado
     */
    public void addChild(OperatorNode child) {
        children.add(child);
    }

    /**
     * Retorna o rótulo deste nó.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Retorna a lista de filhos deste nó.
     */
    public List<OperatorNode> getChildren() {
        return children;
    }

    /**
     * Imprime a árvore de operadores de forma hierárquica no console (útil para debug).
     * @param prefix Prefixo de indentação para visualização hierárquica
     */
    public void printTree(String prefix) {
        System.out.println(prefix + label);
        for (OperatorNode child : children) {
            child.printTree(prefix + "    ");
        }
    }
}
