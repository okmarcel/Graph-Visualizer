package dev.GraphVisualizer.repository;

import dev.GraphVisualizer.models.*;
import java.io.File;

/** Graph repository interface */
public interface GraphRepository {
    /**
     * Method to save the representation of a graph to a file
     * @param graph which is going to be saved
     * @param file handle
     */
    void save(Graph graph, File file);

    /**
     * Method to get a graph from a valid representation from a file
     * @param file handle
     * @return built graph
     */
    Graph load(File file);
}