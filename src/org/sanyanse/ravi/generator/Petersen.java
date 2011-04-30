package org.sanyanse.ravi.generator;

import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

public class Petersen {
	// TODO: see and implement "Generalized Petersen graph".
	public static UndirectedGraph get() {
		UndirectedGraph graph = new UndirectedGraph();
		Vertex v1 = new Vertex("v1");
		Vertex v2 = new Vertex("v2");
		Vertex v3 = new Vertex("v3");
		Vertex v4 = new Vertex("v4");
		Vertex v5 = new Vertex("v5");
		Vertex v6 = new Vertex("v6");
		Vertex v7 = new Vertex("v7");
		Vertex v8 = new Vertex("v8");
		Vertex v9 = new Vertex("v9");
		Vertex v10 = new Vertex("v10");
		
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);
		graph.addVertex(v5);
		graph.addVertex(v6);
		graph.addVertex(v7);
		graph.addVertex(v8);
		graph.addVertex(v9);
		graph.addVertex(v10);
		
		graph.addEdge(v1, v2);
		graph.addEdge(v2, v3);
		graph.addEdge(v3, v4);
		graph.addEdge(v4, v5);
		graph.addEdge(v5, v1);

		graph.addEdge(v1, v6);
		graph.addEdge(v2, v7);
		graph.addEdge(v3, v8);
		graph.addEdge(v4, v9);
		graph.addEdge(v5, v10);
		
		graph.addEdge(v6, v8);
		graph.addEdge(v8, v10);
		graph.addEdge(v10, v7);
		graph.addEdge(v7, v9);
		graph.addEdge(v9, v6);

		return graph;
	}
}
