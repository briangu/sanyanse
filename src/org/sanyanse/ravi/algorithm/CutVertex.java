package org.sanyanse.ravi.algorithm;

import org.sanyanse.ravi.graph.Tree;
import org.sanyanse.ravi.graph.TreeNode;
import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CutVertex {
	private Map<TreeNode, Vertex> m_nodeVertexMap = new HashMap<TreeNode, Vertex>();
	private Map<Vertex, TreeNode> m_vertexNodeMap = new HashMap<Vertex, TreeNode>();
	private Collection<Vertex> m_seen = new HashSet<Vertex>();
	private Tree m_tree = null;
	
	private int m_time = 1;
	
	private Map<TreeNode, Integer> m_startTimeMap = new HashMap<TreeNode, Integer>();
	private Map<TreeNode, Integer> m_endTimeMap = new HashMap<TreeNode, Integer>();
	private Map<TreeNode, Integer> m_lowTimeMap = new HashMap<TreeNode, Integer>();
	
	public Collection<Vertex>  getCutVertices(UndirectedGraph graph) {
		Collection<Vertex> cutVertices = new HashSet<Vertex>();
		Vertex root = graph.getRandomVertex();
		//System.out.println("Root: " + root.getName());
		dfs(graph, root, null, cutVertices);
		//System.out.println("Cut vertices: " + cutVertices);
		//System.out.println("Tree: " + m_tree);
		return cutVertices;
	}
	
	private void dfs(UndirectedGraph graph, Vertex v, TreeNode parent, Collection<Vertex> cutVertices) {
		if (!m_seen.contains(v)) {
			m_seen.add(v);			
			TreeNode node = new TreeNode(v.getName(), parent);
			int startTime = m_time++;
			m_startTimeMap.put(node, startTime);
			m_nodeVertexMap.put(node, v);
			m_vertexNodeMap.put(v, node);
			if (parent == null) {
				m_tree = new Tree(node);
			} else {
				m_tree.addChild(parent, node);
			}
			Collection<Vertex> neighbors = graph.getEdges(v);
			if (neighbors != null) {
				for (Vertex neighbor : neighbors) {
					dfs(graph, neighbor, node, cutVertices);
				}
			}
			int endTime = m_time++;
			m_endTimeMap.put(node, endTime);
			
			int low = startTime;
			
			// Get the lowest start time among its neighbors.
			// (except when the neighbor is its parent)
			if (neighbors != null) {
				for (Vertex neighbor : neighbors) {
					TreeNode tempNode = m_vertexNodeMap.get(neighbor);
					if (parent != tempNode) {
						if (low > m_startTimeMap.get(tempNode)) {
							low = m_startTimeMap.get(tempNode);
						}
					}
				}
			}
			
			// Get the lowest low time among its children.
			List<TreeNode> children = node.getChildren();
			if (children != null) {
				for (TreeNode child : children) {
					if (low > m_lowTimeMap.get(child)) {
						low = m_lowTimeMap.get(child);
					}
				}
			}
			
			// We now have the low point for this node.
			m_lowTimeMap.put(node, low);
			//System.out.println(node.getName() + " start=" + startTime + ", low=" + low + ", end=" + endTime);
			
			// Determine if this is a cut vertex or not.
			if (parent == null) {
				// Special logic for the root of the tree.
				// If this is the root and it has children,
				// it is a cut vertex.
				if ((children != null) && (children.size() > 1)) {
					cutVertices.add(v);
					//System.out.println("cutvertex: " + v + ": root has " + children.size() + " children.");
				}
			} else {
				// Non root node of the tree.
				if (children != null) {
					int start = m_startTimeMap.get(node);
					for (TreeNode child : children) {
						if (start <= m_lowTimeMap.get(child)) {
							//System.out.println("cutvertex: " + v + ": start=" + start + ", lowchild=" + m_lowTimeMap.get(child) + " for child=" + child);
							cutVertices.add(v);
							break;
						}
					}
				}
			}
		}
	}
	
	public Collection<Vertex> getTraversedVertices() {
		return m_seen;
	}
}
