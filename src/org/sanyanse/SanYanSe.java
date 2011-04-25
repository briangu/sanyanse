package org.sanyanse;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.sanyanse.colorer.BasicBacktrackColorer;
import org.sanyanse.colorer.CompactGeneticAlgorithmColorer;
import org.sanyanse.colorer.MultiColorer;
import org.sanyanse.common.*;
import org.sanyanse.loader.IIDFileLoader;
import org.sanyanse.loader.LinkedInFileLoader;
import org.sanyanse.loader.RandomGraphLoader;
import org.sanyanse.writer.StdoutGraphSpecWriter;
import org.sanyanse.writer.StdoutResultWriter;


public class SanYanSe
{
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
    GraphLoader loader;

    String graphName = args.length > 0 ? args[0] : "memory";

    //= LinkedInFileLoader.create(args[0]);
//    loader = new RandomGraphLoader(2048, 0.30);
    loader = IIDFileLoader.create("/home/brian/src/IID/250/4.00/graph_2835");
//    loader = LinkedInFileLoader.create("/home/brian/git/sanyanse/test/Sample3Colorable.3color");
//    loader = new PetersenLoader();
    Graph graph = loader.load();
    if (graph == null)
    {
      System.out.println("failed to load graph");
      return;
    }

//    processGraph(graph, graphName);
    graph.SortByMetric(graph.Decomposition.getCentrality(graph));
    processGraph(graph, graphName);
  }

  private static void processGraph(Graph graph, String graphName)
  {
    System.out.println("graph spec");
    StdoutGraphSpecWriter.create().write(graph);
    System.out.println();

    List<GraphColorer> colorers = new ArrayList<GraphColorer>();
    colorers.add(new BasicBacktrackColorer(graph));
//    colorers.add(CompactGeneticAlgorithmColorer.create(graph, 0.05));
//    colorers.add(new SpectralColorer(graph, 0.35));

    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new SimpleThreadFactory());
    MultiColorer mc = MultiColorer.create(executor, colorers);

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    try
    {
      ColoringResult result = mc.call();
      if (result == null)
      {
        result = ColoringResult.createNotColorableResult();
      }

      stopWatch.stop();
      System.out.println(String.format("elapsed time: %s", stopWatch.getDuration()));
      System.out.println();

      String outfileName = String.format("%s_%s_out", "sanyanse", graphName);
      StdoutResultWriter.create().write(result);
    }
    catch (Exception e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    executor.shutdown();
  }
}
