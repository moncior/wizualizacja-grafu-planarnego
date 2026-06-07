package view;

import model.Edge;
import model.Graph;
import model.Node;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.geom.Rectangle2D;

public class GraphPanel extends JPanel {
    private Graph graph;
    private Node draggedNode = null;

    private double scale = 1.0;
    private double offsetX = 0.0;
    private double offsetY = 0.0;

    private Point lastMousePosition;
    private boolean showLabels = true;
    private boolean isPanning = false;

    public void setGraph(Graph graph) {
        this.graph = graph;
        resetView();
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
        repaint();
    }

    public void resetView() {
        if (graph == null || graph.getNodes().isEmpty()) {
            offsetX = getWidth() / 2.0;
            offsetY = getHeight() / 2.0;
            scale = 1.0;
            repaint();
            return;
        }

        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;

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

        int panelWidth = getWidth() > 0 ? getWidth() : 800;
        int panelHeight = getHeight() > 0 ? getHeight() : 600;

        double scaleX = (panelWidth * 0.75) / graphWidth;
        double scaleY = (panelHeight * 0.75) / graphHeight;
        this.scale = Math.min(scaleX, scaleY);

        if (this.scale <= 0) this.scale = 1.0;

        double avgX = (minX + maxX) / 2.0;
        double avgY = (minY + maxY) / 2.0;

        offsetX = panelWidth / 2.0 - (avgX * scale);
        offsetY = panelHeight / 2.0 - (avgY * scale);

        repaint();
    }

    public GraphPanel() {
        setBackground(Color.WHITE);

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (graph == null) return;
                lastMousePosition = e.getPoint();
                draggedNode = null;
                isPanning = false;

                for (Node node : graph.getNodes()) {
                    int sx = (int) (node.x * scale + offsetX);
                    int sy = (int) (node.y * scale + offsetY);

                    double distance = Math.hypot(e.getX() - sx, e.getY() - sy);
                    if (distance <= 20) {
                        draggedNode = node;
                        break;
                    }
                }

                if (draggedNode == null) {
                    isPanning = true;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (graph == null || lastMousePosition == null) return;

                if (draggedNode != null) {
                    draggedNode.x = (e.getX() - offsetX) / scale;
                    draggedNode.y = (e.getY() - offsetY) / scale;
                } else if (isPanning) {
                    int dx = e.getX() - lastMousePosition.x;
                    int dy = e.getY() - lastMousePosition.y;
                    offsetX += dx;
                    offsetY += dy;
                    lastMousePosition = e.getPoint();
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedNode = null;
                isPanning = false;
                lastMousePosition = null;
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (graph == null) return;

                double zoomFactor = (e.getWheelRotation() < 0) ? 1.1 : 1.0 / 1.1;

                double mouseX = e.getX();
                double mouseY = e.getY();

                offsetX = mouseX - (mouseX - offsetX) * zoomFactor;
                offsetY = mouseY - (mouseY - offsetY) * zoomFactor;
                scale *= zoomFactor;

                if (scale < 0.00001) scale = 0.00001;
                if (scale > 1000.0) scale = 1000.0;

                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graph == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        for (Edge edge : graph.getEdges()) {
            Node fromNode = graph.findNode(edge.fromId);
            Node toNode = graph.findNode(edge.toId);
            if (fromNode != null && toNode != null) {
                int x1 = (int) (fromNode.x * scale + offsetX);
                int y1 = (int) (fromNode.y * scale + offsetY);
                int x2 = (int) (toNode.x * scale + offsetX);
                int y2 = (int) (toNode.y * scale + offsetY);
                g2d.drawLine(x1, y1, x2, y2);
            }
        }

        for (Node node : graph.getNodes()) {
            int sx = (int) (node.x * scale + offsetX);
            int sy = (int) (node.y * scale + offsetY);

            g2d.setColor(new Color(173, 216, 230));
            g2d.fillOval(sx - 18, sy - 18, 36, 36);

            g2d.setColor(new Color(140, 180, 200));
            g2d.drawOval(sx - 18, sy - 18, 36, 36);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            String idStr = String.valueOf(node.id);
            FontMetrics fm = g2d.getFontMetrics();
            int idWidth = fm.stringWidth(idStr);
            g2d.drawString(idStr, sx - idWidth / 2, sy + (fm.getAscent() / 2) - 2);

            if (showLabels) {
                g2d.setColor(Color.GRAY);
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                String coordsStr = String.format("(%.2f, %.2f)", node.x, node.y);
                int coordsWidth = g2d.getFontMetrics().stringWidth(coordsStr);
                g2d.drawString(coordsStr, sx - coordsWidth / 2, sy - 22);
            }
        }

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        for (Edge edge : graph.getEdges()) {
            Node fromNode = graph.findNode(edge.fromId);
            Node toNode = graph.findNode(edge.toId);
            if (fromNode != null && toNode != null && edge.edgeName != null && !edge.edgeName.isEmpty()) {
                int x1 = (int) (fromNode.x * scale + offsetX);
                int y1 = (int) (fromNode.y * scale + offsetY);
                int x2 = (int) (toNode.x * scale + offsetX);
                int y2 = (int) (toNode.y * scale + offsetY);

                int midX = (x1 + x2) / 2;
                int midY = (y1 + y2) / 2;

                FontMetrics fm = g2d.getFontMetrics();
                Rectangle2D rect = fm.getStringBounds(edge.edgeName, g2d);
                int textWidth = (int) rect.getWidth();
                int textHeight = (int) rect.getHeight();

                g2d.setColor(Color.WHITE);
                g2d.fillRect(midX - textWidth / 2 - 4, midY - textHeight / 2, textWidth + 8, textHeight);

                g2d.setColor(Color.BLACK);
                g2d.drawString(edge.edgeName, midX - textWidth / 2, midY + textHeight / 4);
            }
        }
    }

    public void saveToPNG(String path) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        paintComponent(g2d);
        g2d.dispose();
        ImageIO.write(image, "png", new File(path));
    }
}