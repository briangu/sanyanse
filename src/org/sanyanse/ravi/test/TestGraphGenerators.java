package org.sanyanse.ravi.test;

import org.sanyanse.ravi.generator.GraphGenerator;
import org.sanyanse.ravi.generator.Petersen;
import org.sanyanse.ravi.graph.UndirectedGraph;

public class TestGraphGenerators {
	public static void main(String[] args) {
		//testPetersen();
		testRandomGraphGenerator();
	}
	
	private static void testPetersen() {
		UndirectedGraph graph = Petersen.get();
		System.out.println(graph);
	}
	
	private static void testRandomGraphGenerator() {
		System.out.println("Graph 1");
		UndirectedGraph graph = GraphGenerator.generateRandomGraph(5, 5);
		System.out.println(graph);

		System.out.println("Graph 2");
		graph = GraphGenerator.generateRandomGraph(10, 8);
		System.out.println(graph);
	}
}
