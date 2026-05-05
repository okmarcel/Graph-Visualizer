package dev.GraphVisualizer.ui.toolbar;

import dev.GraphVisualizer.service.GraphService;
import dev.GraphVisualizer.ui.canvas.GraphCanvas;
import dev.GraphVisualizer.ui.canvas.GraphCanvas.CanvasMode;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;

public class GraphToolBar extends ToolBar {

    private final GraphService graphService;
    private GraphCanvas canvas;

    private final ToggleButton panBtn     = modeBtn("Pan");
    private final ToggleButton addNodeBtn = modeBtn("Add Node");
    private final ToggleButton addEdgeBtn = modeBtn("Add Edge");
    private final ToggleButton removeBtn  = modeBtn("Remove");
    private final Button       clearBtn   = actionBtn("Clear");

    private final ToggleButton directedBtn = flagBtn("Directed");
    private final ToggleButton weightedBtn = flagBtn("Weighted");

    private boolean directed = false;
    private boolean weighted = false;

    public GraphToolBar(GraphService graphService, GraphCanvas canvas,
                        boolean directed, boolean weighted) {
        this.graphService = graphService;
        this.canvas = canvas;
        this.directed = directed;
        this.weighted = weighted;

        ToggleGroup modeGroup = new ToggleGroup();
        panBtn.setToggleGroup(modeGroup);
        addNodeBtn.setToggleGroup(modeGroup);
        addEdgeBtn.setToggleGroup(modeGroup);
        removeBtn.setToggleGroup(modeGroup);
        panBtn.setSelected(true);

        directedBtn.setSelected(directed);
        weightedBtn.setSelected(weighted);

        setStyle("-fx-background-color: #2d2d3f; -fx-padding: 6 10;");
        getItems().addAll(
            panBtn, addNodeBtn, addEdgeBtn, removeBtn, clearBtn,
            new Separator(),
            directedBtn, weightedBtn
        );
    }

    public void setCanvas(GraphCanvas canvas) {
        this.canvas = canvas;
        wireHandlers();
    }

    private void wireHandlers() {
        panBtn.setOnAction(e     -> canvas.setMode(CanvasMode.PAN));
        addNodeBtn.setOnAction(e -> canvas.setMode(CanvasMode.ADD_NODE));
        addEdgeBtn.setOnAction(e -> canvas.setMode(CanvasMode.ADD_EDGE));
        removeBtn.setOnAction(e  -> canvas.setMode(CanvasMode.REMOVE));

        clearBtn.setOnAction(e -> {
            graphService.getGraph().getAllNodes().clear();
            graphService.getGraph().getAllEdges().clear();
            canvas.setMode(CanvasMode.PAN);
        });

        directedBtn.setOnAction(e -> {
            directed = directedBtn.isSelected();
            canvas.setDirected(directed);
        });

        weightedBtn.setOnAction(e -> {
            weighted = weightedBtn.isSelected();
            canvas.setWeighted(weighted);
        });
    }

    private static ToggleButton modeBtn(String text) {
        ToggleButton b = new ToggleButton(text);
        b.setStyle(
            "-fx-background-color: #3d3d5c; -fx-text-fill: #f3f4f6; " +
            "-fx-background-radius: 6; -fx-cursor: hand;");
        b.selectedProperty().addListener((obs, old, sel) ->
            b.setStyle(sel
                ? "-fx-background-color: #7c3aed; -fx-text-fill: #ffffff; " +
                  "-fx-background-radius: 6; -fx-cursor: hand;"
                : "-fx-background-color: #3d3d5c; -fx-text-fill: #f3f4f6; " +
                  "-fx-background-radius: 6; -fx-cursor: hand;"));
        return b;
    }

    private static ToggleButton flagBtn(String text) {
        ToggleButton b = new ToggleButton(text);
        b.setStyle(
            "-fx-background-color: #3d3d5c; -fx-text-fill: #94a3b8; " +
            "-fx-background-radius: 6; -fx-cursor: hand;");
        b.selectedProperty().addListener((obs, old, sel) ->
            b.setStyle(sel
                ? "-fx-background-color: #065f46; -fx-text-fill: #6ee7b7; " +
                  "-fx-background-radius: 6; -fx-cursor: hand;"
                : "-fx-background-color: #3d3d5c; -fx-text-fill: #94a3b8; " +
                  "-fx-background-radius: 6; -fx-cursor: hand;"));
        return b;
    }

    private static Button actionBtn(String text) {
        Button b = new Button(text);
        b.setStyle(
            "-fx-background-color: #7f1d1d; -fx-text-fill: #fca5a5; " +
            "-fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }
}