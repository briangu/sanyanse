package org.sanyanse.colorer;


import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import org.sanyanse.common.ColorableNode;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphBuilder;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphDecomposition;


public class SpectralColorer implements GraphColorer
{
  Graph _spec;
  double _p;
  double _d;

  public SpectralColorer(Graph spec, double p)
  {
    _spec = spec;
    _p = p;
    _d = p * spec.NodeCount;
  }

  @Override
  public ColoringResult call()
    throws Exception
  {
    Graph gPrime = computeGPrime(_spec);
    GraphDecomposition comp = gPrime.Decomposition;
    computeSpectrum(comp);
    return null;
  }

  private Graph computeGPrime(Graph graph)
  {
    GraphBuilder builder = GraphBuilder.createFrom(graph);

    for (ColorableNode node : graph.Nodes)
    {
      if (node.Edges.length > (5 *_d))
      {
        builder.removeNode(node.Id);
      }
    }

    Graph gPrime = builder.build();

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

    int a = 5;
    int b = 3;

    comp.getAdjacency().print(0,0);

//    maxV.print(a,b);
//    maxV2.print(a,b);
    t.print(a, b);

    System.out.print("V =");
    V.print(a,b);

    Matrix Q = comp.getAdjacency().times(maxV);
    Q.print(a,b);
  }
}
