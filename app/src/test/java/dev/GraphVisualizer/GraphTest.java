package dev.GraphVisualizer;

import dev.GraphVisualizer.models.Graph;
import dev.GraphVisualizer.models.Node;
import dev.GraphVisualizer.models.Edge;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {
    @Test
    void testFullConstructor() {
        Graph graph = new Graph(true, true);
        assertTrue(graph.isDirected());
        assertTrue(graph.isWeighted());

        graph = new Graph(false, false);
        assertFalse(graph.isDirected());
        assertFalse(graph.isWeighted());
    }

    @Test
    void testConstructorDirected() {
        Graph graph = new Graph(true);
        assertTrue(graph.isDirected());
        assertFalse(graph.isWeighted());

        graph = new Graph(false);
        assertFalse(graph.isDirected());
        assertFalse(graph.isWeighted());
    }

    @Test
    void testDefaultConstructor() {
        Graph graph = new Graph();
        assertFalse(graph.isDirected());
        assertFalse(graph.isWeighted());
    }

    @Test
    void addNodeIncreasesNodeCount() {
        Graph graph = new Graph();
        assertEquals(0, graph.getNumberOfNodes());

        graph.addNode(new Node("Test"));
        assertEquals(1, graph.getNumberOfNodes());
    }

    @Test
    void addedNodeIsStored() {
        Graph graph = new Graph();
        Node node = new Node("Test");
        graph.addNode(node);
        assertTrue(graph.getAllNodes().contains(node));
    }

    @Test
    void removeNodeDecreasesCount() {
        Graph graph = new Graph();
        Node node = new Node("Test");
        graph.addNode(node);
        assertEquals(1, graph.getNumberOfNodes());

        graph.removeNode(node);
        assertEquals(0, graph.getNumberOfNodes());
    }

    @Test
    void removedNodeIsNotStored() {
        Graph graph = new Graph();
        Node node = new Node("Test");
        graph.addNode(node);
        graph.removeNode(node);
        assertFalse(graph.getAllNodes().contains(node));
    }

    @Test
    void removeNodeNotInGraphDoesNotThrow() {
        Graph graph = new Graph();
        Node node = new Node("Test");
        assertDoesNotThrow(() -> graph.removeNode(node));
    }

    @Test
    void addEdgeIncreasesEdgeCount() {
        Graph graph = new Graph();
        assertEquals(0, graph.getNumberOfEdges());

        graph.addEdge(new Edge(new Node("Source"), new Node("Target")));
        assertEquals(1, graph.getNumberOfEdges());
    }

    @Test
    void addedEdgeIsStored() {
        Graph graph = new Graph();
        Edge edge = new Edge(new Node("Source"), new Node("Target"));
        graph.addEdge(edge);
        assertTrue(graph.getAllEdges().contains(edge));
    }

    @Test
    void removeEdgeDecreasesCount() {
        Graph graph = new Graph();
        Edge edge = new Edge(new Node("Source"), new Node("Target"));
        graph.addEdge(edge);
        assertEquals(1, graph.getNumberOfEdges());

        graph.removeEdge(edge);
        assertEquals(0, graph.getNumberOfEdges());
    }

    @Test
    void removedEdgeIsNotStored() {
        Graph graph = new Graph();
        Edge edge = new Edge(new Node("Source"), new Node("Target"));
        graph.addEdge(edge);
        graph.removeEdge(edge);
        assertFalse(graph.getAllEdges().contains(edge));
    }

    @Test
    void removeEdgeNotInGraphDoesNotThrow() {
        Graph graph = new Graph();
        Edge edge = new Edge(new Node("Source"), new Node("Target"));
        assertDoesNotThrow(() -> graph.removeEdge(edge));
    }
}