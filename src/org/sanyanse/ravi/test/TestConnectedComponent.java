package test;

import org.sanyanse.ravi.algorithm.ConnectedComponents;
import org.sanyanse.ravi.generator.GraphGenerator;
import org.sanyanse.ravi.graph.UndirectedGraph;

import java.util.Collection;

public class TestConnectedComponent {
    public static void main(String[] args) {
    	test1();
    }
    
    private static void test1() {
    	UndirectedGraph graph = GraphGenerator.generateRandomGraph(10, 3);
    	System.out.println("===== original graph =====");
    	System.out.println(graph);
    	Collection<UndirectedGraph> components = ConnectedComponents.getConnectedComponents(graph);
    	for (UndirectedGraph component : components) {
        	System.out.println("===== component =====");
    		System.out.println(component);
    	}
    }
}
