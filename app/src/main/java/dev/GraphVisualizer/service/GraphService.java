package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.repository.*;
import java.io.File;
import java.util.Locale;

/** Class GraphService */
public final class GraphService {
    /** Member graph - instance of class Graph */
    private Graph graph;

    /** Instance of JsonGrapRepository */
    private final GraphRepository jsonRepository = new JsonGraphRepository();
    
    /** Instance of CsvGrapRepository */  
    private final GraphRepository csvRepository  = new CsvGraphRepository();
    
    /** Instance of TxtGrapRepository */
    private final GraphRepository txtRepository  = new TxtGraphRepository();

    /** Constructor taking graph - build adjacent table for all nodes as well  */
    public GraphService(Graph graph) {
        this.graph = graph;
        this.graph.buildAdjacent();
    }

    /** Saves the current graph to the file selected by the user. */
    public void save(File file) {
        File target = requireFile(file);
        File parent = target.getAbsoluteFile().getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs() && !parent.exists()) {
            throw new GraphIOException("Failed to create directory: " + parent.getPath(), null);
        }
        repositoryFor(target).save(graph, target);
    }

    /** Loads a graph from the file selected by the user. */
    public void load(File file) {
        File source = requireFile(file);
        if (!source.exists()) {
            throw new GraphIOException("File not found: " + source.getPath(), null);
        }
        this.graph = repositoryFor(source).load(source);
        this.graph.buildAdjacent();
        this.graph.setCache(false);
    }

    /**
     * Method to get a private member graph
     * @return graph
     */
    public Graph getGraph() {
        return graph;
    }

    /** Returns whether the current graph type is directed. */
    public boolean isDirectedGraph() {
        return graph instanceof DirectedGraph || graph instanceof WeightedDirectedGraph;
    }

    /** Returns whether the current graph type is weighted. */
    public boolean isWeightedGraph() {
        return graph instanceof WeightedDirectedGraph || graph instanceof WeightedUndirectedGraph;
    }

    /**
     * Graph setter
     * @param graph
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * Migrates the current graph to a new type determined by the directed/weighted flags.
     * All nodes are copied by reference. Edges are rebuilt; weights are reset to 1.0
     * when switching to an unweighted graph type.
     * @param directed whether the new graph should be directed
     * @param weighted whether the new graph should have weighted edges
     */
    public void switchGraphType(boolean directed, boolean weighted) {
        Graph target;
        if      (directed && weighted)  target = new WeightedDirectedGraph();
        else if (directed)              target = new DirectedGraph();
        else if (weighted)              target = new WeightedUndirectedGraph();
        else                            target = new UndirectedGraph();

        for (Node n : graph.getAllNodes())
            target.addNode(n);
        for (Edge e : graph.getAllEdges()) {
            double w = weighted ? e.getWeight() : 1.0;
            target.addEdge(new Edge(e.getSource(), e.getTarget(), w));
        }
        this.graph = target;
        this.graph.buildAdjacent();
        this.graph.setCache(false);
    }

    private File requireFile(File file) {
        if (file == null) {
            throw new GraphIOException("No file was selected.", null);
        }
        return file;
    }

    private GraphRepository repositoryFor(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == name.length() - 1) {
            throw new GraphIOException("Unsupported graph file type: " + name, null);
        }

        return switch (name.substring(dotIndex + 1).toLowerCase(Locale.ROOT)) {
            case "json" -> jsonRepository;
            case "csv"  -> csvRepository;
            case "txt"  -> txtRepository;
            default -> throw new GraphIOException("Unsupported graph file type: " + name, null);
        };
    }
  
}
