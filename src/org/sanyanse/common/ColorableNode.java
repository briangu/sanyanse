package org.sanyanse.common;


import java.util.Arrays;


public class ColorableNode
{
  public String Id;
  public int Color;
  public int[] Edges;

  public ColorableNode(ColorableNode node)
  {
    Id = node.Id;
    Color = node.Color;
    Edges = Arrays.copyOf(node.Edges, node.Edges.length);
  }

  public ColorableNode(String id)
  {
    this(id, 0);
  }

  public ColorableNode(String id, Integer l)
  {
    Id = id;
    Color = l;
  }

  public boolean equals(Object o)
  {
    return ((ColorableNode) o).Color == Color;
  }

  public String toString()
  {
    return Id;
  }
}
