package dev.GraphVisualizer.algorithms;

/**
 * Custom Exception thrown when graph provided to BFS algorithm is null
 */
public class GraphNotFoundException extends RuntimeException {
    public GraphNotFoundException(String message) {
        super(message);
    }
}