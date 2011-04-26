package org.sanyanse.colorer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphDecomposition;


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
    _graph = _graph.clone();
    GraphDecomposition decomposition = GraphDecomposition.createFrom(_graph);
    _graph.SortByMetric(decomposition.getCentrality(_graph));
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

    System.out.println("centrality finished");

    return result;
  }
}
