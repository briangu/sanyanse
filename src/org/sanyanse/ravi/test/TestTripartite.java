package org.sanyanse.ravi.test;

import org.sanyanse.ravi.algorithm.Tripartite;
import org.sanyanse.ravi.generator.CormenTextbook;
import org.sanyanse.ravi.generator.GraphGenerator;
import org.sanyanse.ravi.generator.Petersen;
import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;
import org.sanyanse.ravi.util.ColoringVerifier;

import java.util.Map;

public class TestTripartite {
    public static void main(String[] args) {
//    	testPetersenGraph();
//    	testCormen();
//    	testCormenModified();
//    	testCompleteGraph();
//    	testEmptyGraph();
//    	testSparseGraph();
//    	testExample1();
//    	testLargeGraph();
//    	testManyLargishGraphs();
    	testDifferentApproaches();
    }
    private static void testDifferentApproaches() {
    	System.out.println("=========== Test different approaches ============");
		int numVertices = 30 + (int) (Math.random() * 10);
		int numEdges = (int) (numVertices * (1 + Math.random() * 3));
		System.out.println("Graph(vertices=" + numVertices + ", edges=" + numEdges + ")\n");
    	UndirectedGraph graph = GraphGenerator.generateRandomGraph(numVertices, numEdges);
    	runDifferentApproaches(graph);
    	System.out.println("==================== DONE ========================");
    }
    
    private static void runDifferentApproaches(UndirectedGraph graph) {
    	long startTime = 0;
    	long endTime = 0;
    	Map<Vertex, Integer> partition = null;
    	
    	startTime = System.currentTimeMillis();
    	partition = Tripartite.partition(Tripartite.Algorithm.BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_SET_2V, graph);
    	endTime = System.currentTimeMillis();
    	print("Brute/lex/set2v", graph, partition, startTime, endTime);

    	System.out.println();
    	
    	startTime = System.currentTimeMillis();
    	partition = Tripartite.partition(Tripartite.Algorithm.BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_2, graph);
    	endTime = System.currentTimeMillis();
    	print("Brute/lex/set1v", graph, partition, startTime, endTime);

    	System.out.println();
    	
    	startTime = System.currentTimeMillis();
    	partition = Tripartite.partition(Tripartite.Algorithm.BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION, graph);
    	endTime = System.currentTimeMillis();
    	print("Brute/lex/set0v", graph, partition, startTime, endTime);

    	System.out.println();
    	
    	startTime = System.currentTimeMillis();
    	partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET, graph);
    	endTime = System.currentTimeMillis();
    	print("MIS-3/lex", graph, partition, startTime, endTime);

    	System.out.println();
    	
    	startTime = System.currentTimeMillis();
    	partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET_DIV2, graph);
    	endTime = System.currentTimeMillis();
    	print("MIS-2/lex", graph, partition, startTime, endTime);
    }

    private static void print(String approach, UndirectedGraph graph, Map<Vertex, Integer> partition, long startTime, long endTime) {
    	System.out.print(approach);
    	System.out.print(": ");
    	if (partition != null) {
	    	boolean isValid = ColoringVerifier.isValid(graph, partition);
	    	if (isValid) {
	    		System.out.println("valid coloring (" + (endTime - startTime) + " ms.)");
	    	} else {
	    		System.out.println("INVALID INVALID INVALID COLORING! (" + (endTime - startTime) + " ms.)");
	    		System.out.println(graph);
	    		System.out.println(partition);
	    	}
    	} else {
    		System.out.println("no coloring found (" + (endTime - startTime) + " ms.)");
    	}
    }

    private static void testManyLargishGraphs() {
    	System.out.println("Test many 'largish' graphs.");
    	for (int i=0; i<100; i++) {
    		int numVertices = 20 + (int) (Math.random() * 20);
    		int numEdges = (int) (numVertices * (1 + Math.random() * 5));
    		System.out.print("Graph(vertices=" + numVertices + ", edges=" + numEdges + ") - ");
        	UndirectedGraph graph = GraphGenerator.generateRandomGraph(numVertices, numEdges);
        	long startTime = System.currentTimeMillis();
        	Map<Vertex, Integer> partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET, graph);
        	long endTime = System.currentTimeMillis();
        	print("MIS/lex", graph, partition, startTime, endTime);
//        	if (partition != null) {
//    	    	boolean isValid = ColoringVerifier.isValid(graph, partition);
//    	    	if (isValid) {
//    	    		System.out.println("valid coloring (" + (endTime - startTime) + " ms.)");
//    	    	} else {
//    	    		System.out.println("INVALID INVALID INVALID COLORING! (" + (endTime - startTime) + " ms.)");
//    	    		System.out.println(graph);
//    	    		System.out.println(partition);
//    	    	}
//        	} else {
//        		System.out.println("no coloring found (" + (endTime - startTime) + " ms.)");
//        	}
    	}
    	System.out.println("==================================================");
    }

    private static void testLargeGraph() {
    	System.out.println("Test large graph.");
    	UndirectedGraph graph = GraphGenerator.generateRandomGraph(50, 100);
    	System.out.println(graph);
    	long startTime = System.currentTimeMillis();
    	Map<Vertex, Integer> partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET, graph);
    	long endTime = System.currentTimeMillis();
    	System.out.println("Coloring: " + partition);
    	System.out.println("Finished in " + (endTime - startTime) + " ms.");
    	System.out.println();
    	testValidColoring(graph, partition);
    	System.out.println("==================================================");
    }

    private static void testPetersenGraph() {
    	System.out.println("Test Petersen.");
    	UndirectedGraph graph = Petersen.get();
    	Map<Vertex, Integer> partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET, graph);
    	System.out.println("Coloring: " + partition);
    	testValidColoring(graph, partition);
    	System.out.println("==================================================");
    }
    
    private static void testCormen() {
    	System.out.println("Test Cormen.");
    	UndirectedGraph graph = CormenTextbook.create_figure_23_10();
    	Map<Vertex, Integer> partition = Tripartite.partition(Tripartite.Algorithm.BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION, graph);
    	System.out.println("Coloring: " + partition);
    	testValidColoring(graph, partition);
    	long startTime = System.currentTimeMillis();
    	partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET, graph);
    	long endTime = System.currentTimeMillis();
    	System.out.println("Coloring: " + partition);
    	testValidColoring(graph, partition);
    	System.out.println("Finished in " + (endTime - startTime) + " ms.");
    	System.out.println("==================================================");
    }
    
    private static void testCormenModified() {
    	System.out.println("Test Cormen Modified.");
    	UndirectedGraph graph = CormenTextbook.create_figure_23_10_modified_3colorable();
    	//Map<Vertex, Integer> partition = Tripartite.partition(Tripartite.Algorithm.BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION, graph);
    	Map<Vertex, Integer> partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET, graph);
    	System.out.println("Coloring: " + partition);
    	testValidColoring(graph, partition);
    	long startTime = System.currentTimeMillis();
    	partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET, graph);
    	long endTime = System.currentTimeMillis();
    	System.out.println("Coloring: " + partition);
    	testValidColoring(graph, partition);
    	System.out.println("==================================================");
    }
    
    private static void testCompleteGraph() {
    	System.out.println("Test Complete Graph.");
    	UndirectedGraph graph = GraphGenerator.generateCompleteGraph(10);
    	Map<Vertex, Integer> partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET, graph);
    	System.out.println("Coloring: " + partition);
    	testValidColoring(graph, partition);
    	System.out.println("==================================================");
    }

    private static void testEmptyGraph() {
    	System.out.println("Test Empty Graph.");
    	UndirectedGraph graph = GraphGenerator.generateEmptyGraph(10);
    	Map<Vertex, Integer> partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET, graph);
    	System.out.println("Coloring: " + partition);
    	testValidColoring(graph, partition);
    	System.out.println("==================================================");
    }

    private static void testSparseGraph() {
    	System.out.println("Test Sparse Graph.");
    	UndirectedGraph graph = GraphGenerator.generateRandomGraph(1000, 10);
    	Map<Vertex, Integer> partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET, graph);
    	System.out.println("Coloring: " + partition);
    	testValidColoring(graph, partition);
    	System.out.println("==================================================");
    }
    
    private static void testExample1() {
    	System.out.println("Test Example 1.");
    	Vertex v1 = new Vertex("v1");
    	Vertex v2 = new Vertex("v2");
    	Vertex v3 = new Vertex("v3");
    	Vertex v4 = new Vertex("v4");
    	Vertex v5 = new Vertex("v5");
    	Vertex v6 = new Vertex("v6");
    	Vertex v7 = new Vertex("v7");
    	Vertex v8 = new Vertex("v8");
    	Vertex v9 = new Vertex("v9");
    	
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
    	
    	graph.addEdge(v1, v2);
    	graph.addEdge(v1, v7);
    	graph.addEdge(v1, v8);
    	graph.addEdge(v2, v3);
    	graph.addEdge(v2, v8);
    	graph.addEdge(v3, v4);
    	graph.addEdge(v4, v5);
    	graph.addEdge(v4, v6);
    	graph.addEdge(v4, v9);
    	graph.addEdge(v5, v6);
    	graph.addEdge(v6, v7);
    	graph.addEdge(v6, v8);
    	graph.addEdge(v6, v9);
    	graph.addEdge(v8, v9);
    	
    	Map<Vertex, Integer> partition = Tripartite.partition(Tripartite.Algorithm.MAXIMAL_INDEPENDENT_SET, graph);
    	System.out.println("Coloring: " + partition);
    	testValidColoring(graph, partition);
    	System.out.println("==================================================");
    }
    
    private static void testValidColoring(UndirectedGraph graph, Map<Vertex, Integer> partition) {
    	if (partition != null) {
	    	boolean isValid = ColoringVerifier.isValid(graph, partition);
	    	System.out.println(isValid ? "Valid coloring" : "INVALID INVALID INVALID coloring !!!!!");
    	} else {
    		System.out.println("No coloring provided.");
    	}
    }
}
