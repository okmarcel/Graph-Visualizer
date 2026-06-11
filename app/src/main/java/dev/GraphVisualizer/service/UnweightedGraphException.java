package dev.GraphVisualizer.service;

/** Custom Exception thrown when graph provided to Dijkstra algorithm is unweighted */
public final class UnweightedGraphException extends RuntimeException {
    /**
     * Constructor taking the exception message.
     * @param message error description
     */
    public UnweightedGraphException(String message) {
        super(message);
    }
}
