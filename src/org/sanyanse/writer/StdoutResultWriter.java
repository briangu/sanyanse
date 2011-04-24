package org.sanyanse.writer;


import org.sanyanse.common.Coloring;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.ColoringResultWriter;


public class StdoutResultWriter implements ColoringResultWriter
{
  public void write(ColoringResult result) {

    System.out.println(String.format("%s", Boolean.toString(result.IsColored)));

    if (result.IsColored) {
      for (Coloring coloring : result.Colorings) {
        System.out.println(String.format("%s:%s", coloring.NodeId, coloring.Color));
      }
    }
  }

  public static StdoutResultWriter create() {
    return new StdoutResultWriter();
  }
}
