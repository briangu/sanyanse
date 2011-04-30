package org.sanyanse.ravi.util;

import java.util.Iterator;

public class BaseNCounter implements Iterator<int[]>{
	private int m_base;
	private int m_maxDigits;
	private int[] m_currentDigits;
	
	// Used to determine if 0 was output or not.
	// If we don't have this variable,
	// because next() is used to read the current value,
	// the first value returned is 1, which is not right.
	private boolean m_firstValueRead = false;
	
	public BaseNCounter(int base, int maxDigits) {
		m_base = base;
		m_maxDigits = maxDigits;
		m_currentDigits = new int[m_maxDigits];
		reset();
	}
	
	private void reset() {
		for (int i=0; i<m_maxDigits; i++) {
			m_currentDigits[i] = 0;
		}
		m_firstValueRead = false; // Zero has not been read/output yet.
	}
	
	private void reset(int[] value) {
		if (value.length == m_maxDigits) {
			for (int i=0; i<m_maxDigits; i++) {
				m_currentDigits[i] = value[i];
			}
			m_firstValueRead = false; // Zero has not been read/output yet.
		} else {
			throw new IllegalArgumentException("digits don't match");
		}
	}
	
	public void setCounter(int[] value) {
		reset(value);
	}
	
	public boolean hasNext() {
		if (m_firstValueRead) {
			boolean maxReached = true;
			for (int i=0; i<m_maxDigits; i++) {
				if (m_currentDigits[i] != (m_base - 1)) {
					maxReached = false;
					break;
				}
			}
			return !maxReached;
		} else {
			return true; // implication: 0^0 is also allowed!
		}
	}
	
	public int[] next() {
		if (m_firstValueRead) {
			// Determine the first digit that we can increment.
			// All digits below it get initialized to zero.
			int firstDigitToIncrement = m_maxDigits;
			for (int i=0; i<m_maxDigits; i++) {
				if (m_currentDigits[i] < (m_base - 1)) {
					firstDigitToIncrement = i;
					break;
				}
			}
	
			if (firstDigitToIncrement < m_maxDigits) {
				m_currentDigits[firstDigitToIncrement]++;
				for (int i=0; i<firstDigitToIncrement; i++) {
					m_currentDigits[i] = 0;
				}
			}
		} else {
			// Zero has not been read yet.
			// Mark it as been read and output it.
			m_firstValueRead = true;
		}

		return m_currentDigits;
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=m_maxDigits - 1; i>= 0; i--) {
			sb.append(m_currentDigits[i]);
			sb.append(".");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1); // remove "." from the end.
		}
		return sb.toString();
	}
}
