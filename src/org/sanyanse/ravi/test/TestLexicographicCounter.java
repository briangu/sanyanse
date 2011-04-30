package org.sanyanse.ravi.test;

import org.sanyanse.ravi.util.LexicographicCounter;

public class TestLexicographicCounter {
	public static void main(String[] args) {
		test2();
	}
	
	private static void test1() {
		LexicographicCounter cc = new LexicographicCounter(3, 6);
		print(cc.increment());
		cc.deeper();
		print(cc.increment());
		cc.deeper();
		print(cc.increment());
		print(cc.increment());
		print(cc.increment());
		print(cc.increment());
		cc.deeper();
		print(cc.increment());
		cc.deeper();
		print(cc.increment());
		print(cc.increment());
		print(cc.increment());
		print(cc.increment());
		print(cc.increment());
		print(cc.increment());
//		while (cc.hasNext()) {
//			print(cc.increment());
//		}
	}

	private static void test2() {
		int maxPartitions = 3;
		LexicographicCounter cc = new LexicographicCounter(maxPartitions, 10);
		for (int i=0; i<maxPartitions-1; i++) {
			print(cc.increment());
		}
		print(cc.increment());
		cc.deeper();
		for (int i=0; i<maxPartitions-2; i++) {
			print(cc.increment());
		}
		System.out.println("Set. Need to run increment again.");
		print(cc.increment());
		cc.deeper();
		print(cc.increment());
		cc.deeper();
		print(cc.increment());
		cc.deeper();
		print(cc.increment());
	}
	
	private static void print(int[] count) {
		System.out.print("> ");
		for (int i=0; i<count.length; i++) {
			System.out.print(count[i]);
			System.out.print(".");
		}
		System.out.println();
	}
}
