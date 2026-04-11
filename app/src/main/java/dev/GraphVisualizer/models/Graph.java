package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;

public class Graph{
    /**
     * List of all nodes in the graph
     */
    private List<Node> nodes;

    /**
     * List of all edges in the graph
     */
    private List<Edge> edges;

    /**
     * Boolean flag to hold information whether the graph is directed or not
     */
    private boolean directed;

    /**
     * Boolean flag to hold information whether the graph is weighted or not
     */
    private boolean weighted;

    /**
     * Contructor which initializes list of nodes and edges.
     * By default we consider graph not to be directed and not weighted.
     */
    public Graph() {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
        directed = false;
        weighted = false;
    }

    /**
     * Adds a new node to the list of nodes aka nodes
     * @param newNode
     */
    public void addNode(Node newNode) {
        nodes.add(newNode);
    }

    /**
     * Adds a new edge to the list of edges aka edges
     * @param newEdge
     */
    public void addEdge(Edge newEdge) {
        edges.add(newEdge);
    }

    /**
     * Removes a node from the list of nodes aka nodes
     * @param toBeRemovedNode
     */
    public void removeNode(Node toBeRemovedNode) {
        nodes.remove(toBeRemovedNode);
    }

    /**
     * Returns list of nodes adjacent to the given Node
     * @param givenNode
     * @return List of Nodes
     */
    public List<Node> getNeighbours(Node givenNode) {
        return givenNode.getNeighbours();
    }

    /**
     * Returns boolean flag which indicates whether the graph is directed or not
     * @return
     */
    public boolean getDirected() {
        return directed;
    }

    /**
     * Sets boolean flag which indicates whether the graph is directed or not
     * @param directed
     */
    public void setDirected(boolean directed) {
        this.directed = directed;
    }
}