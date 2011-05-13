/**
 * SanYanSe
 *
 * @Author Brian Guarraci
 *
 */
package org.sanyanse.common;


import java.util.Arrays;
import java.util.Comparator;


public class Graph
{
  public final int NodeCount;
  public final double EdgeProbability;
  public String[] VertexIds;
  public Vertex[] Vertices;
  public Vertex[] OriginalVertices;

  public Graph(
    int nodeCnt,
    double p,
    Vertex[] nodes,
    String[] vertexIds)
  {
    NodeCount = nodeCnt;
    VertexIds = vertexIds;
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

  public Graph clone()
  {
    String[] newIds = new String[NodeCount];
    Vertex[] newNodes = new Vertex[NodeCount];

    for (int i = 0; i < NodeCount; i++)
    {
      newIds[i] = VertexIds[i];
      newNodes[i] = new Vertex(Vertices[i]);
    }

    Graph copy =
      new Graph(
        NodeCount,
        EdgeProbability,
        newNodes,
        newIds);

    return copy;
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
    ColorState state = ColorState.Complete;

    for (int i = NodeCount - 1; i >= 0; i--)
    {
      int color = Vertices[i].Color;
      if (color == 0)
      {
        state = ColorState.PartialValid;
        continue;
      }

      final short[] edges = Vertices[i].Edges;

      for (int x = edges.length - 1; x >= 0; x--)
      {
        if (Vertices[edges[x]].Color == color)
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

      final short[] row = Vertices[i].Edges;

      for (int x = row.length - 1; x >= 0; x--)
      {
        _cache[i][x] = OriginalVertices[row[x]];
      }
    }
  }
}
