package org.sanyanse.ravi.test;

import org.sanyanse.ravi.util.LexicographicColoringCounter;

public class TestLexicographicColoringCounter {
	public static void main(String[] args) {
		test2();
	}
	
	private static void test1() {
		LexicographicColoringCounter counter = new LexicographicColoringCounter(10, 3);
		int i=0;
		print(i++, counter.next());
		counter.deeper();
		print(i++, counter.next());
		counter.deeper();
		while (counter.hasNext()) {
			int[] count = counter.next();
			print(i++, count);
			if (i > 100) {
				System.out.println("Hmm...");
				break;
			}
		}
		System.out.println("Done.");
	}
	
	private static void test2() {
		LexicographicColoringCounter counter = new LexicographicColoringCounter(10, 10);
		int i=0;
		print(i++, counter.next());
		counter.deeper();
		print(i++, counter.next());
		counter.deeper();
		print(i++, counter.next());
		counter.deeper();
		print(i++, counter.next());
		counter.deeper();
		print(i++, counter.next());
		counter.deeper();
		print(i++, counter.next());
		counter.deeper();
//		print(i++, counter.next());
//		counter.deeper();
//		print(i++, counter.next());
//		counter.deeper();
//		print(i++, counter.next());
//		counter.deeper();
		while (counter.hasNext()) {
			int[] count = counter.next();
			print(i++, count);
			if (counter.hasDeeper()) {
				counter.deeper();
			}
			if (i > 1000) {
				break;
			}
		}
	}
	
	private static void print(int num, int[] count) {
		System.out.print(num + "> ");
		for (int i=0; i<count.length; i++) {
			System.out.print(count[i]);
			System.out.print(".");
		}
		System.out.println();
	}
}
