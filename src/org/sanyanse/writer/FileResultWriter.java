package org.sanyanse.writer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.ColoringResultWriter;


public class FileResultWriter implements ColoringResultWriter
{
  String _fileName;

  FileResultWriter(String fileName) {
    _fileName = fileName;
  }

  public void write(ColoringResult result) {
    // todo: write to file
  }

  public static FileResultWriter create(String filename) {
    return new FileResultWriter(filename);
  }
}

