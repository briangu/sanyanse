package org.sanyanse.ravi.graph;

import org.sanyanse.common.Vertex;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DepthFirstTraversal {
	private UndirectedGraph _graph;
	private Collection<Vertex> m_seen = new HashSet<Vertex>();
	private Tree m_tree = null;
	private Map<TreeNode, Vertex> m_treeToGraphMap = new HashMap<TreeNode, Vertex>();
	
	public DepthFirstTraversal(UndirectedGraph graph) {
		_graph = graph;
	}
	
	public Tree traverse() {
		return traverse(_graph.getRandomVertex());
	}
	
	public Tree traverse(Vertex root) {
		if (root == null) {
			root = _graph.getRandomVertex();
		}
		dfs(_graph, root, null);
		return m_tree;
	}
	
	private void dfs(UndirectedGraph graph, Vertex v, TreeNode parent) {
		if (!m_seen.contains(v)) {
			m_seen.add(v);			
			TreeNode node = new TreeNode(v.Id, parent);
			if (parent == null) {
				m_tree = new Tree(node);
			} else {
				m_tree.addChild(parent, node);
			}
			m_treeToGraphMap.put(node, v);
			Collection<Vertex> neighbors = graph.getEdges(v);
			if (neighbors != null) {
				for (Vertex neighbor : neighbors) {
					dfs(graph, neighbor, node);
				}
			}
		}
	}
	
	public Collection<Vertex> getTraversedVertices() {
		return m_seen;
	}
	
	public Map<TreeNode, Vertex> getTreeToGraphMap() {
		return m_treeToGraphMap;
	}
}
