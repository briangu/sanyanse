package org.sanyanse.ravi.util;


public class LexicographicColoringCounter {
	private int m_totalChoices;
	private int m_maxChoices;

	private int m_rightMost1Index = -1;
	private int m_num1s = 0;

	private int[] m_enumeration;
	private int[] m_output; // Only for output, so caller doesn't mess up our enumeration array.

	private boolean m_goDeeper = false;
	
	public LexicographicColoringCounter(int totalChoices, int maxChoices) {
		m_totalChoices = totalChoices;
		m_maxChoices = maxChoices;
		m_enumeration = new int[m_totalChoices];
		m_output = new int[m_totalChoices];
	}
	
	public boolean hasNext() {
		// If there are any zeros beyond the leftmost 1,
		// we have choices remaining.
		// Determine the leftmost 1.
		int leftMost1Index = -1;
		for (int i=0; i<m_totalChoices; i++) {
			if (m_enumeration[i] == 1) {
				leftMost1Index = i;
				break;
			}
		}
		for (int i=leftMost1Index + 1; i<m_totalChoices; i++) {
			if (m_enumeration[i] == 0) {
				return true;
			}
		}

		// Left most 1 index has been exhausted.
		if (leftMost1Index < m_totalChoices - 1) {
			// We still have more choices to the right of left-most 1 index.
			return true;
		}
		
		return false;
	}
	
	public boolean hasDeeper() {
		return ((m_num1s < m_maxChoices) && (m_rightMost1Index < m_totalChoices - 1));
	}
	
	public void deeper() {
		if (hasDeeper()) {
			m_goDeeper = true;
		} else {
			throw new RuntimeException("Cannot go deeper than " + m_maxChoices);
		}
	}
	
	public int[] next() {
		if (m_goDeeper) {
			m_goDeeper = false;
			// We have already verified the array index in deeper().
			m_enumeration[++m_rightMost1Index] = 1;
			m_num1s++;
		} else {
			if (m_rightMost1Index < m_totalChoices - 1) {
				// Move rightmost 1 to the right by one.
				if (m_rightMost1Index >= 0) {
					m_enumeration[m_rightMost1Index] = 0;
				}
				m_enumeration[++m_rightMost1Index] = 1;
			} else {
				// No room to move rightmost 1 to the right.
				// Remove all rightmost 1s.
				while ((m_rightMost1Index >= 0) && (m_enumeration[m_rightMost1Index] == 1)) {
					m_enumeration[m_rightMost1Index--] = 0;
					m_num1s--;
				}
				// Now m_rightMost1Index is not pointing to a 1.
				// Rectify this situation and move the rightmost 1
				// to the right by one.
				if (m_rightMost1Index >= 0) {
					for (int i=m_rightMost1Index - 1; i >= 0; i--) {
						if (m_enumeration[i] == 1) {
							m_enumeration[i] = 0;
							m_enumeration[i+1] = 1;
							m_rightMost1Index = i+1;
							break;
						}
					}
				} else {
					m_rightMost1Index = 1;
					m_enumeration[m_rightMost1Index] = 1;
					m_num1s = 1;
				}
			}
		}
		copy(m_enumeration, m_output);
		return m_output;
	}
	
	private static void copy(int[] source, int[] dest) {
		for (int i=0; i<source.length; i++) {
			dest[i] = source[i];
		}
	}
}
