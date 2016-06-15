/*
 * Copyright  2000-2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.mahen.ant;

import java.io.File;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.mahen.javascript.bridge.io.id3.MP3;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.ScriptableObject;


/**
 * 
 * @author Andrew Mahen
 * 
 */
public class MusicCompare extends Task {

  protected Vector  filesets               = new Vector();
  protected Vector  baseFiles              = new Vector();

  protected Mapper  mapperElement          = null;
  
  protected boolean filtering              = false;
  protected boolean forceOverwrite         = false;
  protected boolean flatten                = false;
  protected int     verbosity              = Project.MSG_VERBOSE;
  protected boolean includeEmpty           = true;
  protected boolean failonerror            = true;
  
  private Vector    filterChains           = new Vector();
  private Vector    filterSets             = new Vector();
  
  private boolean   enableMultipleMappings = false;
  private FileUtils fileUtils;
  private String    inputEncoding          = null;
  private String    outputEncoding         = null;

  /**
   * MusicCompare task constructor.
   */
  public MusicCompare() {

    fileUtils = FileUtils.newFileUtils();
  }

//  shell                             shell;
  ScriptableObject                  scope;
//  org.trm.javascript.bridge.io.File out;
  String                            outFileName;

  /**
   * Base directory to compare against.
   * 
   * @param file
   */
  public void setBasedir(File file) {

    if (file != null) {
      File[] files = file.listFiles();
      for (int i = 0; i < files.length; i++) {
        if (files[i].isDirectory()
            || !files[i].getPath().toLowerCase().endsWith(".mp3"))
          continue;
        try {
          MP3 mp3 = createMp3(files[i]);
          baseFiles.add(mp3.getArtist() + "-" + mp3.getTitle());
        } catch (Exception e) {
          System.out.println("Bad Mp3 " + files[i]);
        }
      }
    }
    System.out.println("comparing " + baseFiles.size());
  }

  private MP3 createMp3(File file) throws Exception {

    Context context = Context.enter();
//    if (shell == null) {
//      shell = new shell();
//      scope = ScopeFactory.createScope(context, shell, false);
//      // try {
//      // out = (org.trm.javascript.bridge.io.File) context.newObject(scope,
//      // "File", new Object[] { outFileName });
//      // } catch (Throwable e) {
//      // throw new RuntimeException(e);
//      // }
//    }
    return (MP3) context.newObject(scope, "MP3", new Object[] { file
        .getAbsolutePath() });
  }

  /**
   * @return the fileutils object
   */
  protected FileUtils getFileUtils() {

    return fileUtils;
  }

  /**
   * Adds a FilterChain.
   * 
   * @return a filter chain object
   */
  public FilterChain createFilterChain() {

    FilterChain filterChain = new FilterChain();
    filterChains.addElement(filterChain);
    return filterChain;
  }

  /**
   * Adds a filterset.
   * 
   * @return a filter set object
   */
  public FilterSet createFilterSet() {

    FilterSet filterSet = new FilterSet();
    filterSets.addElement(filterSet);
    return filterSet;
  }

  /**
   * Get the filtersets being applied to this operation.
   * 
   * @return a vector of FilterSet objects
   */
  protected Vector getFilterSets() {

    return filterSets;
  }

  /**
   * Get the filterchains being applied to this operation.
   * 
   * @return a vector of FilterChain objects
   */
  protected Vector getFilterChains() {

    return filterChains;
  }

  /**
   * If true, enables filtering.
   * 
   * @param filtering
   *          if true enable filtering, default is false
   */
  public void setFiltering(boolean filtering) {

    this.filtering = filtering;
  }

  /**
   * Overwrite any existing destination file(s).
   * 
   * @param overwrite
   *          if true force overwriting of destination file(s) even if the
   *          destination file(s) are younger than the corresponding source
   *          file. Default is false.
   */
  public void setOverwrite(boolean overwrite) {

    this.forceOverwrite = overwrite;
  }

  /**
   * When copying directory trees, the files can be "flattened" into a single
   * directory. If there are multiple files with the same name in the source
   * directory tree, only the first file will be copied into the "flattened"
   * directory, unless the forceoverwrite attribute is true.
   * 
   * @param flatten
   *          if true flatten the destination directory. Default is false.
   */
  public void setFlatten(boolean flatten) {

    this.flatten = flatten;
  }

  /**
   * Used to force listing of all names of copied files.
   * 
   * @param verbose
   *          output the names of copied files. Default is false.
   */
  public void setVerbose(boolean verbose) {

    if (verbose) {
      this.verbosity = Project.MSG_INFO;
    } else {
      this.verbosity = Project.MSG_VERBOSE;
    }
  }

  /**
   * Attribute to handle mappers that return multiple mappings for a given
   * source path.
   * 
   * @param enableMultipleMappings
   *          If true the task will copy to all the mappings for a given source
   *          path, if false, only the first file or directory is processed. By
   *          default, this setting is false to provide backward compatibility
   *          with earlier releases.
   * @since 1.6
   */
  public void setEnableMultipleMappings(boolean enableMultipleMappings) {

    this.enableMultipleMappings = enableMultipleMappings;
  }

  /**
   * @return the value of the enableMultipleMapping attribute
   */
  public boolean isEnableMultipleMapping() {

    return enableMultipleMappings;
  }

  /**
   * If false, note errors to the output but keep going.
   * 
   * @param failonerror
   *          true or false
   */
  public void setFailOnError(boolean failonerror) {

    this.failonerror = failonerror;
  }

  /**
   * Adds a set of files to copy.
   * 
   * @param set
   *          a set of files to copy
   */
  public void addFileset(FileSet set) {

    filesets.addElement(set);
  }

  /**
   * Defines the mapper to map source to destination files.
   * 
   * @return a mapper to be configured
   * @exception BuildException
   *              if more than one mapper is defined
   */
  public Mapper createMapper() throws BuildException {

    if (mapperElement != null) {
      throw new BuildException("Cannot define more than one mapper",
          getLocation());
    }
    mapperElement = new Mapper(getProject());
    return mapperElement;
  }

  /**
   * A nested filenamemapper
   * 
   * @param fileNameMapper
   *          the mapper to add
   * @since Ant 1.6.3
   */
  public void add(FileNameMapper fileNameMapper) {

    createMapper().add(fileNameMapper);
  }

  /**
   * Sets the character encoding
   * 
   * @param encoding
   *          the character encoding
   * @since 1.32, Ant 1.5
   */
  public void setEncoding(String encoding) {

    this.inputEncoding = encoding;
    if (outputEncoding == null) {
      outputEncoding = encoding;
    }
  }

  /**
   * @return the character encoding, <code>null</code> if not set.
   * 
   * @since 1.32, Ant 1.5
   */
  public String getEncoding() {

    return inputEncoding;
  }

  /**
   * Sets the character encoding for output files.
   * 
   * @param encoding
   *          the character encoding
   * @since Ant 1.6
   */
  public void setOutputEncoding(String encoding) {

    this.outputEncoding = encoding;
  }

  /**
   * @return the character encoding for output files, <code>null</code> if not
   *         set.
   * 
   * @since Ant 1.6
   */
  public String getOutputEncoding() {

    return outputEncoding;
  }

  /**
   * Performs the copy operation.
   * 
   * @exception BuildException
   *              if an error occurs
   */
  public void execute() throws BuildException {

    // make sure we don't have an illegal set of options
    validateAttributes();

    // deal with the filesets
    for (int i = 0; i < filesets.size(); i++) {
      FileSet fs = (FileSet) filesets.elementAt(i);
      DirectoryScanner ds = null;
      try {
        ds = fs.getDirectoryScanner(getProject());
      } catch (BuildException e) {
        if (failonerror || !e.getMessage().endsWith(" not found.")) {
          throw e;
        } else {
          log("Warning: " + e.getMessage());
          continue;
        }
      }

      File fromDir = fs.getDir(getProject());

      String[] srcFiles = ds.getIncludedFiles();
      String[] srcDirs = ds.getIncludedDirectories();
      scan(fromDir, srcFiles, srcDirs);
    }
  }

  /*****************************************************************************
   * * protected and private methods
   ****************************************************************************/

  /**
   * Ensure we have a consistent and legal set of attributes, and set any
   * internal flags necessary based on different combinations of attributes.
   * 
   * @exception BuildException
   *              if an error occurs
   */
  protected void validateAttributes() throws BuildException {

  }

  /**
   * Compares source files to destination files to see if they should be copied.
   * 
   * @param fromDir
   *          The source directory
   * @param files
   *          A list of files to copy
   * @param dirs
   *          A list of directories to copy
   */
  protected void scan(File fromDir, String[] files, String[] dirs) {

    for (int i = 0; i < files.length; i++) {
      File file = new File(fromDir + "/" + files[i]);

      if (!file.exists()) {
        System.out.println("doesn't exist " + file);
        continue;
      }

      try {
        MP3 mp3 = createMp3(file);
        if (baseFiles.remove(mp3.getArtist() + "-" + mp3.getTitle())) {
          System.out.println("File exists! " + mp3.getArtist() + "-"
              + mp3.getTitle());
        }
      } catch (Exception e) {
        System.out.println("Bad MP3: " + file);
      }
    }
  }

}
