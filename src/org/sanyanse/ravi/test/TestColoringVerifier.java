package org.sanyanse.ravi.test;


import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;
import org.sanyanse.ravi.util.ColoringVerifier;

import java.util.HashMap;
import java.util.Map;

public class TestColoringVerifier {
	public static void main(String[] args) {
		test1();
		test2();
	}
	
	private static void test1() {
		UndirectedGraph graph = new UndirectedGraph();
		Vertex v1 = new Vertex("v1");
		Vertex v2 = new Vertex("v2");
		Vertex v3 = new Vertex("v3");
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addEdge(v1, v2);
		graph.addEdge(v1, v3);
		graph.addEdge(v2, v3);
		Map<Vertex, Integer> coloring = new HashMap<Vertex, Integer>();
		coloring.put(v1, 0);
		coloring.put(v2, 1);
		coloring.put(v3, 2);
		System.out.println(ColoringVerifier.isValid(graph, coloring) ? "Valid (Correct)" : "Invalid (Incorrect)");
	}

	private static void test2() {
		UndirectedGraph graph = new UndirectedGraph();
		Vertex v1 = new Vertex("v1");
		Vertex v2 = new Vertex("v2");
		Vertex v3 = new Vertex("v3");
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addEdge(v1, v2);
		graph.addEdge(v1, v3);
		graph.addEdge(v2, v3);
		Map<Vertex, Integer> coloring = new HashMap<Vertex, Integer>();
		coloring.put(v1, 0);
		coloring.put(v2, 1);
		coloring.put(v3, 1);
		System.out.println(ColoringVerifier.isValid(graph, coloring) ? "Valid (Incorrect)" : "Invalid (Correct)");
	}
}
