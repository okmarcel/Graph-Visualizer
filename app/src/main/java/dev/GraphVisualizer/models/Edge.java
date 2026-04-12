package dev.GraphVisualizer.models;

/**
 * class Edge
 */
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
     * Contructor taking weight sets source and target to null
     * @param weight
     */
    public Edge(double weight) {
        this(null, null, weight);
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

    /**
     * source setter
     */
    public void setSource(Node source){
        this.source = source;
    }

    /**
     * target setter
     */
    public void setTarget(Node target){
        this.target = target;
    }

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

    @Override
	public boolean equals(Object edge) {
		if (this == edge)
			return true;
		if (edge instanceof Edge e && e.getSource().equals(source) && e.getTarget().equals(target))
			return true;
		return false;
	}
}
