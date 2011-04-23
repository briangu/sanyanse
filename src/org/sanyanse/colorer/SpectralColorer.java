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
    GraphDecomposition comp = computeAdjacencyMatrix(gPrime);
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

  private static class GraphDecomposition
  {
    public Matrix Adjacency;
    public Matrix Degree;
    public Matrix Laplacian;
  }

  private static GraphDecomposition computeAdjacencyMatrix(GraphSpec spec)
  {
    Matrix adj = new Matrix(spec.NodeCount, spec.NodeCount);
    Matrix deg = new Matrix(spec.NodeCount, spec.NodeCount);

    for (int i = 0; i < spec.NodeCount; i++)
    {
      String xNodeId = spec.Nodes.get(i);
      Set<String> xEdges = spec.Edges.get(xNodeId);

      deg.set(i, i, xEdges.size());

      for (int j = 0; j < spec.NodeCount; j++)
      {
        String yNodeId = spec.Nodes.get(j);

        adj.set(i, j, xEdges.contains(yNodeId) ? 1.0 : 0);
      }
    }

    adj.print(new PrintWriter(System.out,true), 0, 0);

    GraphDecomposition comp = new GraphDecomposition();
    comp.Adjacency = adj;
    comp.Degree = deg;
    comp.Laplacian = deg.minus(adj);

    return comp;
  }

  public static void computeSpectrum(GraphDecomposition comp) {
    Matrix adj = comp.Adjacency;

     // create a symmetric positive definite matrix
//     Matrix A = Matrix.random(N, N);
//     A = A.transpose().times(A);

     // compute the spectral decomposition
    EigenvalueDecomposition e = adj.eig();
    Matrix V = e.getV();
    Matrix D = e.getD();

//    Matrix maxV = V.getMatrix(0, adj.getRowDimension() - 1, adj.getColumnDimension() - 1, adj.getColumnDimension() - 1);
//    Matrix maxV2 = V.getMatrix(0, adj.getRowDimension() - 1, adj.getColumnDimension() - 2, adj.getColumnDimension() - 2);

    Matrix maxV = V.getMatrix(0, adj.getRowDimension() - 1, 0, 0);
    Matrix maxV2 = V.getMatrix(0, adj.getRowDimension() - 1, 1, 1);

    Matrix t = maxV.times(1.0).plus(maxV2.times(3.0));

    int a = 6;
    int b = 3;

//    maxV.print(a,b);
//    maxV2.print(a,b);
    t.print(a, b);

//    System.out.print("A =");
//    adj.print(9, 6);
//    System.out.print("D =");
//    D.print(9, 6);

    System.out.print("V =");
    V.print(a,b);

    Matrix Q = adj.times(maxV);
    Q.print(a,b);

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
