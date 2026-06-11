package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.algorithms.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Class AlgorithmService - responsible for running the algorithms */
public final class AlgorithmService {
    /** Graph representation plus the adjacency list for every node */
    private GraphService service;

    /** Map holding the state of AlgorithmAddInfo needed for algorithm maped to every node in graph */
    private Map<Node, AlgorithmAddInfo> state;

    /** Snapshots of state recorded after each significant algorithm step */
    private List<Map<Node, AlgorithmAddInfo>> steps = new ArrayList<>();

    /** Index into steps currently shown on the canvas */
    private int stepIndex = -1;

    /**
     * Constructor - sets the initial state of nodes neeeded for BFS and DFS algorithms
     * @param service
     */
    public AlgorithmService(GraphService service) {
        this.service = service;
        this.state = new HashMap<>();
        syncState();
    }

    /**
     * Runs BFS algorithm
     * @param service API with the representation of graph and adjacency list for every node
     * @param sourceNode from which the algorithm starts
     */
    public void runBFS(Node sourceNode) {
        resetState();
        BFS.runBFS(service.getGraph().getAdjacent(), state, sourceNode, this::recordStep);
    }

    /**
     * Runs DFS algorithm
     * @param service API with the representation of graph and adjacency list for every node
     * @param sourceNode from which the algorithm starts
     */
    public void runDFS(Node sourceNode) {
        resetState();
        double[] time = {0.0};
        DFS.runDFS(service.getGraph().getAdjacent(), state, time, sourceNode, this::recordStep);
    }

    /**
     * Runs Dijkstra algorithm
     * @param service API with the representation of graph and adjacency list for every node
     * @param sourceNode from which the algorithm starts
     */
    public void runDijkstra(Node sourceNode) {
        resetState();
        if(!(service.getGraph() instanceof WeightedDirectedGraph) && !(service.getGraph() instanceof WeightedUndirectedGraph)) {
            throw new UnweightedGraphException("Dijkstra requires a weighted graph.");
        }
        for(Edge e : service.getGraph().getAllEdges()) {
            if(e.getWeight() < 0.0) {
                throw new NegativeWeightException("Dijkstra cannot run on graphs with negative weights. Edge: "
                    + e.getSource().getLabel() + " -> " + e.getTarget().getLabel());
            }
        }
        Dijkstra.runDijkstra(service.getGraph().getAdjacent(), state, service.getGraph().getAllEdges(), sourceNode, this::recordStep);
    }

    /**
     * service getter
     * @return service
     */
    public GraphService getService() {
        return service;
    }

    /**
     * state getter
     * @return state
     */
    public Map<Node, AlgorithmAddInfo> getState() {
        return state;
    }

    /** 
     * Synchronization of state between:
     * - service: graph from graph service
     * - state: map of nodes and their algorithm state
     * needed when graph changes representation betweeen 
     * algorithm calls could also use some form of cache or observer pattern 
     */
    private void syncState() {
        for(Node i : service.getGraph().getAllNodes()) {
            state.putIfAbsent(i, new AlgorithmAddInfo());
        }
        state.keySet().retainAll(service.getGraph().getAllNodes());
    }   

    /** Returns the state snapshot at the current step index (or final state if no steps). */
    public Map<Node, AlgorithmAddInfo> getStepState() {
        if (steps.isEmpty())
            return state;
        return steps.get(stepIndex);
    }

    /** Returns true if there are recorded steps to navigate. */
    public boolean hasSteps() {
        return steps.size() > 1;
    }

    /** Returns the 1-based current step number for display. */
    public int getStepIndex() {
        return stepIndex + 1;
    }

    /** Returns the total number of recorded steps. */
    public int getStepCount() {
        return steps.size();
    }

    /** Advances to the next step. Returns true if the move was possible. */
    public boolean stepForward() {
        if (stepIndex < steps.size() - 1) {
            stepIndex++; return true;
        }
        return false;
    }

    /** Goes back to the previous step. Returns true if the move was possible. */
    public boolean stepBack() {
        if (stepIndex > 0) {
            stepIndex--;
            return true;
        }
        return false;
    }

    /** Rewinds to the first recorded step. */
    public void goToStart() {
        if (!steps.isEmpty())
            stepIndex = 0;
    }

    /** Follows pi pointers from target back to the source and returns the ordered path. */
    public List<Node> reconstructPath(Node target) {
        List<Node> path = new ArrayList<>();
        Node current = target;
        while (current != null) {
            path.add(0, current);
            AlgorithmAddInfo info = state.get(current);
            if (info == null)
                break;
            current = info.getPi();
        }
        return path;
    }

    /** Resets the state Map and rebuilds the adjacency list before a new algorithm run */
    private void resetState() {
        state = new HashMap<>();
        steps = new ArrayList<>();
        stepIndex = -1;
        if (service.getGraph().getCache()) {
            service.getGraph().buildAdjacent();
            service.getGraph().setCache(false);
        }
        syncState();
    }

    /** Snapshots the current state and appends it to the steps list. */
    private void recordStep() {
        Map<Node, AlgorithmAddInfo> snapshot = new HashMap<>();
        for (Map.Entry<Node, AlgorithmAddInfo> entry : state.entrySet()) {
            AlgorithmAddInfo o = entry.getValue();
            snapshot.put(entry.getKey(), new AlgorithmAddInfo(o.getNodeColor(), o.getD(), o.getPi(), o.getF()));
        }
        steps.add(snapshot);
        stepIndex = steps.size() - 1;
    }
}
