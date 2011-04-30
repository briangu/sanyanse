package org.sanyanse.ravi.algorithm;

import org.sanyanse.common.Vertex;
import org.sanyanse.ravi.graph.DepthFirstTraversal;
import org.sanyanse.ravi.graph.Graph;

import java.util.Collection;
import java.util.HashSet;

public class ConnectedComponents {
    public static Collection<Graph> getConnectedComponents(Graph graph) {
    	Collection<Graph> components = new HashSet<Graph>();
    	Graph graphCopy = graph.clone();

    	do {
    		DepthFirstTraversal dfs = new DepthFirstTraversal(graphCopy);
    		dfs.traverse();
    		Collection<Vertex> vertices = dfs.getTraversedVertices();
    		if ((vertices != null) && (vertices.size() > 0)) {
    			// Recreate the graph which will be returned.
    			// Create vertices first.
    			Graph recreatedComponent = new Graph();
    			for (Vertex vertex : vertices) {
    				recreatedComponent.addVertex(vertex);
    			}
    			// Add edges to these vertices.
    			for (Vertex vertex1 : vertices) {
    				for (Vertex vertex2 : vertices) {
						if (graph.hasEdge(vertex1, vertex2)) {
							recreatedComponent.addEdge(vertex1, vertex2);
						}
    				}
    			}
    			components.add(recreatedComponent);
    			// Delete traversed vertices and their incident edges.
    			for (Vertex vertex : vertices) {
    				graphCopy.removeVertex(vertex);
    			}
    		}
    	} while (graphCopy.getNumVertices() > 0);

    	return components;
    }
}
