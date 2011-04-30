package org.sanyanse.ravi.test;

import org.sanyanse.ravi.generator.GraphGenerator;
import org.sanyanse.ravi.graph.DepthFirstTraversal;
import org.sanyanse.ravi.graph.Tree;
import org.sanyanse.ravi.graph.UndirectedGraph;

public class TestDepthFirstTraversal {
	public static void main(String[] args) {
		test1();
	}
	
	private static void test1() {
		UndirectedGraph graph = GraphGenerator.generateRandomGraph(5, 5);
		DepthFirstTraversal dfs = new DepthFirstTraversal(graph);
		Tree dfsTree = dfs.traverse();
		System.out.println(graph);
		//Map<TreeNode, Vertex> treeToGraphMap = dfs.getTreeToGraphMap();
		System.out.println(dfsTree);
	}
}
