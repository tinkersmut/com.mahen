package org.mahen.javascript.bridge.io.id3;

import sun.org.mozilla.javascript.internal.ScriptableObject;

/**
 * The bridge to an ID3File
 * 
 * @author Andrew Mahen
 * 
 */
public abstract class ID3File extends org.mahen.javascript.bridge.io.fs.File
    implements IID3 {
  /**
   * Names of methods accessible to on the ID3 javascript object
   */
  public static String[] methods    = { };
  /**
   * Names of properties accessible to on the ID3 javascript object
   */
  public static String[] properties = { "album", "artist", "title", "track",
      "genre", "comment", "year"   };

  /**
   * Extend the specified ID3File to implement all of the standard methods and
   * properties common to ID3 files. Includes methods and properties of standard
   * Files. This assumes the passed file's class DIRECTLY implements all IID3 properties
   * 
   * @param file
   * @see org.mahen.javascript.bridge.io.fs.File.extend
   */
  public static void extend(ID3File file) {

//    file.defineFunctionProperties(methods, file.getClass(), ScriptableObject.DONTENUM);

    for (int i = 0; i < properties.length; i++) {
      file.defineProperty(properties[i], file.getClass(),
          ScriptableObject.DONTENUM);
    }
  }

  /**
   * Constructs an ID3File. Should not be called, this object should be
   * constructed through the javascript engine.
   * 
   */
  public ID3File() {
    super();

  }

  /**
   * Initialize this object 
   * 
   * @param path
   */
  protected void init(){
    extend(this);
  }
  
}
