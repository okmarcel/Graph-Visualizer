package dev.GraphVisualizer.algorithms;
import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Map;

import javax.xml.transform.Source;

// /**
//  * enum AlgorithmColor holding three possible stated of BFSNode's algorithmcolor: WHITE, GREY, BLACK
//  */
// enum AlgorithmColor {
//     WHITE, GREY, BLACK;
// }

/**
 * class BFS
 */
public class BFS {
    // /**
    //  * Member Graph graph - graph on which the algorithm executes
    //  */
    // private GraphService service;

    // /**
    //  * Member BFSNode sourceNode - node from which the algorithm starts
    //  */
    // private BFSNode sourceNode;

public static void runBFS(AlgorithmService service, Node sourceNode) {
    service.getState().get(sourceNode).setAll(AlgorithmColor.GREY, 0, null);
    Queue<Node> q = new LinkedList<>();
    q.add(sourceNode);
    while(!q.isEmpty()) { 
        Node u = q.remove();
        for(Node v : service.getService().getAdjacent().get(u)) {
            if(service.getState().get(v).getAlgorithmColor() == AlgorithmColor.WHITE) {
                service.getState().get(v).setAll(AlgorithmColor.GREY, service.getState().get(u).getD() + 1, u);
                q.add(v);
            }
        }
        service.getState().get(u).setAlgorithmColor(AlgorithmColor.BLACK);
    }
}

    // public static void runBFS(AlgorithmService service, Node sourceNode) {
    //     Queue<BFSNode> q = new LinkedList<BFSNode>();
    //     q.add(sourceNode);
    //     while(!q.isEmpty()) { 
    //         BFSNode u = q.remove();
    //         for(Node i : u.getNeighbours()) {
    //             if(((BFSNode) i).getAlgorithmColor() == AlgorithmColor.WHITE) {
    //                 ((BFSNode) i).setAlgorithmColor(AlgorithmColor.GREY); 
    //                 ((BFSNode) i).setD(u.getD() + 1);
    //                 ((BFSNode) i).setPi(u); 
    //                 q.add((BFSNode) i);
    //             }
    //         }
    //         u.setAlgorithmColor(AlgorithmColor.BLACK);
    //     }
    // }
    // /**
    //  * Constructor taking two arguments:
    //  * @param graph graph on which the algorithm executes
    //  * @param sourceNode nodefrom which the algorithm starts
    //  */
    // public BFS(Graph graph, Node sourceNode) {
    //     if(graph == null) {
    //         throw new GraphNotFoundException("You provided null instead of a valid Graph to BFS algorithm");
    //     }
    //     this.graph = initializeGraph(graph);
    //     initializeAllBFSNodes(graph, sourceNode);
    //     this.sourceNode = initializeSourceNode(graph, sourceNode, AlgorithmColor.GREY, 0, null);
    //     initializeAllNodesEdges();
    //     initializeAllBFSEdges(graph);
    // }

    // //TODO add another contructor with some default Node to be taken when only graph provided

    // /**
    //  * Initializes the graph on which the algorith executes
    //  * @param graph
    //  * @return BFS ready copy of original graph
    //  */
    // public Graph initializeGraph(Graph graph) {
    //     Graph BFSGraph = new Graph(graph.getDirected(), graph.getWeighted()); 
    //     return BFSGraph;
    // }

    // /**
    //  * Initializes all BFSNodes and updates the graph accordingly
    //  * @param graph
    //  * @param sourcNode
    //  */
    // public void initializeAllBFSNodes(Graph graph, Node sourceNode) {
    //     for(Node i : graph.getAllNodes()){
    //         if(!i.equals(sourceNode))
    //             this.graph.getAllNodes().add(new BFSNode(i));
    //     }
    // }

    // /**
    //  * Initializes the source node of the graph and updates th egraph accordingly
    //  * @param sourceNode
    //  * @param algorithmcolor
    //  * @param d
    //  * @param pi
    //  * @return Source Node of the BFS algorithm
    //  */
    // public BFSNode initializeSourceNode(Graph graph, Node sourceNode, AlgorithmColor algorithmcolor, int d, BFSNode pi) {
    //     BFSNode start = new BFSNode(sourceNode, algorithmcolor, d, pi);
    //     this.graph.getAllNodes().add(start);
    //     return start;
    // }

    // /**
    //  * Initializes Edges in all Nodes
    //  * @param graph
    //  */
    // public void initializeAllNodesEdges() {
    //     for(Node i : this.graph.getAllNodes()) {
    //         for(Edge j : i.getAdjacentEdges()) {
    //             for(Node k : this.graph.getAllNodes()) {
    //                 if(j.getSource().equals(k)) {
    //                     j.setSource(k);
    //                 }
    //                 if(j.getTarget().equals(k)) {
    //                     j.setTarget(k);
    //                 }
    //             }
    //        }
    //     }
    // }

    // /**
    //  * Initializes Edges in the graph
    //  * @param graph
    //  */
    // public void initializeAllBFSEdges(Graph graph) {
    //     for(Edge i : graph.getAllEdges()) {
    //         Edge e = new Edge(i.getWeight());
    //         for(Node j : graph.getAllNodes()) {
    //             if(i.getSource().equals(j)) {
    //                 e.setSource(j);
    //             }
    //             if(i.getTarget().equals(j)) {
    //                 e.setTarget(j);
    //             }
    //         }
    //         this.graph.getAllEdges().add(e);
    //     }
    // }

    // /**
    //  * Graph getter
    //  * @return
    //  */
    // public Graph getGraph() {
    //     return graph;
    // }
    
    // /**
    //  * SourceNode setter
    //  * @param sourceNode
    //  */
    // public void setSourceNode(BFSNode sourceNode) {
    //     this.sourceNode = sourceNode;
    // }
    
    // /**
    //  * SourceNode getter
    //  * @return
    //  */
    // public BFSNode getSorceNode() {
    //     return sourceNode;
    // }
    
    /**
     * Main logic of the algorithm
     */

}
// /**
//  * class BFSNode inherits after Node
//  * has some additional fields required for the BFS algorithm
//  */
// class BFSNode extends Node {
//     /**
//      * Member algorithmcolor - algorithmcolor of the BFSNode used in the algorithm - WHITE, GREY or BLACK
//      */
//     private AlgorithmColor algorithmcolor;

//     /**
//      * Member d - distance from the source BFSNode from which the algorithm started 
//      */
//     private int d;

//     /**
//      * Member pi - parent of the BFSNode - one that was previously visited by the algorithm
//      */
//     private BFSNode pi;

//     /**
//      * Constructor taking four paramethers:
//      * @param node original Node
//      * @param algorithmcolor algorithmcolor
//      * @param d distance
//      * @param pi parent
//      */
//     public BFSNode(Node node, AlgorithmColor algorithmcolor, int d, BFSNode pi) {
//         super(node.getId(), node.getValue(), node.getPositionX(), node.getPositionY(), node.getAdjacentEdges());
//         this.algorithmcolor = algorithmcolor;
//         this.d = d; 
//         this.pi = pi;
//     }
    
//     /**
//      * Constructor taking three paramethers:
//      * @param node original Node
//      * @param algorithmcolor algorithmcolor
//      * @param d distance
//      * pi = null by default;
//      */
//     public BFSNode(Node node, AlgorithmColor algorithmcolor, int d) {
//         this(node, algorithmcolor, d, null);
//     }
    
//     /**
//      * Constructor taking two paramethers:
//      * @param node original Node
//      * @param algorithmcolor algorithmcolor
//      * d = 0 by default;
//      * pi = null by default;
//      */
//     public BFSNode(Node node, AlgorithmColor algorithmcolor) {
//         this(node, algorithmcolor, 0, null);
//     }

//     /**
//      * Constructor taking one paramether:
//      * @param node original Node
//      * algorithmcolor = WHITE  by default;
//      * d = 0 by default;
//      * pi = null by default;
//      */
//     public BFSNode(Node node){
//         this(node, AlgorithmColor.WHITE, 0, null);
//     }

//     /**
//      * algorithmcolor setter
//      * @param algorithmcolor
//      */
//     public void setAlgorithmColor(AlgorithmColor algorithmcolor) {
//         this.algorithmcolor = algorithmcolor;
//     }

//     /**
//      * algorithmcolor getter
//      * @return
//      */
//     public AlgorithmColor getAlgorithmColor() {
//         return algorithmcolor;
//     }

//     /**
//      * d setter
//      * @param d
//      */
//     public void setD(int d) {
//         this.d = d;
//     }

//     /**
//      * d getter
//      * @return
//      */
//     public int getD() {
//         return d;
//     }

//     /**
//      * pi getter
//      * @param pi
//      */
//     public void setPi(BFSNode pi) {
//         this.pi = pi;
//     }

//     /**
//      * pi getter
//      * @return
//      */
//     public BFSNode getPi() {
//         return pi;
//     }

// 	@Override
// 	public String toString() {
// 		return "BFSNode{algorithmcolor = " + algorithmcolor + "d=" + d + "pi=" + pi + super.toString();
// 	}

//     @Override
// 	public boolean equals(Object node) {
// 		if (this == node)
// 			return true;
// 		if (node instanceof Node n && super.id.equals(n.getId()))
// 			return true;
// 		return false;
// 	}
// }
