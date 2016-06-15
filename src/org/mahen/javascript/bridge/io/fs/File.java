/**
 * 
 */
package org.mahen.javascript.bridge.io.fs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.ScriptableObject;

/**
 * @author amahen
 * 
 */
public class File extends ScriptableObject {
  /**
   * Generated
   */
  private static final long serialVersionUID = 1L;
  /**
   * Names of methods accessible to on the File javascript object
   */
  public static String[]    methods          = { "copy", "move", "remove" };
  /**
   * Names of properties accessible to on the File javascript object
   */
  public static String[]    properties       = { "directory", "exists",
      "fullpath", "isdir", "length", "name", "path", "readonly", "ext" };

  /**
   * @param file
   */
  public static void extend(File file) {

    file.defineFunctionProperties(methods, File.class,
        ScriptableObject.DONTENUM);

    for (int i = 0; i < properties.length; i++) {
      file.defineProperty(properties[i], File.class, ScriptableObject.DONTENUM);
    }
  }

  /**
   * 
   */
  protected java.io.File file;

  /**
   * @param path
   * @throws Exception
   */
  public void jsConstructor(String path) throws Exception {
    setFile(path);
    extend(this);
  }

  /**
   * @param path
   * @throws Exception
   */
  public void setFile(String path) throws Exception {
    file = createFile(path);
  }

  /**
   * @param path
   * @return
   * @throws Exception
   */
  protected java.io.File createFile(String path) throws Exception {
    return new java.io.File(path);
  }

  /**
   * Renames this file to the specified name.
   * 
   * @param newName
   * @return
   * @throws Exception
   */
  public Object move(String newName) throws Exception {

    // sanity check
    if (this.file == null) {
      throw new IOException("Unable to read file.");
    }

    java.io.File file = createFile(newName);

    // can't overwrite
    if (file.exists()) {
      throw new IOException("Failed to move file. File " + newName
          + " already exists.");
    }

    // rename file
    if (this.file.renameTo(file)) {
      return this;
    }

    throw new IOException("Failed to move file.");
  }

  /**
   * @param newName
   * @return
   * @throws Exception
   */
  public Object copy(String newName) throws IOException {
    InputStream in = null;
    OutputStream out = null;
    String from = this.file.getCanonicalPath();
    String to = new java.io.File(newName).getCanonicalPath();
    try {
      in = new FileInputStream(from);
      out = new FileOutputStream(to);
      byte[] buffer = new byte[1024];
      while (true) {
        int amountRead = in.read(buffer);
        if (amountRead == -1) {
          break;
        }
        out.write(buffer, 0, amountRead);
      }
    } finally {
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
    }
    return createInstance(newName);
  }

  /**
   * Creates an instance of the current javascript object for the specified
   * path.
   * 
   * @param path
   *          a <code>String</code>
   * @return a <code>File</code>
   */
  protected File createInstance(String path) {
    return (File) Context.getCurrentContext().newObject(this, getClassName(),
        new Object[] { path });
  }

  /**
   * @return
   */
  public String getName() {
    return file.getName();
  }

  /**
   * Deletes this file from the filesystem
   * 
   * @return a <code>boolean</boolean>
   * @throws IOException
   */
  public boolean delete() throws IOException {

    // sanity check
    if (this.file == null) {
      return false;
    }

    return this.file.delete();
  }

  /**
   * The JS name for the function is remove() because delete is a JS reserved
   * word!
   * 
   * @return
   * @throws IOException
   */
  public boolean remove() throws IOException {
    return delete();
  }

  /**
   * If the file exists on the filesystem
   * 
   * @return a <code>boolean</code>
   */
  public boolean getExists() {

    // sanity check
    if (this.file == null) {
      return false;
    }

    return this.file.exists();
  }

  /**
   * @return
   */
  public boolean getIsdir() {

    // sanity check
    if (this.file == null) {
      return false;
    }

    return this.file.isDirectory();
  }

  /**
   * @return
   * @throws IOException
   */
  public String getPath() throws IOException {

    // sanity check
    if (this.file == null) {
      return "";
    }

    return this.file.getPath();
  }

  /**
   * @return
   * @throws IOException
   */
  public String getFullpath() throws IOException {

    // sanity check
    if (this.file == null) {
      return "";
    }

    return this.file.getAbsolutePath();
  }

  /**
   * @return
   */
  public boolean getReadonly() {

    // sanity check
    if (this.file == null) {
      return false;
    }

    return !this.file.canWrite();
  }

  /**
   * @return
   */
  public long getLength() {

    // sanity check
    if (this.file == null) {
      return 0L;
    }

    return this.file.length();
  }

  /**
   * @return
   * @throws IOException
   */
  public String getDirectory() throws IOException {

    // sanity check
    if (this.file == null) {
      return "";
    }

    return this.file.getParent();
  }
  
  public String getExt() throws IOException {
    String name = getName();
    
    // sanity check
    if(name == null){
      return "";
    }
    
    int i = name.lastIndexOf('.');
    
    // no extension
    if(i < 0){
      return "";
    }
    return name.substring(i+1).toLowerCase();
  }

  /*
   * (non-Javadoc)
   * 
   * @see sun.org.mozilla.javascript.internal.ScriptableObject#getClassName()
   */
  public String getClassName() {
    return "File";
  }
}
