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
     * works well not only for not directed but also for directed graphs
     * @param graph
     */
    public GraphService(Graph graph) {
        this.graph = graph;
        this.adjacent = new HashMap<>();
        for(Node i : this.graph.getAllNodes()) {
            adjacent.put(i, new ArrayList<>());
        }
        for(Edge j : this.graph.getAllEdges()) {
            adjacent.get(j.getSource()).add(j.getTarget());
            if(!this.graph.isDirected()) {
                adjacent.get(j.getTarget()).add(j.getSource());
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

    /**
     * Private method to rebuild the adjacent list after graph change
     */
    private void rebuildAdjacent() {
        this.adjacent = new HashMap<>();
        for(Node i : this.graph.getAllNodes()) {
            adjacent.put(i, new ArrayList<>());
        }
        for(Edge j : this.graph.getAllEdges()) {
            adjacent.get(j.getSource()).add(j.getTarget());
            if(!this.graph.isDirected()) {
                adjacent.get(j.getTarget()).add(j.getSource());
            }
        }
    }

    /**
     * Return map od adjacent list of nodes for every node
     * @return
     */
    public Map<Node, List<Node>> getAdjacent() {
        return adjacent;
    }
}

