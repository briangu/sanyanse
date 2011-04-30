package org.sanyanse.ravi.test;

import org.sanyanse.ravi.algorithm.NCliqueBruteForce;
import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

import java.util.Collection;
import java.util.Iterator;

public class TestNCliqueBruteForce {
	public static void main(String[] args) {
		test4CliquePositive();
		test4CliqueNegative();
		test3ColorableNo3Clique();
	}
	
	public static void test4CliquePositive() {
		System.out.println("=============test4CliquePositive============");
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
		graph.addEdge(v1, v5);
		graph.addEdge(v2, v3);
		graph.addEdge(v2, v4);
		graph.addEdge(v2, v5);
		graph.addEdge(v3, v4);
		graph.addEdge(v3, v5);
		graph.addEdge(v5, v6);
		
		System.out.println(graph);
		
		Collection<Vertex> clique = NCliqueBruteForce.getClique(graph, 4);
		System.out.println("Clique of size 4: ");
		print(clique);

		clique = NCliqueBruteForce.getClique(graph, 3);
		System.out.println("Clique of size 3: ");
		print(clique);
	}

	private static void print(Collection<Vertex> vertices) {
		if (vertices != null) {
			for (Iterator<Vertex> iter = vertices.iterator(); iter.hasNext(); ) {
				System.out.print(iter.next() + " ");
			}
			System.out.println();
		} else {
			System.out.println("No vertices.");
		}
	}
	
	public static void test4CliqueNegative() {
		System.out.println("=============test4CliqueNegative============");
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
		
		Collection<Vertex> cliqueVertices = NCliqueBruteForce.getClique(graph, 4);
		System.out.println("Clique of size 4");
		print(cliqueVertices);

		cliqueVertices = NCliqueBruteForce.getClique(graph, 3);
		System.out.println("Clique of size 3");
		print(cliqueVertices);
	}
	
	public static void test3ColorableNo3Clique() {
		System.out.println("=============test3ColorableNo3Clique============");

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
		graph.addEdge(v1, v6);
		graph.addEdge(v2, v3);
		graph.addEdge(v2, v4);
		graph.addEdge(v2, v5);
		graph.addEdge(v2, v6);
		graph.addEdge(v3, v5);
		graph.addEdge(v3, v6);
		
		System.out.println(graph);
		
		Collection<Vertex> cliqueVertices = NCliqueBruteForce.getClique(graph, 4);
		System.out.println("Clique of size 4");
		print(cliqueVertices);
		
	}
}
