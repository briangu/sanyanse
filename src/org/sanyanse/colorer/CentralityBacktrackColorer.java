package org.sanyanse.colorer;


import org.jblas.FloatMatrix;
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
    ColoringResult result = null;
    try
    {
      _graph = _graph.clone();
      if (Thread.currentThread().isInterrupted()) return null;
      GraphDecomposition decomposition = GraphDecomposition.createFrom(_graph);
      if (Thread.currentThread().isInterrupted()) return null;
      FloatMatrix centrality = decomposition.getCentrality();
      if (Thread.currentThread().isInterrupted()) return null;
      _graph.SortByMetric(centrality);
      if (Thread.currentThread().isInterrupted()) return null;
      GraphColorer colorer = new BacktrackColorer(_graph);

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
