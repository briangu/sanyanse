package org.sanyanse.colorer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;


public class RandomChoiceBacktrackColorer implements GraphColorer
{
  Graph _graph;

  public RandomChoiceBacktrackColorer(Graph graph)
  {
    _graph = graph;
  }

  static int[][] defaultColors = new int[][] {
                                        { 1, 2, 3 },
                                        { 1, 3, 2 },
                                        { 2, 1, 3 },
                                        { 2, 3, 1 },
                                        { 3, 1, 2 },
                                        { 3, 2, 1 }};

  private int[][] buildColorChoices()
  {
    int[][] colorChoices = new int[_graph.NodeCount][];
    int[] colors = new int[3];
    Random r = new Random();
    for (int i = 0; i < _graph.NodeCount && !Thread.currentThread().isInterrupted(); i++)
    {
      colorChoices[i] = defaultColors[r.nextInt(defaultColors.length)];
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
      GraphColorer colorer = new ColorChoiceBacktrackColorer(_graph, buildColorChoices());

      result = colorer.call();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    System.out.println("rcc finished");

    return result;
  }
}
