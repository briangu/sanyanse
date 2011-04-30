package org.sanyanse.writer;


import java.util.Arrays;
import org.sanyanse.common.Vertex;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphSpecWriter;
import org.sanyanse.common.Util;


public class StdoutGraphSpecWriter implements GraphSpecWriter
{
  @Override
  public void write(Graph graph)
  {
    System.out.println(String.format("nodeCnt: %s", graph.NodeCount));

/*
    Collections.sort(graphSpec.Vertices,
                      new Comparator<String>()
                      {
                        @Override
                        public int compare(String s, String s1)
                        {
                          return Integer.valueOf(s).compareTo(Integer.valueOf(s1));
                        }
                      });
*/

    for (Vertex node : graph.Vertices) {
      System.out.println(
          String.format(
              "%s:%s",
              node.Id,
              Util.join(Arrays.asList(node.Edges), ",")));
    }
  }

  public static StdoutGraphSpecWriter create() {
    return new StdoutGraphSpecWriter();
  }
}
