package org.sanyanse.colorer;


import org.sanyanse.common.ColorableNode;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;

import static org.sanyanse.common.Graph.ColorState.*;


public class ColorChoiceBacktrackColorer implements GraphColorer
{
  Graph _graph;
  int[] _colorChoices;

  final static int[] defaultColrChoices = new int[] { 1, 2, 3 };

  public ColorChoiceBacktrackColorer(Graph graph)
  {
    this(graph, defaultColrChoices);
  }

  public ColorChoiceBacktrackColorer(Graph graph, int[] colorChoices)
  {
    _graph = graph;
    _colorChoices = colorChoices;
  }

  @Override
  public ColoringResult call()
  {
    ColorableNode[] arr = _graph.Nodes;
    int[] cci = new int[_graph.NodeCount];

    boolean isColored = false;
    Thread cur = Thread.currentThread();

    int k = 0;

    while ((k >= 0) && !isColored && !cur.isInterrupted())
    {
      while ((cci[k] <= 2) && !cur.isInterrupted())
      {
        arr[k].Color = _colorChoices[cci[k]++];

        Graph.ColorState state = _graph.analyzeState();
        if (state == Invalid)
        {
          continue;
        }
        else if (state == PartialValid)
        {
          k++;
        }
        else if (state == Complete)
        {
          isColored = true;
          break;
        }
      }

      if (!isColored)
      {
        arr[k].Color = 0;
        cci[k] = 0;
        k--;
      }
    }

    ColoringResult result =
        isColored
        ? ColoringResult.createColoredGraphResult(_graph)
        : ColoringResult.createNotColorableResult();

    System.out.println("cc finished");

    return result;
  }
}
