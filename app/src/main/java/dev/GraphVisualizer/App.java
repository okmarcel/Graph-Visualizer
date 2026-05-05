package dev.GraphVisualizer;

import java.io.File;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.repository.CsvGraphRepository;
import dev.GraphVisualizer.repository.JsonGraphRepository;
import dev.GraphVisualizer.repository.TxtGraphRepository;
import dev.GraphVisualizer.ui.MainWindow;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
	@Override
	public void start(Stage stage) {
		MainWindow window = new MainWindow(stage);
		window.show();
	}

    public static void main(String[] args) {
		Node a = new Node("A", 0.0, 0.0);
        Node b = new Node("B", 1.0, 0.0);
        Node c = new Node("C", 2.0, 0.0);

        WeightedDirectedGraph graph = new WeightedDirectedGraph();
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addEdge(new Edge(a, b, 2.5));
        graph.addEdge(new Edge(b, c, 1.0));
        graph.addEdge(new Edge(a, c, 4.0));

        String base = "database/";
		new File(base).mkdirs();
        JsonGraphRepository json = new JsonGraphRepository();
        json.save(graph, new File(base + "graph.json"));

        CsvGraphRepository csv = new CsvGraphRepository();
        csv.save(graph, new File(base + "graph.csv"));

        TxtGraphRepository txt = new TxtGraphRepository();
        txt.save(graph, new File(base + "graph.txt"));

        System.out.println("Saved to: " + base);
        launch(args);
    }
}
