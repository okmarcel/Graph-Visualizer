package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map; 

/** Class UndirectedGraph - class that represents an undirected graph data structure */
public class UndirectedGraph extends Graph{
    /** Map holding Adjacent list of nodes for every node in the directed graph */
    private Map<Node, List<Node>> adjacent;

    /**
     * Constructor invoking constructor of the base class - intializes Adjacent table, takes two arguments
     * @param nodes list of predefined nodes
     * @param edges list of predefined edges
     */
    public UndirectedGraph(List<Node> nodes, List<Edge> edges) {
        super(nodes, edges);
        for(Edge edge : edges) {
            if(edge.getWeight() != 1.0)
                throw new WeightedEdgeException("UndirectedGraph does not support weighted edges. Could not construct graph!");
        }
        buildAdjacent();
    }

    /**
     * Constructor invoking construcotr of the base class - initializes Adjacent table, takes no arguments
     */
    public UndirectedGraph() {
        super();
        buildAdjacent();
    }

    /**
     * Overriden method addEdge form class Graph - provides check whether edge that we are trying to add has a weight different than 1.0
     */
    @Override
    public void addEdge(Edge edge) {
        if(edge.getWeight() != 1.0)
            throw new WeightedEdgeException("UndirectedGraph does not support weighted edges.");
        super.addEdge(edge);
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
            adjacent.get(j.getTarget()).add(j.getSource());
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
