package org.sanyanse.loader;


import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphBuilder;
import org.sanyanse.common.GraphLoader;


public class MemoryLoader implements GraphLoader
{
  @Override
  public Graph load()
  {
    GraphBuilder builder = new GraphBuilder(5);

/*
    builder.addNode("A", new String[] {"B", "C"} );
    builder.addNode("B", new String[] {"A", "C", "D"} );
    builder.addNode("C", new String[] {"A", "B", "D"} );
    builder.addNode("D", new String[] {"B", "C"} );
*/

    // not colorable
    builder.addNode("A", new String[] {"B", "C", "D", "E"});
    builder.addNode("B", new String[] {"A", "C"});
    builder.addNode("C", new String[] {"A", "B", "D", "E"});
    builder.addNode("D", new String[] {"A", "C", "E"});
    builder.addNode("E", new String[] {"A", "C", "D"});

    return builder.build();
  }
}
