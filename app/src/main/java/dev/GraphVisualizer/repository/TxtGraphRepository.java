package dev.GraphVisualizer.repository;

import dev.GraphVisualizer.models.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Class TxtGraphRepository - implementation of GraphRepository for TXT files */
public final class TxtGraphRepository implements GraphRepository {
    /** Creates a text graph repository. */
    public TxtGraphRepository() {}

    /**
     * Saves graph to a TXT file as adjacency list
     * Format:
     * TYPE DirectedGraph
     * NODES 3
     * id label x y
     * ...
     * EDGES 2
     * sourceId targetId weight
     * ...
     * @param graph graph to save
     * @param file file handle
     */
    @Override
    public void save(Graph graph, File file) {
        StringBuilder sb = new StringBuilder();
        sb.append("TYPE ").append(graph.getClass().getSimpleName()).append("\n");
        sb.append("NODES ").append(graph.getNumberOfNodes()).append("\n");
        for (Node n : graph.getAllNodes()) {
            sb.append(n.getId()).append(" ")
              .append(n.getLabel()).append(" ")
              .append(n.getPositionX()).append(" ")
              .append(n.getPositionY()).append("\n");
        }
        sb.append("EDGES ").append(graph.getNumberOfEdges()).append("\n");
        for (Edge e : graph.getAllEdges()) {
            sb.append(e.getSource().getId()).append(" ")
              .append(e.getTarget().getId()).append(" ")
              .append(e.getWeight()).append("\n");
        }
        try {
            Files.writeString(file.toPath(), sb.toString());
        } catch(IOException e) {
            throw new GraphIOException("Failed to save graph to file: " + file.getPath(), e);
        }
    }

    /**
     * Loads graph from a TXT file
     * @param file file handle
     * @return loaded graph
     */
    @Override
    public Graph load(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.isEmpty()) {
                throw new GraphIOException("File is empty: " + file.getPath(), null);
            }

            if (lines.size() < 2) {
                throw GraphLoadValidator.invalid(file, "Missing NODES header.");
            }

            String[] typeHeader = splitLine(lines.get(0));
            GraphLoadValidator.requireParts(typeHeader, 2, file, 1);
            GraphLoadValidator.requireKeyword(typeHeader[0], "TYPE", file, 1);
            Graph graph = GraphLoadValidator.newGraph(typeHeader[1], file);

            String[] nodeHeader = splitLine(lines.get(1));
            GraphLoadValidator.requireParts(nodeHeader, 2, file, 2);
            GraphLoadValidator.requireKeyword(nodeHeader[0], "NODES", file, 2);
            int nodeCount = GraphLoadValidator.parseCount(nodeHeader[1], file, "node count");
            if (lines.size() < 3 + nodeCount) {
                throw GraphLoadValidator.invalid(file, "Missing EDGES header.");
            }

            Map<String, Node> nodeMap = new HashMap<>();
            for (int i = 2; i < 2 + nodeCount; ++i) {
                String[] parts = splitLine(lines.get(i));
                GraphLoadValidator.requireParts(parts, 4, file, i + 1);
                GraphLoadValidator.addNode(
                    graph,
                    nodeMap,
                    parts[0],
                    parts[1],
                    GraphLoadValidator.parseDouble(parts[2], file, "node x"),
                    GraphLoadValidator.parseDouble(parts[3], file, "node y"),
                    file
                );
            }

            String[] edgeHeader = splitLine(lines.get(2 + nodeCount));
            GraphLoadValidator.requireParts(edgeHeader, 2, file, 3 + nodeCount);
            GraphLoadValidator.requireKeyword(edgeHeader[0], "EDGES", file, 3 + nodeCount);
            int edgeStart = 2 + nodeCount + 1;
            int edgeCount = GraphLoadValidator.parseCount(edgeHeader[1], file, "edge count");
            if (lines.size() != edgeStart + edgeCount) {
                throw GraphLoadValidator.invalid(file, "Edge count does not match file contents.");
            }

            for (int i = edgeStart; i < edgeStart + edgeCount; ++i) {
                String[] parts = splitLine(lines.get(i));
                GraphLoadValidator.requireParts(parts, 3, file, i + 1);
                GraphLoadValidator.addEdge(
                    graph,
                    GraphLoadValidator.requireNode(nodeMap, parts[0], file, "source node id"),
                    GraphLoadValidator.requireNode(nodeMap, parts[1], file, "target node id"),
                    GraphLoadValidator.parseDouble(parts[2], file, "edge weight"),
                    file
                );
            }
            return graph;
        } catch(IOException e) {
            throw new GraphIOException("Failed to load graph from file: " + file.getPath(), e);
        }
    }

    private String[] splitLine(String line) {
        return line.trim().split("\\s+");
    }
}
