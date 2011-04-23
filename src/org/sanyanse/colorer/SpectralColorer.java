package org.sanyanse.colorer;


import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import java.io.PrintWriter;
import java.util.Set;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphSpec;


/**
 * Created by IntelliJ IDEA. User: bguarrac Date: 4/21/11 Time: 9:00 PM To change this template use File | Settings |
 * File Templates.
 */
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
    Matrix adj = computeAdjacencyMatrix(gPrime);
    computeSpectrum(adj);
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

  private static Matrix computeAdjacencyMatrix(GraphSpec spec)
  {
    Matrix adj = new Matrix(spec.NodeCount, spec.NodeCount);

    for (int i = 0; i < spec.NodeCount; i++)
    {
      String xNodeId = spec.Nodes.get(i);
      Set<String> xEdges = spec.Edges.get(xNodeId);

      for (int j = 0; j < spec.NodeCount; j++)
      {
        String yNodeId = spec.Nodes.get(j);

        adj.set(i, j, xEdges.contains(yNodeId) ? 1.0 : 0);
      }
    }

    adj.print(new PrintWriter(System.out,true), 0, 0);

    return adj;
  }

  public static void computeSpectrum(Matrix adj) {
     // create a symmetric positive definite matrix
//     Matrix A = Matrix.random(N, N);
//     A = A.transpose().times(A);

     // compute the spectral decomposition
    EigenvalueDecomposition e = adj.eig();
    Matrix V = e.getV();
    Matrix D = e.getD();

    Matrix maxV = V.getMatrix(adj.getColumnDimension()-1, adj.getColumnDimension()-1, 0, adj.getRowDimension()-1);

//    System.out.print("A =");
//    adj.print(9, 6);
//    System.out.print("D =");
//    D.print(9, 6);
    System.out.print("V =");
    V.print(9, 6);

    Matrix Q = adj.times(maxV.transpose());
    Q.print(9, 6);

/*
     // check that V is orthogonal
    System.out.print("||V * V^T - I|| = ");
    System.out.println(V.times(V.transpose()).minus(Matrix.identity(adj.getColumnDimension(),
                                                                    adj.getRowDimension())).normInf());

     // check that A V = D V
    System.out.print("||AV - DV|| = ");
    System.out.println(adj.times(V).minus(V.times(D)).normInf());
*/
  }
}
