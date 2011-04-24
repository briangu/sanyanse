package org.sanyanse.loader;


import org.sanyanse.common.Graph;
import org.sanyanse.common.GraphBuilder;
import org.sanyanse.common.GraphLoader;


public class PetersenLoader implements GraphLoader
{
  @Override
  public Graph load()
  {
    GraphBuilder builder = new GraphBuilder(10);

    builder.addNode("A", new String[]{"E", "F", "G"});
    builder.addNode("B", new String[]{"A", "G", "C"});
    builder.addNode("C", new String[]{"B", "H", "D"});
    builder.addNode("D", new String[]{"C", "I", "E"});
    builder.addNode("E", new String[]{"A", "D", "J"});
    builder.addNode("F", new String[]{"A", "I", "H"});
    builder.addNode("G", new String[]{"B", "J", "I"});
    builder.addNode("H", new String[]{"C", "F", "J"});
    builder.addNode("I", new String[]{"D", "F", "G"});
    builder.addNode("J", new String[]{"E", "G", "H"});

    return builder.build();
  }
}
