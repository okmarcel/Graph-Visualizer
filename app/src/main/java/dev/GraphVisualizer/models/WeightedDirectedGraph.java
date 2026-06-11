package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map; 

/** Class WeightedDirectedGraph - class that represents a directed graph with weighted edges data structure */
public final class WeightedDirectedGraph extends Graph {
    /** Adjacency list for every node in the weighted directed graph */
    private Map<Node, List<Node>> adjacent;

    /**
     * Creates a weighted directed graph with predefined nodes and edges.
     * @param nodes list of predefined nodes
     * @param edges list of predefined edges
     */
    public WeightedDirectedGraph(List<Node> nodes, List<Edge> edges) {
        super(nodes, edges);
        buildAdjacent();
    }

    /** Creates an empty weighted directed graph. */
    public WeightedDirectedGraph() {
        super();
        buildAdjacent();
    }

    /** Rebuilds the adjacency list for every node. */
    public void buildAdjacent(){
        this.adjacent = new HashMap<>();
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
