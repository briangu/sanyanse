package org.sanyanse;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.ColoringResultWriter;


/**
 * Created by IntelliJ IDEA. User: bguarrac Date: 4/14/11 Time: 10:27 AM To change this template use File | Settings |
 * File Templates.
 */
public class StdoutResultWriter implements ColoringResultWriter
{
  ColoringResult _result;

  StdoutResultWriter(ColoringResult result) {
    _result = result;
  }

  public void write() {

    System.out.print(String.format("%s", Boolean.toString(_result.IsColored)));

    if (_result.IsColored) {
      for (ColoringResult.Coloring coloring : _result.Colorings) {
        System.out.println(String.format("%s:%s", coloring.NodeId, coloring.Color));
      }
    }
  }

  public static StdoutResultWriter create(String filename, ColoringResult result) {
    return new StdoutResultWriter(result);
  }
}
