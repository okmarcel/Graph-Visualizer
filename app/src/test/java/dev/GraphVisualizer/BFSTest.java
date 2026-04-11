package dev.GraphVisualizer.algorithms;

import dev.GraphVisualizer.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class BFSTest {

    private Graph graph;
    private Node a, b, c, d;

    @BeforeEach
    public void setUp() {
        a = new Node("A", 0, 0, 0);
        b = new Node("B", 0, 1, 0);
        c = new Node("C", 0, 2, 0);
        d = new Node("D", 0, 3, 0);

        // A → B, A → C, B → D
        Edge ab = new Edge(a, b);
        Edge ac = new Edge(a, c);
        Edge bd = new Edge(b, d);

        a.getAdjacentEdges().add(ab);
        a.getAdjacentEdges().add(ac);
        b.getAdjacentEdges().add(bd);

        graph = new Graph(true, false);
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addEdge(ab);
        graph.addEdge(ac);
        graph.addEdge(bd);
    }

    @Test
    public void testAllNodesVisited() {
        BFS bfs = new BFS(graph, a);
        bfs.runBFS();

        for (Node node : bfs.getGraph().getAllNodes()) {
            BFSNode bfsNode = (BFSNode) node;
            assertEquals(Color.BLACK, bfsNode.getColor(),
                "Node " + node.getId() + " should be BLACK after BFS");
        }
    }

    @Test
    public void testDistances() {
        BFS bfs = new BFS(graph, a);
        bfs.runBFS();

        for (Node node : bfs.getGraph().getAllNodes()) {
            BFSNode bfsNode = (BFSNode) node;
            switch (node.getId()) {
                case "A" -> assertEquals(0, bfsNode.getD(), "Distance A should be 0");
                case "B" -> assertEquals(1, bfsNode.getD(), "Distance B should be 1");
                case "C" -> assertEquals(1, bfsNode.getD(), "Distance C should be 1");
                case "D" -> assertEquals(2, bfsNode.getD(), "Distance D should be 2");
            }
        }
    }

    @Test
    public void testParents() {
        BFS bfs = new BFS(graph, a);
        bfs.runBFS();

        for (Node node : bfs.getGraph().getAllNodes()) {
            BFSNode bfsNode = (BFSNode) node;
            switch (node.getId()) {
                case "A" -> assertNull(bfsNode.getPi(), "Source should have no parent");
                case "B" -> assertEquals("A", bfsNode.getPi().getId(), "Parent of B should be A");
                case "C" -> assertEquals("A", bfsNode.getPi().getId(), "Parent of C should be A");
                case "D" -> assertEquals("B", bfsNode.getPi().getId(), "Parent of D should be B");
            }
        }
    }

    @Test
    public void testNullGraphThrowsException() {
        assertThrows(GraphNotFoundException.class, () -> {
            new BFS(null, a);
        });
    }

    @Test
    public void testSourceNodeIsGrey() {
        BFS bfs = new BFS(graph, a);
        // sourceNode powinien być GREY zaraz po inicjalizacji, przed runBFS
        assertEquals(Color.GREY, bfs.getSorceNode().getColor());
    }
}