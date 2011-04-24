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
        case 0:
          Colors = new double[]{0.5, 0.25, 0.25};
          break;
        case 1:
          Colors = new double[]{0.25, 0.5, 0.25};
          break;
        case 2:
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
      if (r <= Colors[0]) return 0;
      if (r >= (1.0 - Colors[2])) return 2;
      return 1;
    }

    void boostColor(int color)
    {
      if (Colors[color] == 1.0) return;

      double a = ((Colors[color] + _adjFactor) > 1.0) ? (1.0 - Colors[color]) : _adjFactor;

      switch (color)
      {
        case 0:
          Colors[0] += a;
          Colors[1] -= a / 2;
          Colors[2] -= a / 2;
          break;
        case 1:
          Colors[1] += a;
          Colors[0] -= a / 2;
          Colors[2] -= a / 2;
          break;
        case 2:
          Colors[2] += a;
          Colors[1] -= a / 2;
          Colors[2] -= a / 2;
          break;
      }

      double sum = Colors[0] + Colors[1] + Colors[2];
      if (sum != 1.0)
      {
        Colors[color] = 1.0 - sum;
      }
    }

    void suppressColor(int color)
    {
      if (Colors[color] == 0.0) return;

      double a = ((Colors[color] - _adjFactor) < 0) ? Colors[color] : _adjFactor;

      switch (color)
      {
        case 0:
          Colors[0] -= a;
          Colors[1] += a / 2;
          Colors[2] += a / 2;
          break;
        case 1:
          Colors[1] -= a;
          Colors[0] += a / 2;
          Colors[2] += a / 2;
          break;
        case 2:
          Colors[2] -= a;
          Colors[1] += a / 2;
          Colors[2] += a / 2;
          break;
      }

      double sum = Colors[0] + Colors[1] + Colors[2];
      if (sum != 1.0)
      {
        Colors[color] = 1.0 - sum;
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
          bestGraph = candidateA.clone();
        }

        for (int i = 0; i < candidateA.NodeCount; i++)
        {
          int colorA = candidateA.Nodes[i].Color;
          int colorB = candidateB.Nodes[i].Color;
          if (colorA != colorB)
          {
            _genes[i].boostColor(colorA);
          }
        }
      }
      else
      {
        if (analysisB.CorrectEdgeColorings > bestCnt)
        {
          bestGraph = candidateB.clone();
        }

        for (int i = 0; i < candidateA.NodeCount; i++)
        {
          int colorA = candidateA.Nodes[i].Color;
          int colorB = candidateB.Nodes[i].Color;
          if (colorA != colorB)
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
      colorings[i] = r.nextInt(3);
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
