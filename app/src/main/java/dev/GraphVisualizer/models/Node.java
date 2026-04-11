package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
/**
 * class Node
 */
public class Node {
	//TODO use this variable to generate Id
	private static int numberOfNodes = 0;
	
	/**
	 * Member id - String holding an id of Node
	 */
	private String id;

	// TODO - add a parametrized version of node to hold different type of variables

	/**
	 * Memver value - int value stored by Node
	 */
	private int value;

	/**
	 * Member x - position on canvas - x axis
	 */
	private double x;

	/**
	 * Member y - position on canvas - y axis
	 */
	private double y;
	
	/**
	 * Member adjacentEdges - list of Edge variables connected to the Node
	 */
	private List<Edge> adjacentEdges;

	/**
	 * Node constructor taking three arguments:
	 * @param value stored by Node
	 * @param x position on canvas x axis
	 * @param y position on canvas y axis
	 */
	public Node(int value, double x, double y) {
		this.id = setId();
		this.value = value;
		this.x = x;
		this.y = y;
		this.adjacentEdges = new ArrayList<Edge>();
		numberOfNodes++;
	}

	/**
	 * Node constructor taking two arguments:
	 * @param value value stored by Node
	 */
	public Node(int value, double x) {
		this(value, x, 0.0);
	}
	
	public Node(int value) {
		this(value, 0.0, 0.0);
	}
	
	public Node() {
		this(0);
	}
	
	/**
	 * Id getter
	 * @return id of node
	 */
	public String getId() {
		return id;
	}

	/**
	 * Id setter
	 */
	private String setId() {
		return "test_id";
	}

	private void setCustomId(String id) {
		this.id = id;
	}

	/**
	 * Value getter
	 * @return value stored by node
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Value setter
	 * @param value new value to store by node
	 */
	public void setValue(int value){
		this.value = value;
	}

	/**
	 * Getter for x-axis position on canvas
	 * @return x-axis position on canvas
	 */
	public double getPositionX() {
		return x;
	}

	/**
	 * Setter for x-axis position on canvas
	 * @param x x-axis position on canvas
	 */
	public void setPositionX(double x) {
		this.x = x;
	}

	/**
	 * Getter for y-axis position on canvas
	 * @return y-axis position on canvas
	 */
	public double getPositionY() {
		return y;
	}

	/**
	 * Setter for y-axis position on canvas
	 * @param y y-axis position on canvas
	 */
	public void setPositionY(double y) {
		this.y = y;
	}

	/**
	 * Returns a list of Node variables that a Node has a Edge with
	 */
	public List<Node> getNeighbours() {
		List<Node> adjacent = new ArrayList<Node>();
		for(Edge i : adjacentEdges) {
			adjacent.add(i.getTarget());
		}
		return adjacent;
	}

	@Override
	public String toString() {
		return "Node{id= " + id + ", value= " + value 
			+ ", x= " + x + ", y= " + y + "}";
	}

}
