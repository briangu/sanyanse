package org.sanyanse.colorer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;

import java.util.Arrays;


public class LeastPopularChoiceBacktrackColorer implements GraphColorer
{
  Graph _graph;

  public LeastPopularChoiceBacktrackColorer(Graph graph)
  {
    _graph = graph;
  }

  private int[][] buildColorChoices()
  {
    int[][] colorChoices = new int[_graph.NodeCount][];
    int[] colors = new int[3];
    int[][] defaultColors = new int[][] {
/*
                                          { 1, 2, 3 },
                                          { 1, 3, 2 },
                                          { 2, 1, 3 },
                                          { 2, 3, 1 },
                                          { 3, 1, 2 },
                                          { 3, 2, 1 }
*/
                                          { 3, 2, 1 },
                                          { 3, 1, 2 },
                                          { 2, 3, 1 },
                                          { 2, 1, 3 },
                                          { 1, 3, 2 },
                                          { 1, 2, 3 },
    };
    for (int i = 0; i < _graph.NodeCount; i++)
    {
      int[] choices;
      colors[0] = 0;
      colors[1] = 0;
      colors[2] = 0;

      for (int idx : _graph.Vertices[i].Edges)
      {
        int color = _graph.Vertices[i].Color;
        if (color == 0) continue;
        colors[color - 1]++;
      }

      int[] copy = Arrays.copyOf(colors, colors.length);
      Arrays.sort(copy);


      if (colors[0] == copy[0] && colors[1] == copy[1] && colors[2] == copy[2])
      {
        choices = defaultColors[0];
      }
      else if (colors[0] == copy[0] && colors[2] == copy[1] && colors[1] == copy[2])
      {
        choices = defaultColors[1];
      }
      else if (colors[1] == copy[0] && colors[0] == copy[1] && colors[2] == copy[2])
      {
        choices = defaultColors[2];
      }
      else if (colors[1] == copy[0] && colors[2] == copy[1] && colors[0] == copy[2])
      {
        choices = defaultColors[3];
      }
      else if (colors[2] == copy[0] && colors[0] == copy[1] && colors[1] == copy[2])
      {
        choices = defaultColors[4];
      }
      else // if (colors[2] == copy[0] && colors[1] == copy[1] && colors[0] == copy[2])
      {
        choices = defaultColors[5];
      }

      colorChoices[i] = choices;
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
