package org.sanyanse.writer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.ColoringResultWriter;


/**
 * Created by IntelliJ IDEA. User: bguarrac Date: 4/14/11 Time: 10:27 AM To change this template use File | Settings |
 * File Templates.
 */
public class StdoutResultWriter implements ColoringResultWriter
{
  public void write(ColoringResult result) {

    System.out.println(String.format("%s", Boolean.toString(result.IsColored)));

    if (result.IsColored) {
      for (ColoringResult.Coloring coloring : result.Colorings) {
        System.out.println(String.format("%s:%s", coloring.NodeId, coloring.Color));
      }
    }
  }

  public static StdoutResultWriter create() {
    return new StdoutResultWriter();
  }
}
