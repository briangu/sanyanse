package org.sanyanse.colorer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphSpec;

import java.util.*;


public class FoldingColorer implements GraphColorer
{
  GraphSpec _spec;

  private static class FoldableNode
  {
    public SettableInteger Color = new SettableInteger(0);
    public Set<FoldableNode> Edges;
    public String Id;
    public Set<String> Folded = null;
    public FoldableNode FoldedInto = null;

    public boolean IsFolded() {
      return FoldedInto != null;
    }

    public FoldableNode(String id) {
      Id = id;
    }
  }

  private static class SettableInteger
  {
    Integer _value;

    public SettableInteger(Integer l)
    {
      _value = l;
    }

    public Integer get()
    {
      return _value;
    }

    public void set(Integer value)
    {
      _value = value;
    }

    public boolean equals(Object o)
    {
      return ((SettableInteger) o).get() == _value;
    }
  }

  public FoldingColorer(GraphSpec spec)
  {
    _spec = spec;
  }

  static Map<String, FoldableNode> buildColoringArray(GraphSpec spec)
  {
    Map<String, FoldableNode> buildMap = new HashMap<String, FoldableNode>(spec.NodeCount);

    int k = 0;

    for (int i = 0; i < spec.NodeCount; i++)
    {
      final String nodeId = spec.Nodes.get(i);
      FoldableNode node;

      if (buildMap.containsKey(nodeId))
      {
        node = buildMap.get(nodeId);
      }
      else
      {
        node = new FoldableNode(nodeId);
        buildMap.put(nodeId, node);
      }

      List<String> specEdges = spec.Edges.get(spec.Nodes.get(i));
      Set<FoldableNode> edges = new HashSet<FoldableNode>(specEdges.size());

      int length = specEdges.size();
      for (int j = 0; j < length; j++)
      {
        final String neighborId = specEdges.get(j);

        if (!buildMap.containsKey(neighborId))
        {
          FoldableNode newNode = new FoldableNode(neighborId);
          buildMap.put(neighborId, newNode);
        }

        edges.add(buildMap.get(neighborId));
      }

      node.Edges = edges;
    }

    return buildMap;
  }

  @Override
  public ColoringResult call()
  {
    final Map<String, FoldableNode> arr = buildColoringArray(_spec);

    Map<String, FoldableNode> foldedMap = fold(arr);

    if (foldedMap.size() > 3)
    {
      return ColoringResult.createNotColorableResult();
    }

    ColoringResult result = createResult(foldedMap);

    return result;
  }

  private Map<String, FoldableNode> fold(Map<String, FoldableNode> arr)
  {
    Map<String, FoldableNode> foldedMap = new HashMap<String, FoldableNode>(arr);

    foldSingleNodes(foldedMap);

    return foldedMap;
  }

  private void foldSingleNodes(Map<String, FoldableNode> arr)
  {
    for (String nodeId : arr.keySet())
    {
      FoldableNode node = arr.get(nodeId);

      if (node.FoldedInto != null) continue;

      Set<FoldableNode> edges = arr.get(nodeId).Edges;

      if (edges.size() > 1) continue;

      FoldableNode neighbor = edges.iterator().next();

      List<FoldableNode> candidates = getFoldingCandidates(node, neighbor);

      if (candidates.size() == 0) continue;

      foldNode(node, candidates.get(0));
    }
  }

  private List<FoldableNode> getFoldingCandidates(FoldableNode node, FoldableNode neighbor)
  {
    List<FoldableNode> nodes = new ArrayList<FoldableNode>(neighbor.Edges.size());

    for (FoldableNode secondDegree : neighbor.Edges)
    {
      if (canFoldInto(node, secondDegree))
      {
        nodes.add(secondDegree);
      }
    }

    return nodes;
  }

  private void foldNode(FoldableNode node, FoldableNode neighborNode)
  {
    node.FoldedInto = neighborNode;
    neighborNode.Edges.addAll(node.Edges);
    neighborNode.Folded.add(node.Id);
  }

  private boolean canFoldInto(FoldableNode node, FoldableNode secondDegree)
  {
    if (secondDegree.IsFolded()) return false;



    return false;  //To change body of created methods use File | Settings | File Templates.
  }

  private ColoringResult createResult(Map<String, FoldableNode> map)
  {
    Map<String, Integer> colorMap = new HashMap<String, Integer>();

    return ColoringResult.create(_spec, colorMap);
  }
}
