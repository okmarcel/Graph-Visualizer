package dev.GraphVisualizer.algorithms;

/**
 * Custom Exception thrown when graph provided to Dijkstra algorithm has negative weights
 */
public class NegativeWeightException extends RuntimeException {
    public NegativeWeightException(String message) {
        super(message);
    }
}