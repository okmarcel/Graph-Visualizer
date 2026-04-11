package dev.GraphVisualizer;

import dev.GraphVisualizer.models.Node;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    @Test
    void fullConstructorStoresLabel() {
        Node node = new Node("Test Node", 1.2, 3.4);
        assertEquals("Test Node", node.getLabel());
    }

    @Test
    void fullConstructorStoresPosition() {
        Node node = new Node("Test Node", 1.2, 3.4);
        assertEquals(1.2, node.getPositionX());
        assertEquals(3.4, node.getPositionY());
    }

    @Test
    void shortConstructorStoresLabel() {
        Node node = new Node("Test Node");
        assertEquals("Test Node", node.getLabel());
    }

    @Test
    void shortConstructorDefaultsPositionToZero() {
        Node node = new Node("TestNode");
        assertEquals(0.0, node.getPositionX());
        assertEquals(0.0, node.getPositionY());
    }

    @Test
    void constructorThrowsOnNullLabel() {
        assertThrows(NullPointerException.class, () -> new Node(null));
        assertThrows(NullPointerException.class, () -> new Node(null, 1.2, 3.4));
    }

    @Test
    void testIdNotNull() {
        Node node = new Node("TestNode");
        assertNotNull(node.getId());
    }

    @Test
    void testIdNotBlank() {
        Node node = new Node("TestNode");
        assertFalse(node.getId().isBlank());
    }

    @Test
    void testGetLabel() {
        Node node = new Node("TestNode");
        assertEquals("TestNode", node.getLabel());
    }

    @Test
    void setLabelUpdatesLabel() {
        Node node = new Node("TestNode");
        node.setLabel("New Label");
        assertEquals("New Label", node.getLabel());
    }

    @Test
    void setLabelThrowsOnNullLabel() {
        Node node = new Node("TestNode");
        assertThrows(NullPointerException.class, () -> node.setLabel(null));
    }

    @Test
    void testToString() {
        Node node = new Node("TestNode", 1.2, 3.4);
        String expected = "Node{id= " + node.getId() + ", label= " + node.getLabel() 
			    + ", x= " + node.getPositionX() + ", y= " + node.getPositionY() + "}";
        assertEquals(expected, node.toString());
    }

    @Test
    void sameNodeIsEqual() {
        Node node = new Node("TestNode");
        assertEquals(node, node);
    }

    @Test
    void nodesWithSameLabelAreNotEqual() {
        Node a = new Node("TestNode");
        Node b = new Node("TestNode");
        assertNotEquals(a, b);
    }

    @Test
    void hashCodeIsConsistent() {
        Node node = new Node("TestNode");
        assertEquals(node.hashCode(), node.hashCode());
    }
}