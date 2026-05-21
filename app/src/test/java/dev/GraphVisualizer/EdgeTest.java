package dev.GraphVisualizer;

import dev.GraphVisualizer.models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeTest {

    private Node a, b;

    @BeforeEach
    public void setUp() {
        a = new Node("A", 0.0, 0.0);
        b = new Node("B", 1.0, 0.0);
    }

    @Test
    @DisplayName("Default edge weight is 1.0 when no weight is specified")
    public void testDefaultWeight() {
        Edge e = new Edge(a, b);
        assertEquals(1.0, e.getWeight());
    }

    @Test
    @DisplayName("Custom weight is stored correctly when provided to the constructor")
    public void testCustomWeight() {
        Edge e = new Edge(a, b, 5.0);
        assertEquals(5.0, e.getWeight());
    }

    @Test
    @DisplayName("Source and target nodes are stored and returned correctly")
    public void testSourceAndTarget() {
        Edge e = new Edge(a, b);
        assertEquals(a, e.getSource());
        assertEquals(b, e.getTarget());
    }

    @Test
    @DisplayName("Constructor throws NullPointerException when source node is null")
    public void testNullSourceThrows() {
        assertThrows(NullPointerException.class, () -> new Edge(null, b));
    }

    @Test
    @DisplayName("Constructor throws NullPointerException when target node is null")
    public void testNullTargetThrows() {
        assertThrows(NullPointerException.class, () -> new Edge(a, null));
    }

    @Test
    @DisplayName("An edge is equal to itself")
    public void testEquality() {
        Edge e1 = new Edge(a, b);
        assertEquals(e1, e1);
    }

    @Test
    @DisplayName("Two distinct edges between the same nodes are not equal")
    public void testTwoEdgesBetweenSameNodesAreNotEqual() {
        Edge e1 = new Edge(a, b);
        Edge e2 = new Edge(a, b);
        assertNotEquals(e1, e2);
    }

    @Test
    @DisplayName("setWeight updates the edge weight to the new value")
    public void testSetWeight() {
        Edge e = new Edge(a, b);
        e.setWeight(3.5);
        assertEquals(3.5, e.getWeight());
    }

    @Test
    @DisplayName("setSource throws NullPointerException when null is passed")
    public void testSetNullSourceThrows() {
        Edge e = new Edge(a, b);
        assertThrows(NullPointerException.class, () -> e.setSource(null));
    }

    @Test
    @DisplayName("setTarget throws NullPointerException when null is passed")
    public void testSetNullTargetThrows() {
        Edge e = new Edge(a, b);
        assertThrows(NullPointerException.class, () -> e.setTarget(null));
    }
}