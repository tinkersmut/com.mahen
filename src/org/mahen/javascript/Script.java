package org.mahen.javascript;

import java.io.FileReader;
import java.io.IOException;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.ScriptableObject;

/**
 * Runs a file or string that is a compilable javascript block
 * 
 * @author amahen
 * 
 */
public class Script extends ScriptableObject {

  /**
   * Generated
   */
  private static final long serialVersionUID = 1L;

  /**
   * Run a script file. Should be called with like: <bold>java Script path</bold>
   * where path is the filesystem path to a file with compilable javascript.
   * 
   * @param args
   */
  public static void main(String[] args) {

    // check arguments passed
    if (args.length < 1) {
      System.err.println("Invalid arguments");
      help();
      return;
    }

    // initialize the script object to run the file
    Script script = new Script();

    // initialize the environment the script will run in
    new Environment(script);

    // run the script
    try {
      script.run(args[0]);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Prints the usage information for this class.
   */
  protected static void help() {

    System.out.println("Script [filename]");
  }

  /**
   * @param filename
   * @throws IOException
   */
  public void run(String filename) throws IOException {

    FileReader in = new FileReader(filename);
    Context.getCurrentContext().evaluateReader(this, in, filename, 1, null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see sun.org.mozilla.javascript.internal.ScriptableObject#getClassName()
   */
  public String getClassName() {

    return "script";
  }
}
