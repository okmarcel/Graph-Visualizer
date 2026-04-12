package dev.GraphVisualizer;

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
        algorithmService.runDFS(a);
        for (Node node : graph.getAllNodes()) {
            assertEquals(AlgorithmColor.BLACK, algorithmService.getState().get(node).getAlgorithmColor(),
                "Node " + node.getLabel() + " should be BLACK after DFS");
        }
    }

    @Test
    public void testDiscoveryBeforeFinish() {
        algorithmService.runDFS(a);
        for (Node node : graph.getAllNodes()) {
            assertTrue(algorithmService.getState().get(node).getD() < algorithmService.getState().get(node).getF(),
                "Discovery time should be less than finish time for " + node.getLabel());
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

        Graph undirected = new Graph(false, false);
        undirected.addNode(x);
        undirected.addNode(y);
        undirected.addNode(z);
        undirected.addEdge(new Edge(x, y));
        undirected.addEdge(new Edge(y, z));

        AlgorithmService as = new AlgorithmService(new GraphService(undirected));
        as.runDFS(z);

        assertEquals(AlgorithmColor.BLACK, as.getState().get(x).getAlgorithmColor());
        assertEquals(AlgorithmColor.BLACK, as.getState().get(y).getAlgorithmColor());
        assertEquals(AlgorithmColor.BLACK, as.getState().get(z).getAlgorithmColor());
    }
}