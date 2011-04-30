package org.sanyanse.ravi.test;

import org.sanyanse.ravi.graph.Tree;
import org.sanyanse.ravi.graph.TreeNode;

public class TestTree {
	public static void main(String[] args) {
		testBasic();
	}
	
    public static void testBasic() {
    	TreeNode n1 = new TreeNode("n1", null);
    	TreeNode n2 = new TreeNode("n2", null);
    	TreeNode n3 = new TreeNode("n3", null);
    	TreeNode n4 = new TreeNode("n4", null);
    	TreeNode n5 = new TreeNode("n5", null);
    	TreeNode n6 = new TreeNode("n6", null);
    	TreeNode n7 = new TreeNode("n7", null);
    	TreeNode n8 = new TreeNode("n8", null);
    	
    	Tree tree = new Tree(n1);
    	tree.addChild(n1, n2);
    	tree.addChild(n1, n3);
    	tree.addChild(n2, n4);
    	tree.addChild(n2, n5);
    	tree.addChild(n3, n6);
    	tree.addChild(n3, n7);
    	tree.addChild(n4, n8);
    	
    	System.out.println(tree);
    }
}
