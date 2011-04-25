package org.sanyanse.common;


import java.util.*;


public class Graph
{
  public final int NodeCount;
  public final ColorableNode[] Nodes;
  public final Map<String, ColorableNode> NodeMap;
  public final Map<String, Set<ColorableNode>> EdgeMap;
  public final double EdgeProbability;
  public final GraphDecomposition Decomposition;

  public Graph(
    int nodeCnt,
    ColorableNode[] nodes,
    Map<String, ColorableNode> nodeMap,
    double p,
    Map<String, Set<ColorableNode>> edgeMap,
    GraphDecomposition decomposition)
  {
    NodeCount = nodeCnt;
    Nodes = nodes;
    NodeMap = nodeMap;
    EdgeProbability = p;
    EdgeMap = edgeMap;
    Decomposition = decomposition;
  }

  public void SortByMetric(final Map<String, Double> metric)
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
  }

  public Graph clone()
  {
    GraphBuilder builder = GraphBuilder.createFrom(this);
    Graph copy = builder.build();
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
    Map<String, Integer> edgeColroingsMap = new HashMap<String, Integer>();

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

      edgeColroingsMap.put(Nodes[i].Id, nodeEdgeColoringCount);

      if (rowIsCorrectlyColored)
      {
        correctRowColorings++;
      }
    }

    if (hasInvalidColorings)
    {
      state = Graph.ColorState.Invalid;
    }

    return new GraphAnalysis(state, correctEdgeColorings, correctRowColorings, edgeColroingsMap);
  }
}
