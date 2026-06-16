package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;

/** Class AlgorithmAddInfo provides some additional fields required for the algorithms */
public final class AlgorithmAddInfo {
    /**
     * Enum NodeColor used by BFS and DFS algorithms - holds three possible states of Node's nodeColor field:
     * WHITE, GREY or BLACK
     */
    public enum NodeColor {
        /** The node has not yet been visited. */
        WHITE,

        /** The node has been discovered and is being processed. */
        GREY,
        
        /** The node and its neighbors have been fully processed. */
        BLACK;
    }

    /** Member nodeColor - nodeColor of the Node used in the algorithm - WHITE, GREY or BLACK */
    private NodeColor nodeColor;

    /** Member d - distance from the source Node - the one from which the algorithm started */
    private double d;

    /** Member pi - parent of the Node - the one that was previously visited by the algorithm */
    private Node pi;

    /** Time of proceed time used by DFS specifically */
    private double f;
    
    /**
     * Constructor taking four paramethers:
     * @param nodeColor nodeColor
     * @param d distance
     * @param pi parent
     * @param f proceed time
     */
    public AlgorithmAddInfo(NodeColor nodeColor, double d, Node pi, double f) {
        this.nodeColor = nodeColor;
        this.d = d; 
        this.pi = pi;
        this.f = f;
    }

    /**
     * Constructor taking three paramethers:
     * @param nodeColor nodeColor
     * @param d distance
     * @param pi parent
     * f = 0 by default;
     */
    public AlgorithmAddInfo(NodeColor nodeColor, double d, Node pi) {
        this(nodeColor, d, pi, 0.0);
    }
    
    /**
     * Constructor taking two paramethers:
     * @param nodeColor nodeColor
     * @param d distance
     * pi = null by default;
     * f = 0.0 by default;
     */
    public AlgorithmAddInfo(NodeColor nodeColor, double d) {
        this(nodeColor, d, null);
    }
    
    /**
     * Constructor taking one paramether:
     * @param nodeColor nodeColor
     * d = 2147483647 by default;
     * pi = null by default;
     * f = 0.0 by default;
     */
    public AlgorithmAddInfo(NodeColor nodeColor) {
        this(nodeColor, Double.POSITIVE_INFINITY);
    }

    /**
     * Constructor taking no paramethers:
     * nodeColor = WHITE  by default;
     * d = 2147483647 by default;
     * pi = null by default;
     * f = 0.0 by default;
     */
    public AlgorithmAddInfo(){
        this(NodeColor.WHITE);
    }

    /**
     * nodeColor setter
     * @param nodeColor new algorithm-processing color
     */
    public void setNodeColor(NodeColor nodeColor) {
        this.nodeColor = nodeColor;
    }

    /**
     * nodeColor getter
     * @return current algorithm-processing color
     */
    public NodeColor getNodeColor() {
        return nodeColor;
    }

    /**
     * d setter
     * @param d new discovery time or distance from the source node
     */
    public void setD(double d) {
        this.d = d;
    }

    /**
     * d getter
     * @return discovery time or distance from the source node
     */
    public double getD() {
        return d;
    }

    /**
     * pi setter
     * @param pi new parent node, or {@code null} when none exists
     */
    public void setPi(Node pi) {
        this.pi = pi;
    }

    /**
     * pi getter
     * @return parent node, or {@code null} when none exists
     */
    public Node getPi() {
        return pi;
    }

    /**
     * f setter
     * @param f new DFS finish time
     */
    public void setF(double f) {
        this.f = f;
    }

    /**
     * f getter
     * @return DFS finish time
     */
    public double getF() {
        return f;
    }

    /**
     * One big setter to set the state of AlgorithmAddInfo used in context of BFS algorithm
     * @param nodeColor new algorithm-processing color
     * @param d new distance from the source node
     * @param pi new parent node, or {@code null} when none exists
     */
    public void setAllBFS(NodeColor nodeColor, double d, Node pi) {
        this.nodeColor = nodeColor;
        this.d = d;
        this.pi = pi;
        this.f = 0.0;
    }

    /**
     * One big setter to set the state of AlgorithmAddInfo used in context of DFS algorithm
     * @param nodeColor new algorithm-processing color
     * @param d new DFS discovery time
     * @param pi new parent node, or {@code null} when none exists
     * @param f new DFS finish time
     */
    public void setAllDFS(NodeColor nodeColor, double d, Node pi, double f) {
        this.nodeColor = nodeColor;
        this.d = d;
        this.pi = pi;
        this.f = f;
    }

    /** Overrided method toString to pretty-print debug info */
	@Override
	public String toString() {
		return "AlgorithmAddInfo{nodeColor= " + nodeColor + ", d= " + d + ", pi= " + pi + "}";
	}

}
