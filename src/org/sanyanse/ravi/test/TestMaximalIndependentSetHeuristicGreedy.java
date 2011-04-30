package org.sanyanse.ravi.test;

import org.sanyanse.ravi.algorithm.MaximalIndependentSetHeuristicGreedy;
import org.sanyanse.ravi.generator.CormenTextbook;
import org.sanyanse.ravi.generator.GraphGenerator;
import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

import java.util.Set;

public class TestMaximalIndependentSetHeuristicGreedy {
	public static void main(String[] args) {
		testCompleteGraph();
		testEmptyGraph();
		testCormen2310();
	}
	
	private static void testCompleteGraph() {
		getMISandPrint(GraphGenerator.generateCompleteGraph(10));
	}
	
	private static void testEmptyGraph() {
		getMISandPrint(GraphGenerator.generateEmptyGraph(10));
	}
	
	private static void testCormen2310() {
		getMISandPrint(CormenTextbook.create_figure_23_10());
	}
	
	private static void getMISandPrint(UndirectedGraph graph) {
		Set<Vertex> misVertices = MaximalIndependentSetHeuristicGreedy.getMaximalIndependentSet(graph);
		System.out.println("Maximal independent set size: " + misVertices.size());
		for (Vertex misVertex : misVertices) {
			System.out.print(misVertex.getName() + " ");
		}
		System.out.println();
	}
}
