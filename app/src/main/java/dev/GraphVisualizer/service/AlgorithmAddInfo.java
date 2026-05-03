package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;

/** Class AlgorithmAddInfo provides some additional fields required for the algorithms */
public class AlgorithmAddInfo {
    /** Member nodeColor - nodeColor of the Node used in the algorithm - WHITE, GREY or BLACK */
    private NodeColor nodeColor;

    /** Member d - distance from the source Node - the one from which the algorithm started */
    private double d;

    /** Member pi - parent of the Node - the one that was previously visited by the algorithm */
    private Node pi;

    /** Time of proceed time used by DFS specifically */
    private double f;
    
    /**
     * Enum NodeColor used by BFS and DFS algorithms holds three possible states of Node's nodeColor field:
     * WHITE, GREY or BLACK
     */
    public enum NodeColor {
        WHITE, GREY, BLACK;
    }

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
     * @param nodeColor
     */
    public void setNodeColor(NodeColor nodeColor) {
        this.nodeColor = nodeColor;
    }

    /**
     * nodeColor getter
     * @return
     */
    public NodeColor getNodeColor() {
        return nodeColor;
    }

    /**
     * d setter
     * @param d
     */
    public void setD(double d) {
        this.d = d;
    }

    /**
     * d getter
     * @return
     */
    public double getD() {
        return d;
    }

    /**
     * pi getter
     * @param pi
     */
    public void setPi(Node pi) {
        this.pi = pi;
    }

    /**
     * pi getter
     * @return
     */
    public Node getPi() {
        return pi;
    }

    /**
     * f setter
     * @param f
     */
    public void setF(double f) {
        this.f = f;
    }

    /**
     * f getter
     */
    public double getF() {
        return f;
    }

    /**
     * One big setter to set the state of AlgorithmAddInfo used in context of BFS algorithm
     * @param nodeColor
     * @param d
     * @param pi
     */
    public void setAllBFS(NodeColor nodeColor, double d, Node pi) {
        this.nodeColor = nodeColor;
        this.d = d;
        this.pi = pi;
        this.f = 0.0;
    }

    /**
     * One big setter to set the state of AlgorithmAddInfo used in context of DFS algorithm
     * @param nodeColor
     * @param d
     * @param pi
     * @param f
     */
    public void setAllDFS(NodeColor nodeColor, double d, Node pi, double f) {
        this.nodeColor = nodeColor;
        this.d = d;
        this.pi = pi;
        this.f = f;
    }

    /**
     * Overrided method toString to pretty-print debug info
     */
	@Override
	public String toString() {
		return "AlgorithmAddInfo{nodeColor = " + nodeColor + "d=" + d + "pi=" + pi + "}";
	}

}

