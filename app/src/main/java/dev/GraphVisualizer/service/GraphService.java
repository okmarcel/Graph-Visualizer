package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.repository.*;
import java.io.File;

/** Class GraphService */
public class GraphService {
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

    /** Method to save graph represenatation to json file */
    public void saveJson(String path, String filename) {
        File file = new File(path + File.separator + filename + ".json");
        jsonRepository.save(graph, file);
    }

    /** Method to load graph from json file */
    public void loadJson(String path, String filename) {
        File file = new File(path + File.separator + filename + ".json");
        if(!file.exists()) throw new GraphIOException("File not found: " + file.getPath(), null);
        this.graph = jsonRepository.load(file);
        this.graph.buildAdjacent();
    }

    /** Method to save graph represenatation to csv file */ 
    public void saveCsv(String path, String filename) {
        File file = new File(path + File.separator + filename + ".csv");
        csvRepository.save(graph, file);
    }

    /** Method to load graph from csv file */
    public void loadCsv(String path, String filename) {
        File file = new File(path + File.separator + filename + ".csv");
        if(!file.exists()) throw new GraphIOException("File not found: " + file.getPath(), null);
        this.graph = csvRepository.load(file);
        this.graph.buildAdjacent();
    }

    /** Method to save graph represenatation to txt file */
    public void saveTxt(String path, String filename) {
        File file = new File(path + File.separator + filename + ".txt");
        txtRepository.save(graph, file);
    }

    /** Method to load graph from txt file */
    public void loadTxt(String path, String filename) {
        File file = new File(path + File.separator + filename + ".txt");
        if(!file.exists()) throw new GraphIOException("File not found: " + file.getPath(), null);
        this.graph = txtRepository.load(file);
        this.graph.buildAdjacent();
    }

    /**
     * Method to get a private member graph
     * @return graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Graph setter
     * @param graph
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }
  
}

