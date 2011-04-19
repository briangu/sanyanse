package org.sanyanse.colorer;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphSpec;


public class BasicBacktrackColorer implements GraphColorer
{
  GraphSpec _spec;

  private static class ColorableNode
  {
    public SettableInteger Node = new SettableInteger(0);
    public String Id;
    public SettableInteger[] Edges;
  }

  private static class SettableInteger
  {
    Integer _value;

    public SettableInteger(Integer l)
    {
      _value = l;
    }

    public Integer get()
    {
      return _value;
    }

    public void set(Integer value)
    {
      _value = value;
    }

    public boolean equals(Object o)
    {
      return ((SettableInteger) o).get() == _value;
    }
  }

  public BasicBacktrackColorer(GraphSpec spec)
  {
    _spec = spec;
  }

  static ColorableNode[] buildColoringArray(GraphSpec spec)
  {
    ColorableNode[] arr = new ColorableNode[spec.NodeCount];

    Map<String, ColorableNode> buildMap = new HashMap<String, ColorableNode>(arr.length);

    int k = 0;

    for (int i = 0; i < spec.NodeCount; i++)
    {
      final String nodeId = spec.Nodes.get(i);
      ColorableNode node;

      if (buildMap.containsKey(nodeId))
      {
        node = buildMap.get(nodeId);
      }
      else
      {
        node = new ColorableNode() {{ Id = nodeId; }};
        arr[k++] = node;
        buildMap.put(nodeId, node);
      }

      List<String> specEdges = spec.Edges.get(spec.Nodes.get(i));
      SettableInteger[] edges = new SettableInteger[specEdges.size()];

      for (int j = 0; j < edges.length; j++)
      {
        final String neighborId = specEdges.get(j);

        if (!buildMap.containsKey(neighborId))
        {
          ColorableNode newNode = new ColorableNode() {{ Id = neighborId; }};
          buildMap.put(neighborId, newNode);
          arr[k++] = newNode;
        }

        edges[j] = buildMap.get(neighborId).Node;
      }

      node.Edges = edges;
    }

    return arr;
  }

  static boolean isColoringValid(ColorableNode[] arr)
  {
    for (int i = 0; i < arr.length; i++)
    {
      long color = arr[i].Node.get();

      if (color == 0) return false;

      SettableInteger[] row = arr[i].Edges;

      // TODO: explore leveraging undirected nature of connection matrix

      for (int x = 0; x < row.length; x++)
      {
        if (row[x].get() == color)
        {
          return false;
        }
      }
    }

    return true;
  }

  private boolean isPartial(ColorableNode[] arr)
  {
    for (int i = 0; i < arr.length; i++)
    {
      long color = arr[i].Node.get();

      if (color == 0) continue;

      SettableInteger[] row = arr[i].Edges;

      // TODO: explore leveraging undirected nature of connection matrix

      for (int x = 0; x < row.length; x++)
      {
        if (row[x].get() == color)
        {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public ColoringResult call()
  {
    final ColorableNode[] arr = buildColoringArray(_spec);

    boolean isColored = isColoringValid(arr);

    int k = 0;

    while ((k >= 0) && !isColored)
    {
      while (arr[k].Node.get() <= 3)
      {
        arr[k].Node.set(arr[k].Node.get() + 1);

        if (isColoringValid(arr))
        {
          isColored = true;
          break;
        }

        if (isPartial(arr))
        {
          k++;
        }
      }

      if (!isColored)
      {
        arr[k].Node.set(0);
        k--;
      }
    }

    ColoringResult result =
        isColored
        ? createResult(arr)
        : ColoringResult.createNotColorableResult();

    return result;
  }

  private ColoringResult createResult(ColorableNode[] arr)
  {
    Map<String, Integer> colorMap = new HashMap<String, Integer>();

    for (ColorableNode node : arr) {
      colorMap.put(node.Id, node.Node.get());
    }

    return ColoringResult.create(_spec, colorMap);
  }
}
