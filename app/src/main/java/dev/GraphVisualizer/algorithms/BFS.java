package dev.GraphVisualizer.algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.xml.transform.Source;

import dev.GraphVisualizer.models.*;

/**
 * enum Color holding three possible stated of BFSNode's color: WHITE, GREY, BLACK
 */
enum Color {
    WHITE, GREY, BLACK;
}

/**
 * class BFS
 */
public class BFS {
    /**
     * Member Graph graph - graph on which the algorithm executes
     */
    private Graph graph;

    /**
     * Member BFSNode sourceNode - node from which the algorithm starts
     */
    private BFSNode sourceNode;

    /**
     * Constructor taking two arguments:
     * @param graph graph on which the algorithm executes
     * @param sourceNode nodefrom which the algorithm starts
     */
    public BFS(Graph graph, Node sourceNode) {
        if(graph == null) {
            throw new GraphNotFoundException("You provided null instead of a valid Graph to BFS algorithm");
        }
        this.graph = initializeGraph(graph);
        initializeAllBFSNodes(graph, sourceNode);
        this.sourceNode = initializeSourceNode(graph, sourceNode, Color.GREY, 0, null);
        initializeAllNodesEdges();
        initializeAllBFSEdges(graph);
    }

    //TODO add another contructor with some default Node to be taken when only graph provided

    /**
     * Initializes the graph on which the algorith executes
     * @param graph
     * @return BFS ready copy of original graph
     */
    public Graph initializeGraph(Graph graph) {
        Graph BFSGraph = new Graph(graph.getDirected(), graph.getWeighted()); 
        return BFSGraph;
    }

    /**
     * Initializes all BFSNodes and updates the graph accordingly
     * @param graph
     * @param sourcNode
     */
    public void initializeAllBFSNodes(Graph graph, Node sourceNode) {
        for(Node i : graph.getAllNodes()){
            if(!i.equals(sourceNode))
                this.graph.getAllNodes().add(new BFSNode(i));
        }
    }

    /**
     * Initializes the source node of the graph and updates th egraph accordingly
     * @param sourceNode
     * @param color
     * @param d
     * @param pi
     * @return Source Node of the BFS algorithm
     */
    public BFSNode initializeSourceNode(Graph graph, Node sourceNode, Color color, int d, BFSNode pi) {
        BFSNode start = new BFSNode(sourceNode, color, d, pi);
        this.graph.getAllNodes().add(start);
        return start;
    }

    /**
     * Initializes Edges in all Nodes
     * @param graph
     */
    public void initializeAllNodesEdges() {
        for(Node i : this.graph.getAllNodes()) {
            for(Edge j : i.getAdjacentEdges()) {
                for(Node k : this.graph.getAllNodes()) {
                    if(j.getSource().equals(k)) {
                        j.setSource(k);
                    }
                    if(j.getTarget().equals(k)) {
                        j.setTarget(k);
                    }
                }
           }
        }
    }

    /**
     * Initializes Edges in the graph
     * @param graph
     */
    public void initializeAllBFSEdges(Graph graph) {
        for(Edge i : graph.getAllEdges()) {
            Edge e = new Edge(i.getWeight());
            for(Node j : graph.getAllNodes()) {
                if(i.getSource().equals(j)) {
                    e.setSource(j);
                }
                if(i.getTarget().equals(j)) {
                    e.setTarget(j);
                }
            }
            this.graph.getAllEdges().add(e);
        }
    }

    /**
     * Graph setter
     * @param graph
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }
    
    /**
     * Graph getter
     * @return
     */
    public Graph getGraph() {
        return graph;
    }
    
    /**
     * SourceNode setter
     * @param sourceNode
     */
    public void setSourceNode(BFSNode sourceNode) {
        this.sourceNode = sourceNode;
    }
    
    /**
     * SourceNode getter
     * @return
     */
    public BFSNode getSorceNode() {
        return sourceNode;
    }
    
    /**
     * Main logic of the algorithm
     */
    public void runBFS() {
        Queue<BFSNode> q = new LinkedList<BFSNode>();
        q.add(sourceNode);
        while(!q.isEmpty()) { 
            BFSNode u = q.remove();
            for(Node i : u.getNeighbours()) {
                if(((BFSNode) i).getColor() == Color.WHITE) {
                    ((BFSNode) i).setColor(Color.GREY); 
                    ((BFSNode) i).setD(u.getD() + 1);
                    ((BFSNode) i).setPi(u); 
                    q.add((BFSNode) i);
                }
            }
            u.setColor(Color.BLACK);
        }
    }
}
/**
 * class BFSNode inherits after Node
 * has some additional fields required for the BFS algorithm
 */
class BFSNode extends Node {
    /**
     * Member color - color of the BFSNode used in the algorithm - WHITE, GREY or BLACK
     */
    private Color color;

    /**
     * Member d - distance from the source BFSNode from which the algorithm started 
     */
    private int d;

    /**
     * Member pi - parent of the BFSNode - one that was previously visited by the algorithm
     */
    private BFSNode pi;

    /**
     * Constructor taking four paramethers:
     * @param node original Node
     * @param color color
     * @param d distance
     * @param pi parent
     */
    public BFSNode(Node node, Color color, int d, BFSNode pi) {
        super(node.getId(), node.getValue(), node.getPositionX(), node.getPositionY(), node.getAdjacentEdges());
        this.color = color;
        this.d = d; 
        this.pi = pi;
    }
    
    /**
     * Constructor taking three paramethers:
     * @param node original Node
     * @param color color
     * @param d distance
     * pi = null by default;
     */
    public BFSNode(Node node, Color color, int d) {
        this(node, color, d, null);
    }
    
    /**
     * Constructor taking two paramethers:
     * @param node original Node
     * @param color color
     * d = 0 by default;
     * pi = null by default;
     */
    public BFSNode(Node node, Color color) {
        this(node, color, 0, null);
    }

    /**
     * Constructor taking one paramether:
     * @param node original Node
     * color = WHITE  by default;
     * d = 0 by default;
     * pi = null by default;
     */
    public BFSNode(Node node){
        this(node, Color.WHITE, 0, null);
    }

    /**
     * color setter
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * color getter
     * @return
     */
    public Color getColor() {
        return color;
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
    public void setPi(BFSNode pi) {
        this.pi = pi;
    }

    /**
     * pi getter
     * @return
     */
    public BFSNode getPi() {
        return pi;
    }

	@Override
	public String toString() {
		return "BFSNode{color = " + color + "d=" + d + "pi=" + pi + super.toString();
	}

    @Override
	public boolean equals(Object node) {
		if (this == node)
			return true;
		if (node instanceof Node n && super.id.equals(n.getId()))
			return true;
		return false;
	}
}
