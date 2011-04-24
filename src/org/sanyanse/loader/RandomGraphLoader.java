package org.sanyanse.loader;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphBuilder;
import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.Util;


public class RandomGraphLoader implements GraphLoader
{
  double _connectionPercent;
  int _minNodes;
  int _maxNodes;
  long _seed;

  public RandomGraphLoader(int maxNodes, double connectionPercent) {
    this(maxNodes, maxNodes, connectionPercent, System.currentTimeMillis());
  }

  public RandomGraphLoader(int maxNodes, int minNodes, double colorablePercent, long seed) {
    _maxNodes = maxNodes;
    _minNodes = minNodes;
    if (_maxNodes < _minNodes) {
      throw new IllegalArgumentException(String.format("max nodes must be greater than %s", Integer.toString(_minNodes)));
    }

    _connectionPercent = colorablePercent;
    _seed = seed;
  }

  @Override
  public Graph load()
  {
    Random rnd = new Random(_seed);

    int nodeCnt = Math.max(rnd.nextInt(_maxNodes), _minNodes);

    List<String> nodeOrder = new ArrayList<String>(nodeCnt);
    Map<String, Set<String>> buildMap = new HashMap<String, Set<String>>();

    double sum = 0.0;

    for (int i = 1; i <= nodeCnt; i++)
    {
      String nodeId = Util.getNodeName(i);

      nodeOrder.add(nodeId);

      buildMap.put(nodeId, new HashSet<String>());

      Set<String> neighbors = new HashSet<String>();

      for (int j = 1; j <= nodeCnt; j++)
      {
        if (j == i) continue;
        double r = rnd.nextDouble();
        if (r > _connectionPercent) continue;
        String neighborId = Util.getNodeName(j);
        neighbors.add(neighborId);
      }

      buildMap.get(nodeId).addAll(neighbors);

      sum += neighbors.size() / (double )nodeCnt;
    }

    // ensure we have unidirectional connections
    for (String nodeId : buildMap.keySet())
    {
      for (String neighborId : buildMap.get(nodeId))
      {
        buildMap.get(neighborId).add(nodeId);
      }
    }

    double realP = sum / (double )nodeCnt;
    System.out.println(String.format("actual distribution = %s", realP));

    GraphBuilder builder = new GraphBuilder(nodeCnt, realP);

    for (String nodeId : nodeOrder) {
      builder.addNode(nodeId, buildMap.get(nodeId));
    }

    Graph graph = builder.build();

    return graph;
  }
}
