package org.sanyanse.ravi.problem.linkedin.three_colorable_graph;


import org.sanyanse.ravi.generator.CormenTextbook;
import org.sanyanse.ravi.generator.GraphGenerator;

public class Test {
	public static void main(String[] args) throws Exception {
		test2();
	}
	
	private static void test1() throws Exception {
		InputFileCreator.create("cormen", CormenTextbook.create_figure_23_10_modified_3colorable());
		Solution solution = new Solution("cormen");
		String outFile = solution.solve();
		System.out.println("Output at " + outFile);
	}

	private static void test2() throws Exception {
		String inputLocation = InputFileCreator.create("random-graph", GraphGenerator.generateRandomGraph(40, 90));
		System.out.println("Created input at " + inputLocation);
		Solution solution = new Solution("random-graph");
		String outFile = solution.solve();
		System.out.println("Output at " + outFile);
	}
}
