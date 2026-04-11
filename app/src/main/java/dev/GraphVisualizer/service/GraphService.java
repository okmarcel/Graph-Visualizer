package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;

public class GraphService {
    private Graph graph;

    public void loadGraph() {

    }

    public void saveGraph() {

    }

    public Graph getGraph() {
        return graph;
    }

    public void setDirected(boolean directed) {
        graph.setDirected(directed);
    }

    public void setWeighted(boolean weighted) {
        graph.setWeighted(weighted);
    }

}

