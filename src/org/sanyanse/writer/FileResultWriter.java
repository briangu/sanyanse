package org.sanyanse.writer;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.sanyanse.common.ColorableNode;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.ColoringResultWriter;
import org.sanyanse.common.Graph;
import org.sanyanse.common.Util;


public class FileResultWriter implements ColoringResultWriter
{
  String _fileName;

  FileResultWriter(String fileName) {
    _fileName = fileName;
  }

  public void write(ColoringResult result) {
    try
    {
      FileWriter fstream = new FileWriter(_fileName);
      BufferedWriter writer = new BufferedWriter(fstream);

      writer.write(String.format("%s", Boolean.toString(result.IsColored)));
      writer.write("\n");

      if (result.IsColored)
      {
        Graph graph = result.Graph;
        Map<String, ColorableNode> nodeMap = graph.NodeMap;

        for (int i = 1; i <= graph.NodeCount; i++)
        {
          String nodeId = Util.getNodeName(i);
          ColorableNode node = nodeMap.get(nodeId);
          writer.write(
              String.format(
                  "%s:%s",
                  node.Id,
                  node.Color));
          writer.write("\n");
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

