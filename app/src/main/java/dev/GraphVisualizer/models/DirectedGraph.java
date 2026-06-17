package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map; 

/** Class DirectedGraph - class that represents a directed graph data structure */
public final class DirectedGraph extends Graph {
    /** Adjacency list for every node in the directed graph */
    private Map<Node, List<Node>> adjacent;

    /**
     * Creates a directed graph with predefined nodes and edges.
     * @param nodes list of predefined nodes
     * @param edges list of predefined edges
     */
    public DirectedGraph(List<Node> nodes, List<Edge> edges) {
        for(Edge edge : edges) {
            if(Math.abs(edge.getWeight() - 1.0) > 1e-9)
                throw new WeightedEdgeException("DirectedGraph does not support weighted edges. Could not construct graph!");
        }
        super(nodes, edges);
        buildAdjacent();
    }

    /** Creates an empty directed graph. */
    public DirectedGraph() {
        super();
        buildAdjacent();
    }

    /** Adds an edge after verifying that the graph remains unweighted. */
    @Override
    public void addEdge(Edge edge) {
        if(Math.abs(edge.getWeight() - 1.0) > 1e-9)
            throw new WeightedEdgeException("DirectedGraph does not support weighted edges.");
        super.addEdge(edge);
    }

    /** Rebuilds the adjacency list for every node. */
    public void buildAdjacent(){
        this.adjacent = new LinkedHashMap<>();
        for(Node i : getAllNodes()) {
            adjacent.put(i, new ArrayList<>());
        }
        for(Edge j : getAllEdges()) {
            adjacent.get(j.getSource()).add(j.getTarget());
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
