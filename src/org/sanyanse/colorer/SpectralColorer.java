package org.sanyanse.colorer;


import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;

/**
 * Created by IntelliJ IDEA. User: bguarrac Date: 4/21/11 Time: 9:00 PM To change this template use File | Settings |
 * File Templates.
 */
public class SpectralColorer implements GraphColorer
{
  @Override
  public ColoringResult call()
    throws Exception
  {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public static void main(Matrix adjacency) {
     int N = 5;

     // create a symmetric positive definite matrix
     Matrix A = Matrix.random(N, N);
     A = A.transpose().times(A);

     // compute the spectral decomposition
     EigenvalueDecomposition e = A.eig();
     Matrix V = e.getV();
     Matrix D = e.getD();

     System.out.print("A =");
     A.print(9, 6);
     System.out.print("D =");
     D.print(9, 6);
     System.out.print("V =");
     V.print(9, 6);

     // check that V is orthogonal
     System.out.print("||V * V^T - I|| = ");
     System.out.println(V.times(V.transpose()).minus(Matrix.identity(N, N)).normInf());

     // check that A V = D V
     System.out.print("||AV - DV|| = ");
     System.out.println(A.times(V).minus(V.times(D)).normInf());
  }
}
