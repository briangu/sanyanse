package org.sanyanse.ravi.test;

import org.sanyanse.ravi.util.ColoringCounter;

public class TestColoringCounter {
	public static void main(String[] args) {
		//test1();
		test2();
	}
	
	private static void test1() {
		ColoringCounter counter = new ColoringCounter(10, 3);
		int[] count = null;
		int index = 1;
		while ((count = counter.next()) != null) {
			System.out.print(index++);
			System.out.print(". ");
			print(count);
		}
		System.out.println("Done.");
	}
	
	private static void test2() {
		long prev = 1;
		long startTime = System.currentTimeMillis();
		for (int i=1; i<40; i++) {
			long current = lexicographicCount(i);
			long endTime = System.currentTimeMillis();
			long power2 = (long) Math.pow(2, i);
			System.out.println("f(" + i + ") = " + current + ", ratio=" + (current * 1.0F / prev) + ", power-2 ratio: " + (power2 / current) + " , time=" + ((endTime - startTime) * 1.0/1000) + " sec.");
			prev = current;
			startTime = endTime;
		}
	}

	private static long lexicographicCount(int n) {
		int maxChoice = (int) Math.floor(n * 1.0F / 3);
		maxChoice = Math.max(maxChoice, 1);
		long count = 0;
		ColoringCounter counter = new ColoringCounter(n, maxChoice);
		while (counter.next() != null) {
			count++;
		}
		return count;
	}
	
	private static void print(int[] array) {
		for (int i : array) {
			System.out.print(i);
			System.out.print(' ');
		}
		System.out.println();
	}
}
