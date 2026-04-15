package dev.GraphVisualizer.models;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an edge in a graph
 */
public class Edge {
    /** Unique identifier, assigned once at construction and never changed */
	private final String id;

    /** Source Node of the Edge */
    private Node source;

    /** Target Node of the Edge */
    private Node target;

    /** Weight of the Edge, defaults to 1.0 for non-weighted edges */
    private double weight;

    /**
     * Constructs Edge object from nodes and weight value
     * @param source source Node
     * @param target target Node
     * @param weight weight of the Edge
     * @throws NullPointerException if source or target node is null
     */
    public Edge(Node source, Node target, double weight) {
        Objects.requireNonNull(source, "Source node cannot be null.");
        Objects.requireNonNull(target, "Target node cannot be null.");
		this.id = UUID.randomUUID().toString();
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    /**
     * Constructs Edge object from nodes, defaults weight to 1.0
     * @param source
     * @param target
     */
    public Edge(Node source, Node target) {
        this(source, target, 1.0);
    }

    /**
     * Returns the weight of the edge
     * @return weight of the edge
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets weight of the edge
     * @param weight new weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Returns source node
     * @return Source node
     */
    public Node getSource() {
        return source;
    }

    /**
     * Sets source node of the edge
     * @param source source node
     */
    public void setSource(Node source) {
        Objects.requireNonNull(source, "Source node cannot be null.");
        this.source = source;
    }

    /**
     * Returns target node
     * @return Target node
     */
    public Node getTarget() {
        return target;
    }

    /**
     * Sets target node of the edge
     * @param target target node
     */
    public void setTarget(Node target){
        Objects.requireNonNull(target, "Target node cannot be null.");
        this.target = target;
    }

	/**
	 * Checks if objects are equal
	 * @param ob object to compare against
	 * @return true if ob is same or has the same edge id, otherwise false
	 */
	@Override
	public boolean equals(Object ob) {
		if (this == ob)
			return true;
		if (ob instanceof Edge e && e.id.equals(id))
			return true;
		return false;
	}

    /**
	 * Returns hash code of edge object
	 * @return Hash code of edge object
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
