package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.algorithms.*;

import java.util.HashMap;
import java.util.Map; 

public class AlgorithmService {

    private GraphService service;

    private Map<Node, ExtraInfo> state;

    private int time = 0;

    public AlgorithmService(GraphService service) {
        this.service = service;
        this.state = new HashMap<>();
        for(Node i : this.service.getGraph().getAllNodes()) {
            state.put(i, new ExtraInfo());
        }
    }

    public void runBFS(AlgorithmService service, Node sourceNode) {
        resetState();
        BFS.runBFS(service, sourceNode);
    }

    public void runDFS(AlgorithmService service, Node sourceNode) {
        resetState();
        DFS.runDFS(service, sourceNode);
    }

    public void runDijkstra(AlgorithmService service, Node sourceNode) {
        resetState();
        Dijkstra.runDijkstra(service, sourceNode);
    }

    public GraphService getService() {
        return service;
    }

    public Map<Node, ExtraInfo> getState() {
        return state;
    }

    private void resetState() {
        state.clear();
        resetTime();
        for(Node i : service.getGraph().getAllNodes()) {
            state.put(i, new ExtraInfo());
        }
    }

    public int getTime() {
        return time;
    }
    
    public void incrementTime(){
        time++;
    }
    
    public void resetTime() {
        time = 0;
    }
}

