package org.sanyanse.loader;


import org.sanyanse.common.GraphLoader;
import org.sanyanse.common.GraphSpec;


public class LinkedInFileLoader implements GraphLoader
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
    LinkedInFileLoader loader = new LinkedInFileLoader();
    loader._filename = filename;
    return loader;
  }
}
