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
    Map<String, Integer> colorings = new HashMap<String, Integer>();

    final Map<Integer, Integer[]> colorChoiceMap = new HashMap<Integer, Integer[]>() {{
      put(0, new Integer[] {1,2,3});
      put(1, new Integer[] {2,3});
      put(2, new Integer[] {1,3});
      put(3, new Integer[] {1,2});
    }};

    class StackNode {
      String Id;
      Iterator Iter;
      public StackNode(String id, Iterator iter) {
        Id = id;
        Iter = iter;
      }
    }

    Stack<StackNode> stack = new Stack<StackNode>();

    stack.push(new StackNode(_spec.Nodes.get(0), Arrays.asList(colorChoiceMap.get(0)).iterator()));

    while (stack.size() > 0) {
      final StackNode node = stack.pop();

      colorings.remove(node.Id);

      List<String> neighbors = _spec.Edges.get(node.Id);

      while (node.Iter.hasNext()) {
        Integer color = (Integer) node.Iter.next();

        Boolean haveValidColor = true;

        List<StackNode> nextNodes = null;

        for (String neighbor : neighbors) {
          if (colorings.containsKey(neighbor)) {
            if (colorings.get(neighbor) == color) {
              haveValidColor = false;
              break;
            }
          }
          else
          {
            if (nextNodes == null)
            {
              nextNodes = new ArrayList<StackNode>(neighbors.size() + 1) {{ add(node); }};
            }
            nextNodes.add(new StackNode(neighbor, Arrays.asList(colorChoiceMap.get(color)).iterator()));
          }
        }

        if (haveValidColor) {
          colorings.put(node.Id, color);
          if (nextNodes != null)
          {
            stack.addAll(nextNodes);
          }
          break;
        }
      }

      if (colorings.size() == _spec.NodeCount) {
        break;
      }
    }

    ColoringResult result =
      (colorings.size() == _spec.NodeCount)
        ? ColoringResult.create(_spec, colorings)
        : ColoringResult.createNotColorableResult();

    return result;
  }
}
