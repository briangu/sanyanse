package org.sanyanse.ravi.test;

import java.util.Iterator;

import org.sanyanse.ravi.util.Combination;

public class TestCombination {
	public static void main(String[] args) {
		String[] s = {"a", "b", "c", "d", "e"};
		Combination combination = new Combination(s, 3);
		Iterator<Object[]> iter = combination.iterator();
		while (iter.hasNext()) {
			Object[] combo = iter.next();
			for (int i=0; i<combo.length; i++) {
				System.out.print(combo[i] + " ");
			}
			System.out.println();
		}
	}
}
