package org.sanyanse.writer;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.sanyanse.common.Vertex;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.ColoringResultWriter;
import org.sanyanse.common.Graph;


public class FileResultWriter implements ColoringResultWriter
{
  String _fileName;

  FileResultWriter(String fileName) {
    _fileName = fileName;
  }

  public void write(ColoringResult result, Graph origGraph) {
    try
    {
      FileWriter fstream = new FileWriter(_fileName);
      BufferedWriter writer = new BufferedWriter(fstream);

      writer.write(String.format("%s\n", Boolean.toString(result.IsColored)));

      if (result.IsColored)
      {
        if (result.Graph != null)
        {
          Graph coloredGraph = result.Graph;

          for (Vertex node : coloredGraph.OriginalVertices)
          {
            writer.write(
                String.format(
                    "%s:%s\n",
                    node.Id,
                    node.Color));
          }
        }
        else
        {
          Map<Vertex, Integer> coloredMap = result.ColorMap;

          for (Vertex node : origGraph.OriginalVertices)
          {
            writer.write(
                String.format(
                    "%s:%s\n",
                    node.Id,
                    coloredMap.get(node) + 1));
          }
        }
      }

      writer.close();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    catch (IOException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  public static FileResultWriter create(String filename) {
    return new FileResultWriter(filename);
  }
}

