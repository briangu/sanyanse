package org.sanyanse.ravi.test;


import java.util.Collection;
import java.util.Map;

import org.sanyanse.ravi.algorithm.BiconnectedComponents;
import org.sanyanse.ravi.generator.CormenTextbook;
import org.sanyanse.ravi.graph.Tree;
import org.sanyanse.ravi.graph.TreeNode;
import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

public class TestBiconnectedComponent {
	public static void main(String[] args) {
		test1();
	}
	
	private static void test1() {
		// Graph from Figure 23.10 from
		// "Introduction to Algorithms" by Cormen, Leiserson, Rivest.
		UndirectedGraph graph = CormenTextbook.create_figure_23_10();
		BiconnectedComponents bic = new BiconnectedComponents(graph);
		print(bic);
	}

	private static void test2() {
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
    	
		BiconnectedComponents bic = new BiconnectedComponents(graph);
		print(bic);
    }
	
	private static void print(BiconnectedComponents bic) {
		System.out.println("Cut vertices: " + bic.getCutVertices());
		Collection<UndirectedGraph> components = bic.getComponents();
		for (UndirectedGraph component : components) {
			System.out.println("=================");
			System.out.println(component);
		}
		
		Tree componenTree = bic.getBiconnectedComponentTree();
		System.out.println("Component tree: " + componenTree);
		Map<TreeNode, UndirectedGraph> map = bic.getTreeNodeToComponentsMap();
		System.out.println("Map size: " + map.size());
		for (Map.Entry<TreeNode, UndirectedGraph> entry : map.entrySet()) {
			System.out.println("Node\n" + entry.getKey() + "\nGraph\n" + entry.getValue());
		}
	}
}
