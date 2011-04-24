package org.sanyanse.writer;


import org.sanyanse.common.ColorableNode;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.ColoringResultWriter;


public class StdoutResultWriter implements ColoringResultWriter
{
  public void write(ColoringResult result) {

    System.out.println(String.format("%s", Boolean.toString(result.IsColored)));

    if (result.IsColored) {
      for (ColorableNode node : result.Graph.Nodes) {
        System.out.println(String.format("%s:%s", node.Id, node.Color));
      }
    }
  }

  public static StdoutResultWriter create() {
    return new StdoutResultWriter();
  }
}
