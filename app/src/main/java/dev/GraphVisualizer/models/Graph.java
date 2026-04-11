package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;

/**
 * class Graph
 */
public class Graph{
    /**
     * Member nodes - list of all nodes in the graph
     */
    private List<Node> nodes;

    /**
     * Member edges - list of all edges in the graph
     */
    private List<Edge> edges;

    /**
     * Member directed - boolean flag to hold information whether the graph is directed or not
     */
    private boolean directed;

    /**
     * Member weighted - boolean flag to hold information whether the graph is weighted or not
     */
    private boolean weighted;

    /**
     * Contructor which initializes list of nodes and edges.
     * @param directed indicated whether graph is directed or not
     * @param weighted indicates whether graph is weighted or not  
     */
    public Graph(boolean directed, boolean weighted) {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
        this.directed = directed;
        this.weighted = weighted;
    }

    /**
     * Contructor taking one argument:
     * directed = provided argument;
     * weighted = false;
     * @param directed indicated whether graph is directed or not
     */
    public Graph(boolean directed) {
        this(directed, false);
    }

    /**
     * Conctruct taking no arguemnts:
     * directed = false;
     * weighted = false
     */
    public Graph() {
        this(false);
    }

    /**
     * Adds a new Node to the list of nodes - nodes
     * @param newNode
     */
    public void addNode(Node newNode) {
        nodes.add(newNode);
    }

    /**
     * Adds a new Edge to the list of edges - edges
     * @param newEdge
     */
    public void addEdge(Edge newEdge) {
        edges.add(newEdge);
    }

    /**
     * Removes a node from the list of nodes - nodes
     * @param toBeRemovedNode
     */
    public void removeNode(Node toBeRemovedNode) {
        nodes.remove(toBeRemovedNode);
    }

    /**
     * Returns list of nodes adjacent to the given Node
     * @param mainNode
     * @return neighbours of the mainNode
     */
    public List<Node> getNeighbours(Node mainNode) {
        return mainNode.getNeighbours();
    }

    /**
     * Returns boolean flag which indicates whether the graph is directed or not
     * @return directed
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

    /**
     * Returns boolean flag which indicates whether the graph is weighted or not
     * @return boolean
     */
    public boolean getWeighted() {
        return weighted;
    }

    /**
     * Sets boolean flag which indicates whether the graph is weighted or not
     * @param weighted
     */
    public void setWeighted(boolean weighted) {
        this.weighted = weighted;
    }

    /**
     * Returns a number of Node variables in graph
     * @return int
     */
    public int getNumberOfNodes(){
        return nodes.size();
    }

    /**
     * Returns a number of Edge variables in graph
     * @return int
     */
    public int getNumberOfEdges(){
        return edges.size();
    }
}