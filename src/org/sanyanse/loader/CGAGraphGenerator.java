package org.sanyanse.loader;

import org.sanyanse.colorer.BacktrackColorer;
import org.sanyanse.common.*;
import org.sanyanse.writer.FileGraphWriter;

import java.util.*;
import java.util.concurrent.*;


public class CGAGraphGenerator implements GraphLoader
{
  float _connectionPercent;
  int _minNodes;
  int _maxNodes;
  long _seed;
  int _maxIterations;
  int _maxWorkers;

  public CGAGraphGenerator(int maxNodes, float connectionPercent, int maxIterations, int maxWorkers) {
    this(maxNodes, maxNodes, connectionPercent, maxIterations, maxWorkers, System.currentTimeMillis());
  }

  public CGAGraphGenerator(int maxNodes, int minNodes, float colorablePercent, int maxIterations, int maxWorkers, long seed) {
    _maxNodes = maxNodes;
    _minNodes = minNodes;
    if (_maxNodes < _minNodes) {
      throw new IllegalArgumentException(String.format("max nodes must be greater than %s", Integer.toString(_minNodes)));
    }

    _maxIterations = maxIterations;
    _connectionPercent = colorablePercent;
    _seed = seed;
    _maxWorkers = maxWorkers;
  }

  @Override
  public Graph load()
  {
    Random rnd = new Random(_seed);
    int nodeCnt = Math.max(rnd.nextInt(_maxNodes), _minNodes);
    ProbabilityInfo info = buildProbabalisticGraph(rnd, nodeCnt, _connectionPercent, 0.5f);
    SearchResult winner;
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

  private SearchResult search(ProbabilityInfo info, int maxIterations)
  {
    SearchResult best = null;

    for (int i = 0; i < maxIterations; i++)
    {
      System.out.println("generation: " + i);

      ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

      List<SearchResult> result = parallelizeCalls(executor, info);
      if (result.size() < 2)
      {
        System.out.println("missing result! i = " + i);
        continue;
      }

      SearchResult resultA = result.get(0);
      SearchResult resultB = result.get(1);

      List<Boolean> winner = resultA.Cost > resultB.Cost ? resultA.Vector : resultB.Vector;
      List<Boolean> loser = resultA.Cost > resultB.Cost ? resultB.Vector : resultA.Vector;
      SearchResult winningResult = resultA.Cost < resultB.Cost ? resultA : resultB;

      System.out.println("winningResult cost: " + winningResult.Cost);

      if (best == null || winningResult.Cost < best.Cost)
      {
        best = winningResult;
        System.out.println("new best cost: " + best.Cost);
        if (best.Graph != null)
        {
          FileGraphWriter.create(String.format("best_%s_%s.col", info.NodeCount, i)).write(best.Graph);
//          best.Graph = null;
        }
      }

      // horrible barrier hack
      executor.shutdown();

      updateVector(info, winner, loser);
    }

    return best;
  }

  private List<SearchResult> parallelizeCalls(ExecutorService executor, ProbabilityInfo info)
  {
    CompletionService<SearchResult> ecs = new ExecutorCompletionService<SearchResult>(executor);

    List<Future<SearchResult>> futures = new ArrayList<Future<SearchResult>>(2);

    List<CGAWorker> workers = new ArrayList<CGAWorker>();
    for (int i = 0; i < _maxWorkers; i++)
    {
      workers.add(new CGAWorker(info));
    }

    List<SearchResult> result = new ArrayList<SearchResult>();

    try
    {
      for (Callable<SearchResult> s : workers)
      {
        futures.add(ecs.submit(s));
      }

      int j = futures.size();
      while (result.size() < 2 && j > 0)
      {
        try
        {
          SearchResult r = ecs.take().get();
          j--;
          if (r == null)
          {
            System.out.println("a job failed");
            continue;
          }
          result.add(r);
        }
        catch (ExecutionException ignore)
        {
        }
        catch (InterruptedException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      }
    }
    finally
    {
      for (Future<SearchResult> f : futures)
      {
        f.cancel(true);
      }
    }

    return result;
  }

  private void updateVector(ProbabilityInfo info, List<Boolean> winner, List<Boolean> loser)
  {
    float delta = Math.max(100 / (float)(info.NodeCount), 0.1f);

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

  private class CGAWorker implements Callable<SearchResult>
  {
    ProbabilityInfo _info;

    public CGAWorker(ProbabilityInfo info)
    {
      _info = info;
    }

    @Override
    public SearchResult call() throws Exception
    {
      System.out.print("generating candidate..." + Thread.currentThread().getId());
      List<Boolean> vector = generateCandidate(_info);
      System.out.println("done." + Thread.currentThread().getId());

      Graph graph = null;
      ColoringResult result = null;
      StopWatch sw = new StopWatch();

      try {
        System.out.print("generating graph..." + Thread.currentThread().getId());
        graph = generateGraph(_info, vector);
        System.out.println("done." + Thread.currentThread().getId());
        graph.SortByEdgeCount();
        GraphColorer colorer = new BacktrackColorer(graph);
        sw.start();
        result = colorer.call();
        sw.stop();
      } catch (MissingNodeException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      } catch (Exception e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }

      System.out.println("compute time: " + sw.getDuration() + " " + Thread.currentThread().getId());

      SearchResult sc = new SearchResult();
      sc.Cost = result != null && !result.IsColored ? (float)(sw.getDuration() / 1000): 0f;
      sc.Vector = vector;
      sc.Graph = graph;
      sc.ColoringResult = result;

      System.out.println("cost: " + sc.Cost + " " + Thread.currentThread().getId());

      return sc;
    }
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

  private ProbabilityInfo buildProbabalisticGraph(Random rnd, int nodeCount, float connectionPercent, float initProb)
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
    mapProbabilitySwitches(switches, buckets.get(0), buckets.get(1), 1.0f, initProb);
    mapProbabilitySwitches(switches, buckets.get(0), buckets.get(2), connectionPercent, initProb);
    mapProbabilitySwitches(switches, buckets.get(1), buckets.get(2), connectionPercent * 0.5f, initProb);

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
    float connectionPercent,
    float initProb)
  {
    Random r = new Random();

    for (String a : bucketA.keySet())
    {
      Set<ProbabilitySwitch> setA = bucketA.get(a);

      for (String b : bucketB.keySet())
      {
        if (r.nextFloat() > connectionPercent) continue;

        ProbabilitySwitch ps = new ProbabilitySwitch(a, b, initProb);
        switches.add(ps);
        setA.add(ps);
        bucketB.get(b).add(ps);
      }
    }
  }

  private class SearchResult
  {
    public float Cost;
    public List<Boolean> Vector;
    public Graph Graph;
    public ColoringResult ColoringResult;
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
