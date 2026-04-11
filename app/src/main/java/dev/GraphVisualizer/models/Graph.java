package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Edge
 * 
 * @author Jakub Piela
 * @version 0.1
 */

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
     * Booleamn flag ot hold info whether the graph is directed or not
     */
    private boolean directed;
    /**
     * Contructor which initializes li
     */
    public Graph() {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
        directed = false;
    }
    public void addNode(Node newNode) {
        nodes.add(newNode);
    }
    public void addEdge(Edge newEdge) {
        edges.add(newEdge);
    }
    public void removeNode(Node toBeRemovedNode) {
        nodes.remove(toBeRemovedNode);
    }
    public List<Node> getNeighbours(Node central) {
        return central.getNeighbours();
    }
}