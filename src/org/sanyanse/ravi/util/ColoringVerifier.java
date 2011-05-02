package org.sanyanse.ravi.util;


import org.sanyanse.common.Vertex;
import org.sanyanse.ravi.graph.UndirectedGraph;

import java.util.Collection;
import java.util.Map;

public class ColoringVerifier {
	public static boolean isValid(UndirectedGraph graph, Map<Vertex, Integer> coloring) {
		if (coloring != null) {
			Collection<Vertex> vertices = graph.cloneVertices();

			// Make sure that each vertex has been allocated some color.
			for (Vertex vertex : vertices) {
				if (!coloring.containsKey(vertex)) {
					return false;
				}
			}

			// If two vertices have an edge between them,
			// they should not have the same coloring.
			for (Vertex v1 : vertices) {
				for (Vertex v2 : vertices) {
					if (graph.hasEdge(v1, v2) && (coloring.get(v1) == coloring.get(v2))) {
						return false;
					}
				}
			}
		}
		
		// This is a valid coloring.
		return true;
	}
}
