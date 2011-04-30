package org.sanyanse.ravi.graph;

import java.util.ArrayList;
import java.util.List;

// TODO: tree node should contain no parent-child relationship information.
// TODO: it should rest with the tree.
// TODO: this class can perhaps be represeted with Vertex after the above change.
public class TreeNode {
	private String m_name;
	private TreeNode m_parent;
	private List<TreeNode> m_children;
	
	public TreeNode(String name, TreeNode parent) {
		m_name = name;
		m_parent = parent;
	}
	
	public String getName() {
		return m_name;
	}
	
	public void addChild(TreeNode node) {
		if (m_children == null) {
			m_children = new ArrayList<TreeNode>();
		}
		m_children.add(m_children.size(), node);
	}
	
	public List<TreeNode> getChildren() {
		return m_children;
	}
	
	public boolean isLeaf() {
		return m_children == null;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append(", children = [");
		List<TreeNode> children = getChildren();
		if (children != null) {
			for (TreeNode child : children) {
				sb.append(child.getName());
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
