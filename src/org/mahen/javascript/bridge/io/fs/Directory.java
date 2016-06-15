/**
 * 
 */
package org.mahen.javascript.bridge.io.fs;

import java.util.ArrayList;
import java.util.Collection;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.ScriptableObject;

/**
 * A FileSystem directory. Acts slightly differently than a file.
 * 
 * @author amahen
 * 
 */
public class Directory extends File {

  /**
   * Generated
   */
  private static final long serialVersionUID = 1L;
  /**
   * Names of properties accessible to on the Directory javascript object
   */
  public static String[]    properties       = { "files" };
  /**
   * @param file
   */
  public static void extend(Directory dir) {

//    file.defineFunctionProperties(methods, File.class, ScriptableObject.DONTENUM);

    for (int i = 0; i < properties.length; i++) {
      dir.defineProperty(properties[i], Directory.class, ScriptableObject.DONTENUM);
    }
  }

  /* (non-Javadoc)
   * @see org.mahen.javascript.bridge.io.fs.File#jsConstructor(java.lang.String)
   */
  public void jsConstructor(String path) throws Exception {
    super.jsConstructor(path);
    extend(this);
  }

  /* (non-Javadoc)
   * @see org.mahen.javascript.bridge.io.fs.File#getClassName()
   */
  public String getClassName() {
    return "Directory";
  }

  /**
   * Get the list of files and directories under this directory.
   * 
   * @return a <code>NativeArray</code>
   * @throws Exception
   */
  public Object getFiles() throws Exception {
    Collection retval = new ArrayList();
    java.io.File[] files = super.file.listFiles();
    
    // sanity check
    if(files == null){
      return Context.getCurrentContext().newArray(this, 0);
    }
    
    for (int i = 0; i < files.length; i++) {
      if(files[i].isDirectory()){
        retval.add(Context.getCurrentContext().newObject(this, getClassName(), new Object[]{files[i].getAbsolutePath()}));
      }else{
        retval.add(Context.getCurrentContext().newObject(this, super.getClassName(), new Object[]{files[i].getAbsolutePath()}));
      }
    }
    
    return Context.getCurrentContext().newArray(this, retval.toArray());
  }

  /**
   * Number of files in this collection
   */
  public long getLength() {
    return super.file.list().length;
  }
}
