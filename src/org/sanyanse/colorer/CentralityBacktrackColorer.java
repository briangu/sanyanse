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
    Graph graph = _graph.clone();
    graph.SortByMetric(graph.Decomposition.getCentrality(graph));
    GraphColorer colorer = new BacktrackColorer(graph);

    ColoringResult result = null;
    try
    {
      result = colorer.call();
    }
    catch (Exception e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    return result;
  }
}
