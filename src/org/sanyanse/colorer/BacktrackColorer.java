/**
 * SanYanSe
 *
 * @Author Brian Guarraci
 *
 */
package org.sanyanse.colorer;


import org.sanyanse.common.Vertex;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;

import static org.sanyanse.common.Graph.ColorState.Complete;
import static org.sanyanse.common.Graph.ColorState.Invalid;
import static org.sanyanse.common.Graph.ColorState.PartialValid;


public class BacktrackColorer implements GraphColorer
{
  Graph _graph;

  public BacktrackColorer(Graph graph)
  {
    _graph = graph;
  }

  @Override
  public ColoringResult call()
  {
    Vertex[] arr = _graph.Vertices;

    boolean isColored = false;
    Thread cur = Thread.currentThread();

    int k = 0;

    while ((k >= 0) && !isColored && !cur.isInterrupted())
    {
      while ((arr[k].Color <= 2) && !cur.isInterrupted())
      {
        arr[k].Color += 1;

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
