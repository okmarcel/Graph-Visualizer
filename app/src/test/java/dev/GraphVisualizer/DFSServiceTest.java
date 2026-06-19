package dev.GraphVisualizer;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("DFS visits every node and marks it BLACK")
    public void testAllNodesVisited() {
        algorithmService.runDFS(a);
        for (Node node : graph.getAllNodes()) {
            assertEquals(AlgorithmAddInfo.NodeColor.BLACK,
                algorithmService.getState().get(node).getNodeColor(),
                "Node " + node.getLabel() + " should be BLACK");
        }
    }

    @Test
    @DisplayName("Discovery time d is always less than finish time f for each node")
    public void testDiscoveryBeforeFinish() {
        algorithmService.runDFS(a);
        for (Node node : graph.getAllNodes()) {
           assertTrue(algorithmService.getState().get(node).getD() < 
    algorithmService.getState().get(node).getF());
        }
    }

    @Test
    @DisplayName("Both discovery and finish times are positive after DFS")
    public void testTimesArePositive() {
        algorithmService.runDFS(a);
        for (Node node : graph.getAllNodes()) {
            assertTrue(algorithmService.getState().get(node).getD() > 0);
            assertTrue(algorithmService.getState().get(node).getF() > 0);
        }
    }

    @Test
    @DisplayName("Running DFS twice from the same source produces identical discovery times")
    public void testTimesResetBetweenRuns() {
        algorithmService.runDFS(a);
        double firstD = algorithmService.getState().get(a).getD();
        algorithmService.runDFS(a);
        double secondD = algorithmService.getState().get(a).getD();
        assertEquals(firstD, secondD);
    }

    @Test
    @DisplayName("Full-forest DFS visits nodes unreachable from source in separate DFS trees")
    public void testFullForestVisitsUnreachableNodes() {
        Node f = new Node("F", 4.0, 0.0);
        Node g = new Node("G", 5.0, 0.0);
        graph.addNode(f);
        graph.addNode(g);
        graph.addEdge(new Edge(f, a));
        graph.addEdge(new Edge(g, a));

        GraphService gs = new GraphService(graph);
        AlgorithmService as = new AlgorithmService(gs);
        as.runDFS(a);

        assertEquals(AlgorithmAddInfo.NodeColor.BLACK, as.getState().get(f).getNodeColor(),
            "Full-forest DFS visits F eventually");
        assertEquals(AlgorithmAddInfo.NodeColor.BLACK, as.getState().get(g).getNodeColor(),
            "Full-forest DFS visits G eventually");
        assertNull(as.getState().get(f).getPi(), "F is not reachable from A so pi must be null");
        assertNull(as.getState().get(g).getPi(), "G is not reachable from A so pi must be null");
    }

    @Test
    @DisplayName("DFS works correctly on an undirected graph")
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