package org.sanyanse.ravi.test;

import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

import java.util.Collection;
import java.util.Iterator;

public class TestUndirectedGraph {
	public static void main(String[] args) {
		System.out.println("=========test1()===============");
		test1();
		System.out.println("=========testRemove()===============");
		testRemove();
		System.out.println("=========testClone()===============");
		testClone();
	}
	
	private static void test1() {
		UndirectedGraph graph = new UndirectedGraph();
		Vertex a = new Vertex("a");
		Vertex b = new Vertex("b");
		Vertex c = new Vertex("c");
		Vertex d = new Vertex("d");
		
		graph.addVertex(a);
		graph.addVertex(b);
		graph.addVertex(c);
		graph.addVertex(d);
		
		graph.addEdge(a, b);
		graph.addEdge(a, c);
		graph.addEdge(b, c);
		graph.addEdge(b, d);
		
		System.out.println(graph);
	}

	
	public static void testRemove() {
		UndirectedGraph graph = createTestGraph();
		System.out.println("Original graph");
		System.out.println(graph);

		Collection<Vertex> vertices = graph.getVertices();
		for (Iterator<Vertex> iter = vertices.iterator(); iter.hasNext(); ) {
			Vertex v = iter.next();
			graph.removeVertex(v);
			System.out.println("Original graph after deleting one vertex - " + v);
			System.out.println(graph);
			break;
		}
	}
	
	public static void testClone() {
		UndirectedGraph graph = createTestGraph();
		System.out.println("Original graph");
		System.out.println(graph);

		UndirectedGraph clonedGraph = graph.clone();
		System.out.println("Cloned graph");
		System.out.println(clonedGraph);
		
		Collection<Vertex> vertices = graph.getVertices();
		Vertex v = null;
		for (Iterator<Vertex> iter = vertices.iterator(); iter.hasNext(); ) {
			v = iter.next();
			graph.removeVertex(v);

			System.out.println("Original graph after deleting one vertex - " + v);
			System.out.println(graph);
			System.out.println("Cloned graph");
			System.out.println(clonedGraph);

			break;
		}
		
		Vertex e = new Vertex("e");
		Vertex f = new Vertex("f");
		clonedGraph.addVertex(e);
		clonedGraph.addVertex(f);
		clonedGraph.addEdge(e, f);
		if (v != null) {
			clonedGraph.addEdge(v, e);
			clonedGraph.addEdge(v, f);
		}
		
		System.out.println("Original graph");
		System.out.println(graph);
		System.out.println("Cloned graph after adding a few vertices");
		System.out.println(clonedGraph);
	}
	
	private static UndirectedGraph createTestGraph() {
		UndirectedGraph graph = new UndirectedGraph();
		Vertex a = new Vertex("a");
		Vertex b = new Vertex("b");
		Vertex c = new Vertex("c");
		Vertex d = new Vertex("d");
		
		graph.addVertex(a);
		graph.addVertex(b);
		graph.addVertex(c);
		graph.addVertex(d);
		
		graph.addEdge(a, b);
		graph.addEdge(a, c);
		graph.addEdge(b, c);
		graph.addEdge(b, d);
		
		return graph;
	}
}
