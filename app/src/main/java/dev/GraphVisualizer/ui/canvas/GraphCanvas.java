package dev.GraphVisualizer.ui.canvas;

import dev.GraphVisualizer.models.Edge;
import dev.GraphVisualizer.models.Graph;
import dev.GraphVisualizer.models.Node;
import dev.GraphVisualizer.service.AlgorithmAddInfo;
import dev.GraphVisualizer.service.CommandManager;
import dev.GraphVisualizer.service.GraphService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.beans.binding.Bindings;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GraphCanvas extends Pane {
    private final Group graphGroup = new Group();
    private double dragStartX, dragStartY, translateX, translateY;
    private boolean isPanning = false;

    private final VBox zoomControls = new VBox(4);
    private final VBox legend       = new VBox(5);
    private double scale = 1.0;
    private static final double SCALE_STEP = 0.2;
    private static final double SCALE_MIN  = 0.2;
    private static final double SCALE_MAX  = 3.0;
    private static final double NODE_RADIUS = 22.0;

    private final GraphService   graphService;
    private final CommandManager commandManager;
    private CanvasMode mode = CanvasMode.PAN;
    private Node selectedNode = null;
    private int  nodeCounter  = 0;
    private boolean directed;
    private boolean weighted;
    private boolean darkTheme = true;

    // node dragging state
    private Node    draggingNode      = null;
    private double  dragNodeOrigX     = 0;
    private double  dragNodeOrigY     = 0;
    private boolean isDraggingNode    = false;
    private boolean nodeDragConsumed  = false;

    // highlighted shortest path (Dijkstra)
    private List<Node> highlightedPath = new ArrayList<>();
    private static final Color PATH_COLOR = Color.web("#fde047");

    private final Affine viewTransform = new Affine();

    private final Map<Circle, Node> circleToNode = new HashMap<>();
    private final Map<Line,   Edge> lineToEdge   = new HashMap<>();

    // algorithm result overlay — null when no algorithm has been run
    private Map<Node, AlgorithmAddInfo> algorithmState = null;

    // colors — updated by applyTheme()
    private Color NODE_FILL     = Color.web("#0ea5e9");
    private Color NODE_STROKE   = Color.web("#38bdf8");
    private Color NODE_SELECTED = Color.web("#f59e0b");
    private Color EDGE_COLOR    = Color.web("#475569");
    private Color LABEL_COLOR   = Color.web("#f1f5f9");
    private Color WEIGHT_COLOR  = Color.web("#7dd3fc");
    private Color ALGO_WHITE    = Color.web("#334155");   // unvisited during algorithm
    private Color ALGO_GREY     = Color.web("#f59e0b");   // in-queue / being processed
    private Color ALGO_BLACK    = Color.web("#4ade80");   // finished

    public enum CanvasMode {
        PAN, ADD_NODE, ADD_EDGE, REMOVE
    }

    public GraphCanvas(GraphService graphService, boolean directed, boolean weighted,
                       CommandManager commandManager) {
        this.graphService   = graphService;
        this.directed       = directed;
        this.weighted       = weighted;
        this.commandManager = commandManager;
        this.nodeCounter    = graphService.getGraph().getAllNodes().size();

        applyTheme();

        clipProperty().bind(Bindings.createObjectBinding(
            () -> new Rectangle(getWidth(), getHeight()),
            widthProperty(), heightProperty()));

        graphGroup.getTransforms().add(viewTransform);

        zoomControls.setAlignment(Pos.CENTER);
        zoomControls.setPadding(new Insets(4));

        Button zoomIn  = makeZoomBtn("+");
        Button zoomOut = makeZoomBtn("-");
        zoomIn.setOnAction(e  -> applyZoom(SCALE_STEP));
        zoomOut.setOnAction(e -> applyZoom(-SCALE_STEP));
        zoomControls.getChildren().addAll(zoomIn, zoomOut);
        zoomControls.layoutXProperty().bind(
            widthProperty().subtract(zoomControls.widthProperty()).subtract(12));
        zoomControls.setLayoutY(12);

        legend.setLayoutX(12);
        legend.layoutYProperty().bind(
            heightProperty().subtract(legend.heightProperty()).subtract(12));
        legend.setVisible(false);
        buildLegend();

        getChildren().addAll(graphGroup, zoomControls, legend);
        addPanHandlers();
        addCanvasClickHandler();
        refresh();
    }

    private Button makeZoomBtn(String text) {
        Button b = new Button(text);
        b.setMinSize(32, 32);
        b.setStyle(
            "-fx-background-color: #3d3d5c; -fx-text-fill: #f3f4f6; " +
            "-fx-font-size: 16; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6;");
        return b;
    }

    public void setDarkTheme(boolean dark) {
        this.darkTheme = dark;
        applyTheme();
        refresh();
    }

    private void applyTheme() {
        if (darkTheme) {
            setStyle("-fx-background-color: #0f172a;");
            zoomControls.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 8; -fx-border-radius: 8;");
            NODE_FILL     = Color.web("#0ea5e9");
            NODE_STROKE   = Color.web("#38bdf8");
            EDGE_COLOR    = Color.web("#475569");
            LABEL_COLOR   = Color.web("#f1f5f9");
            WEIGHT_COLOR  = Color.web("#7dd3fc");
            ALGO_WHITE    = Color.web("#334155");
            ALGO_GREY     = Color.web("#f59e0b");
            ALGO_BLACK    = Color.web("#4ade80");
        } else {
            setStyle("-fx-background-color: #f1f5f9;");
            zoomControls.setStyle("-fx-background-color: #cbd5e1; -fx-background-radius: 8; -fx-border-radius: 8;");
            NODE_FILL     = Color.web("#3b82f6");
            NODE_STROKE   = Color.web("#1d4ed8");
            EDGE_COLOR    = Color.web("#64748b");
            LABEL_COLOR   = Color.web("#0f172a");
            WEIGHT_COLOR  = Color.web("#1e40af");
            ALGO_WHITE    = Color.web("#94a3b8");
            ALGO_GREY     = Color.web("#f97316");
            ALGO_BLACK    = Color.web("#15803d");
        }
        NODE_SELECTED = Color.web("#e11d48");
        if (legend.isVisible()) buildLegend();
    }

    public void setMode(CanvasMode mode) {
        this.mode = mode;
        this.selectedNode = null;
        refresh();
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
        refresh();
    }

    public void setWeighted(boolean weighted) {
        this.weighted = weighted;
        refresh();
    }

    /** Overlays algorithm result colors and distance labels on the canvas. */
    public void showAlgorithmResult(Map<Node, AlgorithmAddInfo> state) {
        this.algorithmState = state;
        this.highlightedPath = new ArrayList<>();
        buildLegend();
        legend.setVisible(true);
        refresh();
    }

    /** Removes algorithm result overlay and restores normal node colors. */
    public void clearAlgorithmResult() {
        this.algorithmState = null;
        legend.setVisible(false);
        clearPath();
    }

    public void showPath(List<Node> path) {
        this.highlightedPath = path;
        refresh();
    }

    public void clearPath() {
        this.highlightedPath = new ArrayList<>();
        refresh();
    }

    public void resetNodeCounter() {
        nodeCounter = 0;
    }

    public void refresh() {
        graphGroup.getChildren().clear();
        circleToNode.clear();
        lineToEdge.clear();
        drawEdges();
        drawNodes();
    }

    private void drawNodes() {
        for (Node node : graphService.getGraph().getAllNodes()) {
            Color fill;
            if (algorithmState != null && algorithmState.containsKey(node)) {
                fill = switch (algorithmState.get(node).getNodeColor()) {
                    case WHITE -> ALGO_WHITE;
                    case GREY  -> ALGO_GREY;
                    case BLACK -> ALGO_BLACK;
                };
            } else {
                fill = (node == selectedNode) ? NODE_SELECTED : NODE_FILL;
            }

            Circle circle = new Circle(node.getPositionX(), node.getPositionY(), NODE_RADIUS, fill);
            boolean nodeOnPath = highlightedPath.contains(node);
            circle.setStroke(nodeOnPath ? PATH_COLOR : (node == selectedNode) ? NODE_SELECTED : NODE_STROKE);
            circle.setStrokeWidth(nodeOnPath ? 4 : 2);

            Text label = new Text(node.getLabel());
            label.setFill(LABEL_COLOR);
            label.setFont(Font.font("System", FontWeight.BOLD, 13));
            label.setX(node.getPositionX() - label.getLayoutBounds().getWidth() / 2);
            label.setY(node.getPositionY() + label.getLayoutBounds().getHeight() / 4);
            label.setMouseTransparent(true);

            circle.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.SECONDARY) {
                    handleNodeDoubleClick(node);
                    e.consume();
                    return;
                }
                if (e.getClickCount() == 2) handleNodeDoubleClick(node);
                else                        handleNodeClick(node, e);
                e.consume();
            });

            circleToNode.put(circle, node);
            graphGroup.getChildren().addAll(circle, label);

            // distance label below node when an algorithm has run
            if (algorithmState != null && algorithmState.containsKey(node)) {
                double d = algorithmState.get(node).getD();
                if (d != Double.POSITIVE_INFINITY) {
                    String dStr = (d == Math.floor(d))
                        ? String.valueOf((long) d)
                        : String.format("%.1f", d);
                    Text dist = new Text(dStr);
                    dist.setFill(LABEL_COLOR);
                    dist.setFont(Font.font("System", FontWeight.BOLD, 16));
                    dist.setX(node.getPositionX() - dist.getLayoutBounds().getWidth() / 2);
                    dist.setY(node.getPositionY() - NODE_RADIUS - 6);
                    dist.setMouseTransparent(true);
                    graphGroup.getChildren().add(dist);
                }
            }
        }
    }

    private void drawEdges() {
        for (Edge edge : graphService.getGraph().getAllEdges()) {
            Node src = edge.getSource();
            Node tgt = edge.getTarget();
            if (src == null || tgt == null) continue;

            boolean onPath = false;
            for (int i = 0; i < highlightedPath.size() - 1; i++) {
                Node p = highlightedPath.get(i);
                Node q = highlightedPath.get(i + 1);
                if ((src.equals(p) && tgt.equals(q)) || (src.equals(q) && tgt.equals(p))) { onPath = true; break; }
            }
            Color lineColor = onPath ? PATH_COLOR : EDGE_COLOR;

            Line line = new Line(
                src.getPositionX(), src.getPositionY(),
                tgt.getPositionX(), tgt.getPositionY());
            line.setStroke(lineColor);
            line.setStrokeWidth(onPath ? 5 : 3);
            lineToEdge.put(line, edge);
            graphGroup.getChildren().add(line);

            if (directed) {
                Polygon arrow = buildArrow(src, tgt);
                arrow.setFill(lineColor);
                graphGroup.getChildren().add(arrow);
            }

            if (weighted) {
                double mx = (src.getPositionX() + tgt.getPositionX()) / 2;
                double my = (src.getPositionY() + tgt.getPositionY()) / 2;
                double edgeDx = tgt.getPositionX() - src.getPositionX();
                double edgeDy = tgt.getPositionY() - src.getPositionY();
                double edgeLen = Math.sqrt(edgeDx * edgeDx + edgeDy * edgeDy);
                double perpX = edgeLen == 0 ? 0 : -edgeDy / edgeLen * 14;
                double perpY = edgeLen == 0 ? 0 :  edgeDx / edgeLen * 14;
                Text wLabel = new Text(String.format("%.1f", edge.getWeight()));
                wLabel.setFill(WEIGHT_COLOR);
                wLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
                wLabel.setX(mx + perpX - wLabel.getLayoutBounds().getWidth() / 2);
                wLabel.setY(my + perpY + wLabel.getLayoutBounds().getHeight() / 4);
                graphGroup.getChildren().add(wLabel);
            }

            // Wide transparent line on top so edges are easy to click
            Line hitLine = new Line(
                src.getPositionX(), src.getPositionY(),
                tgt.getPositionX(), tgt.getPositionY());
            hitLine.setStroke(Color.TRANSPARENT);
            hitLine.setStrokeWidth(14);
            hitLine.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.SECONDARY) {
                    handleEdgeDoubleClick(edge);
                    e.consume();
                    return;
                }
                if (e.getClickCount() == 2) handleEdgeDoubleClick(edge);
                else                        handleEdgeClick(edge, e);
                e.consume();
            });
            graphGroup.getChildren().add(hitLine);
        }
    }

    private Polygon buildArrow(Node src, Node tgt) {
        double dx = tgt.getPositionX() - src.getPositionX();
        double dy = tgt.getPositionY() - src.getPositionY();
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return new Polygon();

        double ux = dx / len;
        double uy = dy / len;

        double tipX = tgt.getPositionX() - ux * NODE_RADIUS;
        double tipY = tgt.getPositionY() - uy * NODE_RADIUS;

        double arrowLen  = 14;
        double arrowHalf = 6;
        double baseX = tipX - ux * arrowLen;
        double baseY = tipY - uy * arrowLen;
        double perpX = -uy * arrowHalf;
        double perpY =  ux * arrowHalf;

        return new Polygon(
            tipX, tipY,
            baseX + perpX, baseY + perpY,
            baseX - perpX, baseY - perpY
        );
    }

    private double screenToGraphX(double screenX) {
        return (screenX - translateX) / scale;
    }

    private double screenToGraphY(double screenY) {
        return (screenY - translateY) / scale;
    }

    private void addCanvasClickHandler() {
        addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (nodeDragConsumed) { nodeDragConsumed = false; e.consume(); return; }
            if (zoomControls.getBoundsInParent().contains(e.getX(), e.getY())) return;
            if (legend.isVisible() && legend.getBoundsInParent().contains(e.getX(), e.getY())) return;
            if (mode == CanvasMode.ADD_NODE) {
                double graphX = screenToGraphX(e.getX());
                double graphY = screenToGraphY(e.getY());
                if (!isOverNode(graphX, graphY)) addNode(graphX, graphY);
            }
        });
    }

    private void handleNodeClick(Node node, MouseEvent e) {
        switch (mode) {
            case ADD_EDGE -> {
                if (selectedNode == null) {
                    selectedNode = node;
                    refresh();
                } else if (selectedNode != node) {
                    double w = 1.0;
                    if (weighted) {
                        TextInputDialog wd = new TextInputDialog("1.0");
                        wd.setTitle("Edge weight");
                        String sep = directed ? " → " : " — ";
                        wd.setHeaderText(selectedNode.getLabel() + sep + node.getLabel());
                        wd.setContentText("Weight:");
                        wd.initOwner(getScene().getWindow());
                        styleDialog(wd);
                        Optional<String> wr = wd.showAndWait();
                        if (wr.isEmpty()) { selectedNode = null; refresh(); return; }
                        try { w = Double.parseDouble(wr.get()); }
                        catch (NumberFormatException ignored) { selectedNode = null; refresh(); return; }
                    }
                    double finalW = w;
                    Node src = selectedNode;
                    Edge newEdge = new Edge(src, node, finalW);
                    Graph graph = graphService.getGraph();
                    commandManager.push(
                        () -> graph.addEdge(newEdge),
                        () -> graph.removeEdge(newEdge)
                    );
                    selectedNode = null;
                    refresh();
                }
            }
            case REMOVE -> {
                Graph graph = graphService.getGraph();
                List<Edge> removed = graph.getAllEdges().stream()
                    .filter(ed -> ed.getSource() == node || ed.getTarget() == node)
                    .toList();
                commandManager.push(
                    () -> { removed.forEach(graph::removeEdge); graph.removeNode(node); },
                    () -> { graph.addNode(node); removed.forEach(graph::addEdge); }
                );
                refresh();
            }
            default -> {}
        }
    }

    private void handleEdgeClick(Edge edge, MouseEvent e) {
        if (mode == CanvasMode.REMOVE) {
            Graph graph = graphService.getGraph();
            commandManager.push(
                () -> graph.removeEdge(edge),
                () -> graph.addEdge(edge)
            );
            refresh();
        }
    }

    private void handleNodeDoubleClick(Node node) {
        TextInputDialog dialog = new TextInputDialog(node.getLabel());
        dialog.setTitle("Rename node");
        dialog.setHeaderText(null);
        dialog.setContentText("New label:");
        dialog.initOwner(getScene().getWindow());
        styleDialog(dialog);
        Optional<String> result = dialog.showAndWait();
        result.filter(s -> !s.isBlank()).ifPresent(s -> {
            String oldLabel = node.getLabel();
            commandManager.push(
                () -> node.setLabel(s),
                () -> node.setLabel(oldLabel)
            );
            refresh();
        });
    }

    private void handleEdgeDoubleClick(Edge edge) {
        if (!weighted) return;
        TextInputDialog dialog = new TextInputDialog(String.valueOf(edge.getWeight()));
        dialog.setTitle("Edit weight");
        String sep = directed ? " → " : " — ";
        dialog.setHeaderText(edge.getSource().getLabel() + sep + edge.getTarget().getLabel());
        dialog.setContentText("Weight:");
        dialog.initOwner(getScene().getWindow());
        styleDialog(dialog);
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            try {
                double newWeight = Double.parseDouble(s);
                double oldWeight = edge.getWeight();
                commandManager.push(
                    () -> edge.setWeight(newWeight),
                    () -> edge.setWeight(oldWeight)
                );
                refresh();
            } catch (NumberFormatException ignored) {}
        });
    }

    private void buildLegend() {
        legend.getChildren().clear();
        String bg = darkTheme ? "#1e293b" : "#e2e8f0";
        String fg = darkTheme ? "#cbd5e1" : "#334155";
        legend.setStyle(
            "-fx-background-color: " + bg + "; -fx-background-radius: 8; " +
            "-fx-border-radius: 8; -fx-padding: 8 12;");
        Label title = new Label("Algorithm colors (CLRS):");
        title.setStyle("-fx-text-fill: " + fg + "; -fx-font-weight: bold; -fx-font-size: 12;");
        legend.getChildren().addAll(
            title,
            legendItem(ALGO_WHITE, "WHITE — unvisited", fg),
            legendItem(ALGO_GREY,  "GREY  — in queue / processing", fg),
            legendItem(ALGO_BLACK, "BLACK — finished", fg)
        );
    }

    private HBox legendItem(Color color, String text, String fg) {
        Circle dot = new Circle(7, color);
        dot.setStroke(color.darker());
        dot.setStrokeWidth(1);
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: " + fg + "; -fx-font-size: 11; -fx-font-family: monospace;");
        HBox row = new HBox(8, dot, lbl);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void styleDialog(TextInputDialog dialog) {
        dialog.getDialogPane().setStyle(
            "-fx-background-color: #2d2d3f; -fx-font-size: 13px;");
        javafx.scene.Node content = dialog.getDialogPane().lookup(".content.label");
        if (content != null)
            content.setStyle("-fx-text-fill: #f3f4f6;");
    }

    private void addNode(double x, double y) {
        Node node = new Node(generateLabel(), x, y);
        Graph graph = graphService.getGraph();
        commandManager.push(
            () -> graph.addNode(node),
            () -> graph.removeNode(node)
        );
        refresh();
    }

    private String generateLabel() {
        int n = nodeCounter++;
        StringBuilder sb = new StringBuilder();
        do {
            sb.insert(0, (char) ('A' + n % 26));
            n = n / 26 - 1;
        } while (n >= 0);
        return sb.toString();
    }

    private boolean isOverNode(double x, double y) {
        return getNodeAt(x, y) != null;
    }

    private Node getNodeAt(double x, double y) {
        for (Node node : graphService.getGraph().getAllNodes()) {
            double dx = node.getPositionX() - x;
            double dy = node.getPositionY() - y;
            if (Math.sqrt(dx * dx + dy * dy) < NODE_RADIUS) return node;
        }
        return null;
    }

    private void applyZoom(double delta) {
        double oldScale = scale;
        scale = Math.max(SCALE_MIN, Math.min(SCALE_MAX, scale + delta));
        if (scale == oldScale) return;
        // zoom towards the centre of the visible viewport
        double cx = getWidth() / 2.0;
        double cy = getHeight() / 2.0;
        translateX = cx - (cx - translateX) * scale / oldScale;
        translateY = cy - (cy - translateY) * scale / oldScale;
        viewTransform.setMxx(scale);
        viewTransform.setMyy(scale);
        viewTransform.setTx(translateX);
        viewTransform.setTy(translateY);
    }

    private void addPanHandlers() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (zoomControls.getBoundsInParent().contains(e.getX(), e.getY())) {
                isPanning = false;
                return;
            }
            double gx  = screenToGraphX(e.getX());
            double gy  = screenToGraphY(e.getY());
            Node   hit = getNodeAt(gx, gy);
            if (mode == CanvasMode.PAN && hit != null && e.getButton() == MouseButton.PRIMARY) {
                draggingNode  = hit;
                dragNodeOrigX = hit.getPositionX();
                dragNodeOrigY = hit.getPositionY();
                isDraggingNode = true;
                isPanning      = false;
            } else if (mode == CanvasMode.PAN && e.getButton() == MouseButton.PRIMARY) {
                isPanning  = true;
                dragStartX = e.getX() - translateX;
                dragStartY = e.getY() - translateY;
            }
            e.consume();
        });

        addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if (mode == CanvasMode.PAN && isDraggingNode && draggingNode != null) {
                draggingNode.setPositionX(screenToGraphX(e.getX()));
                draggingNode.setPositionY(screenToGraphY(e.getY()));
                graphService.getGraph().setCache(true);
                refresh();
                e.consume();
            } else if (isPanning) {
                translateX = e.getX() - dragStartX;
                translateY = e.getY() - dragStartY;
                viewTransform.setTx(translateX);
                viewTransform.setTy(translateY);
                e.consume();
            }
        });

        addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (mode == CanvasMode.PAN && isDraggingNode && draggingNode != null) {
                Node   node  = draggingNode;
                double newX  = node.getPositionX();
                double newY  = node.getPositionY();
                double origX = dragNodeOrigX;
                double origY = dragNodeOrigY;
                if (newX != origX || newY != origY) {
                    nodeDragConsumed = true;
                    commandManager.push(
                        () -> { node.setPositionX(newX);  node.setPositionY(newY);  refresh(); },
                        () -> { node.setPositionX(origX); node.setPositionY(origY); refresh(); }
                    );
                }
            }
            isDraggingNode = false;
            draggingNode   = null;
            isPanning      = false;
        });
    }
}
