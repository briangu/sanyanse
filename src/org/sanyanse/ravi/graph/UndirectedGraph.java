package org.sanyanse.ravi.graph;

import org.sanyanse.common.Vertex;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class UndirectedGraph
{
	private Collection<Vertex> m_vertices = new HashSet<Vertex>();
	private Map<String, Collection<Vertex>> m_edges = new HashMap<String, Collection<Vertex>>();
	
	public void addVertex(Vertex v) {
		m_vertices.add(v);
	}
	
	public void addEdge(Vertex v1, Vertex v2) {
		addSingleEdge(v1, v2);
		addSingleEdge(v2, v1);
	}
	
	public void addSingleEdge(Vertex v1, Vertex v2) {
    Collection<Vertex> edges = getEdges(v1);
    if (edges == null) {
      edges = new HashSet<Vertex>();
      m_edges.put(v1.Id, edges);
    }
    edges.add(v2);
	}

	public boolean hasEdge(Vertex v1, Vertex v2) {
    Collection<Vertex> edges = m_edges.get(v1.Id);
    return edges == null ? false : edges.contains(v2);
	}

  public Collection<Vertex> getVertices() {
    return m_vertices;
  }

	public Collection<Vertex> cloneVertices() {
		// Create a new copy of the collection.
		Collection<Vertex> copy = null;
		if (m_vertices != null) {
			copy = new HashSet<Vertex>();
			copy.addAll(m_vertices);
		}
		return copy;
	}
	
	public int getNumVertices() {
		return m_vertices != null ? m_vertices.size() : 0;
	}
	
	public Collection<Vertex> getEdges(Vertex v) {
		return m_edges.get(v.Id);
	}
	
	public int getDegree(Vertex v) {
		Collection<Vertex> edges = getEdges(v);
		return edges != null ? edges.size() : 0;
	}
	
	public boolean removeEdge(Vertex v1, Vertex v2) {
		removeSingleEdge(v1, v2);
		return removeSingleEdge(v2, v1);
	}

	private boolean removeSingleEdge(Vertex v1, Vertex v2) {
		boolean removedEdge = false;
		Collection<Vertex> edges = getEdges(v1);
		if (edges != null) {
			edges.remove(v2);
			removedEdge = true;
		}
		return removedEdge;
	}
	
	public Collection<Vertex> removeVertex(Vertex v) {
		Collection<Vertex> incidentVertices = getEdges(v);
		Collection<Vertex> removedEdges = new HashSet<Vertex>(incidentVertices != null ? incidentVertices.size() : 1, 1.0F);
		if (incidentVertices != null) {
			removedEdges.addAll(incidentVertices);
		}
		if (incidentVertices != null) {
			for (Iterator<Vertex> vertexIter = incidentVertices.iterator(); vertexIter.hasNext(); ) {
				Vertex vertexToRemove = vertexIter.next();
				vertexIter.remove();
				removeSingleEdge(vertexToRemove, v);
			}
		}
		m_vertices.remove(v);
		return removedEdges;
	}
	
	public UndirectedGraph getSubgraph(Collection<Vertex> vertices) {
		UndirectedGraph subgraph = new UndirectedGraph();

		// Add vertices.
		for (Vertex v : vertices) {
			subgraph.addVertex(v);
		}
		
		// Add edges only incident upon those vertices.
		for (Vertex v : vertices) {
			Collection<Vertex> edgeVertices = getEdges(v);
			if (edgeVertices != null) {
				for (Vertex edgeVertex : edgeVertices) {
					if (vertices.contains(edgeVertex)) {
						subgraph.addEdge(v, edgeVertex);
					}
				}
			}
		}

		return subgraph;
	}
	
	public boolean contains(Vertex vertex) {
		return m_vertices.contains(vertex);
	}
	
	public Vertex getRandomVertex() {
		int randomIndex = (int) (m_vertices.size() * 1.0F * Math.random());
		int index = 0;
		for (Vertex temp : m_vertices) {
			if (index < randomIndex) {
				index++;
			} else {
				return temp;
			}
		}
		return null;
	}
	
	public UndirectedGraph clone() {
		UndirectedGraph clone = new UndirectedGraph();

		// First clone all vertices. Then clone all edges.
		for (Vertex v : m_vertices) {
			clone.addVertex(v); // Do not clone vertices per se, just the references.
		}
		for (Vertex v : m_vertices) {
			Collection<Vertex> incidentVertices = getEdges(v);
			if (incidentVertices != null) {
				for (Vertex incidentVertex : incidentVertices) {
					clone.addEdge(v, incidentVertex);
				}
			}
		}

		return clone;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Vertex v : m_vertices) {
			sb.append(v);
			sb.append(" => [");
			Collection<Vertex> incidentVertices = getEdges(v);
			if (incidentVertices != null) {
				for (Vertex incidentVertex : incidentVertices) {
					sb.append(incidentVertex);
					sb.append(",");
				}
				if (sb.charAt(sb.length() - 1) == ',') {
					// Remove comma at the end.
					sb.deleteCharAt(sb.length() - 1);
				}
			}
			sb.append("]\n");
		}
		return sb.toString();
	}
}
