package dev.GraphVisualizer.ui;

import dev.GraphVisualizer.ui.canvas.GraphCanvas;
import dev.GraphVisualizer.ui.toolbar.GraphToolBar;
import dev.GraphVisualizer.service.GraphService;
import dev.GraphVisualizer.models.*;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class MainWindow {
    private final int SCENE_WIDTH     = 1280;
    private final int SCENE_HEIGHT    = 720;
    private final int MIN_SCENE_WIDTH = 480;
    private final int MIN_SCENE_HEIGHT= 320;
    private final String WINDOW_TITLE = "Graph Visualizer";

    private final Stage stage;
    private final GraphService graphService;

    // graph state flags — used when running algorithms
    private boolean directed = false;
    private boolean weighted = false;

    public MainWindow(Stage stage) {
        this.stage = stage;
        Graph graph = new UndirectedGraph();
        initializeSampleGraph(graph);
        this.graphService = new GraphService(graph);
    }

    public void show() {
        GraphCanvas graphCanvas = new GraphCanvas(graphService, directed, weighted);
        GraphToolBar toolBar = new GraphToolBar(graphService, graphCanvas, directed, weighted);
        toolBar.setCanvas(graphCanvas);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e2e;");
        root.setCenter(graphCanvas);
        root.setTop(toolBar);

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        stage.setMinWidth(MIN_SCENE_WIDTH);
        stage.setMinHeight(MIN_SCENE_HEIGHT);
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    private void initializeSampleGraph(Graph graph) {
        Node nodeA = new Node("A", 200, 200);
        Node nodeB = new Node("B", 400, 200);
        Node nodeC = new Node("C", 200, 400);
        Node nodeD = new Node("D", 400, 400);
        Node nodeE = new Node("E", 400, 600);
        graph.addNode(nodeA); graph.addNode(nodeB); graph.addNode(nodeC);
        graph.addNode(nodeD); graph.addNode(nodeE);
        graph.addEdge(new Edge(nodeA, nodeB)); graph.addEdge(new Edge(nodeA, nodeC));
        graph.addEdge(new Edge(nodeB, nodeD)); graph.addEdge(new Edge(nodeB, nodeE));
        graph.addEdge(new Edge(nodeC, nodeD)); graph.addEdge(new Edge(nodeC, nodeE));
    }
}