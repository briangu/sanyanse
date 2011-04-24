package org.sanyanse.loader;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphBuilder;
import org.sanyanse.common.GraphLoader;


public class IIDFileLoader implements GraphLoader
{
  private String _filename;

  public Graph load()
  {
    int nodeCnt = -1;
    Map<String, Set<String>> buildMap = new HashMap<String, Set<String>>();

    GraphBuilder builder = null;

    try {
      FileInputStream fstream = new FileInputStream(_filename);
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;

      while ((strLine = br.readLine()) != null) {
        String[] parts = strLine.split(" ");
        if (strLine.startsWith("e")) {
          String nodeId = parts[1];
          String neighborId = parts[2];
          builder.addNode(nodeId);
          builder.addEdge(nodeId, neighborId);
        } else if (strLine.startsWith("p")) {
          nodeCnt = Integer.parseInt(parts[2]);
          builder = new GraphBuilder(nodeCnt);
        }
      }
      in.close();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

    if (nodeCnt == -1) {
      throw new IllegalArgumentException("file does not contain node count");
    }

    return builder.build();
  }

  // file format:
  // c Comment
  // p <node cnt> <?>
  // e <node id> <neighbor id>

  public static GraphLoader create(String filename)
  {
    IIDFileLoader loader = new IIDFileLoader();
    loader._filename = filename;
    return loader;
  }
}
