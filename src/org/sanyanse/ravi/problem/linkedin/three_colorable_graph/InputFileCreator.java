package org.sanyanse.ravi.problem.linkedin.three_colorable_graph;

import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;

public class InputFileCreator {
	public static String create(String filename, UndirectedGraph graph) throws Exception {
		File file = new File(filename);
		file.createNewFile();
		PrintWriter writer = new PrintWriter(file);
		writer.println(graph == null ? 0 : graph.getNumVertices());
		if (graph != null) {
			for (Vertex vertex : graph.getVertices()) {
				Collection<Vertex> edges = graph.getEdges(vertex);
				writer.print(vertex.getName());
				writer.print(":");
				if (edges != null) {
					boolean firstEdgeVertex = true;
					for (Vertex edgeVertex : edges) {
						if (!firstEdgeVertex) {
							writer.print(",");
						}
						writer.print(edgeVertex.getName());
						firstEdgeVertex = false;
					}
				}
				writer.println();
			}
		}
		writer.close();
		return file.getAbsolutePath();
	}
}
