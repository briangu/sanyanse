package org.sanyanse.colorer;


import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphSpec;

import java.util.*;


public class BetterBacktrackColorer implements GraphColorer
{
  GraphSpec _spec;

  public BetterBacktrackColorer(GraphSpec spec)
  {
    _spec = spec;
  }

  @Override
  public ColoringResult call()
    throws Exception
  {
    final Map<Integer, Integer[]> colorChoiceMap = new HashMap<Integer, Integer[]>() {{
      put(0, new Integer[] {1,2,3});
      put(1, new Integer[] {2,3});
      put(2, new Integer[] {1,3});
      put(3, new Integer[] {1,2});
    }};

    class StackNode {
      String Id;
      Iterator Iter;
      Map<String, Integer> Colorings;
      public StackNode(String id, Iterator iter, Map<String, Integer> colorings) {
        Id = id;
        Iter = iter;
        Colorings = colorings;
      }
    }

    Stack<StackNode> stack = new Stack<StackNode>();

    stack.push(
        new StackNode(
          _spec.Nodes.get(0),
          Arrays.asList(colorChoiceMap.get(0)).iterator(),
          new HashMap<String, Integer>()));

    ColoringResult result = null;

    while (stack.size() > 0) {
      final StackNode node = stack.pop();

      Map<String, Integer> colorings = node.Colorings;

      List<String> neighbors = _spec.Edges.get(node.Id);

      while (node.Iter.hasNext()) {

        Integer color = (Integer) node.Iter.next();

        Boolean haveValidColor = true;

        List<String> nextNodes = null;

        for (String neighborId : neighbors) {
          if (colorings.containsKey(neighborId)) {
            if (colorings.get(neighborId) == color) {
              haveValidColor = false;
              break;
            }
          }
          else
          {
            if (nextNodes == null)
            {
              nextNodes = new ArrayList<String>(neighbors.size() + 1);
            }
            nextNodes.add(neighborId);
          }
        }

        if (haveValidColor) {
          if (nextNodes == null)
          {
            colorings.put(node.Id, color);
            if (colorings.size() == _spec.NodeCount) {
              result = ColoringResult.create(_spec, colorings);
              break;
            }
          }
          else
          {
            Map<String, Integer> newColorings = new HashMap<String, Integer>(colorings);
            newColorings.put(node.Id, color);
            if (newColorings.size() == _spec.NodeCount) {
              result = ColoringResult.create(_spec, newColorings);
              break;
            }

            stack.push(node);

            for (String neighborId : nextNodes)
            {
              stack.push(
                  new StackNode(
                      neighborId,
                      Arrays.asList(colorChoiceMap.get(color)).iterator(),
                      newColorings));
            }
          }

          break;
        }
      }
    }

    result = result == null ? ColoringResult.createNotColorableResult() : result;

    return result;
  }
}
