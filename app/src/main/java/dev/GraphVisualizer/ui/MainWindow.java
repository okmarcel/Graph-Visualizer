package dev.GraphVisualizer.ui;

import dev.GraphVisualizer.ui.canvas.GraphCanvas;
import dev.GraphVisualizer.ui.toolbar.GraphToolBar;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;

/**
 * Main window of the App, holding all GUI elements
 */
public class MainWindow {
    private final int SCENE_WIDTH       = 1280;
    private final int SCENE_HEIGHT      = 720;
    private final int MIN_SCENE_WIDTH   = 480;
    private final int MIN_SCENE_HEIGHT  = 320;
    private final String WINDOW_TITLE   = "Graph Visualizer";

    private final Stage stage;

    /**
     * Constructs MainWindow object from Stage object
     * 
     * @param stage a Stage object
     */
    public MainWindow(Stage stage) {
        this.stage = stage;
    }

    /**
     * Displays App GUI
     */
    public void show() {
        // [Center] Graph canvas
        GraphCanvas graphCanvas = new GraphCanvas();

        // [Top] ToolBar
        GraphToolBar toolBar = new GraphToolBar();

        // [Right-side] Control pane (placeholder)
        BorderPane rightPane = new BorderPane();
        rightPane.setPadding(new Insets(6));
        rightPane.setPrefWidth(200);
        rightPane.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Algorithm step control pane (placeholder)
        BorderPane rightTop = new BorderPane();
        rightTop.setPrefHeight(100);
        rightTop.setStyle("-fx-border-color: green; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Algorithm information (placeholder)
        BorderPane rightBottom = new BorderPane();
        rightBottom.setPrefHeight(150);
        rightBottom.setStyle("-fx-border-color: orange; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        rightPane.setTop(rightTop);
        rightPane.setCenter(rightBottom);

        // Root pane
        BorderPane root = new BorderPane();
        // Place all elements onto root
        root.setCenter(graphCanvas);
        root.setTop(toolBar);
        root.setRight(rightPane);

        // Create and display scene
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        stage.setMinWidth(MIN_SCENE_WIDTH);
        stage.setMinHeight(MIN_SCENE_HEIGHT);
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(scene);
        stage.show();
    }
}