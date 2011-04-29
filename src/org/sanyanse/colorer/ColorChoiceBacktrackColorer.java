package org.sanyanse.colorer;


import org.sanyanse.common.ColorableNode;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;

import static org.sanyanse.common.Graph.ColorState.*;


public class ColorChoiceBacktrackColorer implements GraphColorer
{
  Graph _graph;

  public ColorChoiceBacktrackColorer(Graph graph)
  {
    _graph = graph;
  }

  @Override
  public ColoringResult call()
  {
    ColorableNode[] arr = _graph.Nodes;

    boolean isColored = false;

    int k = 0;

    while ((k >= 0) && !isColored && !Thread.currentThread().isInterrupted())
    {
      while ((arr[k].ColorChoiceIndex <= 2))
      {
        arr[k].Color = arr[k].ColorChoices[arr[k].ColorChoiceIndex++];

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
        arr[k].ColorChoiceIndex = 0;
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
