package org.sanyanse.common;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class ColoringResult
{
  public final boolean IsColored;
  public List<Coloring> Colorings;

  private ColoringResult(boolean isColored) {
    IsColored = isColored;
  }

  private ColoringResult(List<Coloring> colorings) {
    this(true);
    Colorings = Collections.unmodifiableList(colorings);
  }

  public static ColoringResult createNotColorableResult() {
    return new ColoringResult(false);
  }

  public static ColoringResult create(GraphSpec spec, final Map<String, Integer> colorMap) {
    List<Coloring> colorings = new ArrayList<Coloring>(colorMap.size());
    for (final String id : spec.Nodes) {
      colorings.add(new Coloring() {{ NodeId = id; Color = colorMap.get(id); }});
    }
    return new ColoringResult(colorings);
  }
}
