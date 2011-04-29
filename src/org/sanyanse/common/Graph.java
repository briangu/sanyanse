package org.sanyanse.common;


import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jblas.FloatMatrix;


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

  public void SortByEdgeCount()
  {
    Arrays.sort(Nodes, new Comparator<ColorableNode>()
    {
      @Override
      public int compare(ColorableNode colorableNode, ColorableNode colorableNode1)
      {
        // descending
        return colorableNode1.Edges.length - colorableNode.Edges.length;
      }
    });

    for (int i = 0; i < NodeCount; i++)
    {
      ColorableNode node = Nodes[i];
      NodeMap.get(node.Id).Index = i;
    }
  }

  public void SortByMetric(final FloatMatrix metric)
  {
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
  }

  public Graph clone()
  {
    ColorableNode[] newNodes = new ColorableNode[NodeCount];
    Map<String, GraphNodeInfo> newNodeMap = new HashMap<String, GraphNodeInfo>(NodeCount);

    for (int i = 0; i < NodeCount; i++)
    {
      GraphNodeInfo info;

      if (newNodeMap.containsKey(Nodes[i].Id))
      {
        info = newNodeMap.get(Nodes[i].Id);
        newNodes[i] = info.Node;
      }
      else
      {
        newNodes[i] = new ColorableNode(Nodes[i]);
        info = new GraphNodeInfo(newNodes[i], i);
        newNodeMap.put(newNodes[i].Id, info);
      }

      newNodes[i].Edges = new ColorableNode[Nodes[i].Edges.length];
      for (int j = 0; j < newNodes[i].Edges.length; j++)
      {
        ColorableNode neighbor = Nodes[i].Edges[j];
        GraphNodeInfo nodeEdgeNeighborInfo;

        if (!newNodeMap.containsKey(neighbor.Id))
        {
          nodeEdgeNeighborInfo = new GraphNodeInfo(new ColorableNode(neighbor.Id), NodeMap.get(neighbor.Id).Index);
          newNodeMap.put(neighbor.Id, nodeEdgeNeighborInfo);
        }
        else
        {
          nodeEdgeNeighborInfo = newNodeMap.get(neighbor.Id);
        }

        newNodes[i].Edges[j] = nodeEdgeNeighborInfo.Node;
      }
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
