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
  public final double EdgeProbability;
  public final List<ColorableNode> Nodes;
  public final Map<String, GraphNodeInfo> NodeMap;

  public GraphBuilder(int nodeCnt)
  {
    this(nodeCnt, -1);
  }

  public GraphBuilder(int nodeCnt, double p)
  {
    NodeCount = nodeCnt;
    EdgeProbability = p;
    Nodes = new ArrayList<ColorableNode>(nodeCnt);
    NodeMap = new HashMap<String, GraphNodeInfo>(nodeCnt);
  }

  public GraphNodeInfo addNode(String nodeId)
  {
    GraphNodeInfo info;

    if (NodeMap.containsKey(nodeId))
    {
      info = NodeMap.get(nodeId);
    }
    else
    {
      info = new GraphNodeInfo(new ColorableNode(nodeId));
      NodeMap.put(nodeId, info);
    }

    Nodes.add(info.Node);

    return info;
  }

  public void addNode(String nodeId, int color)
  {
    GraphNodeInfo info = addNode(nodeId);
    info.Node.Color = color;
  }

  public GraphNodeInfo addEdge(String nodeId, String neighborId)
  {
    if (!NodeMap.containsKey(neighborId))
    {
      NodeMap.put(neighborId, new GraphNodeInfo(new ColorableNode(neighborId)));
    }
    GraphNodeInfo neighborInfo = NodeMap.get(neighborId);

    NodeMap.get(nodeId).EdgeSet.add(neighborInfo.Node);

    return neighborInfo;
  }

  public void addEdge(String nodeId, String neighborId, int color)
  {
    GraphNodeInfo neighborInfo = addEdge(nodeId, neighborId);
    neighborInfo.Node.Color = color;
  }

  public GraphNodeInfo addNode(String nodeId, String[] edges)
  {
    return addNode(nodeId, new HashSet<String>(Arrays.asList(edges)));
  }

  public GraphNodeInfo addNode(String nodeId, Set<String> edges)
  {
    GraphNodeInfo info = addNode(nodeId);
    for (String neighborId : edges)
    {
      addEdge(nodeId, neighborId);
    }
    return info;
  }

  public void removeNode(String nodeId)
  {
    if (!NodeMap.containsKey(nodeId)) return;

    GraphNodeInfo info = NodeMap.get(nodeId);

    Nodes.remove(info.Node);
    NodeMap.remove(nodeId);

    for (GraphNodeInfo mapInfo : NodeMap.values())
    {
      mapInfo.EdgeSet.remove(info.Node);
    }
  }

  public Graph build()
  {
    ColorableNode[] nodes = Nodes.toArray(new ColorableNode[NodeCount]);

    for (int i = 0; i < NodeCount; i++)
    {
      final ColorableNode node = nodes[i];
      final GraphNodeInfo info = NodeMap.get(node.Id);
      info.Index = i;
      node.Edges = info.EdgeSet.toArray(new ColorableNode[info.EdgeSet.size()]);
    }

    Graph graph = new Graph(NodeCount, EdgeProbability, nodes, NodeMap);

    return graph;
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
