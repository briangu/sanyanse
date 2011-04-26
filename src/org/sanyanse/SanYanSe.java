package org.sanyanse;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.sanyanse.colorer.BacktrackColorer;
import org.sanyanse.colorer.CentralityBacktrackColorer;
import org.sanyanse.colorer.MultiColorer;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.StopWatch;
import org.sanyanse.loader.LinkedInFileLoader;
import org.sanyanse.loader.RandomGraphLoader;
import org.sanyanse.writer.FileResultWriter;
import org.sanyanse.writer.StdoutGraphSpecWriter;


/**
 * SanYanSe: 3 Coloring Graph coloring framework
 *
 * Brian Guarraci
 *
 */
public class SanYanSe
{
  static Boolean _debug = false;

  static class SimpleThreadFactory implements ThreadFactory, Thread.UncaughtExceptionHandler
  {
    public Thread newThread(Runnable r) {
      Thread t = new Thread(r);
      t.setUncaughtExceptionHandler(this);
      return t;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable)
    {
      System.out.print(throwable.toString());
    }
  }

  public static void main(String[] args)
  {
    if (args.length == 0)
    {
      System.out.println("usage: sanyanse <graph filename>");
      return;
    }

    String readFile = args[args.length - 1];
    String graphName = new File(readFile).getName();

    GraphLoader loader = LinkedInFileLoader.create(readFile);
    loader = new RandomGraphLoader(8, 0.30);
    Graph graph = loader.load();
    if (graph == null)
    {
      System.out.println("failed to load graph");
      return;
    }

    StopWatch stopWatch = null;

    if (_debug)
    {
      System.out.println("graph spec");
      StdoutGraphSpecWriter.create().write(graph);
      System.out.println();

      stopWatch = new StopWatch();
      stopWatch.start();
    }

    ColoringResult result = processGraph(graph);
    if (result == null)
    {
      System.out.println("failed to color graph");
      return;
    }

    if (_debug)
    {
      stopWatch.stop();
      System.out.println(String.format("elapsed time: %s", stopWatch.getDuration()));
      System.out.println();
    }

    String outfileName = String.format("%s_%s_out", "sanyanse", graphName);
    FileResultWriter.create(outfileName).write(result, graph);
  }

  private static ColoringResult processGraph(Graph graph)
  {
    ColoringResult result = null;

    GraphColorer colorer;

    List<GraphColorer> colorers = new ArrayList<GraphColorer>();

    colorers.add(new BacktrackColorer(graph));
    colorers.add(new CentralityBacktrackColorer(graph));

    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                                                            new SimpleThreadFactory());
    colorer = MultiColorer.create(executor, colorers);

    try
    {
      result = colorer.call();
      if (result == null)
      {
        result = ColoringResult.createNotColorableResult();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    executor.shutdown();

    return result;
  }
}
