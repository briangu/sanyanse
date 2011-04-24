package org.sanyanse.colorer;


import org.sanyanse.common.ColorableNode;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;

import static org.sanyanse.common.Graph.ColorState.Complete;
import static org.sanyanse.common.Graph.ColorState.PartialValid;


public class BasicBacktrackColorer implements GraphColorer
{
  Graph _graph;

  public BasicBacktrackColorer(Graph graph)
  {
    _graph = graph;
  }

  @Override
  public ColoringResult call()
  {
    ColorableNode[] arr = _graph.Nodes;

    boolean isColored = false;

    int k = 0;

    while ((k >= 0) && !isColored)
    {
      while ((arr[k].Color <= 2))
      {
        arr[k].Color = arr[k].Color + 1;

        Graph.ColorState state = _graph.analyzeState();
        if (state == Complete)
        {
          isColored = true;
          break;
        }
        if (state == PartialValid)
        {
          k++;
        }
      }

      if (!isColored)
      {
        arr[k].Color = 0;
        k--;
      }
    }

    ColoringResult result =
        isColored
        ? ColoringResult.createColoredGraphResult(_graph)
        : ColoringResult.createNotColorableResult();

    return result;
  }
}
