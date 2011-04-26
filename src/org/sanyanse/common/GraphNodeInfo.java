package org.sanyanse.common;


import java.util.HashSet;
import java.util.Set;


public class GraphNodeInfo
  {
    public ColorableNode Node;
    public Set<ColorableNode> EdgeSet;
    public int Index;

    public GraphNodeInfo(ColorableNode node)
    {
      Node = node;
      EdgeSet = new HashSet<ColorableNode>();
    }

    public GraphNodeInfo(ColorableNode node, int index)
    {
      this(node);
      Index = index;
    }
  }

