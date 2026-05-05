package dev.GraphVisualizer.repository;

import dev.GraphVisualizer.models.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Class TxtGraphRepository - implementation of GraphRepository for TXT files */
public class TxtGraphRepository implements GraphRepository {

    /**
     * Saves graph to a TXT file as adjacency list
     * Format:
     * TYPE DirectedGraph
     * NODES 3
     * id label x y
     * ...
     * EDGES 2
     * sourceId targetId weight
     * ...
     * @param graph graph to save
     * @param file file handle
     */
    @Override
    public void save(Graph graph, File file) {
        StringBuilder sb = new StringBuilder();
        sb.append("TYPE ").append(graph.getClass().getSimpleName()).append("\n");
        sb.append("NODES ").append(graph.getNumberOfNodes()).append("\n");
        for(Node n : graph.getAllNodes()) {
            sb.append(n.getId()).append(" ")
              .append(n.getLabel()).append(" ")
              .append(n.getPositionX()).append(" ")
              .append(n.getPositionY()).append("\n");
        }
        sb.append("EDGES ").append(graph.getNumberOfEdges()).append("\n");
        for(Edge e : graph.getAllEdges()) {
            sb.append(e.getSource().getId()).append(" ")
              .append(e.getTarget().getId()).append(" ")
              .append(e.getWeight()).append("\n");
        }
        try {
            Files.writeString(file.toPath(), sb.toString());
        } catch(IOException e) {
            throw new GraphIOException("Failed to save graph to file: " + file.getPath(), e);
        }
    }

    /**
     * Loads graph from a TXT file
     * @param file file handle
     * @return loaded graph
     */
    @Override
    public Graph load(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            if(lines.isEmpty())
                throw new GraphIOException("File is empty: " + file.getPath(), null);

            String type = lines.get(0).split(" ")[1];
            Graph graph = switch(type) {
                case "DirectedGraph" -> new DirectedGraph();
                case "UndirectedGraph" -> new UndirectedGraph();
                case "WeightedDirectedGraph" -> new WeightedDirectedGraph();
                default -> new WeightedUndirectedGraph();
            };

            int nodeCount = Integer.parseInt(lines.get(1).split(" ")[1]);
            Map<String, Node> nodeMap = new HashMap<>();
            for(int i = 2; i < 2 + nodeCount; i++) {
                String[] parts = lines.get(i).split(" ");
                Node n = new Node(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
                nodeMap.put(parts[0], n);
                graph.addNode(n);
            }

            int edgeStart = 2 + nodeCount + 1;
            int edgeCount = Integer.parseInt(lines.get(2 + nodeCount).split(" ")[1]);
            for(int i = edgeStart; i < edgeStart + edgeCount; i++) {
                String[] parts = lines.get(i).split(" ");
                graph.addEdge(new Edge(
                    nodeMap.get(parts[0]),
                    nodeMap.get(parts[1]),
                    Double.parseDouble(parts[2])
                ));
            }
            return graph;
        } catch(IOException e) {
            throw new GraphIOException("Failed to load graph from file: " + file.getPath(), e);
        }
    }
}