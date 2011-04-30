package org.sanyanse.ravi.test;

import org.sanyanse.ravi.util.BaseNCounter;

public class TestBaseNCounter {
	public static void main(String[] args) {
		test2();
	}
	
	private static void test1() {
		BaseNCounter counter = new BaseNCounter(2, 4);
		countAll(counter);
	}
	
	private static void test2() {
		BaseNCounter counter = new BaseNCounter(2, 4);
		counter.setCounter(new int[] {0, 0, 0, 1}); // start with 8.
		countAll(counter);
	}

	private static void countAll(BaseNCounter counter) {
		int num = 1;
		while (counter.hasNext()) {
			int[] count = counter.next();
			System.out.println(num++ + ": " + counter);
		}
	}
}
