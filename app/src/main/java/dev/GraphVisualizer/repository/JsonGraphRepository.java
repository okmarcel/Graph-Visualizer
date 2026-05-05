package dev.GraphVisualizer.repository;

import dev.GraphVisualizer.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Class JsonGraphRepository - implementation of GraphRepository for JSON files */
public class JsonGraphRepository implements GraphRepository {

    private final ObjectMapper mapper = new ObjectMapper();

    private static class NodeDTO {
        public String id;
        public String label;
        public double x;
        public double y;
    }

    private static class EdgeDTO {
        public String sourceId;
        public String targetId;
        public double weight;
    }

    private static class GraphDTO {
        public String type;
        public List<NodeDTO> nodes;
        public List<EdgeDTO> edges;
    }

    /**
     * Saves graph to a JSON file
     * @param graph graph to save
     * @param file file handle
     */
    @Override
    public void save(Graph graph, File file) {
        GraphDTO dto = new GraphDTO();
        dto.type = graph.getClass().getSimpleName();
        dto.nodes = graph.getAllNodes().stream()
            .map(n -> {
                NodeDTO nd = new NodeDTO();
                nd.id = n.getId();
                nd.label = n.getLabel();
                nd.x = n.getPositionX();
                nd.y = n.getPositionY();
                return nd;
            }).toList();
        dto.edges = graph.getAllEdges().stream()
            .map(e -> {
                EdgeDTO ed = new EdgeDTO();
                ed.sourceId = e.getSource().getId();
                ed.targetId = e.getTarget().getId();
                ed.weight = e.getWeight();
                return ed;
            }).toList();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, dto);
        } catch (IOException e) {
            throw new GraphIOException("Failed to save graph to file: " + file.getPath(), e);
        }
    }

    /**
     * Loads graph from a JSON file
     * @param file file handle
     * @return loaded graph
     */
    @Override
    public Graph load(File file) {
        try {
            GraphDTO dto = mapper.readValue(file, GraphDTO.class);
            Graph graph = switch(dto.type) {
                case "DirectedGraph" -> new DirectedGraph();
                case "UndirectedGraph" -> new UndirectedGraph();
                case "WeightedDirectedGraph" -> new WeightedDirectedGraph();
                default -> new WeightedUndirectedGraph();
            };
            Map<String, Node> nodeMap = new HashMap<>();
            for(NodeDTO nd : dto.nodes) {
                Node n = new Node(nd.label, nd.x, nd.y);
                nodeMap.put(nd.id, n);
                graph.addNode(n);
            }
            for(EdgeDTO ed : dto.edges) {
                graph.addEdge(new Edge(
                    nodeMap.get(ed.sourceId),
                    nodeMap.get(ed.targetId),
                    ed.weight
                ));
            }
            return graph;
        } catch (IOException e) {
            throw new GraphIOException("Failed to load graph from file: " + file.getPath(), e);
        }
    }
}