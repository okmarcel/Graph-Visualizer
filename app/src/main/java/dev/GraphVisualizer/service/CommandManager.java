package dev.GraphVisualizer.service;

import java.util.ArrayDeque;
import java.util.Deque;

/** Tracks user actions as do/undo pairs so they can be stepped backward and forward. */
public final class CommandManager {
    private record Entry(Runnable doIt, Runnable undoIt) {}

    private final Deque<Entry> undoStack = new ArrayDeque<>();
    private final Deque<Entry> redoStack = new ArrayDeque<>();

    /**
     * Runs doIt immediately and stores the pair for later undo.
     * @param doIt the action to perform
     * @param undoIt the action that reverses it
     */
    public void push(Runnable doIt, Runnable undoIt) {
        doIt.run();
        undoStack.push(new Entry(doIt, undoIt));
        redoStack.clear();
    }

    /** Undoes the most recent action. */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Entry e = undoStack.pop();
            e.undoIt().run();
            redoStack.push(e);
        }
    }

    /** Re-applies the most recently undone action. */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Entry e = redoStack.pop();
            e.doIt().run();
            undoStack.push(e);
        }
    }

    /** Clears both stacks — call after a structural change like a graph type switch. */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}
