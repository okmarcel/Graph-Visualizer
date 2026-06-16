package dev.GraphVisualizer.ui;

import dev.GraphVisualizer.ui.canvas.GraphCanvas;
import dev.GraphVisualizer.ui.toolbar.GraphToolBar;
import dev.GraphVisualizer.service.AlgorithmService;
import dev.GraphVisualizer.service.CommandManager;
import dev.GraphVisualizer.service.GraphService;
import dev.GraphVisualizer.models.*;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/** Class MainWindow - builds and shows the main application scene */
public class MainWindow {
    private final int SCENE_WIDTH = 1280;
    private final int SCENE_HEIGHT = 720;
    private final int MIN_SCENE_WIDTH = 480;
    private final int MIN_SCENE_HEIGHT = 320;
    private final String WINDOW_TITLE = "Graph Visualizer";

    /** Primary JavaFX stage */
    private final Stage stage;

    /** Service managing graph persistence and graph type switching */
    private final GraphService graphService;

    /** Service responsible for algorithm execution */
    private final AlgorithmService algorithmService;

    /** Shared undo/redo manager */
    private final CommandManager commandManager;

    /** Graph direction flag mirrored by toolbar and canvas */
    private boolean directed = false;
    
    /** Graph weighting flag mirrored by toolbar and canvas */
    private boolean weighted = false;

    /**
     * Constructor taking the primary stage and preparing the initial graph state.
     * @param stage primary application stage
     */
    public MainWindow(Stage stage) {
        this.stage = stage;
        Graph graph = new UndirectedGraph();
        initializeSampleGraph(graph);
        this.graphService = new GraphService(graph);
        this.algorithmService = new AlgorithmService(graphService);
        this.commandManager = new CommandManager();
    }

    /** Builds the scene graph, wires shortcuts and shows the window. */
    public void show() {
        GraphCanvas  graphCanvas = new GraphCanvas(graphService, directed, weighted, commandManager);
        GraphToolBar toolBar = new GraphToolBar(graphService, graphCanvas, directed, weighted, algorithmService, commandManager);
        toolBar.setCanvas(graphCanvas);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e2e;");
        root.setCenter(graphCanvas);
        root.setTop(toolBar);

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.setOnKeyPressed(e -> {
            if (e.isControlDown()) {
                if (e.getCode() == KeyCode.Z) {
                    commandManager.undo();
                    graphCanvas.refresh();
                }
                if (e.getCode() == KeyCode.Y) {
                    commandManager.redo();
                    graphCanvas.refresh();
                }
            }
        });

        stage.setMinWidth(MIN_SCENE_WIDTH);
        stage.setMinHeight(MIN_SCENE_HEIGHT);
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Populates the initial sample graph visible after application start.
     * @param graph graph to populate
     */
    private void initializeSampleGraph(Graph graph) {
        Node nodeA = new Node("A", 300, 150);
        Node nodeB = new Node("B", 500, 280);
        Node nodeC = new Node("C", 130, 320);
        Node nodeD = new Node("D", 480, 480);
        Node nodeE = new Node("E", 220, 520);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addEdge(new Edge(nodeA, nodeB));
        graph.addEdge(new Edge(nodeA, nodeC));
        graph.addEdge(new Edge(nodeB, nodeD)); 
        graph.addEdge(new Edge(nodeB, nodeE));
        graph.addEdge(new Edge(nodeC, nodeD)); 
        graph.addEdge(new Edge(nodeC, nodeE));
    }
}
