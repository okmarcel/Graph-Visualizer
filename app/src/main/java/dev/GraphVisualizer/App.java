package dev.GraphVisualizer;

import dev.GraphVisualizer.ui.MainWindow;

import javafx.application.Application;
import javafx.stage.Stage;

/** Class App - JavaFX entry point of the application */
public class App extends Application {
	/**
	 * Creates and shows the main application window.
	 * @param stage primary stage provided by JavaFX runtime
	 */
	@Override
	public void start(Stage stage) {
		MainWindow window = new MainWindow(stage);
		window.show();
	}
}
