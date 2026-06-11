package dev.GraphVisualizer.models;

/** 
 * Custom Exception thrown when UndirectedGrpah or DirectedGraph is constructed with edge which weight is different than 1.0
 *  Thrown as well when such edge added to the graph 
*/
public final class WeightedEdgeException extends RuntimeException {
    /**
     * Constructor taking the exception message.
     * @param message error description
     */
    public WeightedEdgeException(String message) {
        super(message);
    }
}
