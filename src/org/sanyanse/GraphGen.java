package org.sanyanse;


import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.GraphSpecWriter;
import org.sanyanse.loader.RandomGraphLoader;
import org.sanyanse.writer.FileGraphWriter;


public class GraphGen
{
  public static void main(String[] args)
  {
    Integer nodeCnt = Integer.parseInt(args[0]);
    Double p = Double.parseDouble(args[1]);
    Integer iterCnt = Integer.parseInt(args[2]);
    generateGraphs(nodeCnt, p, iterCnt);
  }

  public static void generateGraphs(int nodeCnt, double p, int iterCnt)
  {
    for (int i = 0; i < iterCnt; i++)
    {
      GraphLoader loader = new RandomGraphLoader(nodeCnt, p);
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
