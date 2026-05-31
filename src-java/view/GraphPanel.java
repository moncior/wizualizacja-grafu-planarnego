package view;

import model.Edge;
import model.Graph;
import model.Node;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.geom.Rectangle2D;

public class GraphPanel extends JPanel {
    private Graph graph;
    private Node draggedNode = null;
    private double scale = 150.0;
    private boolean showLabels = true;

    public void setGraph(Graph graph) {
        this.graph = graph;
        repaint();
    }

    public void setScale(double scale) {
        this.scale = scale;
        repaint();
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
        repaint();
    }

    private Point getOffsets() {
        if (graph == null || graph.getNodes().isEmpty()) {
            return new Point(getWidth() / 2, getHeight() / 2);
        }
        double sumX = 0, sumY = 0;
        for (Node n : graph.getNodes()) {
            sumX += n.x;
            sumY += n.y;
        }
        double avgX = sumX / graph.getNodes().size();
        double avgY = sumY / graph.getNodes().size();

        int offsetX = getWidth() / 2 - (int) (avgX * scale);
        int offsetY = getHeight() / 2 - (int) (avgY * scale);
        return new Point(offsetX, offsetY);
    }

    public GraphPanel() {
        setBackground(Color.WHITE);

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (graph == null) return;
                Point offsets = getOffsets();

                for (Node node : graph.getNodes()) {
                    int sx = (int) (node.x * scale) + offsets.x;
                    int sy = (int) (node.y * scale) + offsets.y;

                    double distance = Math.hypot(e.getX() - sx, e.getY() - sy);
                    if (distance <= 20) {
                        draggedNode = node;
                        break;
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedNode != null) {
                    Point offsets = getOffsets();
                    draggedNode.x = (e.getX() - offsets.x) / scale;
                    draggedNode.y = (e.getY() - offsets.y) / scale;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graph == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Point offsets = getOffsets();

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        for (Edge edge : graph.getEdges()) {
            Node fromNode = graph.findNode(edge.fromId);
            Node toNode = graph.findNode(edge.toId);
            if (fromNode != null && toNode != null) {
                int x1 = (int) (fromNode.x * scale) + offsets.x;
                int y1 = (int) (fromNode.y * scale) + offsets.y;
                int x2 = (int) (toNode.x * scale) + offsets.x;
                int y2 = (int) (toNode.y * scale) + offsets.y;
                g2d.drawLine(x1, y1, x2, y2);
            }
        }

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        for (Edge edge : graph.getEdges()) {
            Node fromNode = graph.findNode(edge.fromId);
            Node toNode = graph.findNode(edge.toId);
            if (fromNode != null && toNode != null && edge.edgeName != null && !edge.edgeName.isEmpty()) {
                int x1 = (int) (fromNode.x * scale) + offsets.x;
                int y1 = (int) (fromNode.y * scale) + offsets.y;
                int x2 = (int) (toNode.x * scale) + offsets.x;
                int y2 = (int) (toNode.y * scale) + offsets.y;

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

        for (Node node : graph.getNodes()) {
            int sx = (int) (node.x * scale) + offsets.x;
            int sy = (int) (node.y * scale) + offsets.y;

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
    }


    public void saveToPNG(String path) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        paintComponent(g2d);
        g2d.dispose();

        ImageIO.write(image, "png", new File(path));
    }
}