package dev.GraphVisualizer;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class BFSServiceTest {
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
    @DisplayName("All nodes start as WHITE before BFS runs")
    public void testInitialStateIsWhite() {
        for (Node node : graph.getAllNodes()) {
            assertEquals(AlgorithmAddInfo.NodeColor.WHITE,
                algorithmService.getState().get(node).getNodeColor());
        }
    }

    @Test
    @DisplayName("BFS visits every reachable node and marks it BLACK")
    public void testAllNodesVisited() {
        algorithmService.runBFS(a);
        for (Node node : graph.getAllNodes()) {
            assertEquals(AlgorithmAddInfo.NodeColor.BLACK,
                algorithmService.getState().get(node).getNodeColor(),
                "Node " + node.getLabel() + " should be BLACK");
        }
    }

    @Test
    @DisplayName("BFS computes correct shortest distances from source")
    public void testDistances() {
        algorithmService.runBFS(a);
        assertEquals(0.0, algorithmService.getState().get(a).getD());
        assertEquals(1.0, algorithmService.getState().get(b).getD());
        assertEquals(1.0, algorithmService.getState().get(c).getD());
        assertEquals(2.0, algorithmService.getState().get(d).getD());
    }

    @Test
    @DisplayName("BFS sets correct parent (pi) pointers for each node")
    public void testParents() {
        algorithmService.runBFS(a);
        assertNull(algorithmService.getState().get(a).getPi());
        assertEquals(a, algorithmService.getState().get(b).getPi());
        assertEquals(a, algorithmService.getState().get(c).getPi());
        assertEquals(b, algorithmService.getState().get(d).getPi());
    }

    @Test
    @DisplayName("Unreachable node stays WHITE with infinite distance")
    public void testUnreachableNode() {
        algorithmService.runBFS(b);
        assertEquals(Double.POSITIVE_INFINITY, algorithmService.getState().get(a).getD());
        assertEquals(AlgorithmAddInfo.NodeColor.WHITE,
            algorithmService.getState().get(a).getNodeColor());
    }

    @Test
    @DisplayName("State resets correctly when BFS runs a second time from a different source")
    public void testStateResetsBeforeSecondRun() {
        algorithmService.runBFS(a);
        algorithmService.runBFS(b);
        assertEquals(0.0, algorithmService.getState().get(b).getD());
        assertEquals(Double.POSITIVE_INFINITY, algorithmService.getState().get(a).getD());
    }

    @Test
    @DisplayName("Nodes with edges pointing into the source are not visited from that source")
    public void testIncomingOnlyNodesNotVisited() {
        Node f = new Node("F", 4.0, 0.0);
        Node g = new Node("G", 5.0, 0.0);
        graph.addNode(f);
        graph.addNode(g);
        graph.addEdge(new Edge(f, a));
        graph.addEdge(new Edge(g, a));

        GraphService gs = new GraphService(graph);
        AlgorithmService as = new AlgorithmService(gs);
        as.runBFS(a);

        assertEquals(AlgorithmAddInfo.NodeColor.WHITE, as.getState().get(f).getNodeColor(),
            "F has no edge from A so it must stay WHITE");
        assertEquals(AlgorithmAddInfo.NodeColor.WHITE, as.getState().get(g).getNodeColor(),
            "G has no edge from A so it must stay WHITE");
        assertEquals(Double.POSITIVE_INFINITY, as.getState().get(f).getD());
        assertEquals(Double.POSITIVE_INFINITY, as.getState().get(g).getD());
    }

    @Test
    @DisplayName("BFS works correctly on an undirected graph")
    public void testUndirectedBFS() {
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
        as.runBFS(z);

        assertEquals(AlgorithmAddInfo.NodeColor.BLACK, as.getState().get(x).getNodeColor());
        assertEquals(2.0, as.getState().get(x).getD());
        assertEquals(1.0, as.getState().get(y).getD());
        assertEquals(0.0, as.getState().get(z).getD());
    }
}