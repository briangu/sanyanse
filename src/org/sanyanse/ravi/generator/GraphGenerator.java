package org.sanyanse.ravi.generator;

import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

public class GraphGenerator {
	public static UndirectedGraph generateCompleteGraph(int n) {
		UndirectedGraph graph = new UndirectedGraph();
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
	
	public static UndirectedGraph generateRandomGraph(int numVertices, int numEdges) {
		UndirectedGraph graph = new UndirectedGraph();
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
	
	public static UndirectedGraph generateEmptyGraph(int numVertices) {
		return generateRandomGraph(numVertices, 0);
	}
}
