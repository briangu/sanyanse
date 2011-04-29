package org.sanyanse.common;


public class ColorableNode
{
  public String Id;
  public int Color;
  public ColorableNode[] Edges;
  public int[] ColorChoices = DefaultColorChoices;
  public int ColorChoiceIndex = 0;

  public static final int[] DefaultColorChoices = new int[] { 1, 2, 3 };

  public ColorableNode(ColorableNode node)
  {
    Id = node.Id;
    Color = node.Color;
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

  public ColorableNode(String id, Integer l, ColorableNode[] edges)
  {
    this(id);
    Color = l;
    Edges = edges;
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
