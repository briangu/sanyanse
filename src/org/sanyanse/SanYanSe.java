package org.sanyanse;


import java.util.ArrayList;
import java.util.List;
import org.sanyanse.common.ColoringResult;
import org.sanyanse.common.ColoringResultWriter;
import org.sanyanse.common.GraphColorer;
import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.GraphSpec;


public class SanYanSe
{
  public static void main(String[] args)
  {
    GraphLoader loader;
    //= FileLoader.create(args[0]);
    loader = new MemoryLoader();
    GraphSpec graphSpec = loader.load();

    List<GraphColorer> colorers = new ArrayList<GraphColorer>();
    colorers.add(new WaveColorer(graphSpec));
//    colorers.add(new BacktrackColorer(graphSpec));

    MultiColorer mc = MultiColorer.create(colorers);

    try
    {
      ColoringResult result = mc.call();
      if (result == null)
      {
        result = new ColoringResult(false);
      }

      String outfileName = String.format("%s_%s_out", "sanyanse", args[0]);
      ColoringResultWriter writer = FileResultWriter.create(outfileName, result);
      writer.write();
    }
    catch (Exception e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }
}
