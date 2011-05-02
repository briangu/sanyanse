package org.sanyanse.colorer;

import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.Vertex;
import org.sanyanse.ravi.algorithm.Tripartite;
import org.sanyanse.ravi.graph.UndirectedGraph;

import java.util.Map;

public class RaviTripartiteColorer implements GraphColorer
{
  Graph _graph;
  Tripartite.Algorithm _algorithm;

  public RaviTripartiteColorer(Graph graph, Tripartite.Algorithm algorithm)
  {
    _graph = graph;
    _algorithm = algorithm;
  }

  private UndirectedGraph convertToUndirectedGraph(Graph graph)
  {
    UndirectedGraph undirectedGraph = new UndirectedGraph();

    Vertex[] arr = new Vertex[graph.NodeCount];
    int i = 0;
    for (Vertex node : graph.Vertices)
    {
      Vertex clone = new Vertex(node);
      undirectedGraph.addVertex(clone);
      arr[i++] = clone;
    }

    for (Vertex node : arr)
    {
      for (i = 0; i < node.Edges.length; i++)
      {
        undirectedGraph.addSingleEdge(node, arr[node.Edges[i]]);
      }
    }

    return undirectedGraph;
  }

  @Override
  public ColoringResult call() throws Exception
  {
    UndirectedGraph undirectedGraph = convertToUndirectedGraph(_graph);
    Map<Vertex, Integer> colorMap = Tripartite.partition(_algorithm, undirectedGraph);

    return
      (colorMap == null)
      ? ColoringResult.createNotColorableResult()
      : ColoringResult.createColoredGraphResult(colorMap);
  }
}
