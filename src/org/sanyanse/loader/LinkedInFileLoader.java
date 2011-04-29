package org.sanyanse.loader;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
      FileInputStream fstream = new FileInputStream(_filename);
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));

      nodeCnt = Integer.parseInt(br.readLine());

      builder = new GraphBuilder(nodeCnt);

      String strLine;

      while ((strLine = br.readLine()) != null) {
        String[] parts = strLine.split(":");
        if (parts.length <= 1) break;

        String nodeId = parts[0];
        String neighborIdList = parts[1];

        builder.addNode(nodeId, neighborIdList.split(","));
      }

      in.close();
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
