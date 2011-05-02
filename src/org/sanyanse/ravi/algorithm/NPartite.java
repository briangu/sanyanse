package org.sanyanse.ravi.algorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.sanyanse.common.Vertex;
import org.sanyanse.ravi.graph.UndirectedGraph;
import org.sanyanse.ravi.util.BaseNCounter;
import org.sanyanse.ravi.util.LexicographicCounter;

public class NPartite {
	public enum Algorithm {
		BRUTE_FORCE_DUMB, // O(partitions^n)
		BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION, // smarter than O(partitions^n)
		BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_2, // smarter than O(partitions^n)
		BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_SET_2V, // fixes colors for 2 vertices, smarter than O(partitions^n)
		HEURISTIC_GREEDY // O(n^2), may not be able to find a partition if one exists.
	}
	
	public static Map<Vertex, Integer> partition(Algorithm algorithm, UndirectedGraph graph, int maxPartitions) {
		switch (algorithm) {
			case BRUTE_FORCE_DUMB:
				return partitionBruteForce(graph, maxPartitions);
			case BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION:
				return partitionSmartBruteForce(graph, maxPartitions);
			case BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_2:
				return partitionSmartBruteForce2(graph, maxPartitions);
			case BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_SET_2V:
				return partitionSmartBruteForce3(graph, maxPartitions);
			case HEURISTIC_GREEDY:
				return partitionHeuristicGreedy(graph, maxPartitions);
			default:
				return null;
		}
	}
	
	/**
	 * Returns a map with partition numbers (0 through a max of n-1)
	 * if the given graph is n-partite. Returns null, if the given
	 * graph is not n-partite. Run time is O(partitions^n).
	 */
	private static Map<Vertex, Integer> partitionBruteForce(UndirectedGraph graph, int maxPartitions) {
		Map<Vertex, Integer> partitionMap = null; // the output.

		// Get vertices into an array, so it is easy to reference them.
		int numVertices = graph.getNumVertices();
		Vertex[] vertex = new Vertex[numVertices];
		Collection<Vertex> vertices = graph.cloneVertices();
		int index = 0;
		for (Iterator<Vertex> vertexIter = vertices.iterator(); vertexIter.hasNext(); ) {
			vertex[index++] = vertexIter.next();
		}

		// We will enumerate every possible partition {0 .. maxPartitions-1},
		// for each vertex. This is O(maxPartitions ^ numVertices), so non-polynomial.
		BaseNCounter counter = new BaseNCounter(maxPartitions, numVertices);

		while (counter.hasNext()) {
			int[] count = counter.next();
			// Digits in count are the partition numbers for the vertices.
			// Vertices with the same digit should not have an edge between.
			// Otherwise, the partition denoted by the current count is not valid.
			boolean validPartition = true;
			for (int i=0; i<count.length - 1; i++) {
				for (int j=i+1; j<count.length; j++) {
					if ((count[i] == count[j]) && (graph.hasEdge(vertex[i], vertex[j]))) {
						validPartition = false;
						break;
					}
				}
				if (!validPartition) {
					break;
				}
			}
			
			if (validPartition) {
				// Construct partition in map form.
				partitionMap = new HashMap<Vertex, Integer>();
				for (int i=0; i<numVertices; i++) {
					partitionMap.put(vertex[i], count[i]);
				}
				break;
			}
		}
		
		return partitionMap;
	}

	/**
	 * Returns a map with partition numbers (0 through a max of n-1)
	 * if the given graph is n-partite. Returns null, if the given
	 * graph is not n-partite. Uses lexicographic enumeration, so the
	 * run time less than O(partitions^n) (don't know exactly what it is, yet!).
	 */
	private static Map<Vertex, Integer> partitionSmartBruteForce(UndirectedGraph graph, int maxPartitions) {
		// Get vertices into an array, so it is easy to reference them.
		int numVertices = graph.getNumVertices();
		Vertex[] vertex = new Vertex[numVertices];
		Collection<Vertex> vertices = graph.cloneVertices();
		int index = 0;
		for (Iterator<Vertex> vertexIter = vertices.iterator(); vertexIter.hasNext(); ) {
			vertex[index++] = vertexIter.next();
		}

		// We will enumerate every feasible partition.
		// This is O(maxPartitions ^ numVertices), so non-polynomial.
		long numIterations = 0;
		LexicographicCounter counter = new LexicographicCounter(maxPartitions, numVertices);
		while (counter.hasNext()) {
			numIterations++;
			if (numIterations < 0) {
				System.out.println("iterations beyond " + Long.MAX_VALUE);
			} else if (numIterations % 10000000L == 0) {
				System.out.println("iterations so far: " + numIterations);
			}
			int[] coloring = counter.increment();
			if (isValidColoring(graph, vertex, coloring)) {
				if (vertex.length == coloring.length) {
					// Construct partition in map form.
					Map<Vertex, Integer> partitionMap = new HashMap<Vertex, Integer>();
					for (int i=0; i<numVertices; i++) {
						partitionMap.put(vertex[i], coloring[i]);
					}
					//System.out.println("Graph with " + graph.getNumVertices() + " done in " + numIterations + " iterations.");
					System.out.println("iterations: " + numIterations + " ");
					return partitionMap;
				} else {
					// So far, a valid partition. Check the next one.
					counter.deeper();
				}
			}
		}
		
		// No valid partition found.
		//System.out.println("Graph with " + graph.getNumVertices() + " done in " + numIterations + " iterations.");
		System.out.println("iterations: " + numIterations + " ");
		return null;
	}
	
	private static Map<Vertex, Integer> partitionSmartBruteForce2(UndirectedGraph graph, int maxPartitions) {
		// Get vertices into an array, so it is easy to reference them.
		int numVertices = graph.getNumVertices();
		Vertex[] vertex = new Vertex[numVertices];
		Collection<Vertex> vertices = graph.cloneVertices();
		int index = 0;
		for (Iterator<Vertex> vertexIter = vertices.iterator(); vertexIter.hasNext(); ) {
			vertex[index++] = vertexIter.next();
		}

		// We will enumerate every feasible partition.
		// This is smarter than O(maxPartitions ^ numVertices), but non-polynomial.
		long numIterations = 0;
		LexicographicCounter counter = new LexicographicCounter(maxPartitions, numVertices);
		// Increment counter so that the first vertex only has a choice of 1 color.
		// This reduces total iterations by 1/numPartitions. Smart!
		for (int i=0; i<maxPartitions-1; i++) {
			counter.increment();
		}

		// Enumerate every feasible partition.
		while (counter.hasNext()) {
			numIterations++;
			if (numIterations < 0) {
				System.out.println("iterations beyond " + Long.MAX_VALUE);
			} else if (numIterations % 10000000L == 0) {
				System.out.println("iterations so far: " + numIterations);
			}
			int[] coloring = counter.increment();
			if (isValidColoring(graph, vertex, coloring)) {
				if (vertex.length == coloring.length) {
					// Construct partition in map form.
					Map<Vertex, Integer> partitionMap = new HashMap<Vertex, Integer>();
					for (int i=0; i<numVertices; i++) {
						partitionMap.put(vertex[i], coloring[i]);
					}
					//System.out.println("Graph with " + graph.getNumVertices() + " done in " + numIterations + " iterations.");
					System.out.println("iterations: " + numIterations + " ");
					return partitionMap;
				} else {
					// So far, a valid partition. Check the next one.
					counter.deeper();
				}
			}
		}
		
		// No valid partition found.
		//System.out.println("Graph with " + graph.getNumVertices() + " done in " + numIterations + " iterations.");
		System.out.println("iterations: " + numIterations + " ");
		return null;
	}
	
	private static Map<Vertex, Integer> partitionSmartBruteForce3(UndirectedGraph graph, int maxPartitions) {
		// Get vertices into an array, so it is easy to reference them.
		int numVertices = graph.getNumVertices();
		Vertex[] vertex = new Vertex[numVertices];
		Collection<Vertex> vertices = graph.cloneVertices();
		int index = 0;
		for (Iterator<Vertex> vertexIter = vertices.iterator(); vertexIter.hasNext(); ) {
			vertex[index++] = vertexIter.next();
		}

		// We will enumerate every feasible partition.
		// This is smarter than O(maxPartitions ^ numVertices), but non-polynomial.
		long numIterations = 0;
		LexicographicCounter counter = new LexicographicCounter(maxPartitions, numVertices);
		// Increment counter so that the first vertex only has a choice of 1 color.
		// This reduces total iterations by 1/numPartitions. Smart!
		for (int i=0; i<maxPartitions-1; i++) {
			counter.increment();
		}
		// Fix color for vertex adjacent to the first vertex. This reduces
		// total iterations by another 1/numPartitions at most. Smart once more!
		Collection<Vertex> adjacentToFirstVertex = graph.getEdges(vertex[0]);
		if ((adjacentToFirstVertex != null) && (adjacentToFirstVertex.size() > 0)) {
			Vertex adjacentVertex = adjacentToFirstVertex.iterator().next();
			// Determine the index for this vertex in the array.
			int adjacentVertexIndex;
			for (adjacentVertexIndex = 0; (adjacentVertexIndex < numVertices)
					&& (vertex[adjacentVertexIndex] != adjacentVertex); adjacentVertexIndex++)
				;
			// Swap this vertex with the second vertex.
			if (adjacentVertexIndex < numVertices) {
				Vertex temp = vertex[1];
				vertex[1] = vertex[adjacentVertexIndex];
				vertex[adjacentVertexIndex] = temp;
				// Set the color for the adjacent vertex (now 2nd in the vertex array).
				counter.increment();
				counter.deeper();
				for (int i=0; i<maxPartitions-2; i++) {
					counter.increment();
				}
			}
			
		}

		// Enumerate every feasible partition.
		while (counter.hasNext()) {
			numIterations++;
			if (numIterations < 0) {
				System.out.println("iterations beyond " + Long.MAX_VALUE);
			} else if (numIterations % 10000000L == 0) {
				System.out.println("iterations so far: " + numIterations);
			}
			int[] coloring = counter.increment();
//			if (numIterations == 1) {
//				// Sanity check, print out current counter.
//				for (int i=0; i<coloring.length; i++) {
//					System.out.print(coloring[i]);
//					System.out.print('.');
//				}
//				System.out.println();
//			}
			if (isValidColoring(graph, vertex, coloring)) {
				if (vertex.length == coloring.length) {
					// Construct partition in map form.
					Map<Vertex, Integer> partitionMap = new HashMap<Vertex, Integer>();
					for (int i=0; i<numVertices; i++) {
						partitionMap.put(vertex[i], coloring[i]);
					}
					//System.out.println("Graph with " + graph.getNumVertices() + " done in " + numIterations + " iterations.");
					System.out.println("iterations: " + numIterations + " ");
					return partitionMap;
				} else {
					// So far, a valid partition. Check the next one.
					counter.deeper();
				}
			}
		}
		
		// No valid partition found.
		//System.out.println("Graph with " + graph.getNumVertices() + " done in " + numIterations + " iterations.");
		System.out.println("iterations: " + numIterations + " ");
		return null;
	}
	
	private static boolean isValidColoring(UndirectedGraph graph, Vertex[] vertex, int[] coloring) {
		for (int i=0; i<coloring.length - 1; i++) {
			for (int j=i+1; j<coloring.length; j++) {
				if (coloring[i] == coloring[j]) {
					// if the 2 vertices have an edge in between,
					// then this is not a valid coloring.
					if (graph.hasEdge(vertex[i], vertex[j])) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns a map with partition numbers (0 through a max of n)
	 * if the given graph is n-partite. Returns null, if the given
	 * graph doesn't seem n-partite to this greedy heuristic.
	 */
	private static Map<Vertex, Integer> partitionHeuristicGreedy(UndirectedGraph graph, int n) {
		Map<Vertex, Integer> partitionMap = null;
		if (graph != null) {
			Set<Vertex>[] partitions = new Set[n];
			for (int i=0; i<n; i++) {
				partitions[i] = new HashSet<Vertex>();
			}

			UndirectedGraph g = graph.clone();
			for (int i=0; i<n; i++) {
				newPartition(g, partitions[i]);
			}

			if (g.getNumVertices() == 0) {
				partitionMap = new HashMap<Vertex, Integer>();
				for (int i=0; i<n; i++) {
					for (Vertex v : partitions[i]) {
						partitionMap.put(v, i);
					}
				}
				return partitionMap;
			} else {
				// According to our greedy heuristic,
				// this graph is not n-partite.
				return null;
			}
		}
		return partitionMap;
	}
	
	private static void newPartition(UndirectedGraph g, Set<Vertex> partitionVertices) {
		Set<Vertex> notCurrentPartitionVertices = new HashSet<Vertex>();
		for (Iterator<Vertex> vIter = g.cloneVertices().iterator(); vIter.hasNext(); ) {
			Vertex vertex = vIter.next();
			if (!notCurrentPartitionVertices.contains(vertex)) {
				// Use current partition for v.
				partitionVertices.add(vertex);
				// For all vertices connected to v,
				// mark them so as to not use current partition.
				Collection<Vertex> incidentVertices = g.getEdges(vertex);
				if (incidentVertices != null) {
					for (Vertex incidentVertex : incidentVertices) {
						notCurrentPartitionVertices.add(incidentVertex);
					}
				}
				g.removeVertex(vertex);
			}
		}
	}
}

