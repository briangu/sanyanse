package org.sanyanse.ravi.util;

public class LexicographicSelector {
	private int m_totalSelections;
	private int m_maxSelections;
	
	// Index into selection array, indicating first selection.
	private int m_firstSelection;

	// Index into selection array, indicating last selection.
	private int m_latestSelection;

	// Number of selections in the current selection.
	private int m_numSelections;
	
	// Current selection.
	private boolean[] m_selection;
	
	// Only for output purposes, a copy of the current selection.
	private boolean[] m_output;
	
	// State variable to determine whether
	// to keep the latest in the enumeration.
	private boolean m_keepLatest;
	
	public LexicographicSelector(int totalSelections, int maxSelections) {
		m_totalSelections = totalSelections;
		m_maxSelections = maxSelections;
		m_selection = new boolean[totalSelections];
		m_output = new boolean[totalSelections];
		
		m_firstSelection = -1;
		m_latestSelection = -1;
		m_numSelections = 0;
		
		m_keepLatest = false;
	}
	
	public boolean hasNext() {
		return m_firstSelection < m_totalSelections - 1;
	}
	
	public boolean canKeep() {
		return (m_latestSelection < m_totalSelections - 1)
				&& (m_numSelections < m_maxSelections);
	}

	public void keep() {
		if (canKeep()) {
			m_keepLatest = true;
		}
	}
	
	public boolean[] next() {
		// By default, we change our latest selection
		// unless we have been asked to keep it.
		if (m_keepLatest) {
			m_keepLatest = false;
			m_latestSelection++;
			m_selection[m_latestSelection] = true;
			m_numSelections++;
		} else {
			int selectionToChange = m_latestSelection;
			if (m_latestSelection >= m_totalSelections - 1) {
				// There is no room to select beyond
				// our current selection. Void our current
				// selection and determine the first one
				// we have been asked to keep. That selection
				// will be changed.
				m_selection[m_latestSelection] = false;
				// m_latestSelection is no longer accurate.
				m_numSelections--;
				// Determine the latest selection that 
				// we have been asked to keep.
				for (int i=m_latestSelection-1; i>= 0; i--) {
					if (m_selection[i] == true) {
						selectionToChange = i;
						break;
					}
				}
			}
			
			if (selectionToChange >= 0) {
				if (selectionToChange < m_totalSelections) {
					m_selection[selectionToChange] = false;
					m_latestSelection = selectionToChange + 1;
					m_selection[m_latestSelection] = true;
				}
			} else {
				m_selection[0] = true;
				m_latestSelection = 0;
				m_numSelections = 1;
			}
			
			if (m_numSelections == 1) {
				m_firstSelection = m_latestSelection;
			}
		}
		
		copy(m_selection, m_output);
		return m_output;
	}

	private static void copy(boolean[] source, boolean[] dest) {
		for (int i=0; i<source.length; i++) {
			dest[i] = source[i];
		}
	}
}
