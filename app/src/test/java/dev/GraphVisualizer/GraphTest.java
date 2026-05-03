package dev.GraphVisualizer;

import dev.GraphVisualizer.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class GraphTest {

    private Node a, b, c;
    private Edge ab, bc;

    @BeforeEach
    public void setUp() {
        a = new Node("A", 0.0, 0.0);
        b = new Node("B", 1.0, 0.0);
        c = new Node("C", 2.0, 0.0);
        ab = new Edge(a, b);
        bc = new Edge(b, c);
    }

    // DirectedGraph tests
    @Test
    public void testDirectedGraphAddNode() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        assertEquals(1, g.getNumberOfNodes());
    }

    @Test
    public void testDirectedGraphAddEdge() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        g.addNode(b);
        g.addEdge(ab);
        assertEquals(1, g.getNumberOfEdges());
    }

    @Test
    public void testDirectedGraphRejectsWeightedEdge() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        g.addNode(b);
        assertThrows(WeightedEdgeException.class, () -> g.addEdge(new Edge(a, b, 5.0)));
    }

    @Test
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
    public void testDirectedGraphRemoveNode() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        g.removeNode(a);
        assertEquals(0, g.getNumberOfNodes());
    }

    @Test
    public void testDirectedGraphCacheSetOnAdd() {
        DirectedGraph g = new DirectedGraph();
        g.addNode(a);
        assertTrue(g.getCache());
    }

    // UndirectedGraph tests
    @Test
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
    public void testUndirectedGraphRejectsWeightedEdge() {
        UndirectedGraph g = new UndirectedGraph();
        g.addNode(a);
        g.addNode(b);
        assertThrows(WeightedEdgeException.class, () -> g.addEdge(new Edge(a, b, 5.0)));
    }

    // WeightedDirectedGraph tests
    @Test
    public void testWeightedDirectedGraphAcceptsWeightedEdge() {
        WeightedDirectedGraph g = new WeightedDirectedGraph();
        g.addNode(a);
        g.addNode(b);
        assertDoesNotThrow(() -> g.addEdge(new Edge(a, b, 5.0)));
    }

    @Test
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