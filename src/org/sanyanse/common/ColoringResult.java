package org.sanyanse.common;


public class ColoringResult
{
  public final boolean IsColored;
  public final Graph Graph;

  private ColoringResult() {
    IsColored = false;
    Graph = null;
  }

  private ColoringResult(Graph graph) {
    IsColored = true;
    Graph = graph;
  }

  public static ColoringResult createNotColorableResult() {
    return new ColoringResult();
  }

  public static ColoringResult createColoredGraphResult(Graph graph) {
    return new ColoringResult(graph);
  }
}
