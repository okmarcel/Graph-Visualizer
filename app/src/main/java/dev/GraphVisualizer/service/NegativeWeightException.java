package dev.GraphVisualizer.service;

/** Custom Exception thrown when graph provided to Dijkstra algorithm has negative weights */
public final class NegativeWeightException extends RuntimeException {
    public NegativeWeightException(String message) {
        super(message);
    }
}