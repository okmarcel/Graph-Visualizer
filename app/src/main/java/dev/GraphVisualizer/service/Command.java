package dev.GraphVisualizer.service;

/** Represents a reversible user action on the graph */
public interface Command {
    void execute();
    void undo();
}
