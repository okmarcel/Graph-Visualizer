package dev.GraphVisualizer.repository;

import dev.GraphVisualizer.models.Graph;

/**
 * Graph repository interface
 */
public interface GraphRepository {
    void save(Graph graph, File file);
    Graph load(File file);
}