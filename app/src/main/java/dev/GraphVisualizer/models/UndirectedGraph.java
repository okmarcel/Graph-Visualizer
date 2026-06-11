package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map; 

/** Class UndirectedGraph - class that represents an undirected graph data structure */
public final class UndirectedGraph extends Graph {
    /** Adjacency list for every node in the undirected graph */
    private Map<Node, List<Node>> adjacent;

    /**
     * Creates an undirected graph with predefined nodes and edges.
     * @param nodes list of predefined nodes
     * @param edges list of predefined edges
     */
    public UndirectedGraph(List<Node> nodes, List<Edge> edges) {
        for(Edge edge : edges) {
            if(Math.abs(edge.getWeight() - 1.0) > 1e-9)
                throw new WeightedEdgeException("UndirectedGraph does not support weighted edges. Could not construct graph!");
        }
        super(nodes, edges);
        buildAdjacent();
    }

    /** Creates an empty undirected graph. */
    public UndirectedGraph() {
        super();
        buildAdjacent();
    }

    /** Adds an edge after verifying that the graph remains unweighted. */
    @Override
    public void addEdge(Edge edge) {
        if(Math.abs(edge.getWeight() - 1.0) > 1e-9)
            throw new WeightedEdgeException("UndirectedGraph does not support weighted edges.");
        super.addEdge(edge);
    }

    /** Rebuilds the adjacency list for every node. */
    public void buildAdjacent(){
        this.adjacent = new HashMap<>();
        for(Node i : getAllNodes()) {
            adjacent.put(i, new ArrayList<>());
        }
        for(Edge j : getAllEdges()) {
            adjacent.get(j.getSource()).add(j.getTarget());
            adjacent.get(j.getTarget()).add(j.getSource());
        }
    }

    /** Rebuilds adjacency data only when the graph has changed. */
    public void rebuildAdjacent() {
        if(cache) {
            buildAdjacent();
            cache = false;
        }
    }

    /**
     * Returns the adjacency list.
     * @return adjacency list
     */
    public Map<Node, List<Node>> getAdjacent() {
        return adjacent;
    }
}
