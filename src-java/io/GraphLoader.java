package io;

import model.Graph;
import model.Edge;
import model.Node;
import java.io.*;
import java.io.EOFException;
import java.io.BufferedInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GraphLoader {

    public void loadEdges(String path, Graph graph) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split("\\s+");
                if (tokens.length >= 4) {
                    String name = tokens[0];
                    int fromId = Integer.parseInt(tokens[1]);
                    int toId = Integer.parseInt(tokens[2]);
                    double weight = Double.parseDouble(tokens[3]);

                    graph.addNode(new Node(fromId, 0.0, 0.0));
                    graph.addNode(new Node(toId, 0.0, 0.0));
                    graph.addEdge(new Edge(name, fromId, toId, weight));
                }
            }
        }
    }

    public void loadCoordinates(String path, Graph graph) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split("\\s+");
                if (tokens.length >= 3) {
                    int id = Integer.parseInt(tokens[0]);
                    double x = Double.parseDouble(tokens[1]);
                    double y = Double.parseDouble(tokens[2]);

                    graph.updateNodePos(id, x, y);
                }
            }
        }
    }

    public void loadCoordinatesBinary(String path, Graph graph) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] buffer = new byte[20];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) == 20) {
                ByteBuffer bb = ByteBuffer.wrap(buffer);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                int id   = bb.getInt();
                double x = bb.getDouble();
                double y = bb.getDouble();
                graph.updateNodePos(id, x, y);
            }
        }
    }


}