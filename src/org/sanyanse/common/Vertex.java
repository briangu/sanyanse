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
  public byte Color;
  public short[] Edges;

  public Vertex(Vertex node)
  {
    Color = node.Color;
    Edges = Arrays.copyOf(node.Edges, node.Edges.length);
  }

  public Vertex()
  {
    this((byte)0);
  }

  public Vertex(byte l)
  {
    Color = l;
  }

  public boolean equals(Object o)
  {
    return ((Vertex) o).Color == Color;
  }
}
