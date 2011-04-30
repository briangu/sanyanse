package org.sanyanse.loader;


import java.io.BufferedReader;
import java.io.FileReader;
import org.sanyanse.common.ColorableNode;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphBuilder;
import org.sanyanse.common.GraphLoader;


public class LinkedInFileLoader implements GraphLoader
{
  private String _filename;

  public Graph load()
  {
    int nodeCnt = -1;

    GraphBuilder builder = null;

    try {
      FileReader fstream = new FileReader(_filename);
      BufferedReader br = new BufferedReader(fstream);

      nodeCnt = Integer.parseInt(br.readLine());

      if (nodeCnt == 0) {
        return new Graph(0, 0, new ColorableNode[0]);
      }

      builder = new GraphBuilder(nodeCnt);

      String strLine;

      while ((strLine = br.readLine()) != null) {
        String[] parts = strLine.split(":");
        if (parts.length <= 1) break;
        builder.addNode(parts[0], parts[1].split(","));
      }

      br.close();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

    if (nodeCnt == -1) {
      throw new IllegalArgumentException("file does not contain node count");
    }
    if (builder == null) {
      throw new IllegalArgumentException("could not load file");
    }

    Graph graph = builder.build();

    return graph;
  }

  // file format:
  // <CNT>
  // <NODE ID>:<EDGE LIST>
  //
  // EDGE LIST: <NODE ID>,<NODE ID>, ... ,<NODE ID>
  public static GraphLoader create(String filename) {
    LinkedInFileLoader loader = new LinkedInFileLoader();
    loader._filename = filename;
    return loader;
  }
}
