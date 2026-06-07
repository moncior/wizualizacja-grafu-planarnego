package view;

import model.Edge;
import model.Graph;
import model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class GraphPanel extends JPanel {

    private Graph graph;
    private Node draggedNode = null;
    private boolean showLabels = true;
    private final int NODE_RADIUS = 15;

    private double scale = 1.0;
    private int offsetX = 0;
    private int offsetY = 0;

    private int lastMouseX;
    private int lastMouseY;

    public GraphPanel() {
        setBackground(Color.WHITE);

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (graph == null) return;

                lastMouseX = e.getX();
                lastMouseY = e.getY();

                for (Node node : graph.getNodes()) {
                    double screenX = node.x * scale + offsetX;
                    double screenY = node.y * scale + offsetY;

                    double distance = Math.hypot(e.getX() - screenX, e.getY() - screenY);
                    if (distance <= NODE_RADIUS) {
                        draggedNode = node;
                        return;
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedNode != null) {
                    draggedNode.x = (e.getX() - offsetX) / scale;
                    draggedNode.y = (e.getY() - offsetY) / scale;
                } else {
                    offsetX += e.getX() - lastMouseX;
                    offsetY += e.getY() - lastMouseY;
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedNode = null;
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    scale *= 1.1;
                } else {
                    scale /= 1.1;
                }
                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
    }

    public void fitToScreen() {
        if (graph == null || graph.getNodes().isEmpty() || getWidth() == 0) return;

        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (Node n : graph.getNodes()) {
            if (n.x < minX) minX = n.x;
            if (n.x > maxX) maxX = n.x;
            if (n.y < minY) minY = n.y;
            if (n.y > maxY) maxY = n.y;
        }

        double graphWidth = maxX - minX;
        double graphHeight = maxY - minY;

        if (graphWidth == 0) graphWidth = 1;
        if (graphHeight == 0) graphHeight = 1;

        int padding = 50;
        double availWidth = getWidth() - 2 * padding;
        double availHeight = getHeight() - 2 * padding;

        scale = Math.min(availWidth / graphWidth, availHeight / graphHeight);

        offsetX = (int) ((getWidth() - (graphWidth * scale)) / 2 - (minX * scale));
        offsetY = (int) ((getHeight() - (graphHeight * scale)) / 2 - (minY * scale));

        repaint();
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
                int sx = (int) (source.x * scale) + offsetX;
                int sy = (int) (source.y * scale) + offsetY;
                int tx = (int) (target.x * scale) + offsetX;
                int ty = (int) (target.y * scale) + offsetY;
                g2d.drawLine(sx, sy, tx, ty);
            }
        }

        int diameter = NODE_RADIUS * 2;
        for (Node node : graph.getNodes()) {
            int nx = (int) (node.x * scale) + offsetX;
            int ny = (int) (node.y * scale) + offsetY;

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