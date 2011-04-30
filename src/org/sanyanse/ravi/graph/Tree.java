package org.sanyanse.ravi.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Tree {
	private TreeNode m_root;
	private Set<TreeNode> m_allTreeNodes;

	public Tree(TreeNode node) {
		m_root = node;
		m_allTreeNodes = new HashSet<TreeNode>();
		m_allTreeNodes.add(m_root);
	}
	
	public TreeNode getRoot() {
		return m_root;
	}
	
	public void addChild(TreeNode parent, TreeNode child) {
		if (m_allTreeNodes.contains(child)) {
			throw new IllegalArgumentException("Adding " + child.getName() + " as child of " + parent.getName() + " creates a cycle.");
		} else {
			m_allTreeNodes.add(child);
			parent.addChild(child);
		}
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		LinkedList<TreeNode> nodes = new LinkedList<TreeNode>();
		buf.append("Root: ");
		buf.append(m_root.getName());
		buf.append("\n");
		nodes.add(m_root);
		while (nodes.size() > 0) {
			TreeNode node = nodes.removeFirst();
			buf.append(node.toString());
			buf.append("\n");
			List<TreeNode> children = node.getChildren();
			if (children != null) {
				for (TreeNode child : children) {
					nodes.addLast(child);
				}
			}
		}
		return buf.toString();
	}
}
