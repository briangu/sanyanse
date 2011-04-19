package org.sanyanse.loader;


import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.GraphSpec;


public class PetersenLoader implements GraphLoader
{
  @Override
  public GraphSpec load()
  {
    GraphSpec spec = new GraphSpec(10);

    spec.addNode("A", new String[] {"E", "F", "G"});
    spec.addNode("B", new String[] {"A", "G", "C"});
    spec.addNode("C", new String[] {"B", "H", "D"});
    spec.addNode("D", new String[] {"C", "I", "E"});
    spec.addNode("E", new String[] {"A", "D", "J"});
    spec.addNode("F", new String[] {"A", "I", "H"});
    spec.addNode("G", new String[] {"B", "J", "I"});
    spec.addNode("H", new String[] {"C", "F", "J"});
    spec.addNode("I", new String[] {"D", "F", "G"});
    spec.addNode("J", new String[] {"E", "G", "H"});

    return spec;
  }
}
