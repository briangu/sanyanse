package org.sanyanse.colorer;

import org.sanyanse.common.Coloring;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphSpec;

import java.util.ArrayList;
import java.util.List;

public class CompactGeneticAlgorithmColorer implements GraphColorer
{
  GraphSpec _spec;
  List<Gene> _genes;

  class Gene
  {
    public double[] Colors;

    public Gene()
    {
      Colors = new double[] { 1.0/3.0, 1.0/3.0, 1.0/3.0 };
    }

    public Gene(double[] colors)
    {
      Colors = colors;
    }


  }

  private CompactGeneticAlgorithmColorer(GraphSpec spec, List<Gene> genes)
  {
    _spec = spec;
    _genes = genes;
  }

  @Override
  public ColoringResult call() throws Exception
  {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  /**
   * Create with a randomized init
   * @param spec
   * @return
   */
  public static CompactGeneticAlgorithmColorer create(GraphSpec spec)
  {
    List<Coloring> colorings = new ArrayList<Coloring>(spec.NodeCount);
    return CompactGeneticAlgorithmColorer.create(spec, colorings);
  }

  /**
   * Create with a seeded init.
   * @param spec
   * @param colorings
   * @return
   */
  public static CompactGeneticAlgorithmColorer create(GraphSpec spec, List<Coloring> colorings)
  {
    List<Gene> genes = new ArrayList<Gene>(colorings.size());
    // populate the probabilities
    return new CompactGeneticAlgorithmColorer(spec, genes);
  }
}
