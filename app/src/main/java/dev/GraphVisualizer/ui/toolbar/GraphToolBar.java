package dev.GraphVisualizer.ui.toolbar;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

public class GraphToolBar extends ToolBar {
    private final Button addNodeBtn = new Button("Add Node");
    private final Button addEdgeBtn = new Button("Add Edge");
    private final Button removeNodeBtn = new Button("Remove Node");
    private final Button removeEdgeBtn = new Button("Remove Edge");
    // private final Button runAlgoBtn = new Button("Run Algorithm");
    private final Button clearBtn = new Button("Clear Canvas");

    public GraphToolBar() {
        getItems().addAll(
            addNodeBtn, addEdgeBtn,
            removeNodeBtn, removeEdgeBtn,
            clearBtn
        );
        setPadding(new Insets(4, 8, 4, 8));
        wireHandlers();
    }

    private void wireHandlers() {
        // Temporary log on action to test functionality
        addNodeBtn.setOnAction(e -> System.out.println("Add node clicked"));
    }
}