package dev.GraphVisualizer;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class JsonGraphRepositoryTest {
    @TempDir
    Path tempDir;

    private JsonGraphRepository repository;
    private Node a, b, c;

    @BeforeEach
    public void setUp() {
        repository = new JsonGraphRepository();
        a = new Node("A", 0.0, 0.0);
        b = new Node("B", 1.0, 0.0);
        c = new Node("C", 2.0, 0.0);
    }

    @Test
    @DisplayName("Directed graph is saved and loaded with correct type, node count, and edge count")
    public void testSaveAndLoadDirectedGraph() {
        DirectedGraph original = new DirectedGraph();
        original.addNode(a);
        original.addNode(b);
        original.addNode(c);
        original.addEdge(new Edge(a, b));
        original.addEdge(new Edge(b, c));

        File file = tempDir.resolve("directed.json").toFile();
        repository.save(original, file);

        Graph loaded = repository.load(file);

        assertTrue(loaded instanceof DirectedGraph);
        assertEquals(3, loaded.getNumberOfNodes());
        assertEquals(2, loaded.getNumberOfEdges());
    }

    @Test
    @DisplayName("Undirected graph is saved and loaded with correct type and structure")
    public void testSaveAndLoadUndirectedGraph() {
        UndirectedGraph original = new UndirectedGraph();
        original.addNode(a);
        original.addNode(b);
        original.addEdge(new Edge(a, b));

        File file = tempDir.resolve("undirected.json").toFile();
        repository.save(original, file);

        Graph loaded = repository.load(file);

        assertTrue(loaded instanceof UndirectedGraph);
        assertEquals(2, loaded.getNumberOfNodes());
        assertEquals(1, loaded.getNumberOfEdges());
    }

    @Test
    @DisplayName("Weighted directed graph preserves edge weights after save and load")
    public void testSaveAndLoadWeightedDirectedGraph() {
        WeightedDirectedGraph original = new WeightedDirectedGraph();
        original.addNode(a);
        original.addNode(b);
        original.addEdge(new Edge(a, b, 3.5));

        File file = tempDir.resolve("weighted_directed.json").toFile();
        repository.save(original, file);

        Graph loaded = repository.load(file);

        assertTrue(loaded instanceof WeightedDirectedGraph);
        assertEquals(3.5, loaded.getAllEdges().get(0).getWeight());
    }

    @Test
    @DisplayName("Weighted undirected graph preserves edge weights after save and load")
    public void testSaveAndLoadWeightedUndirectedGraph() {
        WeightedUndirectedGraph original = new WeightedUndirectedGraph();
        original.addNode(a);
        original.addNode(b);
        original.addEdge(new Edge(a, b, 2.0));

        File file = tempDir.resolve("weighted_undirected.json").toFile();
        repository.save(original, file);

        Graph loaded = repository.load(file);

        assertTrue(loaded instanceof WeightedUndirectedGraph);
        assertEquals(2.0, loaded.getAllEdges().get(0).getWeight());
    }

    @Test
    @DisplayName("Node labels are preserved after save and load")
    public void testNodeLabelsPreserved() {
        DirectedGraph original = new DirectedGraph();
        original.addNode(a);
        original.addNode(b);

        File file = tempDir.resolve("labels.json").toFile();
        repository.save(original, file);

        Graph loaded = repository.load(file);
        List<String> labels = loaded.getAllNodes().stream()
            .map(Node::getLabel)
            .toList();

        assertTrue(labels.contains("A"));
        assertTrue(labels.contains("B"));
    }

    @Test
    @DisplayName("Node canvas positions are preserved after save and load")
    public void testNodePositionsPreserved() {
        DirectedGraph original = new DirectedGraph();
        original.addNode(a);

        File file = tempDir.resolve("positions.json").toFile();
        repository.save(original, file);

        Graph loaded = repository.load(file);
        Node loadedA = loaded.getAllNodes().get(0);

        assertEquals(0.0, loadedA.getPositionX());
        assertEquals(0.0, loadedA.getPositionY());
    }

    @Test
    @DisplayName("Edge source and target labels are preserved after save and load")
    public void testEdgeConnectionsPreserved() {
        DirectedGraph original = new DirectedGraph();
        original.addNode(a);
        original.addNode(b);
        original.addEdge(new Edge(a, b));

        File file = tempDir.resolve("connections.json").toFile();
        repository.save(original, file);

        Graph loaded = repository.load(file);
        Edge loadedEdge = loaded.getAllEdges().get(0);

        assertEquals("A", loadedEdge.getSource().getLabel());
        assertEquals("B", loadedEdge.getTarget().getLabel());
    }

    @Test
    @DisplayName("Loading from a non-existent file throws GraphIOException")
    public void testLoadNonExistentFileThrows() {
        File file = tempDir.resolve("nonexistent.json").toFile();
        assertThrows(GraphIOException.class, () -> repository.load(file));
    }

    @Test
    @DisplayName("Saving creates the output file on disk")
    public void testSaveCreatesFile() {
        DirectedGraph original = new DirectedGraph();
        original.addNode(a);

        File file = tempDir.resolve("created.json").toFile();
        assertFalse(file.exists());

        repository.save(original, file);

        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Empty graph is saved and loaded with zero nodes and zero edges")
    public void testEmptyGraphSaveAndLoad() {
        DirectedGraph original = new DirectedGraph();

        File file = tempDir.resolve("empty.json").toFile();
        repository.save(original, file);

        Graph loaded = repository.load(file);

        assertEquals(0, loaded.getNumberOfNodes());
        assertEquals(0, loaded.getNumberOfEdges());
    }
}