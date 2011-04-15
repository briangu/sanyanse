package org.sanyanse;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphSpec;


public class BacktrackColorer implements GraphColorer
{
  GraphSpec _spec;

  public BacktrackColorer(GraphSpec spec) {
    _spec = spec;
  }

  @Override
  public ColoringResult call()
      throws Exception
  {
    return new ColoringResult(false);
  }
}
