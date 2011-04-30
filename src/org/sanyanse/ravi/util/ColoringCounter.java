package org.sanyanse.ravi.util;

public class ColoringCounter {
	private int m_totalChoices;
	private int m_maxChoices;
	
	private int[] m_enumeration;
	private int[] m_output; // Only for output, so caller doesn't mess up our enumeration array.
	
	private int m_rightMost1Index = -1;
	private int m_num1s = 0;
	
	public ColoringCounter(int totalChoices, int maxChoices) {
		m_totalChoices = totalChoices;
		m_maxChoices = maxChoices;
		m_enumeration = new int[m_totalChoices];
		m_output = new int[m_totalChoices];
	}
	
	public int[] next() {
		if (m_num1s < m_maxChoices) {
			if (m_rightMost1Index < m_totalChoices - 1) {
				// We can choose more without giving up our current choices.
				m_enumeration[++m_rightMost1Index] = 1;
				m_num1s++;
			} else {
				// We have reached the end of our count.
				// There is nothing more left to count.
				return null;
			}
		} else {
			// We have to drop some of our current choices.
			if (m_rightMost1Index < m_totalChoices - 1) {
				// We can just drop the last choice and
				// choose the one after it.
				m_enumeration[m_rightMost1Index] = 0;
				m_enumeration[++m_rightMost1Index] = 1;
			} else {
				// We have reached the end of our choice array.
				// We need to retrace and drop some of our choices
				// and make pick up another choice.
				while ((m_rightMost1Index >= 0) && (m_enumeration[m_rightMost1Index] == 1)) {
					m_enumeration[m_rightMost1Index--] = 0;
					m_num1s--;
				}
				if (m_rightMost1Index >= 0) {
					// Now we need to find the right most 1 index,
					// since it is now pointing to a 0.
					while ((m_rightMost1Index >= 0) && (m_enumeration[m_rightMost1Index] == 0)) {
						m_rightMost1Index--;
					}
					if (m_rightMost1Index >= 0) {
						m_enumeration[m_rightMost1Index] = 0;
						m_enumeration[++m_rightMost1Index] = 1;
					} else {
						// We have reached the end of our count.
						// There is nothing more left to count.
						// This is the only valid end case.
						return null;
					}
				} else {
					// We have reached the end of our count.
					// There is nothing more left to count.
					// We should never reach this condition though.
					return null;
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
