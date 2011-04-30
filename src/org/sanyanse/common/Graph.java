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
  public Vertex[] Vertices;
  public Vertex[] OriginalVertices;

  public Graph(
    int nodeCnt,
    double p,
    Vertex[] nodes)
  {
    NodeCount = nodeCnt;
    Vertices = nodes;
    EdgeProbability = p;
    OriginalVertices = nodes;
  }

  public void SortByEdgeCount()
  {
    Vertices = Arrays.copyOf(OriginalVertices, OriginalVertices.length);

    Arrays.sort(Vertices, new Comparator<Vertex>()
    {
      @Override
      public int compare(Vertex vertex, Vertex colorableNode1)
      {
        // descending
        return colorableNode1.Edges.length - vertex.Edges.length;
      }
    });
  }

  public void SortByMetric(final FloatMatrix metric)
  {
/*
    final int j = NodeCount - 1;

    Arrays.sort(Vertices, new Comparator<Vertex>()
    {
      @Override
      public int compare(Vertex colorableNode, Vertex colorableNode1)
      {
        // descending
        GraphNodeInfo infoA = NodeMap.get(colorableNode.Id);
        GraphNodeInfo infoB = NodeMap.get(colorableNode1.Id);

        return Float.compare(metric.get(infoB.Index, j), metric.get(infoA.Index, j));
      }
    });

    for (int i = 0; i < NodeCount; i++)
    {
      Vertex node = Vertices[i];
      NodeMap.get(node.Id).Index = i;
    }
*/
  }

  public Graph clone()
  {
    Vertex[] newNodes = new Vertex[NodeCount];

    for (int i = 0; i < NodeCount; i++)
    {
      newNodes[i] = new Vertex(Vertices[i]);
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

  Vertex[][] _cache = null;

  public ColorState analyzeState()
  {
    if (_cache == null)
    {
      buildCache();
    }

    ColorState state = ColorState.Complete;

    for (int i = NodeCount - 1; i >= 0; i--)
    {
      int color = Vertices[i].Color;
      if (color == 0)
      {
        state = ColorState.PartialValid;
        continue;
      }

      final Vertex[] edges = _cache[i];

      for (int x = edges.length - 1; x >= 0; x--)
      {
        if (edges[x].Color == color)
        {
          return ColorState.Invalid;
        }
      }
    }

    return state;
  }

  private void buildCache()
  {
    _cache = new Vertex[NodeCount][];
    for (int i = NodeCount - 1; i >= 0; i--)
    {
      _cache[i] = new Vertex[Vertices[i].Edges.length];

      final int[] row = Vertices[i].Edges;

      for (int x = row.length - 1; x >= 0; x--)
      {
        _cache[i][x] = OriginalVertices[row[x]];
      }
    }
  }

  public GraphAnalysis analyze()
  {
    int correctRowColorings = 0;
    int correctEdgeColorings = 0;
    boolean hasInvalidColorings = false;
    Map<String, Integer> edgeColoringsMap = new HashMap<String, Integer>();

    ColorState state = ColorState.Complete;

    for (int i = Vertices.length - 1; i >= 0; i--)
    {
      long color = Vertices[i].Color;
      if (color == 0)
      {
        state = ColorState.PartialValid;
        continue;
      }

      int[] row = Vertices[i].Edges;

      // TODO: explore leveraging undirected nature of connection matrix

      boolean rowIsCorrectlyColored = true;
      int nodeEdgeColoringCount = 0;

      for (int x = 0; x < row.length; x++)
      {
        if (OriginalVertices[row[x]].Color == color)
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

      edgeColoringsMap.put(Vertices[i].Id, nodeEdgeColoringCount);

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
