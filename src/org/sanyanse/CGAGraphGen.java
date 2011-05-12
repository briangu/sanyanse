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
    Integer nodeCnt = Integer.parseInt(args[0]);
    Double p = Double.parseDouble(args[1]);
    Integer iterCnt = Integer.parseInt(args[2]);
    Integer fileCnt = Integer.parseInt(args[3]);
    generateGraphs(nodeCnt, p, iterCnt, fileCnt);
  }

  public static void generateGraphs(int nodeCnt, double p, int iterCnt, int fileCnt)
  {
    for (int i = 0; i < fileCnt; i++)
    {
      GraphLoader loader = new CGAGraphGenerator(nodeCnt, p, iterCnt);
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
