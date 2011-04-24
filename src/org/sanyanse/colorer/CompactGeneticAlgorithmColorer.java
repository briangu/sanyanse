package org.sanyanse.colorer;


import java.util.Random;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphColorer;


public class CompactGeneticAlgorithmColorer implements GraphColorer
{
  Graph _spec;
  ColorGene[] _genes;

  static class ColorGene
  {
    public double[] Colors;
    double _adjFactor;

    public ColorGene(double adjFactor)
    {
      this(adjFactor, new double[]{1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0});
    }

    public ColorGene(double adjFactor, int color)
    {
      _adjFactor = adjFactor;

      switch (color)
      {
        case 1:
          Colors = new double[]{0.5, 0.25, 0.25};
          break;
        case 2:
          Colors = new double[]{0.25, 0.5, 0.25};
          break;
        case 3:
          Colors = new double[]{0.25, 0.25, 0.5};
          break;
      }
    }

    public ColorGene(double adjFactor, double[] colors)
    {
      _adjFactor = adjFactor;
      Colors = colors;
    }

    int chooseColor(double r)
    {
      if (r <= Colors[0]) return 1;
      if (r >= (1.0 - Colors[2])) return 3;
      return 2;
    }

    void boostColor(int color)
    {
      color -= 1;

      if (Colors[color] >= 1.0) return;

      double a = ((Colors[color] + _adjFactor) > 1.0) ? (1.0 - Colors[color]) : _adjFactor;

      double spill;
      switch (color)
      {
        case 0:
          Colors[0] += a;
          spill = surpressWithSpill(1, a);
          boostWithSpill(2, spill);
          break;
        case 1:
          Colors[1] += a;
          spill = surpressWithSpill(0, a);
          boostWithSpill(2, spill);
          break;
        case 2:
          spill = surpressWithSpill(0, a);
          boostWithSpill(1, spill);
          Colors[2] += a;
          break;
      }
    }

    double surpressWithSpill(int color, double adj)
    {
      if (adj <= 0) return 0;
      if (Colors[color] <= 0.0) return adj;
      if (Colors[color] - adj <= 0.0) {
        double spill = (0.0 - (Colors[color] - adj));
        Colors[color] = 0.0;
        return spill;
      }
      Colors[color] -= adj;
      return 0;
    }

    double boostWithSpill(int color, double adj)
    {
      if (adj <= 0) return 0;
      if (Colors[color] >= 1.0) return adj;
      if (Colors[color] + adj > 1.0) {
        double spill = ((Colors[color] + adj) - 1.0);
        Colors[color] = 1.0;
        return spill;
      }
      Colors[color] += adj;
      return 0;
    }

    void suppressColor(int color)
    {
      color -= 1;

      if (Colors[color] <= 0.0) return;

      double a = ((Colors[color] - _adjFactor) < 0) ? Colors[color] : _adjFactor;

      double spill;

      switch (color)
      {
        case 0:
          Colors[0] -= a;
          spill = boostWithSpill(1, a);
          boostWithSpill(2, spill);
          break;
        case 1:
          Colors[1] -= a;
          spill = boostWithSpill(0, a);
          boostWithSpill(2, spill);
          break;
        case 2:
          spill = boostWithSpill(0, a);
          boostWithSpill(1, spill);
          Colors[2] -= a;
          break;
      }
    }
  }

  private CompactGeneticAlgorithmColorer(Graph spec, ColorGene[] genes)
  {
    _spec = spec;
    _genes = genes;
  }

  // define best
  // get 2 candidates
  // compete, pick winner
  // if winner better than best, then
  //    best = winner
  // reinforce
  @Override
  public ColoringResult call()
    throws Exception
  {
    int bestCnt = 0;
    Graph bestGraph = null;
    Graph.GraphAnalysis bestGraphAnalysis = null;

    Graph candidateA = colorGraph(_spec.clone());
    Graph candidateB = colorGraph(_spec.clone());

    while (true)
    {
      colorGraph(candidateA);
      Graph.GraphAnalysis analysisA = candidateA.analyze();
      if (analysisA.State == Graph.ColorState.Complete)
      {
        bestGraph = candidateA;
        break;
      }

      colorGraph(candidateB);
      Graph.GraphAnalysis analysisB = candidateB.analyze();
      if (analysisB.State == Graph.ColorState.Complete)
      {
        bestGraph = candidateB;
        break;
      }

      Boolean aWins = (analysisA.CorrectEdgeColorings > analysisB.CorrectEdgeColorings);
      if (aWins)
      {
        if (analysisA.CorrectEdgeColorings > bestCnt)
        {
          bestCnt = analysisA.CorrectEdgeColorings;
          bestGraph = candidateA.clone();
          bestGraphAnalysis = analysisA;
        }

        for (int i = 0; i < candidateA.NodeCount; i++)
        {
          int colorA = candidateA.Nodes[i].Color;
          String nodeId = candidateA.Nodes[i].Id;
          if (analysisA.EdgeColoringsMap.get(nodeId) < bestGraphAnalysis.EdgeColoringsMap.get(nodeId))
          {
            _genes[i].suppressColor(colorA);
          }
          else
          {
            _genes[i].boostColor(colorA);
          }
        }
      }
      else
      {
        if (analysisB.CorrectEdgeColorings > bestCnt)
        {
          bestCnt = analysisB.CorrectEdgeColorings;
          bestGraph = candidateB.clone();
          bestGraphAnalysis = analysisB;
        }

        for (int i = 0; i < candidateA.NodeCount; i++)
        {
          int colorB = candidateB.Nodes[i].Color;
          String nodeId = candidateB.Nodes[i].Id;
          if (analysisB.EdgeColoringsMap.get(nodeId) < bestGraphAnalysis.EdgeColoringsMap.get(nodeId))
          {
            _genes[i].suppressColor(colorB);
          }
          else
          {
            _genes[i].boostColor(colorB);
          }
        }
      }
    }

    return ColoringResult.createColoredGraphResult(bestGraph);
  }

  private Graph colorGraph(Graph graph)
  {
    Random r = new Random();

    for (int i = 0; i < graph.NodeCount; i++)
    {
      graph.Nodes[i].Color = _genes[i].chooseColor(r.nextDouble());
    }

    return graph;
  }

  /**
   * Create with a randomized init
   *
   * @param graph
   *
   * @return
   */
  public static CompactGeneticAlgorithmColorer create(Graph graph, double adjFactor)
  {
    Integer[] colorings = new Integer[graph.NodeCount];

    Random r = new Random();

    for (int i = 0; i < graph.NodeCount; i++)
    {
      colorings[i] = r.nextInt(3) + 1;
    }

    return CompactGeneticAlgorithmColorer.create(graph, colorings, adjFactor);
  }

  /**
   * Create with a seeded init.
   *
   * @param spec
   * @param colorings
   *
   * @return
   */
  public static CompactGeneticAlgorithmColorer create(Graph spec, Integer[] colorings, double adjFactor)
  {
    ColorGene[] genes = new ColorGene[colorings.length];

    for (int i = 0; i < spec.NodeCount; i++)
    {
      genes[i] = new ColorGene(adjFactor, colorings[i]);
    }

    return new CompactGeneticAlgorithmColorer(spec, genes);
  }
}
