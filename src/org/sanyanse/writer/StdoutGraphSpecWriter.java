package org.sanyanse.writer;


import org.sanyanse.common.GraphSpec;
import org.sanyanse.common.GraphSpecWriter;
import org.sanyanse.common.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class StdoutGraphSpecWriter implements GraphSpecWriter
{
  @Override
  public void write(GraphSpec graphSpec)
  {
    System.out.println(String.format("nodeCnt: %s", graphSpec.NodeCount));

/*
    Collections.sort(graphSpec.Nodes,
                      new Comparator<String>()
                      {
                        @Override
                        public int compare(String s, String s1)
                        {
                          return Integer.valueOf(s).compareTo(Integer.valueOf(s1));
                        }
                      });
*/

    for (String nodeId : graphSpec.Nodes) {
      System.out.println(
          String.format(
              "%s:%s",
              nodeId,
              Util.join(graphSpec.Edges.get(nodeId), ",")));
    }
  }

  public static StdoutGraphSpecWriter create() {
    return new StdoutGraphSpecWriter();
  }
}
