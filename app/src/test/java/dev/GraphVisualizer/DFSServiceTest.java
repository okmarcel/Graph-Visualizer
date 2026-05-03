package dev.GraphVisualizer;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class DFSServiceTest {

    private DirectedGraph graph;
    private GraphService graphService;
    private AlgorithmService algorithmService;
    private Node a, b, c, d;

    @BeforeEach
    public void setUp() {
        a = new Node("A", 0.0, 0.0);
        b = new Node("B", 1.0, 0.0);
        c = new Node("C", 2.0, 0.0);
        d = new Node("D", 3.0, 0.0);

        graph = new DirectedGraph();
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addEdge(new Edge(a, b));
        graph.addEdge(new Edge(a, c));
        graph.addEdge(new Edge(b, d));

        graphService = new GraphService(graph);
        algorithmService = new AlgorithmService(graphService);
    }

    @Test
    public void testAllNodesVisited() {
        algorithmService.runDFS(a);
        for (Node node : graph.getAllNodes()) {
            assertEquals(AlgorithmAddInfo.NodeColor.BLACK,
                algorithmService.getState().get(node).getNodeColor(),
                "Node " + node.getLabel() + " should be BLACK");
        }
    }

    @Test
    public void testDiscoveryBeforeFinish() {
        algorithmService.runDFS(a);
        for (Node node : graph.getAllNodes()) {
           assertTrue(algorithmService.getState().get(node).getD() < 
    algorithmService.getState().get(node).getF());
        }
    }

    @Test
    public void testTimesArePositive() {
        algorithmService.runDFS(a);
        for (Node node : graph.getAllNodes()) {
            assertTrue(algorithmService.getState().get(node).getD() > 0);
            assertTrue(algorithmService.getState().get(node).getF() > 0);
        }
    }

    @Test
    public void testParentOfD() {
        algorithmService.runDFS(a);
        System.out.println("Parent of D: " + algorithmService.getState().get(d).getPi().getLabel());
        assertNull(algorithmService.getState().get(a).getPi());
        assertEquals(b, algorithmService.getState().get(d).getPi());
    }

    @Test
    public void testTimesResetBetweenRuns() {
        algorithmService.runDFS(a);
        double firstD = algorithmService.getState().get(a).getD();
        algorithmService.runDFS(a);
        double secondD = algorithmService.getState().get(a).getD();
        assertEquals(firstD, secondD);
    }

    @Test
    public void testUndirectedDFS() {
        Node x = new Node("X", 0.0, 0.0);
        Node y = new Node("Y", 1.0, 0.0);
        Node z = new Node("Z", 2.0, 0.0);

        UndirectedGraph g = new UndirectedGraph();
        g.addNode(x);
        g.addNode(y);
        g.addNode(z);
        g.addEdge(new Edge(x, y));
        g.addEdge(new Edge(y, z));

        AlgorithmService as = new AlgorithmService(new GraphService(g));
        as.runDFS(z);

        assertEquals(AlgorithmAddInfo.NodeColor.BLACK, as.getState().get(x).getNodeColor());
        assertEquals(AlgorithmAddInfo.NodeColor.BLACK, as.getState().get(y).getNodeColor());
        assertEquals(AlgorithmAddInfo.NodeColor.BLACK, as.getState().get(z).getNodeColor());
    }
}