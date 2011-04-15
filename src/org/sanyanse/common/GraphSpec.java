package org.sanyanse.common;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GraphSpec
{
  public int NodeCount;
  public List<String> Nodes;
  public Map<String, List<String>> Edges;

  public GraphSpec(int nodeCnt) {
    NodeCount = nodeCnt;
    Nodes = new ArrayList<String>();
    Edges = new HashMap<String, List<String>>();
  }

  public GraphSpec(int nodeCnt, List<String> nodes, Map<String, List<String>> edges) {
    this(nodeCnt);
    Nodes = nodes;
    Edges = edges;
  }

  public void addNode(String id, String[] edges) {
    addNode(id, Arrays.asList(edges));
  }

  public void addNode(String id, List<String> edges) {
    Nodes.add(id);
    Edges.put(id, edges);
  }
}
