package dev.GraphVisualizer;

import dev.GraphVisualizer.models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {
    private Node a, b;
    private Edge ab;

    @BeforeEach
    public void setUp() {
        a = new Node("A", 0.0, 0.0);
        b = new Node("B", 1.0, 0.0);
        ab = new Edge(a, b);
    }

    // DirectedGraph tests
    @Test
    @DisplayName("DirectedGraph: adding a node increases node count by one")
    public void testDirectedGraphAddNode() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        assertEquals(1, g.getNumberOfNodes());
    }

    @Test
    @DisplayName("DirectedGraph: adding an edge increases edge count by one")
    public void testDirectedGraphAddEdge() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        g.addNode(b);
        g.addEdge(ab);
        assertEquals(1, g.getNumberOfEdges());
    }

    @Test
    @DisplayName("DirectedGraph: throws WeightedEdgeException when a weighted edge is added")
    public void testDirectedGraphRejectsWeightedEdge() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        g.addNode(b);
        assertThrows(WeightedEdgeException.class, () -> g.addEdge(new Edge(a, b, 5.0)));
    }

    @Test
    @DisplayName("DirectedGraph: adjacency list contains edge only in the source-to-target direction")
    public void testDirectedGraphAdjacentOnlyOneDirection() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        g.addNode(b);
        g.addEdge(ab);
        g.buildAdjacent();
        assertTrue(g.getAdjacent().get(a).contains(b));
        assertFalse(g.getAdjacent().get(b).contains(a));
    }

    @Test
    @DisplayName("DirectedGraph: removing a node reduces node count to zero")
    public void testDirectedGraphRemoveNode() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        g.removeNode(a);
        assertEquals(0, g.getNumberOfNodes());
    }

    @Test
    @DisplayName("DirectedGraph: cache flag is set to true after adding a node")
    public void testDirectedGraphCacheSetOnAdd() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        assertTrue(g.getCache());
    }

    // UndirectedGraph tests
    @Test
    @DisplayName("UndirectedGraph: adjacency list contains edge in both directions")
    public void testUndirectedGraphAdjacentBothDirections() {
        UndirectedGraph g = new UndirectedGraph();
        g.addNode(a);
        g.addNode(b);
        g.addEdge(ab);
        g.buildAdjacent();
        assertTrue(g.getAdjacent().get(a).contains(b));
        assertTrue(g.getAdjacent().get(b).contains(a));
    }

    @Test
    @DisplayName("UndirectedGraph: throws WeightedEdgeException when a weighted edge is added")
    public void testUndirectedGraphRejectsWeightedEdge() {
        UndirectedGraph g = new UndirectedGraph();
        g.addNode(a);
        g.addNode(b);
        assertThrows(WeightedEdgeException.class, () -> g.addEdge(new Edge(a, b, 5.0)));
    }

    // WeightedDirectedGraph tests
    @Test
    @DisplayName("WeightedDirectedGraph: accepts edges with custom weights without throwing")
    public void testWeightedDirectedGraphAcceptsWeightedEdge() {
        WeightedDirectedGraph g = new WeightedDirectedGraph();
        g.addNode(a);
        g.addNode(b);
        assertDoesNotThrow(() -> g.addEdge(new Edge(a, b, 5.0)));
    }

    @Test
    @DisplayName("WeightedDirectedGraph: adjacency list contains edge only in the source-to-target direction")
    public void testWeightedDirectedGraphAdjacentOneDirection() {
        WeightedDirectedGraph g = new WeightedDirectedGraph();
        g.addNode(a);
        g.addNode(b);
        g.addEdge(new Edge(a, b, 2.0));
        g.buildAdjacent();
        assertTrue(g.getAdjacent().get(a).contains(b));
        assertFalse(g.getAdjacent().get(b).contains(a));
    }

    // WeightedUndirectedGraph tests
    @Test
    @DisplayName("WeightedUndirectedGraph: adjacency list contains edge in both directions")
    public void testWeightedUndirectedGraphAdjacentBothDirections() {
        WeightedUndirectedGraph g = new WeightedUndirectedGraph();
        g.addNode(a);
        g.addNode(b);
        g.addEdge(new Edge(a, b, 3.0));
        g.buildAdjacent();
        assertTrue(g.getAdjacent().get(a).contains(b));
        assertTrue(g.getAdjacent().get(b).contains(a));
    }

    @Test
    @DisplayName("rebuildAdjacent clears the cache flag after rebuilding the adjacency list")
    public void testRebuildAdjacentResetsCache() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        g.addNode(b);
        g.addEdge(ab);
        assertTrue(g.getCache());
        g.rebuildAdjacent();
        assertFalse(g.getCache());
    }
}