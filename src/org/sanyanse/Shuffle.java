package org.sanyanse;


import java.util.Arrays;
import java.util.Collections;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphLoader;
import org.sanyanse.loader.LinkedInFileLoader;
import org.sanyanse.writer.FileGraphWriter;


/**
 * SanYanSe: 3 Coloring Graph coloring framework
 *
 * Brian Guarraci
 *
 */
public class Shuffle
{
  public static void main(String[] args)
  {
    if (args.length == 0)
    {
      System.out.println("usage: sanyanse <graph filename>");
      return;
    }

    String readFile = args[args.length - 2];
    String outfile = args[args.length - 1];

    System.out.println(readFile);
    System.out.println(outfile);

    GraphLoader loader = LinkedInFileLoader.create(readFile);
    Graph graph = loader.load();
    if (graph == null)
    {
      System.out.println("failed to load graph");
      return;
    }

    shuffle(graph);

    FileGraphWriter.create(outfile).write(graph);
  }

  public static void shuffle(Graph graph)
  {
    if (graph.NodeCount == 0) return;
    Collections.shuffle(Arrays.asList(graph.Vertices));
  }
}
