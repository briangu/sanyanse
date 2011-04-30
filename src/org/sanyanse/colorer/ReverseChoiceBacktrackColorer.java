package org.sanyanse.colorer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;


public class ReverseChoiceBacktrackColorer implements GraphColorer
{
  Graph _graph;

  public ReverseChoiceBacktrackColorer(Graph graph)
  {
    _graph = graph;
  }

  @Override
  public ColoringResult call()
  {
    final int[] colorChoices = new int[] {3, 2, 1};

    ColoringResult result = null;
    try
    {
      _graph = _graph.clone(colorChoices);
      if (Thread.currentThread().isInterrupted()) return null;
      _graph.SortByEdgeCount();
      if (Thread.currentThread().isInterrupted()) return null;
      GraphColorer colorer = new ColorChoiceBacktrackColorer(_graph);

      result = colorer.call();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    System.out.println("dcbt finished");

    return result;
  }
}
