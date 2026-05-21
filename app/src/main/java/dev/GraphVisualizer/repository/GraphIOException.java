package dev.GraphVisualizer.repository;

/** Custom exception for graph IO operations */
public final class GraphIOException extends RuntimeException {
    public GraphIOException(String message, Throwable cause) {
        super(message, cause);
    }
}