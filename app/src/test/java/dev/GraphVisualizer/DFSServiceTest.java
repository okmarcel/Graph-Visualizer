package dev.GraphVisualizer.algorithms;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class DFSServiceTest {

    private Graph graph;
    private GraphService graphService;
    private AlgorithmService algorithmService;
    private Node a, b, c, d;

    @BeforeEach
    public void setUp() {
        a = new Node("A", 0.0, 0.0);
        b = new Node("B", 1.0, 0.0);
        c = new Node("C", 2.0, 0.0);
        d = new Node("D", 3.0, 0.0);

        // A → B → D, A → C
        Edge ab = new Edge(a, b);
        Edge ac = new Edge(a, c);
        Edge bd = new Edge(b, d);

        graph = new Graph(true, false);
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addEdge(ab);
        graph.addEdge(ac);
        graph.addEdge(bd);

        graphService = new GraphService(graph);
        algorithmService = new AlgorithmService(graphService);
    }

    @Test
    public void testAllNodesVisited() {
        DFS.runDFS(algorithmService, a);

        for (Node node : graph.getAllNodes()) {
            assertEquals(AlgorithmColor.BLACK, algorithmService.getState().get(node).getAlgorithmColor(),
                "Node " + node.getLabel() + " should be BLACK after DFS");
        }
    }

    @Test
    public void testDiscoveryTimesArePositive() {
        DFS.runDFS(algorithmService, a);

        for (Node node : graph.getAllNodes()) {
            assertTrue(algorithmService.getState().get(node).getD() > 0,
                "Discovery time of " + node.getLabel() + " should be positive");
        }
    }

    @Test
    public void testFinishTimesArePositive() {
        DFS.runDFS(algorithmService, a);

        for (Node node : graph.getAllNodes()) {
            assertTrue(algorithmService.getState().get(node).getF() > 0,
                "Finish time of " + node.getLabel() + " should be positive");
        }
    }

    @Test
    public void testDiscoveryBeforeFinish() {
        DFS.runDFS(algorithmService, a);

        for (Node node : graph.getAllNodes()) {
            assertTrue(algorithmService.getState().get(node).getD() < algorithmService.getState().get(node).getF(),
                "Discovery time should be less than finish time for " + node.getLabel());
        }
    }

    @Test
    public void testParents() {
        DFS.runDFS(algorithmService, a);

        assertNull(algorithmService.getState().get(a).getPi(), "Source should have no parent");
        assertEquals(a, algorithmService.getState().get(b).getPi(), "Parent of B should be A");
        assertEquals(b, algorithmService.getState().get(d).getPi(), "Parent of D should be B");
    }

    @Test
    public void testTimesResetBetweenRuns() {
        DFS.runDFS(algorithmService, a);
        int firstRunTime = algorithmService.getState().get(a).getD();

        DFS.runDFS(algorithmService, a);
        int secondRunTime = algorithmService.getState().get(a).getD();

        assertEquals(firstRunTime, secondRunTime, "Times should be the same after reset");
    }

    @Test
    public void testUndirectedDFS() {
        Node x = new Node("X", 0.0, 0.0);
        Node y = new Node("Y", 1.0, 0.0);
        Node z = new Node("Z", 2.0, 0.0);

        Graph undirected = new Graph(false, false);
        undirected.addNode(x);
        undirected.addNode(y);
        undirected.addNode(z);
        undirected.addEdge(new Edge(x, y));
        undirected.addEdge(new Edge(y, z));

        GraphService gs = new GraphService(undirected);
        AlgorithmService as = new AlgorithmService(gs);

        DFS.runDFS(as, z);

        assertEquals(AlgorithmColor.BLACK, as.getState().get(x).getAlgorithmColor(), "X should be visited");
        assertEquals(AlgorithmColor.BLACK, as.getState().get(y).getAlgorithmColor(), "Y should be visited");
        assertEquals(AlgorithmColor.BLACK, as.getState().get(z).getAlgorithmColor(), "Z should be visited");
    }
}