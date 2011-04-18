package org.sanyanse.common;

import java.util.HashMap;
import java.util.Map;

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
}
