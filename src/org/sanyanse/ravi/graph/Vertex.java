package org.sanyanse.ravi.graph;

public class Vertex {
	private String m_name;
	
	public Vertex(String name) {
		m_name = name;
	}
	
	public String getName() {
		return m_name;
	}
	
	public boolean equals(Vertex v) {
		return m_name.equals(v.m_name);
	}
	
	public int hashCode() {
		return m_name.hashCode();
	}
	
	public Vertex clone() {
		return new Vertex(m_name);
	}
	
	public String toString() {
		return m_name;
	}
}
