package org.sanyanse.colorer;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphSpec;


public class FoldingColorer implements GraphColorer
{
  GraphSpec _spec;

  public FoldingColorer(GraphSpec spec)
  {
    _spec = spec;
  }

  private static class FoldableNode
  {
    public SettableInteger Color = new SettableInteger(0);
    public Set<FoldableNode> Edges;
    public String Id;
    public Set<FoldableNode> Folded = new HashSet<FoldableNode>();
    public FoldableNode FoldedInto;

    public Integer getColor()
    {
      return FoldedInto != null ? FoldedInto.getColor() : Color.get();
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

  // TODO: build stat buckets for edge counts to optimize folding sequence
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
    foldHigherNodes(foldedMap);

    return foldedMap;
  }

  private void foldSingleNodes(Map<String, FoldableNode> arr)
  {
    List<String> oneEdgeNodes = new ArrayList<String>();

    for (String nodeId : arr.keySet())
    {
      if (arr.get(nodeId).Edges.size() > 1) continue;
      oneEdgeNodes.add(nodeId);
    }

    for (String nodeId : oneEdgeNodes)
    {
      FoldableNode node = arr.get(nodeId);
      Set<FoldableNode> edges = node.Edges;
      FoldableNode neighbor = edges.iterator().next();
      List<FoldableNode> candidates = getFoldingCandidates(node, neighbor);
      if (candidates.size() == 0) continue;
      foldNode(node, candidates.get(0));
      arr.remove(node.Id);
    }
  }

  private void foldHigherNodes(Map<String, FoldableNode> arr)
  {
    List<String> nodes = Arrays.asList(arr.keySet().toArray(new String[arr.size()]));

    for (String nodeId : nodes)
    {
      FoldableNode node = arr.get(nodeId);
      Set<FoldableNode> edges = node.Edges;
      FoldableNode neighbor = edges.iterator().next();
      List<FoldableNode> candidates = getFoldingCandidates(node, neighbor);
      if (candidates.size() == 0) continue;
      foldNode(node, candidates.get(0));
      arr.remove(node.Id);
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

  private void foldNode(FoldableNode srcNode, FoldableNode destNode)
  {
    // take node and merge it with the target
    destNode.Edges.addAll(srcNode.Edges);

    // keep track of what got merged into this node
    destNode.Folded.add(srcNode);

    // keep track of where the srcNode got folded into
    if (srcNode.FoldedInto != null)
    {
      throw new IllegalArgumentException("can't fold already folded node");
    }
    srcNode.FoldedInto = destNode;

    // vist all the srcNode's neighbors and update the link to the node to point to the destNode
    for (FoldableNode neighborNode : srcNode.Edges)
    {
      neighborNode.Edges.remove(srcNode);
      neighborNode.Edges.add(destNode);
    }
  }

  private boolean canFoldInto(FoldableNode srcNode, FoldableNode destNode)
  {
    // TESTS:
    if (srcNode == destNode)
    {
      return false;
    }

    // srcNode not connected to destNode
    if (srcNode.Edges.contains(destNode) || destNode.Edges.contains(srcNode))
    {
      return false;
    }

/*
    // destNode not connected to any other second degree of srcNode
    for (FoldableNode neighborNode : destNode.Edges)
    {
      if (Util.intersect(neighborNode.Edges, srcNode.Edges).size() > 0)
      {
        return false;
      }
    }
*/

    return true;
  }



  private ColoringResult createResult(Map<String, FoldableNode> map)
  {
    Map<String, Integer> colorMap = new HashMap<String, Integer>();

    return ColoringResult.create(_spec, colorMap);
  }
}
