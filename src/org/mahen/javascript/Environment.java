package org.mahen.javascript;

import org.mahen.javascript.bridge.io.fs.Directory;
import org.mahen.javascript.bridge.io.fs.File;
import org.mahen.javascript.bridge.io.id3.MP3;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.ScriptableObject;

/**
 * The environment that any javascript will run in. 
 * 
 * @author amahen
 *
 */
public class Environment {
  
  /**
   * The scope that this javascript environment is running in. 
   */
  protected ScriptableObject scope;
  
  /**
   * @param shell
   * @return
   */
  public Environment(ScriptableObject shell){
   setScope(shell);
  }
  
  /**
   * Initializes the scope (by creating a new one) for the specified shell.
   * 
   * @param shell
   */
  protected void setScope(ScriptableObject shell){
    // create the context this script will run in
    Context context = Context.enter();

    // create the scope
    scope = context.initStandardObjects(shell, false);

    // load my classes
    try {
      ScriptableObject.defineClass(scope, MP3.class);
      ScriptableObject.defineClass(scope, File.class);
      ScriptableObject.defineClass(scope, Directory.class);

      // add an out property
      Object out = Context.javaToJS(System.out, scope);
      scope.put("out", scope, out);
     } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
