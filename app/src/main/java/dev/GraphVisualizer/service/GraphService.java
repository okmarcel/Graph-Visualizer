package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;

/** Class GraphService */
public class GraphService {
    /** Member graph - instance of class Graph */
    private Graph graph;

    /**
     * Constructor taking one argument
     * @param graph graph wich is passed to the GraphService object
     */
    public GraphService(Graph graph) {
        this.graph = graph;
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

    /**
     * Graph setter
     * @param graph
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }
  
}

