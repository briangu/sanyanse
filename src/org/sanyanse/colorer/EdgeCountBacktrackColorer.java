package org.sanyanse.colorer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.StopWatch;


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
      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      _graph = _graph.clone();
      stopWatch.stop();
      System.out.println(String.format("clone time: %s", stopWatch.getDuration()));
      System.out.println();
      if (Thread.currentThread().isInterrupted()) return null;
      stopWatch = new StopWatch();
      stopWatch.start();
      _graph.SortByEdgeCount();
      stopWatch.stop();
      System.out.println(String.format("sort time: %s", stopWatch.getDuration()));
      System.out.println();
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
