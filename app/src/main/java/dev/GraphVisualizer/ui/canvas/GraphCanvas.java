package dev.GraphVisualizer.ui.canvas;

import dev.GraphVisualizer.service.GraphService;
import dev.GraphVisualizer.models.Node;
import dev.GraphVisualizer.models.Edge;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.beans.binding.Bindings;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GraphCanvas extends Pane {
    private final Group graphGroup = new Group();
    private double dragStartX, dragStartY, translateX, translateY;
    private boolean isPanning = false;

    private final VBox zoomControls = new VBox(4);
    private double scale = 1.0;
    private static final double SCALE_STEP = 0.2;
    private static final double SCALE_MIN  = 0.2;
    private static final double SCALE_MAX  = 3.0;
    private static final double NODE_RADIUS = 22.0;

    private final GraphService graphService;
    private CanvasMode mode = CanvasMode.PAN;
    private Node selectedNode = null;
    private int nodeCounter = 0;
    private boolean directed;
    private boolean weighted;

    private final Map<Circle, Node> circleToNode = new HashMap<>();
    private final Map<Line, Edge>   lineToEdge   = new HashMap<>();

    // colors
    private static final Color NODE_FILL     = Color.web("#7c3aed");
    private static final Color NODE_STROKE   = Color.web("#a78bfa");
    private static final Color NODE_SELECTED = Color.web("#f59e0b");
    private static final Color EDGE_COLOR    = Color.web("#6b7280");
    private static final Color LABEL_COLOR   = Color.web("#f3f4f6");
    private static final Color WEIGHT_COLOR  = Color.web("#a78bfa");

    public enum CanvasMode {
        PAN, ADD_NODE, ADD_EDGE, REMOVE
    }

    public GraphCanvas(GraphService graphService, boolean directed, boolean weighted) {
        this.graphService = graphService;
        this.directed = directed;
        this.weighted = weighted;

        setStyle("-fx-background-color: #1e1e2e;");

        clipProperty().bind(Bindings.createObjectBinding(
            () -> new Rectangle(getWidth(), getHeight()),
            widthProperty(), heightProperty()));

        zoomControls.setAlignment(Pos.CENTER);
        zoomControls.setPadding(new Insets(4));
        zoomControls.setStyle(
            "-fx-background-color: #2d2d3f; -fx-background-radius: 8; -fx-border-radius: 8;");

        Button zoomIn  = makeZoomBtn("+");
        Button zoomOut = makeZoomBtn("-");
        zoomIn.setOnAction(e -> applyZoom(SCALE_STEP));
        zoomOut.setOnAction(e -> applyZoom(-SCALE_STEP));
        zoomControls.getChildren().addAll(zoomIn, zoomOut);
        zoomControls.layoutXProperty().bind(
            widthProperty().subtract(zoomControls.widthProperty()).subtract(12));
        zoomControls.setLayoutY(12);

        getChildren().addAll(graphGroup, zoomControls);
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

    public void refresh() {
        graphGroup.getChildren().clear();
        circleToNode.clear();
        lineToEdge.clear();
        drawEdges();
        drawNodes();
    }

    private void drawNodes() {
        for (Node node : graphService.getGraph().getAllNodes()) {
            Color fill = (node == selectedNode) ? NODE_SELECTED : NODE_FILL;

            Circle circle = new Circle(node.getPositionX(), node.getPositionY(), NODE_RADIUS, fill);
            circle.setStroke(NODE_STROKE);
            circle.setStrokeWidth(2);

            Text label = new Text(node.getLabel());
            label.setFill(LABEL_COLOR);
            label.setFont(Font.font("System", FontWeight.BOLD, 13));
            label.setX(node.getPositionX() - label.getLayoutBounds().getWidth() / 2);
            label.setY(node.getPositionY() + label.getLayoutBounds().getHeight() / 4);

            circle.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    handleNodeDoubleClick(node);
                } else {
                    handleNodeClick(node, e);
                }
                e.consume();
            });

            circleToNode.put(circle, node);
            graphGroup.getChildren().addAll(circle, label);
        }
    }

    private void drawEdges() {
        for (Edge edge : graphService.getGraph().getAllEdges()) {
            Node src = edge.getSource();
            Node tgt = edge.getTarget();
            if (src == null || tgt == null) continue;

            Line line = new Line(
                src.getPositionX(), src.getPositionY(),
                tgt.getPositionX(), tgt.getPositionY());
            line.setStroke(EDGE_COLOR);
            line.setStrokeWidth(2);
            line.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) handleEdgeDoubleClick(edge);
                else handleEdgeClick(edge, e);
                e.consume();
            });
            lineToEdge.put(line, edge);
            graphGroup.getChildren().add(line);

            // arrowhead for directed graphs
            if (directed) {
                Polygon arrow = buildArrow(src, tgt);
                arrow.setFill(EDGE_COLOR);
                graphGroup.getChildren().add(arrow);
            }

            // weight label
            if (weighted) {
                double mx = (src.getPositionX() + tgt.getPositionX()) / 2;
                double my = (src.getPositionY() + tgt.getPositionY()) / 2;
                Text wLabel = new Text(String.format("%.1f", edge.getWeight()));
                wLabel.setFill(WEIGHT_COLOR);
                wLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
                wLabel.setX(mx + 4);
                wLabel.setY(my - 4);
                graphGroup.getChildren().add(wLabel);
            }
        }
    }

    private Polygon buildArrow(Node src, Node tgt) {
        double dx = tgt.getPositionX() - src.getPositionX();
        double dy = tgt.getPositionY() - src.getPositionY();
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return new Polygon();

        double ux = dx / len;
        double uy = dy / len;

        // tip of arrow at edge of target circle
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

    private void addCanvasClickHandler() {
        addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (zoomControls.getBoundsInParent().contains(e.getX(), e.getY())) return;
            if (mode == CanvasMode.ADD_NODE) {
                double wx = (e.getX() - translateX) / scale;
                double wy = (e.getY() - translateY) / scale;
                if (!isOverNode(wx, wy)) addNode(wx, wy);
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
                    graphService.getGraph().addEdge(new Edge(selectedNode, node));
                    selectedNode = null;
                    refresh();
                }
            }
            case REMOVE -> {
                graphService.getGraph().getAllEdges()
                    .removeIf(edge -> edge.getSource().equals(node) || edge.getTarget().equals(node));
                graphService.getGraph().removeNode(node);
                refresh();
            }
            default -> {}
        }
    }

    private void handleEdgeClick(Edge edge, MouseEvent e) {
        if (mode == CanvasMode.REMOVE) {
            graphService.getGraph().removeEdge(edge);
            refresh();
        }
    }

    private void handleNodeDoubleClick(Node node) {
        TextInputDialog dialog = new TextInputDialog(node.getLabel());
        dialog.setTitle("Rename node");
        dialog.setHeaderText(null);
        dialog.setContentText("New label:");
        styleDialog(dialog);
        Optional<String> result = dialog.showAndWait();
        result.filter(s -> !s.isBlank()).ifPresent(s -> {
            node.setLabel(s);
            refresh();
        });
    }

    private void handleEdgeDoubleClick(Edge edge) {
        if (!weighted) return;
        TextInputDialog dialog = new TextInputDialog(String.valueOf(edge.getWeight()));
        dialog.setTitle("Edit weight");
        dialog.setHeaderText(null);
        dialog.setContentText("Weight:");
        styleDialog(dialog);
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            try {
                edge.setWeight(Double.parseDouble(s));
                refresh();
            } catch (NumberFormatException ignored) {}
        });
    }

    private void styleDialog(TextInputDialog dialog) {
        dialog.getDialogPane().setStyle(
            "-fx-background-color: #2d2d3f; -fx-font-size: 13px;");
        dialog.getDialogPane().lookup(".content.label")
            .setStyle("-fx-text-fill: #f3f4f6;");
    }

    private void addNode(double x, double y) {
        Node node = new Node(generateLabel(), x, y);
        graphService.getGraph().addNode(node);
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
        for (Node node : graphService.getGraph().getAllNodes()) {
            double dx = node.getPositionX() - x;
            double dy = node.getPositionY() - y;
            if (Math.sqrt(dx * dx + dy * dy) < NODE_RADIUS) return true;
        }
        return false;
    }

    private void applyZoom(double delta) {
        scale = Math.max(SCALE_MIN, Math.min(SCALE_MAX, scale + delta));
        graphGroup.setScaleX(scale);
        graphGroup.setScaleY(scale);
    }

    private void addPanHandlers() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (zoomControls.getBoundsInParent().contains(e.getX(), e.getY())) {
                isPanning = false;
                return;
            }
            if (mode == CanvasMode.PAN) {
                isPanning = true;
                dragStartX = e.getX() - translateX;
                dragStartY = e.getY() - translateY;
            }
            e.consume();
        });

        addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if (isPanning) {
                translateX = e.getX() - dragStartX;
                translateY = e.getY() - dragStartY;
                graphGroup.setTranslateX(translateX);
                graphGroup.setTranslateY(translateY);
                e.consume();
            }
        });

        addEventFilter(MouseEvent.MOUSE_RELEASED, e -> isPanning = false);
    }
}