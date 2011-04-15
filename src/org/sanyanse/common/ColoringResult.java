package org.sanyanse.common;


import java.util.List;


public class ColoringResult
{
  public boolean IsColored;
  public List<Coloring> Colorings;

  public ColoringResult(boolean isColored) {
    IsColored = isColored;
  }

  public void addColoring(final String id, final int color) {
    if (!IsColored) {
      throw new IllegalArgumentException("graph is not colorable");
    }
    Colorings.add(new Coloring() {{ NodeId = id; Color = color; }});
  }

  public class Coloring {
    public String NodeId;
    public int Color;
  }
}
