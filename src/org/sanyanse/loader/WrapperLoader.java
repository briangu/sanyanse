package org.sanyanse.loader;

import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphLoader;

public class WrapperLoader implements GraphLoader
{
  private Graph _graph;

  public WrapperLoader(Graph graph)
  {
    _graph = graph;
  }

  @Override
  public Graph load()
  {
    return _graph;
  }
}
