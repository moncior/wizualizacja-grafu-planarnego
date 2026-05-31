package io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Graph;
import model.Node;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GraphLoaderTest {
    private GraphLoader loader;
    private Graph graph;

    @BeforeEach
    void setup() {
        loader = new GraphLoader();
        graph = new Graph();
    }

    @Test
    void shouldLoadValidDataFromTextAndBinaryFiles() throws IOException {
        Path txtEdges = Files.createTempFile("edges", ".txt");
        Files.writeString(txtEdges, "   AB     1      2     4.5   \n");

        loader.loadEdges(txtEdges.toAbsolutePath().toString(), graph);

        assertEquals(2, graph.getNodes().size());
        assertEquals(1, graph.getEdges().size());

        Path txtCoords = Files.createTempFile("coords", ".txt");
        Files.writeString(txtCoords, "1 56.418958 -12.345678");

        loader.loadCoordinates(txtCoords.toAbsolutePath().toString(), graph);
        assertEquals(56.418958, graph.findNode(1).x, 0.000001);

        Path binCoords = Files.createTempFile("coords", ".bin");


        try (FileOutputStream fos = new FileOutputStream(binCoords.toFile())) {
        }

        loader.loadCoordinatesBinary(binCoords.toAbsolutePath().toString(), graph);

        assertNotNull(graph.findNode(4));
        assertEquals(12.5, graph.findNode(4).x, 0.000001);
        Files.deleteIfExists(txtEdges);
        Files.deleteIfExists(txtCoords);
        Files.deleteIfExists(binCoords);
    }

    @Test
    void shouldThrowExceptionWhenFileDoesNotExist() {
        String invalidPath = "nieistniejacy_plik.txt";

        assertThrows(IOException.class, () -> {
            loader.loadEdges(invalidPath, graph);
        });
    }

    @Test
    void shouldThrowNumberFormatExceptionForCorruptedData() throws IOException {
        Path tempFile = Files.createTempFile("corrupted", ".txt");
        Files.writeString(tempFile, "AB BLAD 2 4.5");

        assertThrows(NumberFormatException.class, () -> {
            loader.loadEdges(tempFile.toAbsolutePath().toString(), graph);
        });

        Files.deleteIfExists(tempFile);
    }
}