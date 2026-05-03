package dev.GraphVisualizer;

import dev.GraphVisualizer.algorithms.*;
import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class DijkstraServiceTest {

    private WeightedDirectedGraph graph;
    private GraphService graphService;
    private AlgorithmService algorithmService;
    private Node a, b, c, d;

    @BeforeEach
    public void setUp() {
        a = new Node("A", 0.0, 0.0);
        b = new Node("B", 1.0, 0.0);
        c = new Node("C", 2.0, 0.0);
        d = new Node("D", 3.0, 0.0);

        // A -2-> B, A -4-> C, B -1-> D, C -1-> D
        graph = new WeightedDirectedGraph();
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addEdge(new Edge(a, b, 2.0));
        graph.addEdge(new Edge(a, c, 4.0));
        graph.addEdge(new Edge(b, d, 1.0));
        graph.addEdge(new Edge(c, d, 1.0));

        graphService = new GraphService(graph);
        algorithmService = new AlgorithmService(graphService);
    }

    @Test
    public void testDistances() {
        algorithmService.runDijkstra(a);
        assertEquals(0.0, algorithmService.getState().get(a).getD());
        assertEquals(2.0, algorithmService.getState().get(b).getD());
        assertEquals(4.0, algorithmService.getState().get(c).getD());
        assertEquals(3.0, algorithmService.getState().get(d).getD());
    }

    @Test
    public void testParents() {
        algorithmService.runDijkstra(a);
        assertNull(algorithmService.getState().get(a).getPi());
        assertEquals(a, algorithmService.getState().get(b).getPi());
        assertEquals(a, algorithmService.getState().get(c).getPi());
        assertEquals(b, algorithmService.getState().get(d).getPi());
    }

    @Test
    public void testUnreachableNode() {
        Node e = new Node("E", 4.0, 0.0);
        graph.addNode(e);
        GraphService gs = new GraphService(graph);
        AlgorithmService as = new AlgorithmService(gs);
        as.runDijkstra(a);
        assertEquals(Double.POSITIVE_INFINITY, as.getState().get(e).getD());
    }

    @Test
    public void testNegativeWeightThrows() {
        WeightedDirectedGraph g = new WeightedDirectedGraph();
        Node x = new Node("X", 0.0, 0.0);
        Node y = new Node("Y", 1.0, 0.0);
        g.addNode(x);
        g.addNode(y);
        g.addEdge(new Edge(x, y, -1.0));
        AlgorithmService as = new AlgorithmService(new GraphService(g));
        assertThrows(NegativeWeightException.class, () -> as.runDijkstra(x));
    }

    @Test
    public void testUnweightedGraphThrows() {
        DirectedGraph g = new DirectedGraph();
        Node x = new Node("X", 0.0, 0.0);
        g.addNode(x);
        AlgorithmService as = new AlgorithmService(new GraphService(g));
        assertThrows(UnweightedGraphException.class, () -> as.runDijkstra(x));
    }

    @Test
    public void testStateResetsBeforeSecondRun() {
        algorithmService.runDijkstra(a);
        algorithmService.runDijkstra(b);
        assertEquals(0.0, algorithmService.getState().get(b).getD());
        assertEquals(Double.POSITIVE_INFINITY, algorithmService.getState().get(a).getD());
    }

    @Test
    public void testWeightedUndirectedGraph() {
        Node x = new Node("X", 0.0, 0.0);
        Node y = new Node("Y", 1.0, 0.0);
        Node z = new Node("Z", 2.0, 0.0);

        WeightedUndirectedGraph g = new WeightedUndirectedGraph();
        g.addNode(x);
        g.addNode(y);
        g.addNode(z);
        g.addEdge(new Edge(x, y, 1.0));
        g.addEdge(new Edge(y, z, 2.0));

        AlgorithmService as = new AlgorithmService(new GraphService(g));
        as.runDijkstra(x);

        assertEquals(0.0, as.getState().get(x).getD());
        assertEquals(1.0, as.getState().get(y).getD());
        assertEquals(3.0, as.getState().get(z).getD());
    }
}