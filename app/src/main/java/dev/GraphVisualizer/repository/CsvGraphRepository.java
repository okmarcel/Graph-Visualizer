package dev.GraphVisualizer.repository;

import dev.GraphVisualizer.models.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Class CsvGraphRepository - implementation of GraphRepository for CSV files */
public final class CsvGraphRepository implements GraphRepository {
    /**
     * Saves graph to a CSV file
     * Format:
     * TYPE,DirectedGraph
     * NODE,id,label,x,y
     * EDGE,sourceId,targetId,weight
     * @param graph graph to save
     * @param file file handle
     */
    @Override
    public void save(Graph graph, File file) {
        StringBuilder sb = new StringBuilder();
        sb.append("TYPE,").append(graph.getClass().getSimpleName()).append("\n");
        for(Node n : graph.getAllNodes()) {
            sb.append("NODE,")
              .append(n.getId()).append(",")
              .append(n.getLabel()).append(",")
              .append(n.getPositionX()).append(",")
              .append(n.getPositionY()).append("\n");
        }
        for(Edge e : graph.getAllEdges()) {
            sb.append("EDGE,")
              .append(e.getSource().getId()).append(",")
              .append(e.getTarget().getId()).append(",")
              .append(e.getWeight()).append("\n");
        }
        try {
            Files.writeString(file.toPath(), sb.toString());
        } catch(IOException e) {
            throw new GraphIOException("Failed to save graph to file: " + file.getPath(), e);
        }
    }

    /**
     * Loads graph from a CSV file
     * @param file file handle
     * @return loaded graph
     */
    @Override
    public Graph load(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            if(lines.isEmpty())
                throw new GraphIOException("File is empty: " + file.getPath(), null);

            String type = lines.get(0).split(",")[1];
            Graph graph = switch(type) {
                case "DirectedGraph" -> new DirectedGraph();
                case "UndirectedGraph" -> new UndirectedGraph();
                case "WeightedDirectedGraph" -> new WeightedDirectedGraph();
                default -> new WeightedUndirectedGraph();
            };

            Map<String, Node> nodeMap = new HashMap<>();
            for(String line : lines.subList(1, lines.size())) {
                String[] parts = line.split(",");
                if(parts[0].equals("NODE")) {
                    Node n = new Node(parts[2], Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
                    nodeMap.put(parts[1], n);
                    graph.addNode(n);
                } else if(parts[0].equals("EDGE")) {
                    graph.addEdge(new Edge(
                        nodeMap.get(parts[1]),
                        nodeMap.get(parts[2]),
                        Double.parseDouble(parts[3])
                    ));
                }
            }
            return graph;
        } catch(IOException e) {
            throw new GraphIOException("Failed to load graph from file: " + file.getPath(), e);
        }
    }
}