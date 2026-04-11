package dev.GraphVisualizer.models;

public class Edge {
    /**
     * Source Node of the Edge
     */
    private Node source;

    /**
     * Target Node of the Edge
     */
    private Node target;

    /**
     * Weight of the Edge applicable if the Graph is weighted otherwise 0.0
     */
    private double weight;

    /**
     * Constructor for the Edge, takes 3 parameters:
     * @param source - source Node
     * @param target - target Node
     * @param weight - weight of the Edge applicable if the Graph is weighted
     */
    public Edge(Node source, Node target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    /**
     * Contructor for the Edge of not weighted Graph
     * @param source
     * @param target
     */
    public Edge(Node source, Node target) {
        this(source, target, 1.0);
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
     * @param weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return directed: calls getDirected method from class Graph
     */
    // public boolean isDirected(){
    //     return Graph.getDirected();
    // }

    // public void setSource(){
    //     source.setNode();
    // }

    // public void setTarget(){
    //     target.setNode();
    // }

    /**
     * Returns source Node
     * @return
     */
    public Node getSource(){
        return source;
    }

    /**
     * Returns target Node
     * @return
     */
    public Node getTarget(){
        return target;
    }
}
