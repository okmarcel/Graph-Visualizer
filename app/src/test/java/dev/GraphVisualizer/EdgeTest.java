package dev.GraphVisualizer;

import dev.GraphVisualizer.models.*;
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
    public void testDefaultWeight() {
        Edge e = new Edge(a, b);
        assertEquals(1.0, e.getWeight());
    }

    @Test
    public void testCustomWeight() {
        Edge e = new Edge(a, b, 5.0);
        assertEquals(5.0, e.getWeight());
    }

    @Test
    public void testSourceAndTarget() {
        Edge e = new Edge(a, b);
        assertEquals(a, e.getSource());
        assertEquals(b, e.getTarget());
    }

    @Test
    public void testNullSourceThrows() {
        assertThrows(NullPointerException.class, () -> new Edge(null, b));
    }

    @Test
    public void testNullTargetThrows() {
        assertThrows(NullPointerException.class, () -> new Edge(a, null));
    }

    @Test
    public void testEquality() {
        Edge e1 = new Edge(a, b);
        assertEquals(e1, e1);
    }

    @Test
    public void testTwoEdgesBetweenSameNodesAreNotEqual() {
        Edge e1 = new Edge(a, b);
        Edge e2 = new Edge(a, b);
        assertNotEquals(e1, e2);
    }

    @Test
    public void testSetWeight() {
        Edge e = new Edge(a, b);
        e.setWeight(3.5);
        assertEquals(3.5, e.getWeight());
    }

    @Test
    public void testSetNullSourceThrows() {
        Edge e = new Edge(a, b);
        assertThrows(NullPointerException.class, () -> e.setSource(null));
    }

    @Test
    public void testSetNullTargetThrows() {
        Edge e = new Edge(a, b);
        assertThrows(NullPointerException.class, () -> e.setTarget(null));
    }
}