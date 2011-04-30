package org.sanyanse.ravi.algorithm;

import org.sanyanse.common.Vertex;
import org.sanyanse.ravi.graph.Graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Bipartite {
	/**
	 * Non-null return value implies a valid partition.
	 */
    public static Map<Vertex, Integer> partition(Graph graph) {
    	Map<Vertex, Integer> colorAssignments = new HashMap<Vertex, Integer>();
    	Collection<Vertex> vertices = graph.getVertices();

    	for (Iterator<Vertex> vIter = vertices.iterator(); vIter.hasNext(); ) {
			Vertex vertex = vIter.next();
    		if (!colorAssignments.containsKey(vertex)) {
    			// Vertex has not been colored/visited yet.
    			// If this is not the first vertex in the list,
    			// this is a disconnected subgraph of the original graph.
    			// So we can always start with 0 as the proposed color
    			// and null as the vertex's parent.
    			if (!is2Colorable(graph, vertex, null, 0, colorAssignments)) {
    				// Subgraph is not 2-colorable. Return immediately.
    				return null;
    			}
    		}
			// This vertex has been colored. Remove it from the list.
			vIter.remove();
		}

    	return colorAssignments;
    }
    
    // Implements a DFS algorithm.
	private static boolean is2Colorable(Graph graph, Vertex vertex,
			Vertex parent, Integer proposedColor,
			Map<Vertex, Integer> colorAssignments) {
    	Collection<Vertex> adjacentVertices = graph.getEdges(vertex);

    	// Determine validity of the proposed color.
    	if (adjacentVertices != null) {
    		// Check proposed color against adjacent colored vertices.
    		for (Iterator<Vertex> vIter = adjacentVertices.iterator(); vIter.hasNext(); ) {
    			Vertex adjacentVertex = vIter.next();
    			// Note: adjacent vertex may not have been colored yet.
				if (colorAssignments.get(adjacentVertex) == proposedColor) {
					// Adjacent vertices cannot have the same color.
					// This is not 2-colorable. Color assignments are useless.
					colorAssignments.clear();
					return false;
				}
    		}
    	}
    	
    	// Proposed color is valid. Color this vertex.
    	colorAssignments.put(vertex, proposedColor);
    	
    	// Determine if the rest of the graph is 2-colorable.
    	if (adjacentVertices != null) {
    		int theOtherColor = proposedColor == 0 ? 1 : 0;
    		for (Iterator<Vertex> vIter = adjacentVertices.iterator(); vIter.hasNext(); ) {
    			Vertex adjacentVertex = vIter.next();
    			// Visit non-parent, adjacent vertex only if not visited (i.e. not colored).
    			if ((adjacentVertex != parent) && (colorAssignments.get(adjacentVertex) == null)) {
    				// If we determine that subgraph is not 2-colorable, return immediately.
    				if (!is2Colorable(graph, adjacentVertex, vertex, theOtherColor, colorAssignments)) {
    					return false;
    				}
    			}
    		}
    	}
    	
    	// Proposed color is valid and subgraph is 2-colorable.
    	return true;
    }
}
