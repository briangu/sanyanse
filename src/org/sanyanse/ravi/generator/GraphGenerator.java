package org.sanyanse.ravi.generator;

import org.sanyanse.common.Vertex;
import org.sanyanse.ravi.graph.Graph;

public class GraphGenerator {
	public static Graph generateCompleteGraph(int n) {
		Graph graph = new Graph();
		Vertex[] vertices = new Vertex[n];
		for (int i=0; i<n; i++) {
			Vertex v = new Vertex(String.valueOf(i+1));
			graph.addVertex(v);
			vertices[i] = v;
			for (int j=0; j<i; j++) {
				graph.addEdge(v, vertices[j]);
			}
		}
		return graph;
	}
	
	public static Graph generateRandomGraph(int numVertices, int numEdges) {
		Graph graph = new Graph();
		Vertex[] vertices = new Vertex[numVertices];
		// Create vertices.
		for (int i=0; i<numVertices; i++) {
			Vertex v = new Vertex(String.valueOf(i+1));
			vertices[i] = v;
			graph.addVertex(v);
		}
		
		// Make sure that the number of edges requested is no more
		// than what a maximal graph can support.
		int numEdgesSoFar = 0;
		if (numEdges > numVertices * (numVertices - 1)) {
			numEdges = numVertices * (numVertices - 1);
		}

		while (numEdgesSoFar < numEdges) {
			int index1 = (int) (Math.random() * numVertices);
			int index2 = (int) (Math.random() * numVertices);
			if ((index1 != index2) && (!graph.hasEdge(vertices[index1], vertices[index2]))) {
				graph.addEdge(vertices[index1], vertices[index2]);
				numEdgesSoFar++;
			}
		}
		
		return graph;
	}
	
	public static Graph generateEmptyGraph(int numVertices) {
		return generateRandomGraph(numVertices, 0);
	}
}
