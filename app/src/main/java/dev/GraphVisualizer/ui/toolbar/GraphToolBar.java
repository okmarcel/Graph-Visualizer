package dev.GraphVisualizer.ui.toolbar;

import dev.GraphVisualizer.models.Edge;
import dev.GraphVisualizer.models.Graph;
import dev.GraphVisualizer.models.Node;
import dev.GraphVisualizer.repository.GraphIOException;
import dev.GraphVisualizer.service.AlgorithmService;
import dev.GraphVisualizer.service.CommandManager;
import dev.GraphVisualizer.service.GraphService;
import dev.GraphVisualizer.ui.canvas.GraphCanvas;
import dev.GraphVisualizer.ui.canvas.GraphCanvas.CanvasMode;

import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Class GraphToolBar - toolbar controlling editing, persistence and algorithms */
public class GraphToolBar extends ToolBar {
    /** Supported save formats exposed in the save menu */
    private enum SaveFormat {
        JSON("JSON", ".json"),
        CSV("CSV", ".csv"),
        TXT("Text", ".txt");

        private final String label;
        private final String extension;

        /**
         * Constructor taking menu label and file extension.
         * @param label user-visible format label
         * @param extension required file extension
         */
        SaveFormat(String label, String extension) {
            this.label = label;
            this.extension = extension;
        }

        /**
         * Creates a menu item representing this save format.
         * @return menu item for the format
         */
        private MenuItem toMenuItem() {
            return new MenuItem(label);
        }

        /**
         * Creates a file chooser filter for this format.
         * @return file extension filter
         */
        private FileChooser.ExtensionFilter toExtensionFilter() {
            return new FileChooser.ExtensionFilter(label + " files", "*" + extension);
        }
    }

    /** Graph service used by toolbar actions */
    private final GraphService graphService;

    /** Algorithm execution service */
    private final AlgorithmService algorithmService;

    /** Shared undo/redo manager */
    private final CommandManager commandManager;

    /** Canvas controlled by this toolbar */
    private GraphCanvas canvas;

    private final ToggleButton panBtn = modeBtn("Pan");
    private final ToggleButton addNodeBtn = modeBtn("Add Node");
    private final ToggleButton addEdgeBtn = modeBtn("Add Edge");
    private final ToggleButton removeBtn = modeBtn("Remove");
    private final Button clearBtn = dangerBtn("Clear");
    private final MenuButton saveBtn = menuBtn("Save");
    private final Button loadBtn = subtleBtn("Load");

    /** Toggle switching graph directionality */
    private final ToggleButton directedBtn = flagBtn("Directed");

    /** Toggle switching graph weighting */
    private final ToggleButton weightedBtn = flagBtn("Weighted");

    /** Button running BFS */
    private final Button bfsBtn = algoBtn("BFS");

    /** Button running DFS */
    private final Button dfsBtn = algoBtn("DFS");

    /** Button running Dijkstra */
    private final Button dijkstraBtn = algoBtn("Dijkstra");

    /** Button prompting for a Dijkstra target path */
    private final Button showPathBtn = subtleBtn("Path to…");

    /** Button clearing algorithm overlays */
    private final Button clearAlgoBtn = subtleBtn("Clear Results");

    private final Button undoBtn = subtleBtn("Undo");
    private final Button redoBtn = subtleBtn("Redo");

    /** Step-back button for recorded algorithm states */
    private final Button stepBackBtn = subtleBtn("◀");

    /** Step-forward button for recorded algorithm states */
    private final Button stepFwdBtn = subtleBtn("▶");

    /** Label showing current algorithm step index */
    private final Label  stepLabel = new Label("—");

    private final ToggleButton themeBtn = flagBtn("Dark Theme");

    /** Current graph directionality flag */
    private boolean directed;

    /** Current graph weighting flag */
    private boolean weighted;

    /** Source node from the last successful Dijkstra run */
    private Node lastDijkstraSource = null;
    
    /** Toggle group keeping editing modes mutually exclusive */
    private ToggleGroup modeGroup;

    /**
     * Constructor taking services, canvas and initial graph flags.
     * @param graphService graph service used by the toolbar
     * @param canvas canvas controlled by the toolbar
     * @param directed whether the current graph is directed
     * @param weighted whether the current graph is weighted
     * @param algorithmService algorithm execution service
     * @param commandManager shared undo/redo manager
     */
    public GraphToolBar(GraphService graphService, GraphCanvas canvas,
                        boolean directed, boolean weighted,
                        AlgorithmService algorithmService,
                        CommandManager commandManager) {
        this.graphService = graphService;
        this.canvas = canvas;
        this.directed = directed;
        this.weighted = weighted;
        this.algorithmService = algorithmService;
        this.commandManager = commandManager;

        modeGroup = new ToggleGroup();
        panBtn.setToggleGroup(modeGroup);
        addNodeBtn.setToggleGroup(modeGroup);
        addEdgeBtn.setToggleGroup(modeGroup);
        removeBtn.setToggleGroup(modeGroup);
        panBtn.setSelected(true);
        modeGroup.selectedToggleProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel == null && oldSel != null) {
                oldSel.setSelected(true);
            }
        });

        directedBtn.setSelected(directed);
        weightedBtn.setSelected(weighted);
        themeBtn.setSelected(true);
        updateThemeButtonText(true);
        initializeSaveMenu();

        stepBackBtn.setDisable(true);
        stepFwdBtn.setDisable(true);
        stepLabel.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 12;");

        setStyle("-fx-background-color: #1e293b; -fx-padding: 6 10;");
        getItems().addAll(
            panBtn, addNodeBtn, addEdgeBtn, removeBtn, clearBtn, saveBtn, loadBtn,
            new Separator(),
            directedBtn, weightedBtn,
            new Separator(),
            bfsBtn, dfsBtn, dijkstraBtn, showPathBtn, clearAlgoBtn,
            stepBackBtn, stepLabel, stepFwdBtn,
            new Separator(),
            undoBtn, redoBtn,
            new Separator(),
            themeBtn
        );
    }

    /**
     * Attaches the toolbar to the canvas after both controls are created.
     * @param canvas canvas instance to control
     */
    public void setCanvas(GraphCanvas canvas) {
        this.canvas = canvas;
        wireHandlers();
    }

    /** Wires all toolbar button handlers to canvas and service actions. */
    private void wireHandlers() {
        panBtn.setOnAction(e -> canvas.setMode(CanvasMode.PAN));
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
            resetAlgorithmUi();
            canvas.resetNodeCounter();
            canvas.setMode(CanvasMode.PAN);
            panBtn.setSelected(true);
        });

        loadBtn.setOnAction(e -> loadGraph());

        directedBtn.setOnAction(e -> {
            directed = directedBtn.isSelected();
            graphService.switchGraphType(directed, weighted);
            commandManager.clear();
            resetAlgorithmUi();
            canvas.updateGraphType(directed, weighted);
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
            resetAlgorithmUi();
            canvas.updateGraphType(directed, weighted);
        });

        bfsBtn.setOnAction(e -> runAlgorithm("BFS"));
        dfsBtn.setOnAction(e -> runAlgorithm("DFS"));
        dijkstraBtn.setOnAction(e -> runAlgorithm("Dijkstra"));

        showPathBtn.setDisable(true);
        showPathBtn.setOnAction(e -> showDijkstraPath());

        clearAlgoBtn.setOnAction(e -> resetAlgorithmUi());

        undoBtn.setOnAction(e -> { commandManager.undo(); canvas.syncNodeCounterWithGraph(); canvas.refresh(); });
        redoBtn.setOnAction(e -> { commandManager.redo(); canvas.syncNodeCounterWithGraph(); canvas.refresh(); });

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
            updateThemeButtonText(dark);
            setStyle(dark
                ? "-fx-background-color: #1e293b; -fx-padding: 6 10;"
                : "-fx-background-color: #e2e8f0; -fx-padding: 6 10;");
        });
    }

    /** Populates the save menu with supported output formats. */
    private void initializeSaveMenu() {
        for (SaveFormat format : SaveFormat.values()) {
            MenuItem item = format.toMenuItem();
            item.setOnAction(e -> saveGraph(format));
            saveBtn.getItems().add(item);
        }
    }

    /**
     * Saves the current graph using the selected format.
     * @param format chosen output format
     */
    private void saveGraph(SaveFormat format) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Graph");
        FileChooser.ExtensionFilter filter = format.toExtensionFilter();
        chooser.getExtensionFilters().add(filter);
        chooser.setSelectedExtensionFilter(filter);
        chooser.setInitialFileName("graph" + format.extension);

        File selected = chooser.showSaveDialog(canvas.getScene().getWindow());
        if (selected == null) {
            return;
        }

        try {
            graphService.save(withRequiredExtension(selected, format.extension));
        } catch (GraphIOException ex) {
            showIoError("Save failed", ex.getMessage());
        }
    }

    /** Opens a file chooser and loads a graph from the selected file. */
    private void loadGraph() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load Graph");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graph files", "*.json", "*.csv", "*.txt"));
        for (SaveFormat format : SaveFormat.values()) {
            chooser.getExtensionFilters().add(format.toExtensionFilter());
        }

        File selected = chooser.showOpenDialog(canvas.getScene().getWindow());
        if (selected == null) {
            return;
        }

        try {
            graphService.load(selected);
            syncUiToLoadedGraph();
        } catch (GraphIOException ex) {
            showIoError("Load failed", ex.getMessage());
        }
    }

    /** Synchronizes canvas and toolbar state after loading a graph from disk. */
    private void syncUiToLoadedGraph() {
        directed = graphService.isDirectedGraph();
        weighted = graphService.isWeightedGraph();
        directedBtn.setSelected(directed);
        weightedBtn.setSelected(weighted);
        commandManager.clear();
        resetAlgorithmUi();
        canvas.updateGraphType(directed, weighted);
        canvas.syncNodeCounterWithGraph();
        canvas.resetViewport();
        canvas.setMode(CanvasMode.PAN);
        panBtn.setSelected(true);
    }

    /** Clears algorithm visualization state and disables related controls. */
    private void resetAlgorithmUi() {
        canvas.clearAlgorithmResult();
        showPathBtn.setDisable(true);
        lastDijkstraSource = null;
        stepBackBtn.setDisable(true);
        stepFwdBtn.setDisable(true);
        stepLabel.setText("—");
    }

    /**
     * Shows an error alert for graph save/load failures.
     * @param title dialog title
     * @param message message to display
     */
    private void showIoError(String title, String message) {
        Alert err = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        err.setTitle(title);
        err.setHeaderText(null);
        err.initOwner(canvas.getScene().getWindow());
        err.showAndWait();
    }

    /**
     * Ensures that the selected save target has the expected extension.
     * @param file file selected by the user
     * @param extension required extension
     * @return file with the required extension applied
     */
    private File withRequiredExtension(File file, String extension) {
        String name = file.getName();
        if (name.toLowerCase().endsWith(extension)) {
            return file;
        }

        int dotIndex = name.lastIndexOf('.');
        String baseName = dotIndex > 0 ? name.substring(0, dotIndex) : name;
        return new File(file.getParentFile(), baseName + extension);
    }

    /**
     * Updates the theme toggle label so it describes the next action.
     * @param darkTheme whether dark theme is currently active
     */
    private void updateThemeButtonText(boolean darkTheme) {
        themeBtn.setText(darkTheme ? "Light Theme" : "Dark Theme");
    }

    /** Updates step navigation buttons and label from algorithm service state. */
    private void updateStepControls() {
        int idx = algorithmService.getStepIndex();
        int total = algorithmService.getStepCount();
        stepBackBtn.setDisable(idx <= 1);
        stepFwdBtn.setDisable(idx >= total);
        stepLabel.setText(idx + " / " + total);
    }

    /**
     * Runs the selected algorithm starting from a user-chosen source node.
     * @param type algorithm identifier
     */
    private void runAlgorithm(String type) {
        List<Node> nodes = graphService.getGraph().getAllNodes();
        if (nodes.isEmpty()) {
            return;
        }

        // 1. Pick source node
        List<String> labels = nodes.stream().map(Node::getLabel).toList();
        ChoiceDialog<String> sourceDialog = new ChoiceDialog<>(labels.get(0), labels);
        sourceDialog.setTitle(type + " — select source node");
        sourceDialog.setHeaderText(null);
        sourceDialog.setContentText("Source node:");
        sourceDialog.initOwner(canvas.getScene().getWindow());
        sourceDialog.getDialogPane().setStyle("-fx-background-color: #2d2d3f; -fx-font-size: 13px;");

        Optional<String> sourceChoice = sourceDialog.showAndWait();
        if (sourceChoice.isEmpty()) {
            return;
        }

        Node source = nodes.stream()
            .filter(n -> n.getLabel().equals(sourceChoice.get()))
            .findFirst().orElse(null);
        if (source == null) {
            return;
        }

        // 2. Run the algorithm (records all steps internally)
        try {
            switch (type) {
                case "BFS" -> algorithmService.runBFS(source);
                case "DFS" -> algorithmService.runDFS(source);
                case "Dijkstra" -> algorithmService.runDijkstra(source);
            }
        } catch (RuntimeException ex) {
            Alert err = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            err.initOwner(canvas.getScene().getWindow());
            err.showAndWait();
            return;
        }

        // 3. Ask: show full result or navigate step by step?
        ButtonType completeType = new ButtonType("Complete");
        ButtonType stepByStepType = new ButtonType("Step by Step");
        Alert modeAlert = new Alert(Alert.AlertType.CONFIRMATION);
        modeAlert.setTitle("Execution mode");
        modeAlert.setHeaderText(null);
        modeAlert.setContentText("Show the full result or navigate step by step?");
        modeAlert.getButtonTypes().setAll(completeType, stepByStepType);
        modeAlert.initOwner(canvas.getScene().getWindow());

        Optional<ButtonType> modeChoice = modeAlert.showAndWait();
        if (modeChoice.isEmpty()) {
            return;
        }

        if (modeChoice.get() == stepByStepType) {
            algorithmService.goToStart();
        }
        canvas.showAlgorithmResult(algorithmService.getStepState());
        updateStepControls();

        if (type.equals("Dijkstra")) {
            lastDijkstraSource = source;
            showPathBtn.setDisable(false);
        } else {
            showPathBtn.setDisable(true);
            lastDijkstraSource = null;
        }
    }

    /** Prompts for a target node and highlights the reconstructed Dijkstra path. */
    private void showDijkstraPath() {
        if (lastDijkstraSource == null) {
            return;
        }
        List<Node> nodes = graphService.getGraph().getAllNodes();
        List<String> targetLabels = nodes.stream()
            .filter(n -> !n.equals(lastDijkstraSource))
            .map(Node::getLabel)
            .toList();
        if (targetLabels.isEmpty()) {
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(targetLabels.get(0), targetLabels);
        dialog.setTitle("Highlight shortest path");
        dialog.setHeaderText(null);
        dialog.setContentText("Path to:");
        dialog.initOwner(canvas.getScene().getWindow());
        dialog.getDialogPane().setStyle("-fx-background-color: #2d2d3f; -fx-font-size: 13px;");
        dialog.showAndWait().ifPresent(label -> {
            Node target = nodes.stream().filter(n -> n.getLabel().equals(label)).findFirst().orElse(null);
            if (target == null) {
                return;
            }
            List<Node> path = algorithmService.reconstructPath(target);
            if (path.size() < 2) {
                Alert info = new Alert(Alert.AlertType.INFORMATION,
                    label + " is not reachable from " + lastDijkstraSource.getLabel(), ButtonType.OK);
                info.setTitle("No path");
                info.setHeaderText(null);
                info.initOwner(canvas.getScene().getWindow());
                info.showAndWait();
                return;
            }
            canvas.showPath(path);
        });
    }

    /**
     * Creates a styled toggle button used for mutually exclusive canvas modes.
     * @param text button label
     * @return configured toggle button
     */
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

    /**
     * Creates a styled toggle button used for persistent graph flags.
     * @param text button label
     * @return configured toggle button
     */
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

    /**
     * Creates a styled button for destructive actions.
     * @param text button label
     * @return configured button
     */
    private static Button dangerBtn(String text) {
        Button b = new Button(text);
        b.setStyle(
            "-fx-background-color: #7f1d1d; -fx-text-fill: #fca5a5; " +
            "-fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }

    /**
     * Creates a styled button used for algorithm actions.
     * @param text button label
     * @return configured button
     */
    private static Button algoBtn(String text) {
        Button b = new Button(text);
        b.setStyle(
            "-fx-background-color: #1e3a5f; -fx-text-fill: #93c5fd; " +
            "-fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }

    /**
     * Creates a styled button for secondary actions.
     * @param text button label
     * @return configured button
     */
    private static Button subtleBtn(String text) {
        Button b = new Button(text);
        b.setStyle(
            "-fx-background-color: #374151; -fx-text-fill: #9ca3af; " +
            "-fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }

    /**
     * Creates a styled menu button.
     * @param text button label
     * @return configured menu button
     */
    private static MenuButton menuBtn(String text) {
        MenuButton b = new MenuButton(text);
        b.setStyle(
            "-fx-background-color: #374151; -fx-text-fill: #9ca3af; " +
            "-fx-text-base-color: #9ca3af; -fx-mark-color: #9ca3af; " +
            "-fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }
}
