package dev.GraphVisualizer;

import dev.GraphVisualizer.models.Node;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    @Test
    @DisplayName("Full constructor stores the provided label")
    void fullConstructorStoresLabel() {
        Node node = new Node("Test Node", 1.2, 3.4);
        assertEquals("Test Node", node.getLabel());
    }

    @Test
    @DisplayName("Full constructor stores the provided x and y position")
    void fullConstructorStoresPosition() {
        Node node = new Node("Test Node", 1.2, 3.4);
        assertEquals(1.2, node.getPositionX());
        assertEquals(3.4, node.getPositionY());
    }

    @Test
    @DisplayName("Short constructor stores the provided label")
    void shortConstructorStoresLabel() {
        Node node = new Node("Test Node");
        assertEquals("Test Node", node.getLabel());
    }

    @Test
    @DisplayName("Short constructor defaults position to (0.0, 0.0)")
    void shortConstructorDefaultsPositionToZero() {
        Node node = new Node("TestNode");
        assertEquals(0.0, node.getPositionX());
        assertEquals(0.0, node.getPositionY());
    }

    @Test
    @DisplayName("Constructor throws NullPointerException when label is null")
    void constructorThrowsOnNullLabel() {
        assertThrows(NullPointerException.class, () -> new Node(null));
        assertThrows(NullPointerException.class, () -> new Node(null, 1.2, 3.4));
    }

    @Test
    @DisplayName("Generated node ID is not null")
    void testIdNotNull() {
        Node node = new Node("TestNode");
        assertNotNull(node.getId());
    }

    @Test
    @DisplayName("Generated node ID is not blank")
    void testIdNotBlank() {
        Node node = new Node("TestNode");
        assertFalse(node.getId().isBlank());
    }

    @Test
    @DisplayName("getLabel returns the label passed to the constructor")
    void testGetLabel() {
        Node node = new Node("TestNode");
        assertEquals("TestNode", node.getLabel());
    }

    @Test
    @DisplayName("setLabel replaces the existing label with the new value")
    void setLabelUpdatesLabel() {
        Node node = new Node("TestNode");
        node.setLabel("New Label");
        assertEquals("New Label", node.getLabel());
    }

    @Test
    @DisplayName("setLabel throws NullPointerException when null is passed")
    void setLabelThrowsOnNullLabel() {
        Node node = new Node("TestNode");
        assertThrows(NullPointerException.class, () -> node.setLabel(null));
    }

    @Test
    @DisplayName("toString returns expected format containing id, label, x and y")
    void testToString() {
        Node node = new Node("TestNode", 1.2, 3.4);
        String expected = "Node{id= " + node.getId() + ", label= " + node.getLabel() 
			    + ", x= " + node.getPositionX() + ", y= " + node.getPositionY() + "}";
        assertEquals(expected, node.toString());
    }

    @Test
    @DisplayName("A node is equal to itself")
    void sameNodeIsEqual() {
        Node node = new Node("TestNode");
        assertEquals(node, node);
    }

    @Test
    @DisplayName("Two nodes with the same label but different IDs are not equal")
    void nodesWithSameLabelAreNotEqual() {
        Node a = new Node("TestNode");
        Node b = new Node("TestNode");
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("hashCode returns the same value on repeated calls for the same node")
    void hashCodeIsConsistent() {
        Node node = new Node("TestNode");
        assertEquals(node.hashCode(), node.hashCode());
    }
}