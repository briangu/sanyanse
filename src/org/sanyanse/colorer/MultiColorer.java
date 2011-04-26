package org.sanyanse.colorer;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;


public class MultiColorer implements GraphColorer
{
  List<GraphColorer> _colorers;
  ExecutorService _exec;

  private MultiColorer(ExecutorService e, List<GraphColorer> colorers)
  {
    _exec = e;
    _colorers = colorers;
  }

  @Override
  public ColoringResult call()
      throws Exception
  {
    ColoringResult result = color(_exec, _colorers);
    return result;
  }

  private static ColoringResult color(ExecutorService e, List<GraphColorer> colorers)
      throws InterruptedException
  {
    CompletionService<ColoringResult> ecs = new ExecutorCompletionService<ColoringResult>(e);

    int n = colorers.size();

    List<Future<ColoringResult>> futures = new ArrayList<Future<ColoringResult>>(n);

    ColoringResult result = null;

    try
    {
      for (Callable<ColoringResult> s : colorers)
      {
        futures.add(ecs.submit(s));
      }

      for (int i = 0; i < n; ++i)
      {
        try
        {
          ColoringResult r = ecs.take().get();
          if (r != null)
          {
            result = r;
            break;
          }
        }
        catch (ExecutionException ignore)
        {
        }
      }
    }
    finally
    {
      for (Future<ColoringResult> f : futures)
      {
        f.cancel(true);
      }
    }

    return result;
  }

  public static MultiColorer create(ExecutorService e, List<GraphColorer> colorers)
  {
    return new MultiColorer(e, colorers);
  }
}
