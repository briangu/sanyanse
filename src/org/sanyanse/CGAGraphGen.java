package org.sanyanse;


import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.GraphSpecWriter;
import org.sanyanse.loader.CGAGraphGenerator;
import org.sanyanse.writer.FileGraphWriter;


public class CGAGraphGen
{
  public static void main(String[] args)
  {
    Integer nodeCnt = Integer.parseInt(args[args.length-5]);
    Float p = Float.parseFloat(args[args.length-4]);
    Integer iterCnt = Integer.parseInt(args[args.length-3]);
    Integer fileCnt = Integer.parseInt(args[args.length-2]);
    Integer maxWorkers = Integer.parseInt(args[args.length-1]);
    generateGraphs(nodeCnt, p, iterCnt, fileCnt, maxWorkers);
  }

  public static void generateGraphs(int nodeCnt, float p, int iterCnt, int fileCnt, int maxWorkers)
  {
    for (int i = 0; i < fileCnt; i++)
    {
      GraphLoader loader = new CGAGraphGenerator(nodeCnt, p, iterCnt, maxWorkers);
      Graph graph = loader.load();
      if (graph == null)
      {
        System.out.println("failed to load graph");
        return;
      }

      GraphSpecWriter writer = FileGraphWriter.create(String.format("out_%s_%s.col", nodeCnt, i));
      writer.write(graph);
    }
  }
}
