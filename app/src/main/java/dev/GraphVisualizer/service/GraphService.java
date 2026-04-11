package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;

/**
 * class GraphService
 */
public class GraphService {
    /**
     * Member graph - instance of class Graph
     */
    private Graph graph;

    /**
     * Sets private member directed in graph
     */
    public void setDirected(boolean directed) {
        graph.setDirected(directed);
    }

    /**
     * Sets private member weighted in graph
     */
    public void setWeighted(boolean weighted) {
        graph.setWeighted(weighted);
    }

    /**
     * Method to load a graph from a file
     */
    public void loadGraph() {

    }

    /**
     * Method to save a graph to a file
     */
    public void saveGraph() {

    }

    /**
     * Method to get a private member graph
     * @return graph
     */
    public Graph getGraph() {
        return graph;
    }

}

