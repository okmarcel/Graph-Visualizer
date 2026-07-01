package dev.GraphVisualizer.repository;

import dev.GraphVisualizer.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Class JsonGraphRepository - implementation of GraphRepository for JSON files */
public final class JsonGraphRepository implements GraphRepository {
    /** Creates a JSON graph repository. */
    public JsonGraphRepository() {}

    /** Jackson mapper used to serialize and deserialize graph DTOs */
    private final ObjectMapper mapper = new ObjectMapper();

    /** DTO representing a single node inside JSON graph files */
    private static class NodeDTO {
        /** Node identifier used to reconnect edges during loading */
        public String id;

        /** User-visible node label */
        public String label;

        /** Stored x position */
        public Double x;

        /** Stored y position */
        public Double y;
    }

    /** DTO representing a single edge inside JSON graph files */
    private static class EdgeDTO {
        /** Identifier of the source node */
        public String sourceId;

        /** Identifier of the target node */
        public String targetId;

        /** Stored edge weight */
        public Double weight;
    }

    /** DTO representing the full serialized graph payload */
    private static class GraphDTO {
        /** Concrete graph type name */
        public String type;

        /** Serialized graph nodes */
        public List<NodeDTO> nodes;
        
        /** Serialized graph edges */
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
            if (dto == null) {
                throw GraphLoadValidator.invalid(file, "Missing graph object.");
            }

            Graph graph = GraphLoadValidator.newGraph(dto.type, file);
            Map<String, Node> nodeMap = new HashMap<>();
            for (NodeDTO nd : GraphLoadValidator.requireList(dto.nodes, file, "nodes")) {
                if (nd == null) {
                    throw GraphLoadValidator.invalid(file, "Empty node entry.");
                }
                GraphLoadValidator.addNode(
                    graph,
                    nodeMap,
                    nd.id,
                    nd.label,
                    GraphLoadValidator.requireDouble(nd.x, file, "node x"),
                    GraphLoadValidator.requireDouble(nd.y, file, "node y"),
                    file
                );
            }
            for (EdgeDTO ed : GraphLoadValidator.requireList(dto.edges, file, "edges")) {
                if (ed == null) {
                    throw GraphLoadValidator.invalid(file, "Empty edge entry.");
                }
                GraphLoadValidator.addEdge(
                    graph,
                    GraphLoadValidator.requireNode(nodeMap, ed.sourceId, file, "source node id"),
                    GraphLoadValidator.requireNode(nodeMap, ed.targetId, file, "target node id"),
                    GraphLoadValidator.requireDouble(ed.weight, file, "edge weight"),
                    file
                );
            }
            return graph;
        } catch (IOException e) {
            throw new GraphIOException("Failed to load graph from file: " + file.getPath(), e);
        }
    }
}
