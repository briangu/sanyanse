package org.sanyanse.common;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class GraphBuilder
{
  public final int NodeCount;
  public final double EdgeProbability;
  public final ColorableNode[] Nodes;
  public final Map<String, NodeInfo> NodeMap;
  private int _index = 0;

  public GraphBuilder(int nodeCnt)
  {
    this(nodeCnt, -1);
  }

  class NodeInfo
  {
    public ColorableNode Node;
    public Set<String> EdgeSet;
    public int Index;
    public NodeInfo(ColorableNode node, Set<String> edgeSet, int index)
    {
      Node = node;
      EdgeSet = edgeSet;
      Index = index;
    }
  }

  public GraphBuilder(int nodeCnt, double p)
  {
    NodeCount = nodeCnt;
    EdgeProbability = p;
    Nodes = new ColorableNode[nodeCnt];
    NodeMap = new HashMap<String, NodeInfo>(nodeCnt);
  }

  public ColorableNode addNode(String nodeId, String[] edges)
  {
    return addNode(nodeId, new HashSet<String>(Arrays.asList(edges)));
  }

  public ColorableNode addNode(String nodeId, Set<String> edges, int color)
  {
    ColorableNode node = addNode(nodeId, edges);
    node.Color = color;
    return node;
  }

  public ColorableNode addNode(String nodeId, Set<String> edges)
  {
    ColorableNode node = new ColorableNode(nodeId);
    Nodes[_index] = node;
    NodeMap.put(node.Id, new NodeInfo(node, edges, _index));
    _index++;
    return node;
  }

  public Graph build()
  {
    for (int i = 0; i < NodeCount; i++)
    {
      final NodeInfo info = NodeMap.get(Nodes[i].Id);
      info.Node.Edges = new int[info.EdgeSet.size()];

      // TODO: we may wan to sort the edges by degree
      int j = 0;
      for (String id : info.EdgeSet)
      {
        info.Node.Edges[j++] = NodeMap.get(id).Index;
      }
    }

    Graph graph = new Graph(NodeCount, EdgeProbability, Nodes);

    return graph;
  }
}
