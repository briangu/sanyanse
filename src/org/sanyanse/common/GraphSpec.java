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
  public double EdgeProbability;

  public GraphSpec(int nodeCnt) {
    this(nodeCnt, new ArrayList<String>(), new HashMap<String, Set<String>>(), -1.0);
  }

  public GraphSpec(int nodeCnt, double  p) {
    this(nodeCnt, new ArrayList<String>(), new HashMap<String, Set<String>>(), p);
  }

  public GraphSpec(int nodeCnt, List<String> nodes, Map<String, Set<String>> edges, double p) {
    NodeCount = nodeCnt;
    Nodes = nodes;
    Edges = edges;
    EdgeProbability = p;
  }

  public GraphSpec clone()
  {
    GraphSpec copy = new GraphSpec(NodeCount, EdgeProbability);

    for (String nodeId : Nodes)
    {
      copy.addNode(nodeId, new HashSet<String>(Edges.get(nodeId)));
    }

    return copy;
  }

  public void addNode(String id, String[] edges) {
    addNode(id, new HashSet<String>(Arrays.asList(edges)));
  }

  public void addNode(String id, Set<String> edges) {
    Nodes.add(id);
    Edges.put(id, edges);

    for (String neighborId : edges)
    {
      if (!Edges.containsKey(neighborId))
      {
        Edges.put(neighborId, new HashSet<String>());
      }
      Edges.get(neighborId).add(id);
    }
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
