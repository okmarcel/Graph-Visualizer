package dev.GraphVisualizer.models;

/** Custom Exception thrown when UndirectedGrpah or DirectedGraph is constructed with edge which weight is different than 1.0
 *  Thrown as well when such edge added to the graph 
*/
public class WeightedEdgeException extends RuntimeException {
    public WeightedEdgeException(String message) {
        super(message);
    }
}