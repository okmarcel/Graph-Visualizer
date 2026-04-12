package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Representats a graph data structure
 */
public class Graph {
    /** List of all nodes in the graph */
    private List<Node> nodes;

    /** List of all edges in the graph */
    private List<Edge> edges;

    /** Member directed - boolean flag to hold information whether the graph is directed or not */
    private boolean directed;

    /** Member weighted - boolean flag to hold information whether the graph is weighted or not */
    private boolean weighted;

    /**
     * Constructs Graph object based on argument values
     * 
     * @param directed indicating whether graph is directed or not
     * @param weighted indicating whether graph is weighted or not  
     */
    public Graph(boolean directed, boolean weighted) {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
        this.directed = directed;
        this.weighted = weighted;
    }

    /**
     * Constructs Graph object based on 'directed' value, defaults weighted to false
     * 
     * @param directed indicating whether graph is directed or not
     */
    public Graph(boolean directed) {
        this(directed, false);
    }

    /**
     * Constructs a non-directed, non-weighted graph
     */
    public Graph() {
        this(false, false);
    }

    /**
     * Adds a node to the graph
     * 
     * @param node node to add to graph
     */
    public void addNode(Node node) {
        nodes.add(node);
    }

    /**
     * Adds an edge to the graph
     * 
     * @param edge edge to add to graph
     */
    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    /**
     * Removes a node from the graph
     * 
     * @param node node to be removed from graph
     */
    public void removeNode(Node node) {
        if (!nodes.remove(node))
            System.out.println("Tried to remove a node that is not part of the graph.");
    }

    /**
     * Removes an edge from the graph
     * 
     * @param edge edge to be removed from graph
     */
    public void removeEdge(Edge edge) {
        if (!edges.remove(edge))
            System.out.println("Tried to remove an edge that is not part of the graph.");
    }

    /**
     * Returns whether the graph is directed or not
     * 
     * @return true if graph is directed, otherwise false
     */
    public boolean isDirected() {
        return directed;
    }

    /**
     * Sets graph as directed or non-directed
     * 
     * @param directed indicating whether graph is directed or not
     */
    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    /**
     * Returns whether the graph is weighted or not
     * 
     * @return true if graph is weighted, otherwise false
     */
    public boolean isWeighted() {
        return weighted;
    }

    /**
     * Sets graph as wighted or non-weighted
     * 
     * @param weighted indicating whether graph is weighted or not
     */
    public void setWeighted(boolean weighted) {
        this.weighted = weighted;
    }

    /**
     * Returns a number of nodes in the graph
     * 
     * @return Number of nodes
     */
    public int getNumberOfNodes() {
        return nodes.size();
    }

    /**
     * Returns a number of Edge variables in graph
     * 
     * @return Number of edges
     */
    public int getNumberOfEdges() {
        return edges.size();
    }

    /**
     * Returns a list of all nodes in the graph
     * 
     * @return List of nodes
     */
    public List<Node> getAllNodes() {
        return nodes;
    }

    /**
     * Sets all nodes in the graph from a list
     * 
     * @param nodes list of nodes
     */
    public void setAllNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * Returns a list of all edges in the graph
     * 
     * @return List of edges
     */
    public List<Edge> getAllEdges() {
        return edges;
    }

    /**
     * Sets all edges in the graph from a list
     * 
     * @param edges list of edges
     */
    public void setAllEdges(List<Edge> edges) {
        this.edges = edges;
    }
}