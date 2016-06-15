package org.mahen.javascript;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.ScriptableObject;

/**
 * A console where javascript commands can be entered and executed.
 * 
 * @author amahen
 *
 */
public class Console extends ScriptableObject {

  /**
   * Generated
   */
  private static final long serialVersionUID = 1L;

  /**
   * @param args
   */
  public static void main(String[] args){
    Console console = new Console();
    new Environment(console);
    console.defineFunctionProperties(new String[]{"exit"}, Console.class, DONTENUM);
    console.run();
  }

  /**
   * True when the console is exited
   */
  private boolean exit;
  
  /**
   * 
   */
  public Console(){
    exit = false;
  }
  
  /**
   * Run the console
   */
  public void run(){
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String sourceName = "<stdin>";
    int lineno = 1;
    boolean hitEOF = false;
    do {
      int startline = lineno;
      try {
        String source = "";
        // Collect lines of source to compile.
        while (true) {
          String newline;
          newline = in.readLine();
          if (newline == null) {
            hitEOF = true;
            break;
          }
          source = source + newline + "\n";
          lineno++;
          // Continue collecting as long as more lines
          // are needed to complete the current
          // statement.
          if (Context.getCurrentContext().stringIsCompilableUnit(source))
            break;
        }

        Object result = Context.getCurrentContext().evaluateString(this, source, sourceName, startline, null);

        if (result != Context.getUndefinedValue()) {
          System.err.println(Context.toString(result));
        }
      } catch (Exception e) {

      }

      if (exit) { // The user executed the exit() function.
        break;
      }
    } while (!hitEOF);
    exit = false;
    System.err.println();

  }
  
  /**
   * Exits the console. 
   */
  public void exit(){
    exit = true;
  }
  
  /* (non-Javadoc)
   * @see sun.org.mozilla.javascript.internal.ScriptableObject#getClassName()
   */
  public String getClassName() {
    return "console";
  }

}
