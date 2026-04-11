package dev.GraphVisualizer.models;

import java.util.ArrayList;
import java.util.List;
 
/**
 * Class Node
 * 
 * @author Marcel Gładysz
 * @version 0.1
 */
public class Node {
	private String id;
	private int value;		// value stored by node
	private double x;		// x - position on canvas
	private double y;		// y - position on canvas
	
	private List<Edge> neighbours;

	/**
	 * Node constructor
	 * @param value value stored by Node
	 * @param x x - position on canvas
	 * @param y y - position on canvas
	 */
	public Node(int value, double x, double y) {
		this.id = assignId();
		this.value = value;
		this.x = x;
		this.y = y;
		this.neighbours = new ArrayList<Edge>();
	}

	/**
	 * Node constructor
	 * @param value value stored by Node
	 */
	public Node(int value) {
		this(value, 0.0, 0.0);
	}
	
	/**
	 * Id getter
	 * @return id of node
	 */
	public String getId() { return id; }

	/**
	 * Value getter
	 * @return value stored by node
	 */
	public int getValue() { return value; }

	/**
	 * Value setter
	 * @param value new value to store by node
	 */
	public void setValue(int value) { this.value = value; }

	/**
	 * Assigns id to new node
	 */
	private String assignId() {
		return "test_id";
	}

	@Override
	public String toString() {
		return "Node{id= " + id + ", value= " + value 
			+ ", x= " + x + ", y= " + y + "}";
	}

	public List<Node> getNeighbours(){
		return neighbours;
	}
}
