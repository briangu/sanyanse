package org.sanyanse.colorer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;


public class DefaultChoiceBacktrackColorer implements GraphColorer
{
  Graph _graph;

  public DefaultChoiceBacktrackColorer(Graph graph)
  {
    _graph = graph;
  }

  private int[][] buildColorChoices()
  {
    int[][] colorChoices = new int[_graph.NodeCount][];
    int[] defaultColors = new int[] { 1, 2, 3 };
    for (int i = 0; i < _graph.NodeCount; i++)
    {
      colorChoices[i] = defaultColors;
    }
    return colorChoices;
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
      GraphColorer colorer = new ColorChoiceBacktrackColorer(_graph, buildColorChoices());

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
