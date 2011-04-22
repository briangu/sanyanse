package org.sanyanse.colorer;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphSpec;
import org.sanyanse.common.Util;


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

    // validate the graph against the spec as best we can
    public static boolean isValid(
        GraphSpec spec,
        Map<String, FoldableNode> origGraph,
        Map<String, FoldableNode> foldedGraph)
    {
      for (String nodeId : spec.Nodes)
      {
        if (!origGraph.containsKey(nodeId)) {
          System.out.println(String.format("Missing node in origGraph: %s", nodeId));
          return false;
        }

        FoldableNode node = origGraph.get(nodeId);

        if (node.FoldedInto == null)
        {
          if (!foldedGraph.containsKey(nodeId))
          {
            System.out.println(String.format("Missing node in foldedGraph: %s", nodeId));
            return false;
          }
        }
        else
        {
          if (foldedGraph.containsKey(nodeId))
          {
            System.out.println(String.format("folded noded at top-leve of foldedGraph: %s", nodeId));
            return false;
          }

          String parentId = node.FoldedInto.Id;
          FoldableNode parent = origGraph.get(parentId);
          if (parent == null)
          {
            System.out.println(String.format("parent %s of folded node not found in top-level: %s", parentId));
            return false;
          }

          if (!parent.Folded.contains(node))
          {
            System.out.println(String.format("parent %s of folded node does not contain node of foldedGraph: %s", parent.Id, nodeId));
            return false;
          }
        }
      }

      return true;
    }

    public static String toString(Map<String, FoldableNode> foldedGraph)
    {
      StringBuilder sb = new StringBuilder();

      sb.append(String.format("nodeCnt: %s", foldedGraph.size()));
      sb.append("\n");

      for (String nodeId : foldedGraph.keySet())
      {
        List<String> neighborIds = new ArrayList<String>();

        for (FoldableNode neighbor : foldedGraph.get(nodeId).Edges)
        {
          neighborIds.add(neighbor.Id);
        }

        sb.append(String.format("%s:%s", nodeId, Util.join(neighborIds, ",")));
        sb.append("\n");
      }

      return sb.toString();
    }

    public static GraphSpec toGraphSpec(Map<String, FoldableNode> foldedGraph)
    {
      GraphSpec spec = new GraphSpec(foldedGraph.size());

      for (String nodeId : foldedGraph.keySet())
      {
        List<String> neighborIds = new ArrayList<String>();

        for (FoldableNode neighbor : foldedGraph.get(nodeId).Edges)
        {
          neighborIds.add(neighbor.Id);
        }

        spec.addNode(nodeId, neighborIds);
      }

      return spec;
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
      if (specEdges == null)
      {
        throw new IllegalArgumentException(String.format("missing edges for id %s", i));
      }

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
    final Map<String, FoldableNode> arr;

    try {
      arr = buildColoringArray(_spec);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    Map<String, FoldableNode> foldedMap = fold(arr);

    GraphSpec foldedSpec = FoldableNode.toGraphSpec(foldedMap);

    GraphColorer colorer = new BasicBacktrackColorer(foldedSpec);

    ColoringResult result = null;
    try
    {
      result = colorer.call();
    }
    catch (Exception e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    if (result != null && result.IsColored)
    {
      // translate solved graph into our mapped graph
      for (ColoringResult.Coloring coloring : result.Colorings)
      {
        foldedMap.get(coloring.NodeId).Color.set(coloring.Color);
      }

      Map<String, Integer> coloringMap = new HashMap<String, Integer>(arr.size());
      for (FoldableNode node : arr.values())
      {
        coloringMap.put(node.Id, node.getColor());
      }

      if (!isValidColoring(_spec, coloringMap))
      {
        System.out.println("colored folded map is not valid!");
      }

      // translate colored folded graph into result
      result = ColoringResult.create(_spec, coloringMap);
    }

    result = result == null
        ? ColoringResult.createNotColorableResult()
        : result;

    return result;
  }

  private boolean isValidColoring(GraphSpec spec, Map<String, Integer> coloringMap)
  {
    for (String nodeId : spec.Edges.keySet())
    {
      int color = coloringMap.get(nodeId);

      for (String neighborId : spec.Edges.get(nodeId))
      {
        if (coloringMap.get(neighborId) == color)
        {
          return false;
        }
      }
    }

    return true;
  }

  private Map<String, FoldableNode> fold(Map<String, FoldableNode> arr)
  {
    Map<String, FoldableNode> foldedMap = new HashMap<String, FoldableNode>(arr);

    foldSingleNodes(foldedMap);

    int lastSize;
    do {
      lastSize = foldedMap.size();
      foldHigherNodes(foldedMap);
      FoldableNode.isValid(_spec, arr, foldedMap);
      System.out.println(FoldableNode.toString(foldedMap));
    } while (lastSize != foldedMap.size());

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
      if (candidates.size() == 0)
      {
        continue;
      }
      foldNode(node, candidates.get(0));
      arr.remove(node.Id);
    }
  }

  private void foldHigherNodes(Map<String, FoldableNode> arr)
  {
    List<String> nodes = Arrays.asList(arr.keySet().toArray(new String[arr.size()]));

    Random r = new Random();

    for (String nodeId : nodes)
    {
      FoldableNode node = arr.get(nodeId);
      Set<FoldableNode> edges = node.Edges;
      FoldableNode neighbor = edges.iterator().next();
      List<FoldableNode> candidates = getFoldingCandidates(node, neighbor);
      if (candidates.size() == 0) continue;
      foldNode(node, candidates.get(r.nextInt(candidates.size())));
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
    System.out.println(String.format("fold %s to %s", srcNode.Id, destNode.Id));

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

    for (FoldableNode foldedNodes : destNode.Folded)
    {
      foldedNodes.FoldedInto = destNode;
    }
  }

  private boolean canFoldInto(FoldableNode srcNode, FoldableNode destNode)
  {
    // TESTS:
    if (srcNode == destNode)
    {
      return false;
    }

    if (srcNode.Id == destNode.Id)
    {
      throw new IllegalArgumentException("Ids are equal but objects are not");
    }

    // srcNode not connected to destNode
    if (srcNode.Edges.contains(destNode) || destNode.Edges.contains(srcNode))
    {
      return false;
    }

/*
    if (srcNode.Edges.size() > 1)
    {
      // destNode not connected to any other second degree of srcNode
      for (FoldableNode neighborNode : destNode.Edges)
      {
        if (Util.intersect(neighborNode.Edges, srcNode.Edges).size() > 0)
        {
          return false;
        }
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
