/**
 * SanYanSe
 *
 * @Author Brian Guarraci
 *
 */
package org.sanyanse.common;


import java.util.*;


public class Util
{
  static Map<Integer, String> idMap = new HashMap<Integer, String>();

  public static String getNodeName(int id) {
    int origId = id;

    synchronized (idMap) {
      if (idMap.containsKey(id)) {
        return idMap.get(id);
      }
    }

    StringBuilder sb = new StringBuilder();

    while (id > 0) {
      int digit = ((id - 1) % 26);
      sb.append((char)('A' + (char)digit));
      id = (id - 1) / 26;
    }

    String name = sb.reverse().toString();

    synchronized (idMap) {
      idMap.put(origId, name);
    }

    return name;
  }

  public static String join(Collection<?> s, String delimiter) {
    StringBuilder builder = new StringBuilder();
    Iterator iter = s.iterator();
    while (iter.hasNext()) {
      builder.append(iter.next().toString());
      if (!iter.hasNext()) {
        break;
      }
      builder.append(delimiter);
    }
    return builder.toString();
  }

  public static String join(Graph graph, short[] s, String delimiter) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < s.length; i++)
    {
      builder.append(graph.VertexIds[s[i]]);
      if (i == s.length - 1) {
        continue;
      }
      builder.append(delimiter);
    }
    return builder.toString();
  }

  public static <T> Set<T> intersect(Set<T> s1, Set<T> s2)
  {
    Set<T> intersection = new HashSet<T>(s1);
    intersection.retainAll(s2);
    return intersection;
  }
}
