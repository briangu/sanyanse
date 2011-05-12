import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuddenDeathGraphGenerator {
	private int m_partitionSize;
	
	private Map<Integer, List<Integer>> s1;
	private Map<Integer, List<Integer>> s2;
	private Map<Integer, List<Integer>> s3;

	public SuddenDeathGraphGenerator(int n) {
		m_partitionSize = n;
		s1 = new HashMap<Integer, List<Integer>>(m_partitionSize, 1.0F);
		s2 = new HashMap<Integer, List<Integer>>(m_partitionSize + 2, 1.0F);
		s3 = new HashMap<Integer, List<Integer>>(m_partitionSize, 1.0F);
	}
	
	public void generate() throws Exception {
		long startTime = System.currentTimeMillis();
		
		// Create vertices.
		for (int i=0; i<m_partitionSize; i++) {
			addVertex(i, s1);
			addVertex(m_partitionSize + i, s2);
			addVertex(2*m_partitionSize + i, s3);
		}
		
		// Each i-th vertex in s1 is connected to i-th vertex in s2.
		for (int i=0; i<m_partitionSize; i++) {
			addEdge(i, m_partitionSize + i, s1, s2);
		}

		// Each i-th vertex in s1 is connected to ceil(i/3) vertices in S3
		// in round-robin fashion in s3.
		int s3index = -1;
		for (int i=0; i<m_partitionSize; i++) {
			int numEdgesFromVertexInS1 = (i/3) + 1;
			for (int j=0; j<numEdgesFromVertexInS1; j++) {
				addEdge(i, 2 * m_partitionSize + (++s3index % m_partitionSize), s1, s3);
			}
		}
		
		// i-th vertex in s2 is connected to n/3 + ceil(i/3) vertices in S3
		// in round-robin fashion in s3.
		int temp = m_partitionSize / 3;
		for (int i=0; i<m_partitionSize; i++) {
			int numEdgesFromVertexInS2 = temp + (i/3) + 1;
			for (int j=0; j<numEdgesFromVertexInS2; j++) {
				addEdge(m_partitionSize + i, 2 * m_partitionSize + (++s3index % m_partitionSize), s2, s3);
			}
		}
		
		// Add 2 vertices in s2.
		// Each of these is connected to half of the vertices in s3.
		int vertex = 3 * m_partitionSize;
		addVertex(vertex, s2);
		temp = m_partitionSize / 2;
		for (int i=0; i<temp; i++) {
			addEdge(vertex, 2*m_partitionSize + i, s2, s3);
		}

		vertex++;
		addVertex(vertex, s2);
		for (int i=temp; i<m_partitionSize; i++) {
			addEdge(vertex, 2*m_partitionSize + i, s2, s3);
		}
		
		long endTime1 = System.currentTimeMillis();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("out", false));
		
		// Write out the number of vertices.
		writer.write(String.valueOf(3 * m_partitionSize + 2));
		writer.write('\n');
		
		// Write out individual partitions.
		print(writer, s1);
		print(writer, s2);
		print(writer, s3);
		
		writer.close();
		
		long endTime2 = System.currentTimeMillis();
		System.out.println("Computed in " + (endTime1 - startTime) + " ms. Wrote in " + (endTime2 - endTime1)  + " ms.");
	}
	
	private static void addVertex(int v, Map<Integer, List<Integer>> partition) {
		partition.put(v, new ArrayList<Integer>());
	}
	
	private static void addEdge(int va, int vb, Map<Integer, List<Integer>> partitionA, Map<Integer, List<Integer>> partitionB) {
		List<Integer> lista = partitionA.get(va);
		lista.add(vb);

		List<Integer> listb = partitionB.get(vb);
		listb.add(va);
	}
	
	private static void print(Writer writer, Map<Integer, List<Integer>> partition) throws Exception {
		int maxAdjacencies = 0;
		int minAdjacencies = Integer.MAX_VALUE;
		
		for (Map.Entry<Integer, List<Integer>> entry : partition.entrySet()) {
			List<Integer> adjacencies = entry.getValue();

			//writer.write(entry.getKey().toString());
			print(writer, base26(entry.getKey()));
			writer.write(':');
			boolean firstValue = true;
			for (Integer adjacentVertex : adjacencies) {
				if (firstValue) {
					firstValue = false;
				} else {
					writer.write(',');
				}
				//writer.write(adjacentVertex.toString());
				print(writer, base26(adjacentVertex));
			}
			writer.write('\n');
			
			if (adjacencies.size() > maxAdjacencies) {
				maxAdjacencies = adjacencies.size();
			}
			if (adjacencies.size() < minAdjacencies) {
				minAdjacencies = adjacencies.size();
			}
		}
		
		System.out.println("Partition degrees [min=" + minAdjacencies + ", max=" + maxAdjacencies + "]");
	}
	
	private static final int MAX_BASE26_SIZE = 6;
	private static char[] CHAR_ARRAY = new char[MAX_BASE26_SIZE];
	private static int[] BASE26_ARRAY = new int[] {
		0, // A-Z
		26, // AA-ZZ
		26 + 26*26, // AAA-ZZZ
		26 + 26*26 + 26*26*26, // AAAA-ZZZZ
		26 + 26*26 + 26*26*26 + 26*26*26*26, // AAAAA-ZZZZZ
	};
	
	// Deals with positive numbers only.
	private static int size26(int base10) {
		int size26 = 0;
		for (; (size26 < BASE26_ARRAY.length) && (base10 > BASE26_ARRAY[size26]); size26++);
		return size26;
	}

	private static char[] base26(int base10) {
		for (int i=0; i<MAX_BASE26_SIZE; i++) {
			CHAR_ARRAY[i] = 0;
		}

		base10++;
		
		int size26 = size26(base10);
		base10 = base10 - BASE26_ARRAY[size26-1];

		// Now construct a representation of base10 using size26+1 digits.
		base10--; // Use 0 as starting point instead of 1.
		for (int i=0; i<size26; i++) {
			int rem = base10 % 26;
			CHAR_ARRAY[i] = (char) (65 + rem);
			base10 = base10 - rem;
			base10 = base10 / 26;
		}
		
		return CHAR_ARRAY;
	}
	
	private static void print(Writer writer, char[] c) throws Exception {
		int index = MAX_BASE26_SIZE - 1;
		for (; index >= 0 && (c[index] == 0); index--);
		for (; index >= 0; index--) {
			writer.write(c[index]);
		}
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			SuddenDeathGraphGenerator generator = new SuddenDeathGraphGenerator(Integer.parseInt(args[0]));
			generator.generate();
		} else {
			System.out.println("Usage: java SuddenDeathGraphGenerator <partition-size>");
			System.out.println("Note: <partition-size> should be a multiple of 3.");
			System.out.println("Note: Generated graph has 3 x <partition-size> + 2 vertices.");
		}
	}
}
