package model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();


    public Node findNode(int id) {
        for (Node n : nodes) {
            if (n.id == id) {
                return n;
            }
        }
        return null;
    }

    public void addNode(Node n) {
        if (findNode(n.id) == null) {
            nodes.add(n);
        }
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public void updateNodePos(int id, double x, double y) {
        Node n = findNode(id);
        if (n != null) {
            n.x = x;
            n.y = y;
        } else {
            addNode(new Node(id, x, y));
        }
    }

    public List<Node> getNodes() { return nodes; }
    public List<Edge> getEdges() { return edges; }




}
