package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.repository.*;
import java.nio.file.Path;
import java.io.File;
import java.nio.file.Files;

/** Class GraphService */
public class GraphService {
    /** Member graph - instance of class Graph */
    private Graph graph;

    /** JsonGraphRepository instance */
    private final GraphRepository repository = new JsonGraphRepository();
    /**
     * Constructor taking one argument
     * @param graph graph wich is passed to the GraphService object
     */
    public GraphService(Graph graph) {
        this.graph = graph;
        this.graph.buildAdjacent();
    }

    /**
     * Method to load a graph from a file
     */
    public void saveGraph(String path, String filename) {
        File file = new File(path + File.separator + filename + ".json");
        repository.save(graph, file);
    }


    /**
     * Method to save a graph to a file
     */

    public void loadGraph(String path, String filename) {
        File file = new File(path + File.separator + filename + ".json");
        if(!file.exists()) {
            throw new GraphIOException("File not found: " + file.getPath(), null);
        }
        this.graph = repository.load(file);
        this.graph.buildAdjacent();
    }

    /**
     * Method to get a private member graph
     * @return graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Graph setter
     * @param graph
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }
  
}

