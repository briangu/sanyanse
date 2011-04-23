package org.sanyanse.common;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GraphSpec
{
  public int NodeCount;
  public List<String> Nodes;
  public Map<String, Set<String>> Edges;

  public GraphSpec(int nodeCnt) {
    NodeCount = nodeCnt;
    Nodes = new ArrayList<String>();
    Edges = new HashMap<String, Set<String>>();
  }

  public GraphSpec(int nodeCnt, List<String> nodes, Map<String, Set<String>> edges) {
    this(nodeCnt);
    Nodes = nodes;
    Edges = edges;
  }

  public void addNode(String id, String[] edges) {
    addNode(id, new HashSet<String>(Arrays.asList(edges)));
  }

  public void addNode(String id, Set<String> edges) {
    Nodes.add(id);
    Edges.put(id, edges);
  }

  public GraphSpec clone()
  {
    GraphSpec copy = new GraphSpec(NodeCount);

    for (String nodeId : Nodes)
    {
      copy.addNode(nodeId, new HashSet<String>(Edges.get(nodeId)));
    }

    return copy;
  }

  public void removeNode(String nodeId)
  {
    Nodes.remove(nodeId);
    Edges.remove(nodeId);

    for (Set<String> edges : Edges.values())
    {
      edges.remove(nodeId);
    }
  }
}
