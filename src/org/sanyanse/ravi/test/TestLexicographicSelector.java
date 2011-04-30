package org.sanyanse.ravi.test;

import org.sanyanse.ravi.util.LexicographicSelector;

public class TestLexicographicSelector {
	public static void main(String[] args) {
		//test1();
		test2();
	}
	
	private static void test1() {
		LexicographicSelector selector = new LexicographicSelector(10, 10);
		int i=0;
		for (i=0; i<10; i++) {
			print(i, selector.next());
			selector.keep();
		}
		while (selector.hasNext() && i < 1000) {
			print(i++, selector.next());
		}
	}
	
	private static void test2() {
		LexicographicSelector selector = new LexicographicSelector(10, 10);
		int i=0;
		while (selector.hasNext()) {
			print(++i, selector.next());
			if (selector.canKeep()) {
				selector.keep();
			}
		}
	}
	
	private static void print(int iterationNumber, boolean[] selection) {
		System.out.print(iterationNumber);
		System.out.print("> ");
		for (boolean b : selection) {
			System.out.print(b ? 1 : 0);
			System.out.print('.');
		}
		System.out.println();
	}
}
