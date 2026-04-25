package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Class Graph - abstract class that represents a graph data structure */
public abstract class Graph {
    /** List of all nodes in the graph */
    protected List<Node> nodes;

    /** List of all edges in the graph */
    protected List<Edge> edges;

    /** Boolean flag cache marks whether the graph was changed and if the rebuild of Adjacent table is necessary */
    protected boolean cache;

    /**
     * Constructor with the predefined list of nodes and edges
     * @param nodes list of predefined nodes
     * @param edges list of predefined edges
     */
    public Graph(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
        this.cache = false;
    }

    /**
     * No argument constructor
     */
    public Graph() {
        this(new ArrayList<Node>(), new ArrayList<Edge>());
    }

    /**
     * Adds a node to the graph
     * @param node node to add to graph
     */
    public void addNode(Node node) {
        nodes.add(node);
        cache = true;
    }

    /**
     * Adds an edge to the graph
     * @param edge edge to add to graph
     */
    public void addEdge(Edge edge) {
        edges.add(edge);
        cache = true;
    }

    /**
     * Removes a node from the graph 
     * @param node node to be removed from graph
     */
    public void removeNode(Node node) {
        cache = true;
        if (!nodes.remove(node)) {
            System.out.println("Tried to remove a node that is not part of the graph.");
            cache = false;
        }
    }

    /**
     * Removes an edge from the graph
     * @param edge edge to be removed from graph
     */
    public void removeEdge(Edge edge) {
        cache = true;
        if (!edges.remove(edge)) {
            System.out.println("Tried to remove an edge that is not part of the graph.");
            cache = false;
        }
    }

    /**
     * Returns a number of nodes in the graph
     * @return Number of nodes
     */
    public int getNumberOfNodes() {
        return nodes.size();
    }

    /**
     * Returns a number of edges in the graph
     * @return Number of edges
     */
    public int getNumberOfEdges() {
        return edges.size();
    }

    /**
     * Returns a list of all nodes in the graph
     * @return List of nodes
     */
    public List<Node> getAllNodes() {
        return nodes;
    }

    /**
     * Sets all nodes in the graph from a list
     * @param nodes list of nodes
     */
    public void setAllNodes(List<Node> nodes) {
        this.nodes = nodes;
        cache = true;
    }

    /**
     * Returns a list of all edges in the graph
     * @return List of edges
     */
    public List<Edge> getAllEdges() {
        return edges;
    }

    /**
     * Sets all edges in the graph from a list
     * @param edges list of edges
     */
    public void setAllEdges(List<Edge> edges) {
        this.edges = edges;
        cache = true;
    }

    /**
     * Cache getter return boolean cache flag
     * @return
     */
    public boolean getCache() {
        return cache;
    }

    /**
     * Cache setter sets boolean cache glaf
     * @param cache
     */
    public void setCache(boolean cache) {
        this.cache = cache;
    }

    /**
     * Abstract method building Adjacent table for the graph
     * @param adjacent
     */
    public abstract void buildAdjacent();

    /**
     * Abstract method getter of Adjacent table for the graph
     * @return Adjacent table
     */
    public abstract Map<Node, List<Node>> getAdjacent();
}
