package dev.GraphVisualizer.ui.canvas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.beans.binding.Bindings;

/**
 * 
 */
public class GraphCanvas extends Pane {
    private final Group graphGroup = new Group();
    private double dragStartX;
    private double dragStartY;
    private double translateX;
    private double translateY;
    private boolean isPanning = false;

    private final VBox zoomControls = new VBox(4);
    private double scale = 1.0;
    private static final double SCALE_STEP = 0.2;
    private static final double SCALE_MIN  = 0.2;
    private static final double SCALE_MAX  = 3.0;

    public GraphCanvas() {    
        clipProperty().bind(Bindings.createObjectBinding(() -> {
            Rectangle clip = new Rectangle(getWidth(), getHeight());
            return clip;
        }, widthProperty(), heightProperty()));

        zoomControls.setAlignment(Pos.CENTER);
        zoomControls.setPadding(new Insets(4));

        Button zoomIn = new Button("+");
        Button zoomOut = new Button("-");

        zoomIn.setMinSize(28, 28);
        zoomOut.setMinSize(28, 28);
        
        zoomIn.setOnAction(e -> applyZoom(SCALE_STEP));
        zoomOut.setOnAction(e -> applyZoom(-SCALE_STEP));

        zoomControls.getChildren().addAll(zoomIn, zoomOut);

        zoomControls.layoutXProperty().bind(widthProperty().subtract(zoomControls.widthProperty()).subtract(8));
        zoomControls.setLayoutY(8);

        // Add elements from bottom to top layer
        getChildren().addAll(
            graphGroup,
            zoomControls
        );
        // drawSampleGraph(); // hardcoded graph shape
        addPanHandlers();
    }

    private void applyZoom(double delta) {
        scale += delta;
        if (scale < SCALE_MIN)
            scale = SCALE_MIN;
        else if (scale > SCALE_MAX)
            scale = SCALE_MAX;

        graphGroup.setScaleX(scale);
        graphGroup.setScaleY(scale);
    }

    private void addPanHandlers() {
        addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, e -> {
            if (zoomControls.getBoundsInParent().contains(e.getX(), e.getY())) {
                isPanning = false;
                return; // let the event reach the buttons
            }
            isPanning = true;
            dragStartX = e.getX() - translateX;
            dragStartY = e.getY() - translateY;
            e.consume();
        });

        addEventFilter(javafx.scene.input.MouseEvent.MOUSE_DRAGGED, e -> {
            if (isPanning) {
                translateX = e.getX() - dragStartX;
                translateY = e.getY() - dragStartY;
                graphGroup.setTranslateX(translateX);
                graphGroup.setTranslateY(translateY);
                e.consume();
            }
        });

        addEventFilter(javafx.scene.input.MouseEvent.MOUSE_RELEASED, e -> {
            isPanning = false;
        });
    }

    /** Draw hardcoded graph shape for testing */
    private void drawSampleGraph() {
        // Node positions
        double[] x = {200, 400, 150, 350, 300};
        double[] y = {100, 100, 250, 250, 400};
        String[] labels = {"A", "B", "C", "D", "E"};

        // Edges (index pairs)
        int[][] edges = {{0,1}, {0,2}, {1,3}, {2,3}, {2,4}, {3,4}};

        // Draw edges first so nodes render on top
        for (int[] edge : edges) {
            Line line = new Line(x[edge[0]], y[edge[0]], x[edge[1]], y[edge[1]]);
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(2);
            graphGroup.getChildren().add(line);
        }

        // Draw nodes
        for (int i = 0; i < x.length; i++) {
            Circle circle = new Circle(x[i], y[i], 20, Color.STEELBLUE);
            circle.setStroke(Color.DARKBLUE);
            circle.setStrokeWidth(2);

            Text label = new Text(x[i] - 6, y[i] + 5, labels[i]);
            label.setFill(Color.WHITE);
            label.setFont(new Font(14));

            graphGroup.getChildren().addAll(circle, label);
        }
    }
}