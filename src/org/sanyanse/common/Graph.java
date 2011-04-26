package org.sanyanse.common;


import java.util.*;


public class Graph
{
  public final int NodeCount;
  public final double EdgeProbability;
  public final ColorableNode[] Nodes;
  public final Map<String, GraphNodeInfo> NodeMap;

  public Graph(
    int nodeCnt,
    double p,
    ColorableNode[] nodes,
    Map<String, GraphNodeInfo> nodeMap)
  {
    NodeCount = nodeCnt;
    Nodes = nodes;
    NodeMap = nodeMap;
    EdgeProbability = p;
  }

  public void SortByMetric(final Map<String, Float> metric)
  {
    Arrays.sort(Nodes, new Comparator<ColorableNode>()
    {
      @Override
      public int compare(ColorableNode colorableNode, ColorableNode colorableNode1)
      {
        // descending
        return metric.get(colorableNode1.Id).compareTo(metric.get(colorableNode.Id));
      }
    });

    for (int i = 0; i < NodeCount; i++)
    {
      ColorableNode node = Nodes[i];
      NodeMap.get(node.Id).Index = i;
    }
  }

  public Graph clone()
  {
    ColorableNode[] newNodes = new ColorableNode[NodeCount];
    Map<String, GraphNodeInfo> newNodeMap = new HashMap<String, GraphNodeInfo>(NodeCount);

    for (int i = 0; i < NodeCount; i++)
    {
      newNodes[i] = new ColorableNode(Nodes[i]);
      newNodeMap.put(newNodes[i].Id, new GraphNodeInfo(newNodes[i], i));
    }
    for (int i = 0; i < NodeCount; i++)
    {
      Set<ColorableNode> newEdges = new HashSet<ColorableNode>(Nodes[i].Edges.length);

      for (ColorableNode neighbor : Nodes[i].Edges)
      {
        newEdges.add(newNodeMap.get(neighbor.Id).Node);
      }

      newNodeMap.get(newNodes[i].Id).EdgeSet = newEdges;
      newNodes[i].Edges = newEdges.toArray(new ColorableNode[newEdges.size()]);
    }

    Graph copy = new Graph(NodeCount, EdgeProbability, newNodes, newNodeMap);

    return copy;
  }

  public class GraphAnalysis
  {
    public int CorrectNodeColorings;
    public int CorrectEdgeColorings;
    public ColorState State;
    public Map<String, Integer> EdgeColoringsMap;

    public GraphAnalysis(ColorState state, int correctEdgeColorings, int correctNodeColorings, Map<String, Integer> edgeColoringsMap)
    {
      State = state;
      CorrectEdgeColorings = correctEdgeColorings;
      CorrectNodeColorings = correctNodeColorings;
      EdgeColoringsMap = edgeColoringsMap;
    }

    public Boolean IsColored()
    {
      return State == ColorState.Complete;
    }
  }

  public enum ColorState
  {
    Complete,
    PartialValid,
    Invalid
  }

  public ColorState analyzeState()
  {
    ColorState state = ColorState.Complete;

    for (int i = NodeCount - 1; i >= 0; i--)
    {
      int color = Nodes[i].Color;
      if (color == 0)
      {
        state = ColorState.PartialValid;
        continue;
      }

      ColorableNode[] row = Nodes[i].Edges;

      for (int x = 0; x < row.length; x++)
      {
        if (row[x].Color == color)
        {
          return ColorState.Invalid;
        }
      }
    }

    return state;
  }

  public GraphAnalysis analyze()
  {
    int correctRowColorings = 0;
    int correctEdgeColorings = 0;
    boolean hasInvalidColorings = false;
    Map<String, Integer> edgeColoringsMap = new HashMap<String, Integer>();

    ColorState state = ColorState.Complete;

    for (int i = Nodes.length - 1; i >= 0; i--)
    {
      long color = Nodes[i].Color;
      if (color == 0)
      {
        state = ColorState.PartialValid;
        continue;
      }

      ColorableNode[] row = Nodes[i].Edges;

      // TODO: explore leveraging undirected nature of connection matrix

      boolean rowIsCorrectlyColored = true;
      int nodeEdgeColoringCount = 0;

      for (int x = 0; x < row.length; x++)
      {
        if (row[x].Color == color)
        {
          hasInvalidColorings = true;
          rowIsCorrectlyColored = false;
        }
        else
        {
          correctEdgeColorings++;
          nodeEdgeColoringCount++;
        }
      }

      edgeColoringsMap.put(Nodes[i].Id, nodeEdgeColoringCount);

      if (rowIsCorrectlyColored)
      {
        correctRowColorings++;
      }
    }

    if (hasInvalidColorings)
    {
      state = Graph.ColorState.Invalid;
    }

    return new GraphAnalysis(state, correctEdgeColorings, correctRowColorings, edgeColoringsMap);
  }
}
