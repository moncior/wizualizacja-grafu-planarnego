package model;

public class Edge {
    public String edgeName;
    public int fromId;
    public int toId;
    public double weight;

    public Edge(String edgeName, int fromId, int toId, double weight) {
        this.edgeName = edgeName;
        this.fromId = fromId;
        this.toId = toId;
        this.weight = weight;
    }
}