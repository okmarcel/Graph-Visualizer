package dev.GraphVisualizer.ui.canvas;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.beans.binding.Bindings;

public class GraphCanvas extends Pane {
    private final Group graphGroup = new Group();
    private double dragStartX;
    private double dragStartY;
    private double translateX;
    private double translateY;
    

    public GraphCanvas() {
        clipProperty().bind(Bindings.createObjectBinding(() -> {
            Rectangle clip = new Rectangle(getWidth(), getHeight());
            clip.setArcWidth(14);
            clip.setArcHeight(14);
            return clip;
        }, widthProperty(), heightProperty()));
        
        // Create a border as a Rectangle instance
        Rectangle border = new Rectangle();
        border.setFill(Color.TRANSPARENT);   // make transparent inside border
        border.setStroke(Color.GRAY);       // border color
        border.setStrokeWidth(2);           // border width
        border.setArcWidth(14);             // rounded corners
        border.setArcHeight(14);            // rounded corners
        border.widthProperty().bind(widthProperty());
        border.heightProperty().bind(heightProperty());

        getChildren().add(graphGroup);
        getChildren().add(border);
        drawSampleGraph(); // hardcoded graph shape
        addPanHandlers();
    }

    private void addPanHandlers() {
        setOnMousePressed(e -> {
            dragStartX = e.getX() - translateX;
            dragStartY = e.getY() - translateY;
        });

        setOnMouseDragged(e -> {
            translateX = e.getX() - dragStartX;
            translateY = e.getY() - dragStartY;
            graphGroup.setTranslateX(translateX);
            graphGroup.setTranslateY(translateY);
        });
    }

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