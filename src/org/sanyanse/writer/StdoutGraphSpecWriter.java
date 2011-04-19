package org.sanyanse.writer;


import org.sanyanse.common.GraphSpec;
import org.sanyanse.common.GraphSpecWriter;
import org.sanyanse.common.Util;


public class StdoutGraphSpecWriter implements GraphSpecWriter
{
  @Override
  public void write(GraphSpec graphSpec)
  {
    System.out.println(String.format("nodeCnt: %s", graphSpec.NodeCount));

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
