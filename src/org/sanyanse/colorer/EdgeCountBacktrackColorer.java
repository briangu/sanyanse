package org.sanyanse.colorer;


import org.jblas.FloatMatrix;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphDecomposition;


public class EdgeCountBacktrackColorer implements GraphColorer
{
  Graph _graph;

  public EdgeCountBacktrackColorer(Graph graph)
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
      _graph.SortByEdgeCount();
      if (Thread.currentThread().isInterrupted()) return null;
      GraphColorer colorer = new BacktrackColorer(_graph);

      result = colorer.call();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    System.out.println("ec finished");

    return result;
  }
}
