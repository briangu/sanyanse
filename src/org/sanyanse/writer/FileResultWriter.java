package org.sanyanse.writer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.ColoringResultWriter;


/**
 * Created by IntelliJ IDEA. User: bguarrac Date: 4/14/11 Time: 10:27 AM To change this template use File | Settings |
 * File Templates.
 */
public class FileResultWriter implements ColoringResultWriter
{
  ColoringResult _result;

  FileResultWriter(ColoringResult result) {
    _result = result;
  }

  public void write() {
    // todo: write to file
  }

  public static FileResultWriter create(String filename, ColoringResult result) {
    return new FileResultWriter(result);
  }
}
