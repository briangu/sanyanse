package org.sanyanse.ravi.graph;

import org.sanyanse.common.Vertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Util {
	public static Collection<Graph> getConnectedComponents(Graph graph) {
		Collection<Graph> subgraphs = new HashSet<Graph>();
		
		Collection<Vertex> vertices = graph.getVertices();
		while (vertices.size() > 0) {
			Vertex root = null;
			{
				root = vertices.iterator().next();
			}
			
			// Run a DFS and get reachable vertices.
			Collection<Vertex> reachableVertices = new HashSet<Vertex>();
			dfs(graph, root, reachableVertices);
			
			// Get induced subgraph for the reachable vertices.
			Graph subgraph = graph.getSubgraph(reachableVertices);
			subgraphs.add(subgraph);

			// Remove reachable vertices and proceed with only unreachable vertices.
			for (Iterator<Vertex> traversedVertexIter = reachableVertices.iterator(); traversedVertexIter. hasNext(); ) {
				vertices.remove(traversedVertexIter.next());
			}
		}
		
		return subgraphs;
	}

	private static Tree dfs(Graph graph, Vertex root, Collection<Vertex> traversedVertices) {
		return dfs(graph, root, null, traversedVertices, null);
	}
	
	private static Tree dfs(Graph graph, Vertex v, TreeNode parent, Collection<Vertex> seenVertices, Tree dfsTree) {
		if (seenVertices == null) {
			seenVertices = new HashSet<Vertex>();
		}
		if (!seenVertices.contains(v)) {
			seenVertices.add(v);			
			TreeNode node = new TreeNode(v.Id, parent);
			if (parent == null) {
				dfsTree = new Tree(node);
			} else {
				dfsTree.addChild(parent, node);
			}
			Collection<Vertex> neighbors = graph.getEdges(v);
			if (neighbors != null) {
				for (Vertex neighbor : neighbors) {
					dfs(graph, neighbor, node, seenVertices, dfsTree);
				}
			}
		}
		return dfsTree;
	}

	public static List<Vertex> sortVerticesByDegree(final Graph graph) {
		Collection<Vertex> vertices = graph.getVertices();
		List<Vertex> list = new ArrayList<Vertex>(vertices);
		Collections.sort(list, new Comparator<Vertex>() {
			public int compare(Vertex v1, Vertex v2) {
				Collection<Vertex> edges1 = graph.getEdges(v1);
				Collection<Vertex> edges2 = graph.getEdges(v2);
				int degree1 = (edges1 != null ? edges1.size() : 0);
				int degree2 = (edges2 != null ? edges2.size() : 0);
				return (degree1 < degree2 ? -1 : 1);
			}
		});
		return list;
	}
}
