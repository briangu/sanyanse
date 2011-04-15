package org.sanyanse;


import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.GraphSpec;


public class FileLoader implements GraphLoader
{
  private String _filename;

  public GraphSpec load() {
    return null;
  }

  // file format:
  // <CNT>
  // <NODE ID>:<EDGE LIST>
  //
  // EDGE LIST: <NODE ID>,<NODE ID>, ... ,<NODE ID>

  public static GraphLoader create(String filename) {
    FileLoader loader = new FileLoader();
    loader._filename = filename;
    return loader;
  }
}
