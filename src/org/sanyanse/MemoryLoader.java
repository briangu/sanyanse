package org.sanyanse;


import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.GraphSpec;


public class MemoryLoader implements GraphLoader
{
  @Override
  public GraphSpec load()
  {
    GraphSpec spec = new GraphSpec(4);

    spec.addNode("A", new String[] {"B", "C"} );
    spec.addNode("B", new String[] {"A", "D"} );
    spec.addNode("C", new String[] {"A", "D"} );
    spec.addNode("D", new String[] {"B", "C"} );

    return spec;
  }
}
