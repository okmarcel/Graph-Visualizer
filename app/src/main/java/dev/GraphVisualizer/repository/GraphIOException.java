package dev.GraphVisualizer.repository;

/** Custom exception for graph IO operations */
public final class GraphIOException extends RuntimeException {
    /**
     * Constructor taking an error message and original cause.
     * @param message error description
     * @param cause underlying IO exception
     */
    public GraphIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
