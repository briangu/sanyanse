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

      writer.write(String.format("%s", Boolean.toString(result.IsColored)));
      writer.write("\n");

      if (result.IsColored)
      {
        Graph coloredGraph = result.Graph;
        Map<String, ColorableNode> nodeMap = coloredGraph.NodeMap;

        for (ColorableNode uncoloredNode : origGraph.Nodes)
        {
          ColorableNode node = nodeMap.get(uncoloredNode.Id);
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

