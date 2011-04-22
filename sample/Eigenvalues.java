/*************************************************************************
 *  Compilation:  javac -classpath .:jama.jar Eigenvalues.java
 *  Execution:    java -classpath .:jama.jar Eigenvalues
 *  Dependencies: jama.jar
 *  
 *  Test client for computing eigenvalues and eigenvectors of a real
 *  symmetric matrix A = V D V^T.
 *  
 *       http://math.nist.gov/javanumerics/jama/
 *       http://math.nist.gov/javanumerics/jama/Jama-1.0.1.jar
 *
 *************************************************************************/

import Jama.Matrix;
import Jama.EigenvalueDecomposition;

public class Eigenvalues {
   public static void main(String[] args) { 
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

