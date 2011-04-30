package org.sanyanse.ravi.util;

public class LexicographicCounter {
	private int m_base;
	private int m_currentMaxDigits;
	private BaseNCounter[] m_counter;
	private int[][] m_count;
	
	private boolean m_firstValueRead = false;
	
	private int[][] m_output;

	public LexicographicCounter(int base, int maxDigits) {
		m_base = base;
		m_currentMaxDigits = 1;
		m_counter = new BaseNCounter[maxDigits];
		m_count = new int[maxDigits][];
		for (int i=0; i<maxDigits; i++) {
			m_counter[i] = new BaseNCounter(m_base, i+1);
			m_count[i] = new int[i+1];
		}
		
		// Initialize m_output which is a optimization.
		m_output = new int[maxDigits][];
		for (int i=0; i<maxDigits; i++) {
			m_output[i] = new int[i+1];
		}
	}
	
	public void deeper() {
		if (m_currentMaxDigits < m_count.length) {
			// Transfer current count to the longer counter.
			m_count[m_currentMaxDigits][0] = 0;
			for (int i=0; i<m_currentMaxDigits; i++) {
				m_count[m_currentMaxDigits][i+1] = m_count[m_currentMaxDigits-1][i];
			}
			m_counter[m_currentMaxDigits].setCounter(m_count[m_currentMaxDigits]);
			m_currentMaxDigits++;
		}
	}
	
	public int[] increment() {
		if (m_firstValueRead) {
			while ((m_currentMaxDigits > 0) && (m_count[m_currentMaxDigits-1][0] == (m_base - 1))) {
				// We are done with the counter. We need to drop by one digit.
				m_currentMaxDigits--;
			}
			if (m_currentMaxDigits > 0) {
				m_count[m_currentMaxDigits-1] = m_counter[m_currentMaxDigits-1].next();
				return reverse(m_count[m_currentMaxDigits-1]);
			}
		} else {
			m_count[m_currentMaxDigits-1] = m_counter[m_currentMaxDigits-1].next();
			m_firstValueRead = true;
			return reverse(m_count[m_currentMaxDigits-1]);
		}
		return null;
	}
	
	public boolean hasNext() {
		if (m_firstValueRead) {
			int currentDigits = m_currentMaxDigits;
			while ((currentDigits > 0) && (m_count[currentDigits-1][0] == (m_base - 1))) {
				// We are done with the counter. We need to drop by one digit.
				currentDigits--;
			}
			return currentDigits > 0;
		} else {
			return true;
		}
	}
	
	// Returns a copy of elements of the given array in reverse order.
	private int[] reverse(int[] array) {
		int[] tempArray = m_output[array.length-1]; // this array has array.length size.
		int tempLength = array.length;
		for (int i=0; i<tempLength; i++) {
			tempArray[i] = array[tempLength - i - 1];
		}
		return tempArray;
	}
}
