package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

/**
 * class GraphService
 */
public class GraphService {
    /**
     * Member graph - instance of class Graph
     */
    private Graph graph;

    /**
     * List of Adjacent Nodes for every Node in graph
     */
    private Map<Node, List<Node>> adjacent;

    /**
     * Constructor which crates list of Adjacent Nodes for every Node in the graph
     * @param graph
     */
    public GraphService(Graph graph) {
        this.graph = graph;
        this.adjacent = new HashMap<>();
        for(Node i : this.graph.getAllNodes()) {
            adjacent.put(i, new ArrayList<>());
            for(Edge j : this.graph.getAllEdges()) {
                if(j.getSource().equals(i)) {
                    adjacent.get(i).add(j.getTarget());
                }
            }
        }
    }

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

    /**
     * Graph setter
     * @param graph
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

}

