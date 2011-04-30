package org.sanyanse.ravi.algorithm;

import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.graph.Vertex;
import org.sanyanse.ravi.util.Combination;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class NCliqueBruteForce {
	public static Collection<Vertex> getClique(UndirectedGraph graph, int cliqueSize) {
		Collection<Vertex> clique = null;
		if (graph != null) {
			// Get vertices with degree >= clique size.
			// Only these vertices can participate in an n-clique.
			UndirectedGraph g = graph.clone();
			Collection<Vertex> vertices = g.getVertices();
			for (Iterator<Vertex> vIter = vertices.iterator(); vIter.hasNext(); ) {
				Vertex v = vIter.next();
				if (g.getDegree(v) < cliqueSize - 1) {
					vIter.remove();
				}
			}
			// Only vertices with degree >= clique size are present in our collection.

			// Run a combination of "clique-size" over vertices
			// and determine if they form a clique.
			// If they do, return the first clique.
			Vertex[] vertexArray = new Vertex[vertices.size()];
			int index=0;
			for (Iterator<Vertex> iter = vertices.iterator(); iter.hasNext(); ) {
				vertexArray[index++] = iter.next();
			}
			
			Combination combination = new Combination(vertexArray, cliqueSize);
			Iterator<Object[]> comboIter = combination.iterator();
			while (comboIter.hasNext()) {
				Object[] potentialCliqueArray = comboIter.next();
				// Determine if all elements in this combination are connected to each other.
				boolean isClique = true;
				for (int i=0; i < cliqueSize - 1; i++) {
					Vertex v = (Vertex) potentialCliqueArray[i];
					for (int j=i+1; j < cliqueSize; j++) {
						if (!g.hasEdge(v, (Vertex) potentialCliqueArray[j])) {
							isClique = false;
							break;
						}
					}
					if (!isClique) {
						break;
					}
				}
				if (isClique) {
					// Return this combination as a clique.
					clique = new HashSet<Vertex>();
					for (int i=0; i<cliqueSize; i++) {
						clique.add((Vertex) potentialCliqueArray[i]);
					}
				}
			}
		}
		return clique;
	}
}
