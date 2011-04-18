package org.sanyanse.common;

import java.util.Collections;
import java.util.List;

public class GraphSpecNode
{
  String _id;
  List<GraphSpecNode> _neighbors;

  public GraphSpecNode(String id, List<GraphSpecNode> neighbors)
  {
    _id = id;
    _neighbors = Collections.unmodifiableList(neighbors);
  }

  public String getId() {
    return _id;
  }

  public List<GraphSpecNode> getNeighbors() {
    return _neighbors;
  }
}
