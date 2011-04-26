package org.sanyanse.writer;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import org.sanyanse.common.ColorableNode;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphSpecWriter;
import org.sanyanse.common.Util;


public class FileGraphWriter implements GraphSpecWriter
{
  String _fileName;

  FileGraphWriter(String fileName) {
    _fileName = fileName;
  }

  @Override
  public void write(Graph graph)
  {
    try
    {
      FileWriter fstream = new FileWriter(_fileName);
      BufferedWriter writer = new BufferedWriter(fstream);

      writer.write(String.format("%s", graph.NodeCount));
      writer.write("\n");

      for (ColorableNode node : graph.Nodes) {
        writer.write(
            String.format(
                "%s:%s",
                node.Id,
                Util.join(Arrays.asList(node.Edges), ",")));
        writer.write("\n");
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

  public static FileGraphWriter create(String filename) {
    return new FileGraphWriter(filename);
  }
}
