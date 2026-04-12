package dev.GraphVisualizer.algorithms;

import dev.GraphVisualizer.algorithms.BFS;
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
        // A → B, A → C, B → D
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
            assertEquals(AlgorithmColor.WHITE, algorithmService.getState().get(node).getAlgorithmColor(),
                "Node " + node.getId() + " should be WHITE before BFS");
        }
    }

    @Test
    public void testInitialDistanceIsMaxValue() {
        for (Node node : graph.getAllNodes()) {
            assertEquals(Integer.MAX_VALUE, algorithmService.getState().get(node).getD(),
                "Distance of " + node.getId() + " should be MAX_VALUE before BFS");
        }
    }

    @Test
    public void testInitialParentIsNull() {
        for (Node node : graph.getAllNodes()) {
            assertNull(algorithmService.getState().get(node).getPi(),
                "Parent of " + node.getId() + " should be null before BFS");
        }
    }

    @Test
    public void testAllNodesVisitedAfterBFS() {
        BFS.runBFS(algorithmService, a);

        for (Node node : graph.getAllNodes()) {
            assertEquals(AlgorithmColor.BLACK, algorithmService.getState().get(node).getAlgorithmColor(),
                "Node " + node.getId() + " should be BLACK after BFS");
        }
    }

    @Test
    public void testDistancesAfterBFS() {
        BFS.runBFS(algorithmService, a);

        assertEquals(0, algorithmService.getState().get(a).getD(), "Distance A should be 0");
        assertEquals(1, algorithmService.getState().get(b).getD(), "Distance B should be 1");
        assertEquals(1, algorithmService.getState().get(c).getD(), "Distance C should be 1");
        assertEquals(2, algorithmService.getState().get(d).getD(), "Distance D should be 2");
    }

    @Test
    public void testParentsAfterBFS() {
        BFS.runBFS(algorithmService, a);

        assertNull(algorithmService.getState().get(a).getPi(), "Source should have no parent");
        assertEquals(a, algorithmService.getState().get(b).getPi(), "Parent of B should be A");
        assertEquals(a, algorithmService.getState().get(c).getPi(), "Parent of C should be A");
        assertEquals(b, algorithmService.getState().get(d).getPi(), "Parent of D should be B");
    }

    @Test
    public void testBFSFromDifferentSource() {
        BFS.runBFS(algorithmService, b);

        assertEquals(0, algorithmService.getState().get(b).getD(), "Distance B should be 0");
        assertEquals(1, algorithmService.getState().get(d).getD(), "Distance D should be 1");
        // A i C nie są osiągalne z B w grafie skierowanym
        assertEquals(Integer.MAX_VALUE, algorithmService.getState().get(a).getD(), "A should be unreachable from B");
        assertEquals(Integer.MAX_VALUE, algorithmService.getState().get(c).getD(), "C should be unreachable from B");
    }

    @Test
    public void testAdjacentListBuiltCorrectly() {
        assertEquals(2, graphService.getAdjacent().get(a).size(), "A should have 2 neighbours");
        assertEquals(1, graphService.getAdjacent().get(b).size(), "B should have 1 neighbour");
        assertEquals(0, graphService.getAdjacent().get(c).size(), "C should have 0 neighbours");
        assertEquals(0, graphService.getAdjacent().get(d).size(), "D should have 0 neighbours");
    }
}