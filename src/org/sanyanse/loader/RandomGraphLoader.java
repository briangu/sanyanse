package org.sanyanse.loader;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.GraphSpec;
import org.sanyanse.common.Util;


public class RandomGraphLoader implements GraphLoader
{
  double _colorablePercentage;
  int _minNodes;
  int _maxNodes;
  long _seed;

  public RandomGraphLoader(int maxNodes, double colorablePercent) {
    this(maxNodes, 4, colorablePercent, System.currentTimeMillis());
  }

  public RandomGraphLoader(int maxNodes, int minNodes, double colorablePercent, long seed) {
    _maxNodes = maxNodes;
    _minNodes = minNodes;
    if (_maxNodes < _minNodes) {
      throw new IllegalArgumentException(String.format("max nodes must be greater than %s", Integer.toString(_minNodes)));
    }

    _colorablePercentage = colorablePercent;
    _seed = seed;
  }

  @Override
  public GraphSpec load()
  {
    Random rnd = new Random(_seed);

    int nodeCnt = Math.max(rnd.nextInt(_maxNodes), _minNodes);

    Map<String, Set<String>> buildMap = new HashMap<String, Set<String>>();

    for (int i = 1; i <= nodeCnt; i++) {
      int neighborCnt = Math.max(rnd.nextInt(Math.min(3, nodeCnt)), 1);

      Set<String> neighbors = new HashSet<String>(neighborCnt);
      for (int n = 0; n < neighborCnt; n++) {
        int neighbor;
        while((neighbor = Math.max(rnd.nextInt(nodeCnt), 1)) == i) {};
        neighbors.add(Util.getNodeName(neighbor));
      }

      String nodeId = Util.getNodeName(i);

      if (!buildMap.containsKey(nodeId)) {
        buildMap.put(nodeId, new HashSet<String>());
      }
      buildMap.get(nodeId).addAll(neighbors);

      for (String neighborId : neighbors) {
        if (!buildMap.containsKey(neighborId)) {
          buildMap.put(neighborId, new HashSet<String>());
        }
        buildMap.get(neighborId).add(nodeId);
      }
    }

    GraphSpec spec = new GraphSpec(nodeCnt);

    for (String nodeId : buildMap.keySet()) {
      spec.addNode(nodeId, buildMap.get(nodeId).toArray(new String[0]));
    }

    return spec;
  }
}
