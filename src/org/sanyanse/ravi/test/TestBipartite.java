package org.sanyanse.ravi.test;

import java.util.Map;

import org.sanyanse.ravi.algorithm.Bipartite;
import org.sanyanse.ravi.generator.GraphGenerator;
import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

public class TestBipartite {
	public static void main(String[] args) {
		testEmptyGraph();
		testCompleteGraph();
		testGraphWithOneEdge();
		testRandomGraph();
	}
	
	private static void testEmptyGraph() {
		UndirectedGraph graph = GraphGenerator.generateEmptyGraph(10);
		Map<Vertex, Integer> partition = Bipartite.partition(graph);
		System.out.println(partition);
	}

	private static void testCompleteGraph() {
		UndirectedGraph graph = GraphGenerator.generateCompleteGraph(10);
		Map<Vertex, Integer> partition = Bipartite.partition(graph);
		System.out.println(partition);
	}

	private static void testGraphWithOneEdge() {
		UndirectedGraph graph = GraphGenerator.generateRandomGraph(10, 1);
		Map<Vertex, Integer> partition = Bipartite.partition(graph);
		System.out.println(partition);
	}

	private static void testRandomGraph() {
		UndirectedGraph graph = GraphGenerator.generateRandomGraph(10, 5);
		Map<Vertex, Integer> partition = Bipartite.partition(graph);
		System.out.println(graph);
		System.out.println(partition);
	}
}
