/**
 * SanYanSe
 *
 * @Author Brian Guarraci
 *
 */
package org.sanyanse.common;


import java.util.Arrays;


public class Vertex
{
  public String Id;
  public byte Color;
  public short[] Edges;

  public Vertex(Vertex node)
  {
    Id = node.Id;
    Color = node.Color;
    Edges = Arrays.copyOf(node.Edges, node.Edges.length);
  }

  public Vertex(String id)
  {
    this(id, (byte)0);
  }

  public Vertex(String id, byte l)
  {
    Id = id;
    Color = l;
  }

  public boolean equals(Object o)
  {
    return ((Vertex) o).Color == Color;
  }

  public int hashCode() {
    return Id.hashCode();
  }

  public String toString()
  {
    return Id;
  }
}
