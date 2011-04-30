package org.sanyanse.ravi.generator;

import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

/**
 * Graphs from the textbook "Introduction to Algorithms",
 * by Cormen, Leiserson, Rivest.
 */
public class CormenTextbook {
	// Graph from Figure 23.10
	public static UndirectedGraph create_figure_23_10() {
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
		Vertex v11 = new Vertex("v11");
		Vertex v12 = new Vertex("v12");
		Vertex v13 = new Vertex("v13");
		Vertex v14 = new Vertex("v14");
		Vertex v15 = new Vertex("v15");
		Vertex v16 = new Vertex("v16");
		Vertex v17 = new Vertex("v17");
		Vertex v18 = new Vertex("v18");
		Vertex v19 = new Vertex("v19");
		Vertex v20 = new Vertex("v20");
		Vertex v21 = new Vertex("v21");
		Vertex v22 = new Vertex("v22");
		Vertex v23 = new Vertex("v23");
		
		UndirectedGraph graph = new UndirectedGraph();
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
		graph.addVertex(v11);
		graph.addVertex(v12);
		graph.addVertex(v13);
		graph.addVertex(v14);
		graph.addVertex(v15);
		graph.addVertex(v16);
		graph.addVertex(v17);
		graph.addVertex(v18);
		graph.addVertex(v19);
		graph.addVertex(v20);
		graph.addVertex(v21);
		graph.addVertex(v22);
		graph.addVertex(v23);

		graph.addEdge(v1, v2);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v4);
		graph.addEdge(v2, v3);
		graph.addEdge(v1, v2);
		graph.addEdge(v2, v4);
		graph.addEdge(v3, v4);
		graph.addEdge(v4, v5);
		graph.addEdge(v5, v6);
		graph.addEdge(v5, v8);
		graph.addEdge(v5, v9);
		graph.addEdge(v5, v10);
		graph.addEdge(v5, v11);
		graph.addEdge(v6, v7);
		graph.addEdge(v6, v8);
		graph.addEdge(v9, v10);
		graph.addEdge(v11, v12);
		graph.addEdge(v11, v13);
		graph.addEdge(v12, v14);
		graph.addEdge(v13, v14);
		graph.addEdge(v14, v15);
		graph.addEdge(v15, v16);
		graph.addEdge(v15, v22);
		graph.addEdge(v16, v17);
		graph.addEdge(v16, v20);
		graph.addEdge(v16, v21);
		graph.addEdge(v16, v22);
		graph.addEdge(v17, v18);
		graph.addEdge(v17, v19);
		graph.addEdge(v18, v19);
		graph.addEdge(v21, v22);
		graph.addEdge(v21, v23);
		graph.addEdge(v22, v23);
		
		return graph;
	}
	
	// Graph from Figure 23.10
	public static UndirectedGraph create_figure_23_10_modified_3colorable() {
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
		Vertex v11 = new Vertex("v11");
		Vertex v12 = new Vertex("v12");
		Vertex v13 = new Vertex("v13");
		Vertex v14 = new Vertex("v14");
		Vertex v15 = new Vertex("v15");
		Vertex v16 = new Vertex("v16");
		Vertex v17 = new Vertex("v17");
		Vertex v18 = new Vertex("v18");
		Vertex v19 = new Vertex("v19");
		Vertex v20 = new Vertex("v20");
		Vertex v21 = new Vertex("v21");
		Vertex v22 = new Vertex("v22");
		Vertex v23 = new Vertex("v23");
		
		UndirectedGraph graph = new UndirectedGraph();
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
		graph.addVertex(v11);
		graph.addVertex(v12);
		graph.addVertex(v13);
		graph.addVertex(v14);
		graph.addVertex(v15);
		graph.addVertex(v16);
		graph.addVertex(v17);
		graph.addVertex(v18);
		graph.addVertex(v19);
		graph.addVertex(v20);
		graph.addVertex(v21);
		graph.addVertex(v22);
		graph.addVertex(v23);

		graph.addEdge(v1, v2);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v4);
		//graph.addEdge(v2, v3);
		graph.addEdge(v1, v2);
		graph.addEdge(v2, v4);
		graph.addEdge(v3, v4);
		graph.addEdge(v4, v5);
		graph.addEdge(v5, v6);
		graph.addEdge(v5, v8);
		graph.addEdge(v5, v9);
		graph.addEdge(v5, v10);
		graph.addEdge(v5, v11);
		graph.addEdge(v6, v7);
		graph.addEdge(v6, v8);
		graph.addEdge(v9, v10);
		graph.addEdge(v11, v12);
		graph.addEdge(v11, v13);
		graph.addEdge(v12, v14);
		graph.addEdge(v13, v14);
		graph.addEdge(v14, v15);
		graph.addEdge(v15, v16);
		graph.addEdge(v15, v22);
		graph.addEdge(v16, v17);
		graph.addEdge(v16, v20);
		graph.addEdge(v16, v21);
		graph.addEdge(v16, v22);
		graph.addEdge(v17, v18);
		graph.addEdge(v17, v19);
		graph.addEdge(v18, v19);
		graph.addEdge(v21, v22);
		graph.addEdge(v21, v23);
		graph.addEdge(v22, v23);
		
		return graph;
	}
}
