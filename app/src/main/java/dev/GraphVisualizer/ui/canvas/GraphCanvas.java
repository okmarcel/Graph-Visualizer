package dev.GraphVisualizer.ui.canvas;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GraphCanvas extends Pane {

    public GraphCanvas() {
        setStyle("-fx-border-color: red; -fx-border-width: 2;");
        drawSampleGraph();
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
            getChildren().add(line);
        }

        // Draw nodes
        for (int i = 0; i < x.length; i++) {
            Circle circle = new Circle(x[i], y[i], 20, Color.STEELBLUE);
            circle.setStroke(Color.DARKBLUE);
            circle.setStrokeWidth(2);

            Text label = new Text(x[i] - 6, y[i] + 5, labels[i]);
            label.setFill(Color.WHITE);
            label.setFont(new Font(14));

            getChildren().addAll(circle, label);
        }
    }
}