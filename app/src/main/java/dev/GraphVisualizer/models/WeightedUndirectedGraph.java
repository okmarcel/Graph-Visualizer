package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map; 

/** Class WeightedUndirectedGraph - class that represents a undirected graph with weighted edges data structure */
public final class WeightedUndirectedGraph extends Graph {
    /** Adjacency list for every node in the weighted undirected graph */
    private Map<Node, List<Node>> adjacent;

    /**
     * Creates a weighted undirected graph with predefined nodes and edges.
     * @param nodes list of predefined nodes
     * @param edges list of predefined edges
     */
    public WeightedUndirectedGraph(List<Node> nodes, List<Edge> edges) {
        super(nodes, edges);
        buildAdjacent();
    }

    /** Creates an empty weighted undirected graph. */
    public WeightedUndirectedGraph() {
        super();
        buildAdjacent();
    }

    /** Rebuilds the adjacency list for every node. */
    public void buildAdjacent(){
        this.adjacent = new LinkedHashMap<>();
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
