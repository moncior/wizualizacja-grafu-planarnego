package view;

import model.Edge;
import model.Graph;
import model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GraphPanel extends JPanel {

    private Graph graph;
    private Node draggedNode = null;
    private boolean showLabels = true;
    private final int NODE_RADIUS = 15;

    public GraphPanel() {
        setBackground(Color.WHITE);

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (graph == null) return;

                for (Node node : graph.getNodes()) {
                    double distance = Math.hypot(e.getX() - node.x, e.getY() - node.y);
                    if (distance <= NODE_RADIUS) {
                        draggedNode = node;
                        break;
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedNode != null) {
                    draggedNode.x = e.getX();
                    draggedNode.y = e.getY();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedNode = null;
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        repaint();
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graph == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        for (Edge edge : graph.getEdges()) {
            Node source = graph.findNode(edge.fromId);
            Node target = graph.findNode(edge.toId);

            if (source != null && target != null) {
                g2d.drawLine((int) source.x, (int) source.y, (int) target.x, (int) target.y);
            }
        }

        int diameter = NODE_RADIUS * 2;
        for (Node node : graph.getNodes()) {
            int nx = (int) node.x;
            int ny = (int) node.y;

            g2d.setColor(new Color(100, 150, 255));
            g2d.fillOval(nx - NODE_RADIUS, ny - NODE_RADIUS, diameter, diameter);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawOval(nx - NODE_RADIUS, ny - NODE_RADIUS, diameter, diameter);

            if (showLabels) {
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.valueOf(node.id), nx - NODE_RADIUS, ny - NODE_RADIUS - 5);
            }
        }
    }
}