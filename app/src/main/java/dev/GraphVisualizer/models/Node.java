package dev.GraphVisualizer.models;

import java.util.UUID;
import java.util.Objects;

/** Class Node - represents a node in a graph with a label and position on canvas */
public class Node {
	/** Unique identifier, assigned once at construction and never changed */
	private final String id;

	/** Label displayed in GUI */
	private String label;

	/** x-axis position on canvas */
	private double x;

	/* y-axis position on canvas */
	private double y;

	/**
	 * Constructs Node object from label string and x, y position arguments
	 * @param label node label string
	 * @param x x-axis position on canvas
	 * @param y y-axis position on canvas
	 * @throws NullPointerException if label string is null
	 */
	public Node(String label, double x, double y) {
		Objects.requireNonNull(label, "Node label cannot be null.");
		this.id = UUID.randomUUID().toString();
		this.label = label;
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs Node object from label string.
	 * Position parameters (x, y) default to 0.0 and can later be set
	 * via {@link #setPositionX(double)} and {@link #setPositionY(double)}
	 * @param label node label string
	 * @throws NullPointerException if label string is null
	 */
	public Node(String label) {
		// Note: arguments x, y are assigned 0.0 but they
		// are likely redundant if this constructor was called.
		this(label, 0.0, 0.0);
	}

	/**
	 * Returns node id string
	 * @return Node id string
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns node label string
	 * @return Node label string
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets node label string
	 * @param label new label string
	 * @throws NullPointerException if label string is null
	 */
	public void setLabel(String label) {
		Objects.requireNonNull(label, "Node label cannot be null.");
		this.label = label;
	}

	/**
	 * Returns node x-axis position on canvas
	 * @return Node x-axis position on canvas
	 */
	public double getPositionX() {
		return x;
	}

	/**
	 * Sets node x-axis position on canvas
	 * @param x x-axis position on canvas
	 */
	public void setPositionX(double x) {
		this.x = x;
	}

	/**
	 * Returns node y-axis position on canvas
	 * @return Node y-axis position on canvas
	 */
	public double getPositionY() {
		return y;
	}

	/**
	 * Sets node y-axis position on canvas
	 * @param y y-axis position on canvas
	 */
	public void setPositionY(double y) {
		this.y = y;
	}

	/**
	 * Returns node parameters as String
	 * @return Node parameters in format Node{id=id, label=label, x=x, y=y}
	 */
	@Override
	public String toString() {
		return "Node{id= " + id + ", label= " + label 
			+ ", x= " + x + ", y= " + y + "}";
	}

	/**
	 * Checks if objects are equal
	 * @param ob object to compare against
	 * @return true if ob is same or id strings are the same, otherwise false
	 */
	@Override
	public boolean equals(Object ob) {
		if (this == ob)
			return true;
		if (ob instanceof Node n && n.id.equals(id))
			return true;
		return false;
	}

	/**
	 * Returns hash code of node object
	 * @return Hash code of node object
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
