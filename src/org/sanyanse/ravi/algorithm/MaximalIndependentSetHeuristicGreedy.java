package org.sanyanse.ravi.algorithm;

import org.sanyanse.common.Vertex;
import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaximalIndependentSetHeuristicGreedy {
	public static Set<Vertex> getMaximalIndependentSet(UndirectedGraph g) {
		// MIS = maximal independent set.
		Set<Vertex> misVertices = new HashSet<Vertex>();
		
		// Clone the original graph since we would be modifying the graph.
		UndirectedGraph graph = g.clone();
		
		// Order vertices by increasing order of degree. Arbitrarily break ties.
		List<Vertex> verticesByDegree = Util.sortVerticesByDegree(graph);

		// Choose first available vertex in the maximal independent set.
		// Remove all of its adjacent vertices from consideration.
		for (Vertex v : verticesByDegree) {
			if (graph.contains(v)) {
				// Consider this vertex in maximal independent set.
				misVertices.add(v);
				// Remove it from the original graph.
				// Remove all its adjacent vertices from the original graph.
				Collection<Vertex> adjacentVertices = graph.removeVertex(v);
				if (adjacentVertices != null) {
					for (Vertex adjacentVertex : adjacentVertices) {
						graph.removeVertex(adjacentVertex);
					}
				}
			}
		}
		
		// No vertices left in the graph. This is our maximal independent set.
		return misVertices;
	}
	
}
