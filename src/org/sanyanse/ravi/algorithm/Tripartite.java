package org.sanyanse.ravi.algorithm;


import org.sanyanse.common.Vertex;
import org.sanyanse.ravi.graph.Graph;
import org.sanyanse.ravi.graph.Tree;
import org.sanyanse.ravi.graph.TreeNode;
import org.sanyanse.ravi.util.LexicographicSelector;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Tripartite {
	public enum Algorithm {
		BRUTE_FORCE, // O(3^n)
		BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION, // Smarter than O(3^n)
		BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_2, // Smarter than O(3^n)
		BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_SET_2V, // Smarter than O(3^n)
		MAXIMAL_INDEPENDENT_SET, // includes lexicographic enumeration. Smarter than O(2^n).
		MAXIMAL_INDEPENDENT_SET_DIV2 // includes lexicographic enumeration. Smarter than O(2^n).
	}

	/**
	 * Algorithm for 3-partitioning:
	 * 1. Decompose given graph into biconnected components.
	 * 2. Solve 3-partite problem for individual biconnected components.
	 *    TODO: this is inherently parallelizable. Multi-thread it.
	 * 3. Reconcile partitions, since only cut vertices can be shared
	 *    between any two biconnected components.
	 */
	public static Map<Vertex, Integer> partition(Algorithm algorithm, Graph graph) {
		Map<Vertex, Integer> partition = new HashMap<Vertex, Integer>();
		Collection<Graph> disconnectedComponents = ConnectedComponents.getConnectedComponents(graph);
		for (Graph component : disconnectedComponents) {
			Map<Vertex, Integer> componentPartition = getPartitionsForConnectedComponent(algorithm, component);
			if (componentPartition == null) {
				// Component could not be 3-partitioned.
				return null;
			}
			partition.putAll(componentPartition);
		}
		return partition;
	}
	
	// Input graph is assumed to be connected.
	private static Map<Vertex, Integer> getPartitionsForConnectedComponent(Algorithm algorithm, Graph graph) {
		// 1. Decompose given graph into biconnected components.
		BiconnectedComponents bic = new BiconnectedComponents(graph);
		Collection<Vertex> cutVertices = bic.getCutVertices();
		Collection<Graph> graphs = bic.getComponents();
		
		// 2. Solve 3-partite problem in the tree order of biconnected components.
		// 3. Reconcile partitions in that order as well.
		Tree biconnectedComponentTree = bic.getBiconnectedComponentTree();
		Map<TreeNode, Graph> treeToBiconnectedComponentMap = bic.getTreeNodeToComponentsMap();
		Map<Vertex, Integer> reconciledPartitions = new HashMap<Vertex, Integer>(); // holder for reconciled partitions.

		// Initialize our queue with the root node.
		LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
		queue.add(biconnectedComponentTree.getRoot());

		while (queue.size() > 0) {
			TreeNode node = queue.remove();
			Graph biconnectedGraph = treeToBiconnectedComponentMap.get(node);
			Map<Vertex, Integer> partition = getPartitionsForBiconnectedGraph(algorithm, biconnectedGraph);
			if (partition != null) {
				reconcilePartitions(reconciledPartitions, partition);
				if (!node.isLeaf()) {
					queue.addAll(node.getChildren());
				}
			} else {
				// This biconnected component is not 3-colorable.
				// This implies that the entire graph is not 3-colorable.
				return null;
			}
		}
		
		return reconciledPartitions;
	}

	/**
	 * Input graph is assumed to be bi-connected.
	 * Algorithm:
	 * (1) Run heuristic greedy algorithm - O(V+E).
	 * (2) If we cannot find appropriate partition, try to find a 4-clique. - O(V^4).
	 * (3) If we cannot find a clique, run exhaustive algorithm, currently O(3^n).
	 */
	private static Map<Vertex, Integer> getPartitionsForBiconnectedGraph(Algorithm algorithm, Graph graph) {
		// Heuristic greedy algorithm is O(V+E).
		Map<Vertex, Integer> partitions = NPartite.partition(NPartite.Algorithm.HEURISTIC_GREEDY, graph, 3);
		if (partitions == null) {
			// Our simplifed algorithm could not partition graph into 3 parts.
			// Determine if there is a 4-clique. If there is, we know we don't have
			// a 3-partite graph.
			if (NCliqueBruteForce.getClique(graph, 4) == null) {
				//System.out.println("No 4-clique found.");
				// We couldn't find a 4-clique. We will do an exhaustive search.
				// This is O(3^n), hooray!
				//partitions = NPartition.partitionSmartBruteForce(graph, 3);
				switch (algorithm) {
					case BRUTE_FORCE:
						partitions = NPartite.partition(NPartite.Algorithm.BRUTE_FORCE_DUMB, graph, 3);
						break;
					case BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION:
						partitions = NPartite.partition(NPartite.Algorithm.BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION, graph, 3);
						break;
					case BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_2:
						partitions = NPartite.partition(NPartite.Algorithm.BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_2, graph, 3);
						break;
					case BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_SET_2V:
						partitions = NPartite.partition(NPartite.Algorithm.BRUTE_FORCE_LEXICOGRAPHIC_ENUMERATION_SET_2V, graph, 3);
						break;
					case MAXIMAL_INDEPENDENT_SET:
						partitions = partitionMIS(graph, 3);
						break;
					case MAXIMAL_INDEPENDENT_SET_DIV2:
						partitions = partitionMIS(graph, 2);
						break;
					default:
						throw new IllegalArgumentException("Unknown/unhandled algorithm");
				}
			}
		}
		return partitions;
	}
	
	private static void reconcilePartitions(Map<Vertex, Integer> repository, Map<Vertex, Integer> partition) {
		// Determine if there is any common vertex between the repository and the new partition.
		// There should be at max, one such vertex, which is the cut vertex for that partition.
		Vertex cutVertex = null;
		for (Vertex vertex : partition.keySet()) {
			if (repository.containsKey(vertex)) {
				cutVertex = vertex;
				break;
			}
		}
		
		if ((cutVertex != null) && (repository.get(cutVertex) != partition.get(cutVertex))) {
			Integer repoNumber = repository.get(cutVertex);
			Integer partitionNumber = partition.get(cutVertex);
			// The partitions don't match.
			// Change given partition according to repository.
			// Switch repo partition number with partition number in given partition.
			for (Map.Entry<Vertex, Integer> partitionEntry : partition.entrySet()) {
				Integer newValue = partitionEntry.getValue();
				if (partitionNumber.equals(partitionEntry.getValue())) {
					newValue = repoNumber;
				} else if (repoNumber.equals(partitionEntry.getValue())) {
					newValue = partitionNumber;
				}
				repository.put(partitionEntry.getKey(), newValue);
			}
		} else {
			// There is no reconciliation to be done.
			repository.putAll(partition);
		}
	}

	// TODO: remove input parameter divBy
	private static Map<Vertex, Integer> partitionMIS(Graph graph, int divBy) {
		//Vertex[] vertices = getAsArray(graph.getVertices());
		Vertex[] vertices = new Vertex[graph.getNumVertices()];
		graph.getVertices().toArray(vertices);

		int numVertices = vertices.length;
		int misSize = (int) Math.floor(numVertices * 1.0F / divBy);
		misSize = Math.max(misSize, 1);
		
		LexicographicSelector selector = new LexicographicSelector(numVertices, misSize);
		boolean[] misCandidate;
		long numIterations = 0; // Non-essential variable - counting number of attempts.

		// Data structures for optimization - reuse cloned graph instead of recreating new clones for each iteration.
		// Just delete vertices/edges not required and re-add them at the end of the iteration. O(n^2) savings each time.
		Map<Vertex, Collection<Vertex>> deletedEdges = new HashMap<Vertex, Collection<Vertex>>();
		Graph graphClone = graph.clone();
		
		while (selector.hasNext()) {
			// Get non-essential stuff out of the way.
			numIterations++;
			if (numIterations < 0) {
				System.out.println("iterations beyond " + Long.MAX_VALUE);
			} else if (numIterations % 10000000L == 0) {
				System.out.println("iterations so far: " + numIterations);
			}

			misCandidate = selector.next();

			// Determine if the vertices represented by
			// our selection make up an independent set.
			if (isIndependentSet(graph, vertices, misCandidate)) {
				// Our selection is an independent set.
				// (1) Remove independent set vertices from the graph.
				int vertexNumber = 0;
				for (Vertex v : vertices) {
					// Remove vertex if it is in the independent set.
					// Save the deleted edges, so we can recreate the original.
					if (misCandidate[vertexNumber]) {
						deletedEdges.put(v, graphClone.removeVertex(v));
					}
					vertexNumber++;
				}
				// (2) Check if the residual graph is bipartite.
				Map<Vertex, Integer> partition = Bipartite.partition(graphClone);
				if (partition != null) {
					// (2.a) Residual graph is bipartite. We have a 3-coloring!
					//       We just need to add our independent set vertices
					//       with the 3rd color. Note that it is possible the
					//       graph was colored with 1 color (i.e. color=0).
					for (int i=0; i<numVertices; i++) {
						if (misCandidate[i]) {
							partition.put(vertices[i], 2);
						}
					}
					System.out.println("iterations: " + numIterations + " ");
					return partition;
				} else {
					// (2.b) Although this is an independent set,
					//       G - {IS} is not a bipartite graph.
					//       Go one level deeper if possible.
					if (selector.canKeep()) {
						selector.keep();
					}
				}
				
				// (3) Put back the deleted vertices and edges to recreate the original graph.
				//     This step avoids creating a graph clone each time and fosters reuse.
				//     First add all deleted vertices.
				vertexNumber = 0;
				for (Vertex v : vertices) {
					if (misCandidate[vertexNumber]) {
						graphClone.addVertex(v);
					}
					vertexNumber++;
				}

				// Then add deleted edges.
				vertexNumber = 0;
				for (Vertex v : vertices) {
					if (misCandidate[vertexNumber]) {
						if (deletedEdges.get(v) != null) {
							for (Vertex adjacentVertex : deletedEdges.remove(v)) {
								graphClone.addEdge(v, adjacentVertex);
							}
						}
					}
					vertexNumber++;
				}
			} else {
				// Not an independent set.
				// Go to the next selection.
			}
		}
		
		// We have exhausted our lexicographic count (2^misSize).
		// This implies that there was no independent set of size numVertices/3
		// or less that lead to a 3-coloring. Since in a 3-colorable graph, there
		// will always be such an independent set, we are guaranteed that this
		// graph is not 3-colorable.
		System.out.println("iterations: " + numIterations);
		return null;
	}
	
	// Translates a collection into a vertex array.
	private static Vertex[] getAsArray(Collection<Vertex> collection) {
		Vertex[] vertex = null;
		if (collection != null) {
			vertex = new Vertex[collection.size()];
			int index=0;
			for (Vertex v : collection) {
				vertex[index++] = v;
			}
		}
		return vertex;
	}
	
	private static boolean isIndependentSet(Graph graph, Vertex[] vertices, boolean[] candidate) {
		if ((vertices != null) && (graph != null)) {
			for (int i=0; i<vertices.length - 1; i++) {
				if (candidate[i]) {
					// This vertex is in the candidate independent set.
					Vertex v1 = vertices[i];
					for (int j=i+1; j<vertices.length; j++) {
						if (candidate[j]) {
							// This vertex is also in the candidate independent set.
							Vertex v2 = vertices[j];
							if (graph.hasEdge(v1, v2)) {
								// Vertices in the given set share an edge.
								// This candidate cannot be an independent set.
								return false;
							}
						}
					}
				}
			}
		}
		
		// No two vertices in the given set share an edge.
		// Thus the set forms an independent set.
		return true;
	}
}
