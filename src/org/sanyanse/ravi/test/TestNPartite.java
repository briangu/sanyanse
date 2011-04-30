package org.sanyanse.ravi.test;

import org.sanyanse.ravi.algorithm.NPartite;
import org.sanyanse.ravi.generator.CormenTextbook;
import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

import java.util.Map;
import java.util.Set;

public class TestNPartite {
	public static void main(String[] args) {
		test1();
		test2();
		testNPartiteHeuristicGreedy();
	}
	
	private static void test1() {
    	Vertex v1 = new Vertex("v1");
    	Vertex v2 = new Vertex("v2");
    	Vertex v3 = new Vertex("v3");
    	Vertex v4 = new Vertex("v4");
    	Vertex v5 = new Vertex("v5");
    	Vertex v6 = new Vertex("v6");
    	
    	UndirectedGraph graph = new UndirectedGraph();
    	graph.addVertex(v1);
    	graph.addVertex(v2);
    	graph.addVertex(v3);
    	graph.addVertex(v4);
    	graph.addVertex(v5);
    	graph.addVertex(v6);
    	
    	graph.addEdge(v1, v2);
    	graph.addEdge(v1, v3);
    	graph.addEdge(v1, v4);
    	graph.addEdge(v2, v3);
    	graph.addEdge(v2, v4);
    	graph.addEdge(v3, v4);
    	graph.addEdge(v3, v5);
    	graph.addEdge(v5, v6);
    	
    	System.out.println(graph);

    	long startTime = System.currentTimeMillis();
    	System.out.println("4 partitions: " + NPartite.partition(NPartite.Algorithm.BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION, graph, 4));
    	long endTime = System.currentTimeMillis();
    	System.out.println(graph.getNumVertices() + " vertices: " + (endTime - startTime) + " ms.");
	}

	private static void test2() {
		// Graph from Figure 23.10 from
		// "Introduction to Algorithms" by Cormen, Leiserson, Rivest.
		UndirectedGraph graph = CormenTextbook.create_figure_23_10();
		
    	long startTime = System.currentTimeMillis();
    	System.out.println("4 partitions: " + NPartite.partition(NPartite.Algorithm.BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION, graph, 4));
    	long endTime = System.currentTimeMillis();
    	System.out.println(graph.getNumVertices() + " vertices: " + (endTime - startTime) + " ms.");
	}
	
	private static void testNPartiteHeuristicGreedy() {
		System.out.println("============test3Partite()=============");
		test3Partite();
		System.out.println("============test4Clique()=============");
		test4Clique();
	}
	
	private static void test3Partite() {
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
		Vertex v11 = new Vertex("v11");
		
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
		
		graph.addEdge(v1, v2);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v6);
		graph.addEdge(v2, v4);
		graph.addEdge(v3, v4);
		graph.addEdge(v3, v5);
		graph.addEdge(v4, v5);
		graph.addEdge(v5, v6);
		graph.addEdge(v5, v11);
		graph.addEdge(v6, v7);
		graph.addEdge(v6, v8);
		graph.addEdge(v8, v9);
		graph.addEdge(v9, v10);
		graph.addEdge(v9, v11);
		
		System.out.println(graph);
		
		Map<Vertex, Integer> partitions = NPartite.partition(NPartite.Algorithm.HEURISTIC_GREEDY, graph, 3);
		if (partitions != null) {
			System.out.println("vertex -> color");
			Set<Vertex> vertices = partitions.keySet();
			for (Vertex v : vertices) {
				System.out.println(v + " -> " + partitions.get(v));
			}
		} else {
			System.out.println("Graph is not 3-partite per greedy heuristic.");
		}
	}
	
	private static void test4Clique() {
		UndirectedGraph graph = new UndirectedGraph();
		Vertex v1 = new Vertex("v1");
		Vertex v2 = new Vertex("v2");
		Vertex v3 = new Vertex("v3");
		Vertex v4 = new Vertex("v4");

		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);
		
		graph.addEdge(v1, v2);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v4);
		graph.addEdge(v2, v3);
		graph.addEdge(v2, v4);
		graph.addEdge(v3, v4);

		System.out.println(graph);
		
		Map<Vertex, Integer> partitions = NPartite.partition(NPartite.Algorithm.HEURISTIC_GREEDY, graph, 3);
		if (partitions != null) {
			System.out.println("vertex -> color");
			Set<Vertex> vertices = partitions.keySet();
			for (Vertex v : vertices) {
				System.out.println(v + " -> " + partitions.get(v));
			}
		} else {
			System.out.println("Graph is not 3-partite per greedy heuristic.");
		}
	}
}
