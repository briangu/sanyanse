/**
 * SanYanSe
 *
 * @Author Brian Guarraci
 *
 */
package org.sanyanse.common;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Util
{
  static Map<Integer, String> idMap = new HashMap<Integer, String>();

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

  public static <T> Set<T> intersect(Set<T> s1, Set<T> s2)
  {
    Set<T> intersection = new HashSet<T>(s1);
    intersection.retainAll(s2);
    return intersection;
  }
}
