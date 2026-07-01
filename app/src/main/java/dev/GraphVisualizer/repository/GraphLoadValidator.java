package dev.GraphVisualizer.repository;

import dev.GraphVisualizer.models.*;

import java.io.File;
import java.util.List;
import java.util.Map;

/** Shared validation helpers for graph file loading. */
final class GraphLoadValidator {
    private GraphLoadValidator() {}

    static Graph newGraph(String type, File file) {
        type = requireText(type, file, "graph type");
        return switch(type) {
            case "DirectedGraph" -> new DirectedGraph();
            case "UndirectedGraph" -> new UndirectedGraph();
            case "WeightedDirectedGraph" -> new WeightedDirectedGraph();
            case "WeightedUndirectedGraph" -> new WeightedUndirectedGraph();
            default -> throw invalid(file, "Unsupported graph type: " + type);
        };
    }

    static <T> List<T> requireList(List<T> list, File file, String field) {
        if (list == null) {
            throw invalid(file, "Missing " + field + " list.");
        }
        return list;
    }

    static void requireParts(String[] parts, int expected, File file, int lineNumber) {
        if (parts.length != expected) {
            throw invalid(file, "Line " + lineNumber + " should have " + expected + " values.");
        }
    }

    static void requireKeyword(String actual, String expected, File file, int lineNumber) {
        if (!expected.equals(actual)) {
            throw invalid(file, "Expected " + expected + " on line " + lineNumber + ".");
        }
    }

    static int parseCount(String text, File file, String field) {
        try {
            int value = Integer.parseInt(text);
            if (value < 0) {
                throw invalid(file, field + " cannot be negative.");
            }
            return value;
        } catch(NumberFormatException e) {
            throw invalid(file, "Invalid " + field + ": " + text, e);
        }
    }

    static double parseDouble(String text, File file, String field) {
        try {
            return requireDouble(Double.parseDouble(text), file, field);
        } catch(NumberFormatException e) {
            throw invalid(file, "Invalid " + field + ": " + text, e);
        }
    }

    static double requireDouble(Double value, File file, String field) {
        if (value == null) {
            throw invalid(file, "Missing " + field + ".");
        }
        if (!Double.isFinite(value)) {
            throw invalid(file, "Invalid " + field + ": " + value);
        }
        return value;
    }

    static void addNode(Graph graph, Map<String, Node> nodeMap, String id, String label,
                        double x, double y, File file) {
        id = requireText(id, file, "node id");
        if (label == null) {
            throw invalid(file, "Missing node label.");
        }
        if (nodeMap.containsKey(id)) {
            throw invalid(file, "Duplicate node id: " + id);
        }

        Node node = new Node(label, x, y);
        nodeMap.put(id, node);
        graph.addNode(node);
    }

    static Node requireNode(Map<String, Node> nodeMap, String id, File file, String field) {
        id = requireText(id, file, field);
        Node node = nodeMap.get(id);
        if (node == null) {
            throw invalid(file, "Unknown " + field + ": " + id);
        }
        return node;
    }

    static void addEdge(Graph graph, Node source, Node target, double weight, File file) {
        if (isUnweighted(graph) && Math.abs(weight - 1.0) > 1e-9) {
            throw invalid(file, "Unweighted graphs only support edge weight 1.0.");
        }
        graph.addEdge(new Edge(source, target, weight));
    }

    static GraphIOException invalid(File file, String message) {
        return invalid(file, message, null);
    }

    private static String requireText(String text, File file, String field) {
        if (text == null || text.isBlank()) {
            throw invalid(file, "Missing " + field + ".");
        }
        return text;
    }

    private static boolean isUnweighted(Graph graph) {
        return graph instanceof DirectedGraph || graph instanceof UndirectedGraph;
    }

    private static GraphIOException invalid(File file, String message, Throwable cause) {
        return new GraphIOException("Invalid graph file " + file.getPath() + ": " + message, cause);
    }
}
