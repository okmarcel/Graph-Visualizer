package dev.GraphVisualizer.service;

/** Custom Exception thrown when graph provided to Dijkstra algorithm is unweighted */
public class UnweightedGraphException extends RuntimeException {
    public UnweightedGraphException(String message) {
        super(message);
    }
}