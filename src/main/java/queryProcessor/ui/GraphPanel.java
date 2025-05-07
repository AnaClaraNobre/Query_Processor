package queryProcessor.ui;

import queryProcessor.graph.OperatorNode;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphPanel extends JPanel {

    private OperatorNode root;

    public void setOperatorTree(OperatorNode root) {
        this.root = root;
        repaint(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (root == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawNode(g2, root, getWidth() / 2, 40, getWidth() / 4);
    }

    private void drawNode(Graphics2D g2, OperatorNode node, int x, int y, int offsetX) {
        int boxWidth = 120;
        int boxHeight = 30;

        int boxX = x - boxWidth / 2;
        int boxY = y;

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(boxX, boxY, boxWidth, boxHeight);
        g2.setColor(Color.BLACK);
        g2.drawRect(boxX, boxY, boxWidth, boxHeight);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(node.getLabel());
        g2.drawString(node.getLabel(), x - textWidth / 2, y + 20);

        List<OperatorNode> children = node.getChildren();
        if (children.isEmpty()) return;

        int spacing = Math.max(180, offsetX); 
        int totalWidth = spacing * (children.size() - 1);

        int startX = x - totalWidth / 2;
        int childY = y + 80;

        for (int i = 0; i < children.size(); i++) {
            int childX = startX + i * spacing;
            drawNode(g2, children.get(i), childX, childY, spacing / 2);

            g2.drawLine(x, y + boxHeight, childX, childY);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(2000, 1000);
    }


}
