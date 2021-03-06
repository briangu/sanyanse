package org.sanyanse.writer;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.ColoringResultWriter;
import org.sanyanse.common.Graph;
import org.sanyanse.common.Vertex;


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
        Graph coloredGraph = result.Graph;

        for (int i = 0; i < coloredGraph.NodeCount; i++)
        {
          writer.write(
              String.format(
                  "%s:%s\n",
                  coloredGraph.VertexIds[i],
                  coloredGraph.OriginalVertices[i].Color));
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

