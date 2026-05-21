package dev.GraphVisualizer.ui.toolbar;

import dev.GraphVisualizer.models.Edge;
import dev.GraphVisualizer.models.Graph;
import dev.GraphVisualizer.models.Node;
import dev.GraphVisualizer.service.AlgorithmService;
import dev.GraphVisualizer.service.CommandManager;
import dev.GraphVisualizer.service.GraphService;
import dev.GraphVisualizer.ui.canvas.GraphCanvas;
import dev.GraphVisualizer.ui.canvas.GraphCanvas.CanvasMode;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GraphToolBar extends ToolBar {

    private final GraphService     graphService;
    private final AlgorithmService algorithmService;
    private final CommandManager   commandManager;
    private GraphCanvas canvas;

    private final ToggleButton panBtn     = modeBtn("Pan");
    private final ToggleButton addNodeBtn = modeBtn("Add Node");
    private final ToggleButton addEdgeBtn = modeBtn("Add Edge");
    private final ToggleButton removeBtn  = modeBtn("Remove");
    private final Button       clearBtn   = dangerBtn("Clear");

    private final ToggleButton directedBtn = flagBtn("Directed");
    private final ToggleButton weightedBtn = flagBtn("Weighted");

    private final Button bfsBtn      = algoBtn("BFS");
    private final Button dfsBtn      = algoBtn("DFS");
    private final Button dijkstraBtn = algoBtn("Dijkstra");
    private final Button clearAlgoBtn = subtleBtn("Clear Results");

    private final Button undoBtn = subtleBtn("Undo");
    private final Button redoBtn = subtleBtn("Redo");

    private final Button       stepBackBtn = subtleBtn("◀");
    private final Button       stepFwdBtn  = subtleBtn("▶");
    private final Label        stepLabel   = new Label("—");

    private final ToggleButton themeBtn    = flagBtn("Dark Theme");

    private boolean directed;
    private boolean weighted;

    public GraphToolBar(GraphService graphService, GraphCanvas canvas,
                        boolean directed, boolean weighted,
                        AlgorithmService algorithmService,
                        CommandManager commandManager) {
        this.graphService     = graphService;
        this.canvas           = canvas;
        this.directed         = directed;
        this.weighted         = weighted;
        this.algorithmService = algorithmService;
        this.commandManager   = commandManager;

        ToggleGroup modeGroup = new ToggleGroup();
        panBtn.setToggleGroup(modeGroup);
        addNodeBtn.setToggleGroup(modeGroup);
        addEdgeBtn.setToggleGroup(modeGroup);
        removeBtn.setToggleGroup(modeGroup);
        panBtn.setSelected(true);

        directedBtn.setSelected(directed);
        weightedBtn.setSelected(weighted);
        themeBtn.setSelected(true);

        stepBackBtn.setDisable(true);
        stepFwdBtn.setDisable(true);
        stepLabel.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 12;");

        setStyle("-fx-background-color: #1e293b; -fx-padding: 6 10;");
        getItems().addAll(
            panBtn, addNodeBtn, addEdgeBtn, removeBtn, clearBtn,
            new Separator(),
            directedBtn, weightedBtn,
            new Separator(),
            bfsBtn, dfsBtn, dijkstraBtn, clearAlgoBtn,
            stepBackBtn, stepLabel, stepFwdBtn,
            new Separator(),
            undoBtn, redoBtn,
            new Separator(),
            themeBtn
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
            Graph graph = graphService.getGraph();
            List<Node> savedNodes = new ArrayList<>(graph.getAllNodes());
            List<Edge> savedEdges = new ArrayList<>(graph.getAllEdges());
            commandManager.push(
                () -> { graph.getAllNodes().clear(); graph.getAllEdges().clear(); graph.setCache(true); },
                () -> { savedNodes.forEach(graph::addNode); savedEdges.forEach(graph::addEdge); }
            );
            canvas.clearAlgorithmResult();
            canvas.setMode(CanvasMode.PAN);
        });

        directedBtn.setOnAction(e -> {
            directed = directedBtn.isSelected();
            graphService.switchGraphType(directed, weighted);
            commandManager.clear();
            canvas.clearAlgorithmResult();
            canvas.setDirected(directed);
        });

        weightedBtn.setOnAction(e -> {
            boolean nowWeighted = weightedBtn.isSelected();
            if (!nowWeighted) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Switching to unweighted will reset all edge weights to 1.0.\nContinue?",
                    ButtonType.OK, ButtonType.CANCEL);
                confirm.setTitle("Confirm");
                confirm.setHeaderText(null);
                confirm.initOwner(canvas.getScene().getWindow());
                Optional<ButtonType> result = confirm.showAndWait();
                if (result.isEmpty() || result.get() != ButtonType.OK) {
                    weightedBtn.setSelected(true); // revert the toggle
                    return;
                }
            }
            weighted = nowWeighted;
            graphService.switchGraphType(directed, weighted);
            commandManager.clear();
            canvas.clearAlgorithmResult();
            canvas.setWeighted(weighted);
        });

        bfsBtn.setOnAction(e      -> runAlgorithm("BFS"));
        dfsBtn.setOnAction(e      -> runAlgorithm("DFS"));
        dijkstraBtn.setOnAction(e -> runAlgorithm("Dijkstra"));
        clearAlgoBtn.setOnAction(e -> {
            canvas.clearAlgorithmResult();
            stepBackBtn.setDisable(true);
            stepFwdBtn.setDisable(true);
            stepLabel.setText("—");
        });

        undoBtn.setOnAction(e -> { commandManager.undo(); canvas.refresh(); });
        redoBtn.setOnAction(e -> { commandManager.redo(); canvas.refresh(); });

        stepBackBtn.setOnAction(e -> {
            if (algorithmService.stepBack()) {
                canvas.showAlgorithmResult(algorithmService.getStepState());
                updateStepControls();
            }
        });
        stepFwdBtn.setOnAction(e -> {
            if (algorithmService.stepForward()) {
                canvas.showAlgorithmResult(algorithmService.getStepState());
                updateStepControls();
            }
        });

        themeBtn.setOnAction(e -> {
            boolean dark = themeBtn.isSelected();
            canvas.setDarkTheme(dark);
            setStyle(dark
                ? "-fx-background-color: #1e293b; -fx-padding: 6 10;"
                : "-fx-background-color: #e2e8f0; -fx-padding: 6 10;");
        });
    }

    private void updateStepControls() {
        int idx   = algorithmService.getStepIndex();
        int total = algorithmService.getStepCount();
        stepBackBtn.setDisable(idx <= 1);
        stepFwdBtn.setDisable(idx >= total);
        stepLabel.setText(idx + " / " + total);
    }

    private void runAlgorithm(String type) {
        List<Node> nodes = graphService.getGraph().getAllNodes();
        if (nodes.isEmpty()) return;

        // 1. Pick source node
        List<String> labels = nodes.stream().map(Node::getLabel).toList();
        ChoiceDialog<String> sourceDialog = new ChoiceDialog<>(labels.get(0), labels);
        sourceDialog.setTitle(type + " — select source node");
        sourceDialog.setHeaderText(null);
        sourceDialog.setContentText("Source node:");
        sourceDialog.initOwner(canvas.getScene().getWindow());
        sourceDialog.getDialogPane().setStyle("-fx-background-color: #2d2d3f; -fx-font-size: 13px;");

        Optional<String> sourceChoice = sourceDialog.showAndWait();
        if (sourceChoice.isEmpty()) return;

        Node source = nodes.stream()
            .filter(n -> n.getLabel().equals(sourceChoice.get()))
            .findFirst().orElse(null);
        if (source == null) return;

        // 2. Run the algorithm (records all steps internally)
        try {
            switch (type) {
                case "BFS"      -> algorithmService.runBFS(source);
                case "DFS"      -> algorithmService.runDFS(source);
                case "Dijkstra" -> algorithmService.runDijkstra(source);
            }
        } catch (RuntimeException ex) {
            Alert err = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            err.initOwner(canvas.getScene().getWindow());
            err.showAndWait();
            return;
        }

        // 3. Ask: show full result or navigate step by step?
        ButtonType completeType   = new ButtonType("Complete");
        ButtonType stepByStepType = new ButtonType("Step by Step");
        Alert modeAlert = new Alert(Alert.AlertType.CONFIRMATION);
        modeAlert.setTitle("Execution mode");
        modeAlert.setHeaderText(null);
        modeAlert.setContentText("Show the full result or navigate step by step?");
        modeAlert.getButtonTypes().setAll(completeType, stepByStepType);
        modeAlert.initOwner(canvas.getScene().getWindow());

        Optional<ButtonType> modeChoice = modeAlert.showAndWait();
        if (modeChoice.isEmpty()) return;

        if (modeChoice.get() == stepByStepType) algorithmService.goToStart();
        canvas.showAlgorithmResult(algorithmService.getStepState());
        updateStepControls();
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

    private static Button dangerBtn(String text) {
        Button b = new Button(text);
        b.setStyle(
            "-fx-background-color: #7f1d1d; -fx-text-fill: #fca5a5; " +
            "-fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }

    private static Button algoBtn(String text) {
        Button b = new Button(text);
        b.setStyle(
            "-fx-background-color: #1e3a5f; -fx-text-fill: #93c5fd; " +
            "-fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }

    private static Button subtleBtn(String text) {
        Button b = new Button(text);
        b.setStyle(
            "-fx-background-color: #374151; -fx-text-fill: #9ca3af; " +
            "-fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }
}
