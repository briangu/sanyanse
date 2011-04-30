package org.sanyanse.ravi.test;

import org.sanyanse.ravi.algorithm.CutVertex;
import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

import java.util.Collection;

public class TestCutVertex {
    public static void main(String[] args) {
    	test1();
    }
    
    public static void test1() {
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
    	
    	CutVertex cutVertexAlgorithm = new CutVertex();
    	Collection<Vertex> cutVertices = cutVertexAlgorithm.getCutVertices(graph);
    	System.out.println("Cut vertices: " + cutVertices);
    	
//    	TreeNode root = tree.getRoot();
//    	System.out.println("StartTime: " + dfs.getStartTime(root) + ", endTime: " + dfs.getEndTime(root));
    }

    public static void test2() {
    	Vertex v1 = new Vertex("v1");
    	Vertex v2 = new Vertex("v2");
    	Vertex v3 = new Vertex("v3");
    	Vertex v4 = new Vertex("v4");
    	Vertex v5 = new Vertex("v5");
    	Vertex v6 = new Vertex("v6");
    	Vertex v7 = new Vertex("v7");
    	Vertex v8 = new Vertex("v8");
    	
    	UndirectedGraph graph = new UndirectedGraph();
    	graph.addVertex(v1);
    	graph.addVertex(v2);
    	graph.addVertex(v3);
    	graph.addVertex(v4);
    	graph.addVertex(v5);
    	graph.addVertex(v6);
    	graph.addVertex(v7);
    	graph.addVertex(v8);
    	
    	graph.addEdge(v1, v2);
    	graph.addEdge(v1, v5);
    	graph.addEdge(v2, v3);
    	graph.addEdge(v2, v5);
    	graph.addEdge(v2, v6);
    	graph.addEdge(v3, v4);
    	graph.addEdge(v3, v7);
    	graph.addEdge(v5, v6);
    	graph.addEdge(v6, v7);
    	graph.addEdge(v7, v8);
    	
    	System.out.println(graph);
    	
    	CutVertex cutVertexAlgorithm = new CutVertex();
    	Collection<Vertex> cutVertices = cutVertexAlgorithm.getCutVertices(graph);
    	System.out.println("Cut vertices: " + cutVertices);
    }
}
