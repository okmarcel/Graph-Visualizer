package dev.GraphVisualizer.algorithms;

/** Custom Exception thrown when graph provided to Dijkstra algorithm is undirected */
public class UndirectedGraphException extends RuntimeException {
    public UndirectedGraphException(String message) {
        super(message);
    }
}