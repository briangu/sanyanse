package org.sanyanse.common;


import java.util.Map;

public class ColoringResult
{
  public final boolean IsColored;
  public final Graph Graph;
  public final Map<Vertex, Integer> ColorMap;

  private ColoringResult() {
    IsColored = false;
    Graph = null;
    ColorMap = null;
  }

  private ColoringResult(Graph graph) {
    IsColored = true;
    Graph = graph;
    ColorMap = null;
  }

  private ColoringResult(Map<Vertex, Integer> colorMap) {
    IsColored = true;
    ColorMap = colorMap;
    Graph = null;
  }

  public static ColoringResult createNotColorableResult() {
    return new ColoringResult();
  }

  public static ColoringResult createColoredGraphResult(Graph graph) {
    return new ColoringResult(graph);
  }

  public static ColoringResult createColoredGraphResult(Map<Vertex, Integer> colorMap) {
    return new ColoringResult(colorMap);
  }
}
