package dev.GraphVisualizer.ui.toolbar;

import dev.GraphVisualizer.service.GraphService;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

public class GraphToolBar extends ToolBar {
    private final Button addNodeBtn = new Button("Add Node");
    private final Button addEdgeBtn = new Button("Add Edge");
    private final Button removeNodeBtn = new Button("Remove Node");
    private final Button removeEdgeBtn = new Button("Remove Edge");
    private final Button clearBtn = new Button("Clear Canvas");

    private final GraphService graphService;
    public GraphToolBar(GraphService graphService) {
        this.graphService = graphService;
        
        getItems().addAll(
            addNodeBtn, removeNodeBtn,
            addEdgeBtn, removeEdgeBtn,
            clearBtn
        );
        // setPadding(new Insets(4, 8, 4, 8));
        wireHandlers();
    }

    private void wireHandlers() {
        // Temporary logs - replace with service functions
        addNodeBtn.setOnAction(e -> System.out.println("Add node clicked"));
        removeNodeBtn.setOnAction(e -> System.out.println("Remove node clicked"));
        addEdgeBtn.setOnAction(e -> System.out.println("Add edge clicked"));
        removeEdgeBtn.setOnAction(e -> System.out.println("Remove edge clicked"));
        clearBtn.setOnAction(e -> System.out.println("Clear button clicked"));
    }
}