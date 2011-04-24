package org.sanyanse.colorer;


import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphDecomposition;
import org.sanyanse.common.GraphSpec;


public class SpectralColorer implements GraphColorer
{
  GraphSpec _spec;
  double _p;
  double _d;

  public SpectralColorer(GraphSpec spec, double p)
  {
    _spec = spec;
    _p = p;
    _d = p * spec.NodeCount;
  }

  @Override
  public ColoringResult call()
    throws Exception
  {
    GraphSpec gPrime = computeGPrime(_spec);
    GraphDecomposition comp = GraphDecomposition.computeGraphDecomposition(gPrime);
    computeSpectrum(comp);
    return null;
  }

  private GraphSpec computeGPrime(GraphSpec spec)
  {
    GraphSpec gPrime = spec.clone();

    for (String nodeId : spec.Nodes)
    {
      if (spec.Edges.get(nodeId).size() > (5 *_d))
      {
        gPrime.removeNode(nodeId);
      }
    }

    return gPrime;
  }

  public static void computeSpectrum(GraphDecomposition comp) {
    EigenvalueDecomposition e = comp.getAdjacencySpectrum();

    Matrix V = e.getV();

//    Matrix maxV = V.getMatrix(0, adj.getRowDimension() - 1, adj.getColumnDimension() - 1, adj.getColumnDimension() - 1);
//    Matrix maxV2 = V.getMatrix(0, adj.getRowDimension() - 1, adj.getColumnDimension() - 2, adj.getColumnDimension() - 2);

    Matrix maxV = V.getMatrix(0, comp.getAdjacency().getRowDimension() - 1, 0, 0);
    Matrix maxV2 = V.getMatrix(0, comp.getAdjacency().getRowDimension() - 1, 1, 1);

    Matrix t = maxV.times(1.0).plus(maxV2.times(3.0));

    int a = 6;
    int b = 3;

//    maxV.print(a,b);
//    maxV2.print(a,b);
    t.print(a, b);

    System.out.print("V =");
    V.print(a,b);

    Matrix Q = comp.getAdjacency().times(maxV);
    Q.print(a,b);
  }
}
