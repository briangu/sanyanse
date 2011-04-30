package org.sanyanse.ravi.problem.linkedin.three_colorable_graph;

import org.sanyanse.ravi.algorithm.Tripartite;
import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This class determines if the graph represented in a given file
 * is 3-colorable or not. It writes out the vertex coloring into
 * another file as specified in the LinkedIn TopCoder competition
 * for April 2011.
 * 
 * @author Ravi Bhide (rbhide@linkedin.com)
 * @since 2011.04
 */
public class Solution {
	private String m_inputFilename;
	
	// Ordered list of vertices, used for pretty print only.
	private List<Vertex> m_orderedVertices = new ArrayList<Vertex>();
	
	public Solution(String filename) {
		m_inputFilename = filename;
	}
	
	public static void main(String[] args) throws Exception {
		Solution solution = new Solution(args[0]);
		String outfile = solution.solve();
	}
	
	// Solves the three coloring problem.
	public String solve() throws Exception {
		UndirectedGraph graph = constructGraph(m_inputFilename);
		Map<Vertex, Integer> partitions = Tripartite.partition(Tripartite.Algorithm.BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_SET_2V, graph);
		return write(m_inputFilename, partitions);
	}
	
	// Constructs an undirected graph from data in the given file.
	private UndirectedGraph constructGraph(String filename) throws Exception {
		long startTime = System.currentTimeMillis();
		
		FileReader fileReader = new FileReader(new File(filename));
		BufferedReader reader = new BufferedReader(fileReader);
		int numVertices = Integer.parseInt(reader.readLine());
		
		UndirectedGraph graph = new UndirectedGraph();
		Map<String, Vertex> vertexByName = new HashMap<String, Vertex>();
		Map<Vertex, String> vertexConnections = new HashMap<Vertex, String>();

		for (int i=0; i<numVertices; i++) {
			String line = reader.readLine();
			int indexOfColon = line.indexOf(":");
			String vertexName = line.substring(0, indexOfColon);

			// Create vertex for name just seen.
			Vertex vertex = new Vertex(vertexName);
			graph.addVertex(vertex);

			// Record this vertex in the order it was seen.
			// We will use this information later on when
			// writing out the partition.
			m_orderedVertices.add(vertex);
			
			// We may not have created vertices for connections yet.
			// Just record those connections and move on.
			// We will create the edges once we have read all input.
			vertexByName.put(vertexName, vertex);
			vertexConnections.put(vertex, line.substring(indexOfColon + 1));
		}

		fileReader.close();
		reader.close();

		for (Map.Entry<Vertex, String> entry : vertexConnections.entrySet()) {
			Vertex vertex = entry.getKey();
			StringTokenizer st = new StringTokenizer(entry.getValue(), ",");
			while (st.hasMoreTokens()) {
				graph.addEdge(vertex, vertexByName.get(st.nextToken()));
			}
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Graph constructed in " + (endTime - startTime) + " ms.");
		return graph;
	}
	
	// Writes out the given partitions into a file as determined by the given filename.
	// Returns the location of the output file.
	private String write(String filename, Map<Vertex, Integer> partitions) throws Exception {
		int slashIndex = filename.lastIndexOf("/");
		String localFilename = (slashIndex >= 0) ? filename.substring(slashIndex + 1) : filename;
		File file = new File(Solution.class.getSimpleName() + "_" + localFilename + "_out");
		file.createNewFile();
		PrintWriter writer = new PrintWriter(file);
		writer.println(partitions != null);
		if (partitions != null) {
			for (Vertex vertex : m_orderedVertices) {
				writer.print(vertex.getName());
				writer.print(":");
				// Our partitions start from 0,
				// whereas the problem statement has it starting from 1.
				// So increment partition number by 1 when writing out.
				writer.println(partitions.get(vertex) + 1);
			}
		}
		writer.flush();
		writer.close();
		
		return file.getAbsolutePath();
	}
}
