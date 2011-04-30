package org.sanyanse.ravi.test;

import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Util;
import org.sanyanse.ravi.graph.Vertex;

import java.util.Collection;

public class TestUtil {
    public static void main(String[] args) {
    	// v1-5 are connected.
    	Vertex v1 = new Vertex("v1");
    	Vertex v2 = new Vertex("v2");
    	Vertex v3 = new Vertex("v3");
    	Vertex v4 = new Vertex("v4");
    	Vertex v5 = new Vertex("v5");
    	
    	// v6-10 are connected.
    	Vertex v6 = new Vertex("v6");
    	Vertex v7 = new Vertex("v7");
    	Vertex v8 = new Vertex("v8");
    	Vertex v9 = new Vertex("v9");
    	Vertex v10 = new Vertex("v10");
    	
    	// v11-15 are connected.
    	Vertex v11 = new Vertex("v11");
    	Vertex v12 = new Vertex("v12");
    	Vertex v13 = new Vertex("v13");
    	Vertex v14 = new Vertex("v14");
    	Vertex v15 = new Vertex("v15");
    	
    	UndirectedGraph graph = new UndirectedGraph();
    	graph.addVertex(v1);
    	graph.addVertex(v2);
    	graph.addVertex(v3);
    	graph.addVertex(v4);
    	graph.addVertex(v5);
    	
    	graph.addEdge(v1, v2);
    	graph.addEdge(v1, v3);
    	graph.addEdge(v1, v4);
    	graph.addEdge(v2, v3);
    	graph.addEdge(v2, v4);
    	graph.addEdge(v3, v4);
    	graph.addEdge(v3, v5);
    	
    	graph.addVertex(v6);
    	graph.addVertex(v7);
    	graph.addVertex(v8);
    	graph.addVertex(v9);
    	graph.addVertex(v10);
    	
    	graph.addEdge(v6, v7);
    	graph.addEdge(v6, v8);
    	graph.addEdge(v6, v9);
    	graph.addEdge(v7, v8);
    	graph.addEdge(v7, v9);
    	graph.addEdge(v8, v9);
    	graph.addEdge(v8, v10);
    	
    	graph.addVertex(v11);
    	graph.addVertex(v12);
    	graph.addVertex(v13);
    	graph.addVertex(v14);
    	graph.addVertex(v15);
    	
    	graph.addEdge(v11, v12);
    	graph.addEdge(v11, v13);
    	graph.addEdge(v11, v14);
    	graph.addEdge(v12, v13);
    	graph.addEdge(v12, v14);
    	graph.addEdge(v13, v14);
    	graph.addEdge(v13, v15);
    	
    	System.out.println(graph);
    	
    	Collection<UndirectedGraph> subgraphs = Util.getConnectedComponents(graph);
    	System.out.println("There are " + subgraphs.size() + " connected components.");
    	for (UndirectedGraph subgraph : subgraphs) {
    		System.out.println("Sugraph: ");
    		System.out.println(subgraph);
    	}
    }
}
