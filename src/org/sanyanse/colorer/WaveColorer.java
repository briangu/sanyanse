package org.sanyanse.colorer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphSpec;


public class WaveColorer implements GraphColorer
{
  GraphSpec _spec;

  public WaveColorer(GraphSpec spec) {
    _spec = spec;
  }

  @Override
  public ColoringResult call()
      throws Exception
  {
    Thread.sleep(10000);
    ColoringResult result = ColoringResult.createNotColorableResult();
    return result;
  }
}
