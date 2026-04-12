package dev.GraphVisualizer;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class BFSServiceTest {

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
    public void testInitialStateIsWhite() {
        for (Node node : graph.getAllNodes()) {
            assertEquals(AlgorithmColor.WHITE, algorithmService.getState().get(node).getAlgorithmColor());
        }
    }

    @Test
    public void testAllNodesVisited() {
        algorithmService.runBFS(a);
        for (Node node : graph.getAllNodes()) {
            assertEquals(AlgorithmColor.BLACK, algorithmService.getState().get(node).getAlgorithmColor(),
                "Node " + node.getLabel() + " should be BLACK after BFS");
        }
    }

    @Test
    public void testDistances() {
        algorithmService.runBFS(a);
        assertEquals(0, algorithmService.getState().get(a).getD());
        assertEquals(1, algorithmService.getState().get(b).getD());
        assertEquals(1, algorithmService.getState().get(c).getD());
        assertEquals(2, algorithmService.getState().get(d).getD());
    }

    @Test
    public void testParents() {
        algorithmService.runBFS(a);
        assertNull(algorithmService.getState().get(a).getPi());
        assertEquals(a, algorithmService.getState().get(b).getPi());
        assertEquals(a, algorithmService.getState().get(c).getPi());
        assertEquals(b, algorithmService.getState().get(d).getPi());
    }

    @Test
    public void testStateResetsBeforeSecondRun() {
        algorithmService.runBFS(a);
        algorithmService.runBFS(b);
        assertEquals(0, algorithmService.getState().get(b).getD());
        assertEquals(Integer.MAX_VALUE, algorithmService.getState().get(a).getD());
    }

    @Test
    public void testUnreachableNodeFromB() {
        algorithmService.runBFS(b);
        assertEquals(Integer.MAX_VALUE, algorithmService.getState().get(a).getD());
        assertEquals(AlgorithmColor.WHITE, algorithmService.getState().get(a).getAlgorithmColor());
    }

    @Test
    public void testUndirectedBFS() {
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
        as.runBFS(z);

        assertEquals(AlgorithmColor.BLACK, as.getState().get(x).getAlgorithmColor());
        assertEquals(2, as.getState().get(x).getD());
        assertEquals(1, as.getState().get(y).getD());
        assertEquals(0, as.getState().get(z).getD());
    }
}