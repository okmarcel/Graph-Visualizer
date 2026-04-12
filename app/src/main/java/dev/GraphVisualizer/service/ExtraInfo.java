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
    private int d;

    /**
     * Member pi - parent of the Node - the one that was previously visited by the algorithm
     */
    private Node pi;

    /**
     * Constructor taking three paramethers:
     * @param algorithmcolor algorithmcolor
     * @param d distance
     * @param pi parent
     */
    public ExtraInfo(AlgorithmColor algorithmcolor, int d, Node pi) {
        this.algorithmcolor = algorithmcolor;
        this.d = d; 
        this.pi = pi;
    }
    
    /**
     * Constructor taking twoe paramethers:
     * @param algorithmcolor algorithmcolor
     * @param d distance
     * pi = null by default;
     */
    public ExtraInfo(AlgorithmColor algorithmcolor, int d) {
        this(algorithmcolor, d, null);
    }
    
    /**
     * Constructor taking one paramether:
     * @param algorithmcolor algorithmcolor
     * d = 2147483647 by default;
     * pi = null by default;
     */
    public ExtraInfo(AlgorithmColor algorithmcolor) {
        this(algorithmcolor, Integer.MAX_VALUE, null);
    }

    /**
     * Constructor taking no paramethers:
     * algorithmcolor = WHITE  by default;
     * d = 2147483647 by default;
     * pi = null by default;
     */
    public ExtraInfo(){
        this(AlgorithmColor.WHITE, Integer.MAX_VALUE, null);
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
    public void setD(int d) {
        this.d = d;
    }

    /**
     * d getter
     * @return
     */
    public int getD() {
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
     * One big setter to set the state of ExtraInfo
     * @param algorithmcolor
     * @param d
     * @param pi
     */
    public void setAll(AlgorithmColor algorithmcolor, int d, Node pi) {
        this.algorithmcolor = algorithmcolor;
        this.d = d;
        this.pi = pi;
    }

	@Override
	public String toString() {
		return "ExtraInfo{algorithmcolor = " + algorithmcolor + "d=" + d + "pi=" + pi + "}";
	}

    // @Override
	// public boolean equals(Object node) {
	// 	if (this == node)
	// 		return true;
	// 	if (node instanceof Node n && super.id.equals(n.getId()))
	// 		return true;
	// 	return false;
	// }
}

