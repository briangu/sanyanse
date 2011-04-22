package org.sanyanse.loader;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.GraphSpec;


public class LinkedInFileLoader implements GraphLoader
{
  private String _filename;

  public GraphSpec load()
  {
    int nodeCnt = -1;
    Map<String, Set<String>> buildMap = new HashMap<String, Set<String>>();

    try {
      FileInputStream fstream = new FileInputStream(_filename);
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));

      nodeCnt = Integer.parseInt(br.readLine());

      String strLine;

      while ((strLine = br.readLine()) != null) {
        String[] parts = strLine.split(":");
        String nodeId = parts[0];
        String neighborIdList = parts[1];

        for (String neighborId : neighborIdList.split(","))
        {
          if (!buildMap.containsKey(nodeId)) {
            buildMap.put(nodeId, new HashSet<String>());
          }
          if (!buildMap.containsKey(neighborId))
          {
            buildMap.put(neighborId, new HashSet<String>());
          }
          buildMap.get(nodeId).add(neighborId);
          buildMap.get(neighborId).add(nodeId);
        }
      }
      in.close();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

    if (nodeCnt == -1) {
      throw new IllegalArgumentException("file does not contain node count");
    }

    if (nodeCnt != buildMap.size())
    {
      System.out.println(String.format("Warning: nodeCnt != buildMap: %s %s", nodeCnt, buildMap.size()));
    }

    GraphSpec spec = new GraphSpec(buildMap.size());

    for (String nodeId : buildMap.keySet()) {
      spec.addNode(nodeId, buildMap.get(nodeId));
    }

    return spec;
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
