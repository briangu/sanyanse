package org.sanyanse.colorer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;


public class CentralityBacktrackColorer implements GraphColorer
{
  Graph _graph;

  public CentralityBacktrackColorer(Graph graph)
  {
    _graph = graph;
  }

  @Override
  public ColoringResult call()
  {
//    Graph graph = _graph.clone();
    _graph.SortByMetric(_graph.Decomposition.getCentrality(_graph));
    GraphColorer colorer = new BacktrackColorer(_graph);

    ColoringResult result = null;
    try
    {
      result = colorer.call();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    if (result != null)
    {
      System.out.println("centrality finished");
    }

    return result;
  }
}
