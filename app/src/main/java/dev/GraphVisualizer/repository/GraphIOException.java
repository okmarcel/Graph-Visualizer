package dev.GraphVisualizer.repository;

/** Custom exception for graph IO operations */
public class GraphIOException extends RuntimeException {
    public GraphIOException(String message, Throwable cause) {
        super(message, cause);
    }
}