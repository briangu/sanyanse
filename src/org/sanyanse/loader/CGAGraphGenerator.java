package org.sanyanse.loader;

import org.sanyanse.colorer.BacktrackColorer;
import org.sanyanse.common.*;

import java.util.*;


public class CGAGraphGenerator implements GraphLoader
{
  double _connectionPercent;
  int _minNodes;
  int _maxNodes;
  long _seed;
  int _maxIterations;

  public CGAGraphGenerator(int maxNodes, double connectionPercent, int maxIterations) {
    this(maxNodes, maxNodes, connectionPercent, maxIterations, System.currentTimeMillis());
  }

  public CGAGraphGenerator(int maxNodes, int minNodes, double colorablePercent, int maxIterations, long seed) {
    _maxNodes = maxNodes;
    _minNodes = minNodes;
    if (_maxNodes < _minNodes) {
      throw new IllegalArgumentException(String.format("max nodes must be greater than %s", Integer.toString(_minNodes)));
    }

    _maxIterations = maxIterations;
    _connectionPercent = colorablePercent;
    _seed = seed;
  }

  @Override
  public Graph load()
  {
    Random rnd = new Random(_seed);
    int nodeCnt = Math.max(rnd.nextInt(_maxNodes), _minNodes);
    ProbabilityInfo info = buildProbabalisticGraph(rnd, nodeCnt, 0.5f);
    SearchCost winner;
    Graph graph = null;
    try
    {
      winner = search(info, _maxIterations);
      graph = generateGraph(info, winner.Vector);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return graph;
  }

  private class SearchCost
  {
    public float Cost;
    public List<Boolean> Vector;
  }

  private SearchCost search(ProbabilityInfo info, int maxIterations)
  {
    SearchCost best = null;

    for (int i = 0; i < maxIterations; i++)
    {
      // should be threads
      SearchCost costA = computeCost(info);
      SearchCost costB = computeCost(info);

      List<Boolean> winner = costA.Cost > costB.Cost ? costA.Vector : costB.Vector;
      List<Boolean> loser = costA.Cost > costB.Cost ? costB.Vector : costA.Vector;
      SearchCost winningCost = costA.Cost > costB.Cost ? costA : costB;

      if (best == null || winningCost.Cost > best.Cost)
      {
        best = winningCost;
      }

      try
      {
        updateVector(info, winner, loser);
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }

    return best;
  }

  private void updateVector(ProbabilityInfo info, List<Boolean> winner, List<Boolean> loser)
  {
    float delta = 1 / (float)(info.NodeCount);

    for (int i = 0; i < winner.size(); i++)
    {
      if (winner.get(i) != loser.get(i))
      {
        if (winner.get(i))
        {
          info.Vector.get(i).boost(delta);
        }
        else
        {
          info.Vector.get(i).suppress(delta);
        }
      }
    }
  }

  private SearchCost computeCost(ProbabilityInfo info)
  {
    List<Boolean> vector = generateCandidate(info);

    Graph graph;
    ColoringResult result = null;
    StopWatch sw = new StopWatch();

    try {
      graph = generateGraph(info, vector);
      GraphColorer colorer = new BacktrackColorer(graph);
      sw.start();
      result = colorer.call();
      sw.stop();
    } catch (MissingNodeException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    SearchCost sc = new SearchCost();
    sc.Cost = result != null && result.IsColored ? 1 / (float)(sw.getDuration() / 1000): 0f;
    sc.Vector = vector;

    return sc;
  }

  private List<Boolean> generateCandidate(ProbabilityInfo info)
  {
    List<Boolean> vector = new ArrayList<Boolean>(info.Vector.size());

    Random r = new Random();

    for (int i = 0; i < info.Vector.size(); i++)
    {
      vector.add(info.Vector.get(i).computeIsConnected(r.nextFloat()));
    }

    return vector;
  }

  private Graph generateGraph(ProbabilityInfo info, List<Boolean> bitvector) throws MissingNodeException
  {
    Map<String, Set<String>> map = mapBitVectorToGraph(info.Vector, bitvector);

    GraphBuilder builder = new GraphBuilder(info.NodeCount);

    for (String nodeId : map.keySet())
    {
      Set<String> set = map.get(nodeId);
      if (set == null || set.size() == 0)
      {
        throw new MissingNodeException();
      }
      builder.addNode(nodeId, map.get(nodeId));
    }

    Graph graph = builder.build();

    return graph;
  }

  private class MissingNodeException extends Exception
  {
  }

  private Map<String, Set<String>> mapBitVectorToGraph(List<ProbabilitySwitch> psVector, List<Boolean> bitvector)
  {
    Map<String, Set<String>> map = new HashMap<String, Set<String>>(psVector.size());

    for (int i = 0; i < psVector.size(); i++)
    {
      ProbabilitySwitch ps = psVector.get(i);

      if (!map.containsKey(ps.VertexA))
      {
        map.put(ps.VertexA, new HashSet<String>());
      }
      if (!map.containsKey(ps.VertexB))
      {
        map.put(ps.VertexB, new HashSet<String>());
      }

      if (bitvector.get(i))
      {
        map.get(ps.VertexA).add(ps.VertexB);
        map.get(ps.VertexB).add(ps.VertexA);
      }
    }

    return map;
  }

  private ProbabilityInfo buildProbabalisticGraph(Random rnd, int nodeCount, float initProb)
  {
    int nodeCnt = Math.max(rnd.nextInt(_maxNodes), _minNodes);
    int bucketSize = (int)(nodeCnt / 3);

    List<Map<String, Set<ProbabilitySwitch>>> buckets = new ArrayList<Map<String, Set<ProbabilitySwitch>>>(3);
    buckets.add(new HashMap<String, Set<ProbabilitySwitch>>(bucketSize));
    buckets.add(new HashMap<String, Set<ProbabilitySwitch>>(bucketSize));
    buckets.add(new HashMap<String, Set<ProbabilitySwitch>>(bucketSize));

    final Random r = new Random();

    // bin vertices
    for (int i = 1; i <= nodeCnt; i++)
    {
      String nodeId = Util.getNodeName(i);
      int b = r.nextInt(3);
      buckets.get(b).put(nodeId, new HashSet<ProbabilitySwitch>(bucketSize*2));
    }

    HashSet<ProbabilitySwitch> switches = new HashSet<ProbabilitySwitch>();

    // setup shared edge objects for unidirectional edges
    for (int j = 1; j <= 2; j++)
    {
      mapProbabilitySwitches(switches, buckets.get(0), buckets.get(j), initProb);
    }
    mapProbabilitySwitches(switches, buckets.get(1), buckets.get(2), initProb);

    ProbabilityInfo info = new ProbabilityInfo();
    info.NodeCount = nodeCount;
    info.ProbGraph = buckets;
    info.Vector = new ArrayList(switches);

    return info;
  }

  // for every node in A, add a probedge to everynode in B
  private void mapProbabilitySwitches(
    Set<ProbabilitySwitch> switches,
    Map<String, Set<ProbabilitySwitch>> bucketA,
    Map<String, Set<ProbabilitySwitch>> bucketB,
    float initProb)
  {
    for (String a : bucketA.keySet())
    {
      Set<ProbabilitySwitch> setA = bucketA.get(a);

      for (String b : bucketB.keySet())
      {
        ProbabilitySwitch ps = new ProbabilitySwitch(a, b, initProb);
        switches.add(ps);
        setA.add(ps);
        bucketB.get(b).add(ps);
      }
    }
  }

  private class ProbabilityInfo
  {
    public int NodeCount;
    public List<Map<String, Set<ProbabilitySwitch>>> ProbGraph;
    public List<ProbabilitySwitch> Vector;
  }

  private class ProbabilitySwitch
  {
    public final String VertexA;
    public final String VertexB;
    private float _probability;

    public ProbabilitySwitch(String a, String b, float initialProbability)
    {
      VertexA = a;
      VertexB = b;
      _probability = initialProbability;
    }

    public void boost(float delta)
    {
      _probability = (float)Math.min(_probability + delta, 1.0);
    }

    public void suppress(float delta)
    {
      _probability = Math.max(_probability - delta, 0);
    }

    public Boolean computeIsConnected(float f)
    {
      return f < _probability;
    }
  }
}
