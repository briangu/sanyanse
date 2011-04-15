package org.sanyanse;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;


public class MultiColorer implements GraphColorer
{
  List<GraphColorer> _colorers;

  private MultiColorer(List<GraphColorer> colorers)
  {
    _colorers = colorers;
  }

  @Override
  public ColoringResult call()
      throws Exception
  {
    Executor e = Executors.newCachedThreadPool();
    ColoringResult result = color(e, _colorers);
    return result;
  }

  private static ColoringResult color(Executor e, List<GraphColorer> colorers)
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

  public static MultiColorer create(List<GraphColorer> colorers)
  {
    return new MultiColorer(colorers);
  }
}
