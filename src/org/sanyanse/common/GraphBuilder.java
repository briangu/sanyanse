package org.sanyanse.common;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GraphBuilder
{
  public final int NodeCount;
  public final List<String> Nodes;
  public final Map<String, Set<String>> EdgeMap;
  public final double EdgeProbability;
  public final Map<String, Integer> ColorMap = new HashMap<String, Integer>();

  public GraphBuilder(int nodeCnt)
  {
    this(nodeCnt, -1);
  }

  public GraphBuilder(int nodeCnt, double p)
  {
    NodeCount = nodeCnt;
    Nodes = new ArrayList<String>(nodeCnt);
    EdgeMap = new HashMap<String, Set<String>>();
    EdgeProbability = p;
  }

  public void addNode(String nodeId)
  {
    Nodes.add(nodeId);
    EdgeMap.put(nodeId, new HashSet<String>());
  }

  public void addNode(String nodeId, int color)
  {
    addNode(nodeId);
    ColorMap.put(nodeId, color);
  }

  public void addEdge(String nodeId, String neighborId)
  {
    EdgeMap.get(nodeId).add(neighborId);
  }

  public void addEdge(String nodeId, String neighborId, int color)
  {
    addEdge(nodeId, neighborId);
    ColorMap.put(nodeId, color);
  }

  public void addNode(String id, String[] edges)
  {
    addNode(id, new HashSet<String>(Arrays.asList(edges)));
  }

  public void addNode(String id, Set<String> edges)
  {
    Nodes.add(id);
    EdgeMap.put(id, edges);

    for (String neighborId : edges)
    {
      if (!EdgeMap.containsKey(neighborId))
      {
        EdgeMap.put(neighborId, new HashSet<String>());
      }

      EdgeMap.get(neighborId).add(id);
    }
  }

  public void removeNode(String nodeId)
  {
    Nodes.remove(nodeId);
    EdgeMap.remove(nodeId);

    for (Set<String> edgeSet : EdgeMap.values())
    {
      edgeSet.remove(nodeId);
    }
  }

  public Graph build()
  {
    ColorableNode[] nodes = new ColorableNode[NodeCount];
    Map<String, ColorableNode> nodeMap = new HashMap<String, ColorableNode>(NodeCount);

    int k = 0;

    for (int i = 0; i < NodeCount; i++)
    {
      final String nodeId = Nodes.get(i);

      if (!nodeMap.containsKey(nodeId))
      {
        nodeMap.put(nodeId, createColorableNode(nodeId));
      }
      ColorableNode node = nodeMap.get(nodeId);
      nodes[k++] = node;

      Set<String> specEdges = EdgeMap.get(Nodes.get(i));
      ColorableNode[] edges = new ColorableNode[specEdges.size()];

      int j = 0;
      for (String neighborId : specEdges)
      {
        if (!nodeMap.containsKey(neighborId))
        {
          ColorableNode newNode = createColorableNode(neighborId);
          nodeMap.put(neighborId, newNode);
        }
        edges[j++] = nodeMap.get(neighborId);
      }

      node.Edges = edges;
    }

    Graph graph = new Graph(NodeCount, nodes, nodeMap, EdgeProbability, EdgeMap);

    return graph;
  }

  private ColorableNode createColorableNode(String nodeId)
  {
    return (ColorMap.containsKey(nodeId))
      ? new ColorableNode(nodeId, ColorMap.get(nodeId))
      : new ColorableNode(nodeId);
  }

  public static GraphBuilder createFrom(Graph graph)
  {
    GraphBuilder builder = new GraphBuilder(graph.NodeCount, graph.EdgeProbability);

    for (ColorableNode node : graph.Nodes)
    {
      builder.addNode(node.Id, node.Color);
      for (ColorableNode neighbor : node.Edges)
      {
        builder.addEdge(node.Id, neighbor.Id, neighbor.Color);
      }
    }

    return builder;
  }
}
