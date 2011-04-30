package org.sanyanse.common;


import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.jblas.FloatMatrix;


public class Graph
{
  public final int NodeCount;
  public final double EdgeProbability;
  public ColorableNode[] Nodes;
  public ColorableNode[] OriginalNodes;

  public Graph(
    int nodeCnt,
    double p,
    ColorableNode[] nodes)
  {
    NodeCount = nodeCnt;
    Nodes = nodes;
    EdgeProbability = p;
    OriginalNodes = nodes;
  }

  public void SortByEdgeCount()
  {
    Nodes = Arrays.copyOf(OriginalNodes, OriginalNodes.length);

    Arrays.sort(Nodes, new Comparator<ColorableNode>()
    {
      @Override
      public int compare(ColorableNode colorableNode, ColorableNode colorableNode1)
      {
        // descending
        return colorableNode1.Edges.length - colorableNode.Edges.length;
      }
    });
  }

  public void SortByMetric(final FloatMatrix metric)
  {
/*
    final int j = NodeCount - 1;

    Arrays.sort(Nodes, new Comparator<ColorableNode>()
    {
      @Override
      public int compare(ColorableNode colorableNode, ColorableNode colorableNode1)
      {
        // descending
        GraphNodeInfo infoA = NodeMap.get(colorableNode.Id);
        GraphNodeInfo infoB = NodeMap.get(colorableNode1.Id);

        return Float.compare(metric.get(infoB.Index, j), metric.get(infoA.Index, j));
      }
    });

    for (int i = 0; i < NodeCount; i++)
    {
      ColorableNode node = Nodes[i];
      NodeMap.get(node.Id).Index = i;
    }
*/
  }

  public Graph clone()
  {
    ColorableNode[] newNodes = new ColorableNode[NodeCount];

    for (int i = 0; i < NodeCount; i++)
    {
      newNodes[i] = new ColorableNode(Nodes[i]);
    }

    Graph copy =
      new Graph(
        NodeCount,
        EdgeProbability,
        newNodes);

    return copy;
  }

  public Graph clone(int[] colorChoices)
  {
    ColorableNode[] newNodes = new ColorableNode[NodeCount];

    for (int i = 0; i < NodeCount; i++)
    {
      newNodes[i] = new ColorableNode(Nodes[i], colorChoices);
    }

    Graph copy =
      new Graph(
        NodeCount,
        EdgeProbability,
        newNodes);

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

      final int[] row = Nodes[i].Edges;

      for (int x = row.length - 1; x >= 0; x--)
      {
        if (OriginalNodes[row[x]].Color == color)
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

      int[] row = Nodes[i].Edges;

      // TODO: explore leveraging undirected nature of connection matrix

      boolean rowIsCorrectlyColored = true;
      int nodeEdgeColoringCount = 0;

      for (int x = 0; x < row.length; x++)
      {
        if (OriginalNodes[row[x]].Color == color)
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
