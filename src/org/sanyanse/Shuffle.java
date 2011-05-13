package org.sanyanse;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;


/**
 * SanYanSe: 3 Coloring Graph coloring framework
 *
 * Brian Guarraci
 *
 */
public class Shuffle
{
  public static void main(String[] args)
  {
    if (args.length == 0)
    {
      System.out.println("usage: sanyanse <graph filename>");
      return;
    }

    String readFile = args[args.length - 2];
    String outfile = args[args.length - 1];

    System.out.println(readFile);
    System.out.println(outfile);

    int nodeCnt = -1;

    String[] arr = null;
    int i = 0;

    try {
      FileReader fstream = new FileReader(readFile);
      BufferedReader br = new BufferedReader(fstream);

      nodeCnt = Integer.parseInt(br.readLine());

      arr = new String[nodeCnt];

      String strLine;

      while ((strLine = br.readLine()) != null) {
        arr[i++] = strLine;
      }

      br.close();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

    Collections.shuffle(Arrays.asList(arr));

    try
    {
      FileWriter fstream = new FileWriter(outfile);
      BufferedWriter writer = new BufferedWriter(fstream);

      writer.write(String.format("%s", nodeCnt));
      writer.write("\n");

      for (i = 0; i < nodeCnt; i++) {
        writer.write(arr[i]);
        writer.write("\n");
      }

      writer.close();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    catch (IOException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }
}
