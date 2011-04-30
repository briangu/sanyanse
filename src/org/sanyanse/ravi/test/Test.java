package org.sanyanse.ravi.test;

public class Test {
	public static void main(String args[]) {
		System.out.println("raviE(10, 10) = " + raviE(10, 10)); // 1023.
		System.out.println("raviE(49, 24) = " + raviE(46, 24));
	}
	
	// nC1 + nC2 + ... + nCr
	public static double raviE(int n, int r) {
		double raviE = 0;
		for (int i=1; i<=r; i++) {
			raviE += combination(n, i);
		}
		return raviE;
	}
	
	// nCr
	public static double combination(int n, int r) {
		double combination = 1.0D;
		for (int i=0; i<r; i++) {
			combination *= (n-i);
		}
		combination /= factorial(r);
		return combination;
	}
	
	public static double factorial(int n) {
		double factorial = 1.0D;
		for (int i=2; i<=n; i++) {
			factorial *= i;
		}
		return factorial;
	}
}
