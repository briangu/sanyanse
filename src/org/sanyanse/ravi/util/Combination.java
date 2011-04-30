package org.sanyanse.ravi.util;

import java.util.Iterator;

public class Combination implements Iterator<Object[]> {
	private Object[] m_list;
	private int m_choiceSize;
	private int[] m_currentChoiceIndex;
	private boolean m_hasNext;
	private Object[] m_currentChoice;
	private boolean m_choiceInitialized;

	public Combination(Object[] list, int size) {
		m_list = list;
		m_choiceSize = size;
		
		m_currentChoiceIndex = new int[m_choiceSize];
		m_currentChoice = new Object[m_choiceSize];

		m_choiceInitialized = false;
	}
	
	public Iterator<Object[]> iterator() {
		return this;
	}
	
	public boolean hasNext() {
		// Determine the next choice.
		if (!m_choiceInitialized) {
			for (int i=0; i<m_choiceSize; i++) {
				m_currentChoiceIndex[i] = i;
			}
			m_hasNext = true;
			m_choiceInitialized = true;
		} else {
			increment();
		}
		return m_hasNext;
	}
	
	public Object[] next() {
		// Construct the current-choice set.
		for (int i=0; i<m_choiceSize; i++) {
			m_currentChoice[i] = m_list[m_currentChoiceIndex[i]];
		}
		return m_currentChoice;
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	private void increment() {
		// Determine the first one that has not reached its max.
		// Increment that by one. All subsequent ones are set
		// in increasing order following it.
		for (int i=m_choiceSize-1; i>=0; i--) {
			if (m_currentChoiceIndex[i] < (m_list.length - (m_choiceSize - i))) {
				// Found the first choice that hasn't reached its max.
				// Increment it by one.
				m_currentChoiceIndex[i]++;
				for (int j=i+1; j<m_choiceSize; j++) {
					m_currentChoiceIndex[j] = m_currentChoiceIndex[i] + j - i;
				}
				return;
			}
		}
		m_hasNext = false;
	}
}
