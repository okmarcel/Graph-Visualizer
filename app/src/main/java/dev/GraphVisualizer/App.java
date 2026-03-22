package dev.GraphVisualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
	private final int sceneWidth = 640;
	private final int sceneHeight = 480;

	@Override
	public void start(Stage stage) {
		Label label = new Label("Example text in JavaFX.");
		StackPane root = new StackPane(label);
		Scene scene = new Scene(root, sceneWidth, sceneHeight);

		stage.setTitle("Graph Visualizer");
		stage.setScene(scene);
		stage.show();
	}

    public static void main(String[] args) {
        launch(args);
    }
}
