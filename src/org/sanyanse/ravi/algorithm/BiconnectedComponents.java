package org.sanyanse.ravi.algorithm;


import org.sanyanse.common.Vertex;
import org.sanyanse.ravi.graph.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class BiconnectedComponents {
	private Graph m_originalGraph;
	private Collection<Graph> m_components; // Biconnected subgraphs within the original graph.
	private Collection<Vertex> m_cutVertices; // Cut vertices within the original graph.
	private Tree m_biconnectedComponentTree; // Tree of biconnected components representing the original graph.
	private Map<TreeNode, Graph> m_treeNodeToComponentsMap; // Vertices in the tree mapping to biconnected components.
	
	public BiconnectedComponents(Graph graph) {
		m_originalGraph = graph;
		process();
	}
	
	private void process() {
		// Get cut vertices.
		CutVertex cutVertexAlgo = new CutVertex();
		m_cutVertices = cutVertexAlgo.getCutVertices(m_originalGraph);
		
		// Get biconnected subgraphs based on these cut vertices.
		m_components = new HashSet<Graph>();
		m_components.add(m_originalGraph.clone());
		for (Vertex cutVertex : m_cutVertices) {
			for (Iterator<Graph> iter = m_components.iterator(); iter.hasNext();) {
				Graph disconnectedGraph = iter.next();
				if (disconnectedGraph.contains(cutVertex)) {
					iter.remove();
					Collection<Vertex> deletedEdges = disconnectedGraph.removeVertex(cutVertex);
					Collection<Graph> connectedComponents = Util.getConnectedComponents(disconnectedGraph);
					m_components.addAll(connectedComponents);
					for (Vertex deletedEdge : deletedEdges) {
						for (Graph component : connectedComponents) {
							if (component.contains(deletedEdge)) {
								component.addVertex(cutVertex);
								component.addEdge(cutVertex, deletedEdge);
							}
						}
					}
					break;
				}
			}
		}

		// Biconnected subgraphs form a tree structure.
		// Each of the connected components becomes a node in the tree.
		Map<String, Graph> vertexToGraphMap = new HashMap<String, Graph>();
		
		// (a) Determine nodes in the biconnected-component tree.
		Map<Graph, Vertex> tempMap2 = new HashMap<Graph, Vertex>();

		Graph tempBiconnectedComponentGraph = new Graph();
		int vertexNameIndex = 0;
		for (Graph biconnectedGraph : m_components) {
			// Create one vertex in the biconnected-component graph
			// for each biconnected component.
			Vertex v = new Vertex(String.valueOf(vertexNameIndex++));
			tempBiconnectedComponentGraph.addVertex(v);
			vertexToGraphMap.put(v.Id, biconnectedGraph);
			tempMap2.put(biconnectedGraph, v);
		}

		// (b) Determine edges in the biconnected-component graph.
		for (Vertex cutVertex : m_cutVertices) {
			for (Graph biconnectedGraph1 : m_components) {
				if (biconnectedGraph1.contains(cutVertex)) {
					for (Graph biconnectedGraph2 : m_components) {
						if (biconnectedGraph2.contains(cutVertex)) {
							if (biconnectedGraph1 != biconnectedGraph2) {
								// Both graph1 and graph2 contain the cut vertex.
								// They should have an edge between them in the biconnected component graph.
								tempBiconnectedComponentGraph.addEdge(tempMap2.get(biconnectedGraph1), tempMap2.get(biconnectedGraph2));
							}
						}
					}
				}
			}
		}

		// (c) Run DFS on the biconnected-component graph and get a tree.
		DepthFirstTraversal dfs = new DepthFirstTraversal(tempBiconnectedComponentGraph);
		m_biconnectedComponentTree = dfs.traverse();

		m_treeNodeToComponentsMap = new HashMap<TreeNode, Graph>();
		// Go through the tree and map tree node to connected components.
		LinkedList<TreeNode> list = new LinkedList<TreeNode>();
		list.add(m_biconnectedComponentTree.getRoot());
		while (!list.isEmpty()) {
			TreeNode node = list.remove();
			Graph graph = vertexToGraphMap.get(node.getName());
			m_treeNodeToComponentsMap.put(node, graph);
			// Get all children of this node and put them into the queue.
			if (!node.isLeaf()) {
				list.addAll(node.getChildren());
			}
		}
	}
	
	public Collection<Graph> getComponents() {
		return m_components;
	}
	
	public Collection<Vertex> getCutVertices() {
		return m_cutVertices;
	}
	
	public Tree getBiconnectedComponentTree() {
		return m_biconnectedComponentTree;
	}
	
	public Map<TreeNode, Graph> getTreeNodeToComponentsMap() {
		return m_treeNodeToComponentsMap;
	}
}
