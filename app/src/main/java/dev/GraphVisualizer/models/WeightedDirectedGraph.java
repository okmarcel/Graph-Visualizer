package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map; 

/** Class WeightedDirectedGraph - class that represents a directed graph with weighted edges data structure */
public class WeightedDirectedGraph extends Graph {
    /** Map holding Adjacent list of nodes for every node in the directed graph */
    protected Map<Node, List<Node>> adjacent;

    /**
     * Constructor invoking constructor of the base class - intializes Adjacent table, takes two arguments
     * @param nodes list of predefined nodes
     * @param edges list of predefined edges
     */
    public WeightedDirectedGraph(List<Node> nodes, List<Edge> edges) {
        super(nodes, edges);
        buildAdjacent();
    }

    /**
     * Constructor invoking construcotr of the base class - initializes Adjacent table, takes no arguments
     */
    public WeightedDirectedGraph() {
        super();
        buildAdjacent();
    }

    /**
     * Method used to build Adjacent table for every node
     */
    public void buildAdjacent(){
        this.adjacent = new HashMap<>();
        for(Node i : getAllNodes()) {
            adjacent.put(i, new ArrayList<>());
        }
        for(Edge j : getAllEdges()) {
            adjacent.get(j.getSource()).add(j.getTarget());
        }
    }

    /**
     * Method used to rebuild Adjacent table for every node after changes in the graph - resets the cache
     */
    public void rebuildAdjacent() {
        if(cache) {
            buildAdjacent();
            cache = false;
        }
    }

    /**
     * Adjacent table getter
     * @return Adjacent table
     */
    public Map<Node, List<Node>> getAdjacent() {
        return adjacent;
    }
}
