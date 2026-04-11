package dev.GraphVisualizer.ui;

import dev.GraphVisualizer.ui.canvas.GraphCanvas;
import dev.GraphVisualizer.ui.toolbar.GraphToolBar;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class MainWindow {
    private final int sceneWidth = 640;
    private final int sceneHeight = 480;
    private final Stage stage;

    public MainWindow(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        GraphCanvas graphCanvas = new GraphCanvas();
        // graphCanvas.setStyle("-fx-border-color: gray; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Control pane
        BorderPane rightPane = new BorderPane();
        rightPane.setPadding(new Insets(6));
        rightPane.setPrefWidth(200);
        rightPane.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Change modes (modify graph, run algorithm)
        BorderPane rightTop = new BorderPane();
        rightTop.setPrefHeight(100);
        rightTop.setStyle("-fx-border-color: green; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Mode controls
        BorderPane rightBottom = new BorderPane();
        rightBottom.setPrefHeight(150);
        rightBottom.setStyle("-fx-border-color: orange; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        rightPane.setTop(rightTop);
        rightPane.setCenter(rightBottom);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(8));
        root.setCenter(graphCanvas);
        root.setTop(new GraphToolBar());
        root.setRight(rightPane);

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setMinWidth(480);
        stage.setMinHeight(360);
        stage.setTitle("Graph Visualizer");
        stage.setScene(scene);
        stage.show();
    }
}