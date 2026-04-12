package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;

/**
 * class ExtraInfo provides some additional fields required for the algorithms
 */
public class ExtraInfo {
    /**
     * Member algorithmcolor - algorithmcolor of the Node used in the algorithm - WHITE, GREY or BLACK
     */
    private AlgorithmColor algorithmcolor;

    /**
     * Member d - distance from the source Node - the one from which the algorithm started 
     */
    private double d;

    /**
     * Member pi - parent of the Node - the one that was previously visited by the algorithm
     */
    private Node pi;

    /**
     * Time of proceed time used by DFS specifically
     */
    private double f;
    
    /**
     * Constructor taking four paramethers:
     * @param algorithmcolor algorithmcolor
     * @param d distance
     * @param pi parent
     * @param f proceed time
     */
    public ExtraInfo(AlgorithmColor algorithmcolor, double d, Node pi, double f) {
        this.algorithmcolor = algorithmcolor;
        this.d = d; 
        this.pi = pi;
        this.f = f;
    }

    /**
     * Constructor taking three paramethers:
     * @param algorithmcolor algorithmcolor
     * @param d distance
     * @param pi parent
     * f = 0 by default;
     */
    public ExtraInfo(AlgorithmColor algorithmcolor, double d, Node pi) {
        this(algorithmcolor, d, pi, 0.0);
    }
    
    /**
     * Constructor taking two paramethers:
     * @param algorithmcolor algorithmcolor
     * @param d distance
     * pi = null by default;
     * f = 0.0 by default;
     */
    public ExtraInfo(AlgorithmColor algorithmcolor, double d) {
        this(algorithmcolor, d, null);
    }
    
    /**
     * Constructor taking one paramether:
     * @param algorithmcolor algorithmcolor
     * d = 2147483647 by default;
     * pi = null by default;
     * f = 0.0 by default;
     */
    public ExtraInfo(AlgorithmColor algorithmcolor) {
        this(algorithmcolor, Double.POSITIVE_INFINITY);
    }

    /**
     * Constructor taking no paramethers:
     * algorithmcolor = WHITE  by default;
     * d = 2147483647 by default;
     * pi = null by default;
     * f = 0.0 by default;
     */
    public ExtraInfo(){
        this(AlgorithmColor.WHITE);
    }

    /**
     * algorithmcolor setter
     * @param algorithmcolor
     */
    public void setAlgorithmColor(AlgorithmColor algorithmcolor) {
        this.algorithmcolor = algorithmcolor;
    }

    /**
     * algorithmcolor getter
     * @return
     */
    public AlgorithmColor getAlgorithmColor() {
        return algorithmcolor;
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
     * One big setter to set the state of ExtraInfo used in context of BFS algorithm
     * @param algorithmcolor
     * @param d
     * @param pi
     */
    public void setAllBFS(AlgorithmColor algorithmcolor, double d, Node pi) {
        this.algorithmcolor = algorithmcolor;
        this.d = d;
        this.pi = pi;
        this.f = 0.0;
    }

    /**
     * One big setter to set the state of ExtraInfo used in context of DFS algorithm
     * @param algorithmcolor
     * @param d
     * @param pi
     * @param f
     */
    public void setAllDFS(AlgorithmColor algorithmcolor, double d, Node pi, double f) {
        this.algorithmcolor = algorithmcolor;
        this.d = d;
        this.pi = pi;
        this.f = f;
    }

    /**
     * Overrided method toString to pretty-print debug info
     */
	@Override
	public String toString() {
		return "ExtraInfo{algorithmcolor = " + algorithmcolor + "d=" + d + "pi=" + pi + "}";
	}

}

