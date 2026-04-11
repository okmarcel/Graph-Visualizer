package dev.GraphVisualizer.models;

// import org.w3c.dom.Node;

/**
 * Class Edge
 * 
 * @author Jakub Piela
 * @version 0.1
 */

public class Edge {

    private final Node source;
    private final Node target;
    private double weight;

    public Edge(Node source, Node target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    /**
     * Public method which returns the weight of the Edge
     * @return weight
     */
    public double getWeight() {
        return weight;
    }
    /**
     * Changes private field weight of the Edge
     * @param newWeight
     */
    public void setWeight(double newWeight) {
        weight = newWeight;
    }
    /**
     * @return directed: calls getDirected method from class Graph
     */
    // public boolean isDirected(){
    //     return Graph.getDirected();
    // }

}
