package org.sanyanse.common;


import org.sanyanse.colorer.BacktrackColorer;
import org.sanyanse.colorer.MultiColorer;
import org.sanyanse.colorer.WaveColorer;
import org.sanyanse.loader.MemoryLoader;
import org.sanyanse.writer.StdoutResultWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


public class PerfTests
{
  private static final int ARRSIZE = 128;

  public static class SettableLong
  {
    Long _value;

    public SettableLong(long l)
    {
      _value = l;
    }

    public Long get() {
      return _value;
    }

    public void set(long value) {
      _value = value;
    }

    public boolean equals(Object o) {
      return ((SettableLong) o).get() == _value;
    }
  }

  public static void mainTest(String[] args) {
    testfloats();
    testLongs();
    testBoxedLongs();
    testSettablesLongs();

    SettableLong[] arr = new SettableLong[5];
    arr[0] = new SettableLong(0);
    arr[1] = arr[0];
    arr[2] = new SettableLong(1);
    arr[3] = arr[2];
    arr[4] = new SettableLong(2);
    arr[0].set(12);
  }

  private static void testBoxedLongs() {
    Long[][] arr = buildBoxedLongArray();
    Long[] base = new Long[arr.length];
    for (int i = 0; i < base.length; i++) {
      base[i] = new Long(0);
    }

    StopWatch sw = new StopWatch();
    sw.start();
    for (int i = 0; i < 10000; i++) {
      cmpArrays(arr, base);
    }
    sw.stop();
    System.out.print(sw.getDuration());
    System.out.print("\n");
  }

  private static void testSettablesLongs() {
    SettableLong[][] arr = buildSettableLongArray();
    SettableLong[] base = new SettableLong[arr.length];
    for (int i = 0; i < base.length; i++) {
      base[i] = new SettableLong(0);
    }

    StopWatch sw = new StopWatch();
    sw.start();
    for (int i = 0; i < 10000; i++) {
      cmpArrays(arr, base);
    }
    sw.stop();
    System.out.print(sw.getDuration());
    System.out.print("\n");
  }

  private static void testLongs() {
    long[][] arr = buildLongArray();
    long[] base = new long[arr.length];
    for (int i = 0; i < base.length; i++) {
      base[i] = 0;
    }

    StopWatch sw = new StopWatch();
    sw.start();
    for (int i = 0; i < 10000; i++) {
      cmpArrays(arr, base);
    }
    sw.stop();
    System.out.print(sw.getDuration());
    System.out.print("\n");
  }

  private static void testfloats() {
    float[][][] arr = buildArray();
    float[][] base = new float[arr.length][];
    for (int i = 0; i < base.length; i++) {
      base[i] = new float[3];
    }

    StopWatch sw = new StopWatch();
    sw.start();
    for (int i = 0; i < 10000; i++) {
      cmpArray(arr, base);
    }
    sw.stop();
    System.out.print(sw.getDuration());
    System.out.print("\n");
  }

  static class SimpleThreadFactory implements ThreadFactory, Thread.UncaughtExceptionHandler
  {
    public Thread newThread(Runnable r) {
      Thread t = new Thread(r);
      t.setUncaughtExceptionHandler(this);
      return t;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable)
    {
      System.out.print(throwable.toString());
    }
  }

  public static void main(String[] args)
  {
    GraphLoader loader;

    String graphName = args.length > 0 ? args[0] : "memory";

    //= LinkedInFileLoader.create(args[0]);
    loader = new MemoryLoader();
    GraphSpec graphSpec = loader.load();

    List<GraphColorer> colorers = new ArrayList<GraphColorer>();
    colorers.add(new WaveColorer(graphSpec));
    colorers.add(new BacktrackColorer(graphSpec));

    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new SimpleThreadFactory());
    MultiColorer mc = MultiColorer.create(executor, colorers);

    try
    {
      ColoringResult result = mc.call();
      if (result == null)
      {
        result = ColoringResult.createNotColorableResult();
      }

      String outfileName = String.format("%s_%s_out", "sanyanse", graphName);
      ColoringResultWriter writer = StdoutResultWriter.create(outfileName, result);
      writer.write();
    }
    catch (Exception e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    executor.shutdown();
  }

  static float[][][] buildArray() {
    float[][][] arr = new float[ARRSIZE][][];

    for (int i = 0; i < arr.length; i++) {
      arr[i] = new float[ARRSIZE][];
      for (int j = 0; j < arr[i].length; j++) {
        arr[i][j] = new float[3];
      }
    }

    return arr;
  }

  static long cmpArray(float arr[][][], float base[][]) {
    long cnt = 0;

    for (int y = 0; y < base.length; y++) {
      for (int x = 0; x < arr[y].length; x++) {
        boolean eq = Arrays.equals(arr[y][x], base[y]);
        if (eq) {
          cnt++;
        }
      }
    }

    return cnt;
  }

  static BigInteger[][] buildBigIntegerArray() {
    BigInteger[][] arr = new BigInteger[ARRSIZE][];

    for (int i = 0; i < arr.length; i++) {
      arr[i] = new BigInteger[ARRSIZE];
      for (int j = 0; j < arr[i].length; j++) {
        arr[i][j] = BigInteger.ZERO;
      }
    }

    return arr;
  }

  static long[][] buildLongArray() {
    long[][] arr = new long[ARRSIZE][];

    for (int i = 0; i < arr.length; i++) {
      arr[i] = new long[ARRSIZE];
      for (int j = 0; j < arr[i].length; j++) {
        arr[i][j] = 0;
      }
    }

    return arr;
  }

  static long cmpArrays(long arr[][], long base[]) {
    long cnt = 0;

    for (int y = 0; y < base.length; y++) {
      for (int x = 0; x < arr[y].length; x++) {
        boolean eq = arr[y][x] == base[y];
        if (eq) {
          cnt++;
        }
      }
    }

    return cnt;
  }

  static Long[][] buildBoxedLongArray() {
    Long[][] arr = new Long[ARRSIZE][];

    for (int i = 0; i < arr.length; i++) {
      arr[i] = new Long[ARRSIZE];
      for (int j = 0; j < arr[i].length; j++) {
        arr[i][j] = new Long(0);
      }
    }

    return arr;
  }

  static long cmpArrays(Long arr[][], Long base[]) {
    long cnt = 0;

    for (int y = 0; y < base.length; y++) {
      for (int x = 0; x < arr[y].length; x++) {
        boolean eq = arr[y][x] == base[y];
        if (eq) {
          cnt++;
        }
      }
    }

    return cnt;
  }

  static SettableLong[][] buildSettableLongArray() {
    SettableLong[][] arr = new SettableLong[ARRSIZE][];

    for (int i = 0; i < arr.length; i++) {
      arr[i] = new SettableLong[ARRSIZE];
      for (int j = 0; j < arr[i].length; j++) {
        arr[i][j] = new SettableLong(0);
      }
    }

    return arr;
  }

  static long cmpArrays(SettableLong arr[][], SettableLong base[]) {
    long cnt = 0;

    for (int y = 0; y < base.length; y++) {
      for (int x = 0; x < arr[y].length; x++) {
        boolean eq = arr[y][x] == base[y];
        if (eq) {
          cnt++;
        }
      }
    }

    return cnt;
  }
}
