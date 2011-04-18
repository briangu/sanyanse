package org.sanyanse.loader;


import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.GraphSpec;


public class MemoryLoader implements GraphLoader
{
  @Override
  public GraphSpec load()
  {
    GraphSpec spec = new GraphSpec(5);

/*
    spec.addNode("A", new String[] {"B", "C"} );
    spec.addNode("B", new String[] {"A", "C", "D"} );
    spec.addNode("C", new String[] {"A", "B", "D"} );
    spec.addNode("D", new String[] {"B", "C"} );
*/

    // not colorable
    spec.addNode("A", new String[] {"B", "C", "D", "E"});
    spec.addNode("B", new String[] {"A", "C"});
    spec.addNode("C", new String[] {"A", "B", "D", "E"});
    spec.addNode("D", new String[] {"A", "C", "E"});
    spec.addNode("E", new String[] {"A", "C", "D"});

    return spec;
  }
}
