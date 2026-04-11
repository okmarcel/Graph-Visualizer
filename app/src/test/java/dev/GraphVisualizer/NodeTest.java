package dev.GraphVisualizer;

import dev.GraphVisualizer.models.Node;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    private Node nodeA;
    private final int valueA = 2;
    private Node nodeB;
    private final int valueB = 5;

    @BeforeEach
    void setUp() {
        nodeA = new Node(valueA);
        nodeB = new Node(valueA);
    }

    @Test
    void fullConstructorStoresValues() {
        Node node = new Node(42, 1.5, 3.7);
        assertEquals(42, node.getValue());
    }

    @Test
    void shortConstructorDefaultsPositionToZero() {
        Node node = new Node(10);
        assertEquals("Node{id= test_id, value= 10, x= 0.0, y= 0.0}", node.toString());
    }

    @Test
    void shortConstructorDelegatesToFullConstructor() {
        Node a = new Node(5);
        Node b = new Node(5, 0.0, 0.0);
        assertEquals(a.getValue(), b.getValue());
    }

    @Test
    void testIdNotNull() {
        assertNotNull(nodeA.getId());
    }

    @Test
    void testIdNotBlank() {
        assertFalse(nodeA.getId().isBlank());
    }

    @Test
    void testGetValue() {
        assertEquals(nodeA.getValue(), valueA);
    }

    @Test
    void setValueUpdatesValue() {
        Node node = new Node(1);
        node.setValue(42);
        assertEquals(42, node.getValue());
    }

    @Test
    void setValueAllowsNegative() {
        Node node = new Node(1);
        node.setValue(-10);
        assertEquals(-10, node.getValue());
    }

    @Test
    void testToString() {
        String expected = "Node{id= " + nodeA.getId() + ", value= " + nodeA.getValue() 
			    + ", x= " + nodeA.getPositionX() + ", y= " + nodeA.getPositionY() + "}";
        assertEquals(expected, nodeA.toString());
    }
}