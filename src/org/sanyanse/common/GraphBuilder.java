package org.sanyanse.common;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Jama.Matrix;


public class GraphBuilder
{
  public final int NodeCount;
  public final double EdgeProbability;
  public final List<ColorableNode> Nodes;
  public final Map<String, ColorableNode> NodeMap;
  public final Map<String, Set<ColorableNode>> EdgeMap;
  public final Map<String, Integer> IndexMap;

  public GraphBuilder(int nodeCnt)
  {
    this(nodeCnt, -1);
  }

  public GraphBuilder(int nodeCnt, double p)
  {
    NodeCount = nodeCnt;
    EdgeProbability = p;
    Nodes = new ArrayList<ColorableNode>(nodeCnt);
    NodeMap = new HashMap<String, ColorableNode>(nodeCnt);
    EdgeMap = new HashMap<String, Set<ColorableNode>>(nodeCnt);
    IndexMap = new HashMap<String, Integer>(nodeCnt);
  }

  public ColorableNode addNode(String nodeId)
  {
    ColorableNode node;

    if (NodeMap.containsKey(nodeId))
    {
      node = NodeMap.get(nodeId);
    }
    else
    {
      node = new ColorableNode(nodeId);
      NodeMap.put(nodeId, node);
    }

    IndexMap.put(nodeId, Nodes.size());
    Nodes.add(node);
    EdgeMap.put(nodeId, new HashSet<ColorableNode>());

    return node;
  }

  public ColorableNode addNode(String nodeId, int color)
  {
    ColorableNode node = addNode(nodeId);
    node.Color = color;
    return node;
  }

  public ColorableNode addEdge(String nodeId, String neighborId)
  {
    if (!NodeMap.containsKey(neighborId))
    {
      NodeMap.put(neighborId, new ColorableNode(neighborId));
    }
    ColorableNode neighborNode = NodeMap.get(neighborId);

    EdgeMap.get(nodeId).add(neighborNode);

    return neighborNode;
  }

  public ColorableNode addEdge(String nodeId, String neighborId, int color)
  {
    ColorableNode neighborNode = addEdge(nodeId, neighborId);
    neighborNode.Color = color;
    return neighborNode;
  }

  public ColorableNode addNode(String nodeId, String[] edges)
  {
    return addNode(nodeId, new HashSet<String>(Arrays.asList(edges)));
  }

  public ColorableNode addNode(String nodeId, Set<String> edges)
  {
    ColorableNode node = addNode(nodeId);
    for (String neighborId : edges)
    {
      addEdge(nodeId, neighborId);
    }
    return node;
  }

  public void removeNode(String nodeId)
  {
    if (!NodeMap.containsKey(nodeId)) return;

    ColorableNode node = NodeMap.get(nodeId);

    Nodes.remove(nodeId);
    EdgeMap.remove(nodeId);
    NodeMap.remove(nodeId);

    for (Set<ColorableNode> edgeSet : EdgeMap.values())
    {
      edgeSet.remove(node);
    }
  }

  public Graph build()
  {
    ColorableNode[] nodes = Nodes.toArray(new ColorableNode[NodeCount]);
    Matrix adjacency = new Matrix(NodeCount, NodeCount);
    Matrix degree = new Matrix(NodeCount, NodeCount);

    for (int i = 0; i < NodeCount; i++)
    {
      final ColorableNode node = nodes[i];

      Set<ColorableNode> edgeSet = EdgeMap.get(node.Id);
      final ColorableNode[] edges = edgeSet.toArray(new ColorableNode[edgeSet.size()]);
      node.Edges = edges;

      degree.set(i, i, edges.length);

      for (ColorableNode neighbor : edgeSet)
      {
        adjacency.set(i, IndexMap.get(neighbor.Id), 1);
      }
    }

    GraphDecomposition decomposition = new GraphDecomposition(adjacency, degree);

    Graph graph = new Graph(NodeCount, nodes, NodeMap, EdgeProbability, EdgeMap, decomposition);

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
