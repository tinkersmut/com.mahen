package org.mahen.javascript.bridge.io.id3;

import java.io.File;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.ID3v1;
import org.farng.mp3.id3.ID3v2_2;

import sun.org.mozilla.javascript.internal.Context;

/**
 * Wraps an mp3 file and makes it editable
 * 
 * @author Andrew Mahen
 * 
 */
public class MP3 extends ID3File {

  /**
   * Generated
   */
  private static final long serialVersionUID = -6313278788333020240L;

  /**
   * The wrapped java.io.File
   */
  protected MP3File         file;

  /**
   * Cached album name
   */
  private String            album;
  /**
   * Cached artist name
   */
  private String            artist;
  /**
   * Cached comment field
   */
  private String            comment;
  /**
   * Cached genre field
   */
  private String            genre;
  /**
   * Cached title field
   */
  private String            title;
  /**
   * Cached year field
   */
  private String            year;
  /**
   * Cached track field
   */
  private int               track;
  /**
   * TRUE if this object has changed from its saved state
   */
  private boolean           dirty;

  /**
   * Throws an exception if this is called before the mp3 is initialized.
   * 
   * @return MP3File
   */
  private MP3File getFile() {

    if (file == null) {
      throw new RuntimeException("MP3File was never initialized.");
    }
    return file;
  }

  /**
   * Means this file has been modified from its original state.
   * 
   */
  protected void markDirty() {
    dirty = true;
  }

  /**
   * @param path
   * @throws Exception
   */
  public void jsConstructor(String path) throws Exception{
    super.jsConstructor(path);
    init();
  }

  /* (non-Javadoc)
   * @see org.mahen.javascript.bridge.io.fs.File#createFile(java.lang.String)
   */
  protected File createFile(String path) throws Exception{
    this.file = new MP3File(path);
    return this.file.getMp3file();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mozilla.javascript.ScriptableObject#getClassName()
   */
  public String getClassName() {
    return "MP3";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#getAlbum()
   */
  public String getAlbum() {
    if (album == null) {
      if (getFile().hasID3v2Tag()) {
        album = getFile().getID3v2Tag().getAlbumTitle();
      }
      if (album == null) {
        album = getFile().getID3v1Tag().getAlbumTitle();
      }
    }
    return album;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#getArtist()
   */
  public String getArtist() {
    if (artist == null) {
      if (getFile().hasID3v2Tag()) {
        artist = getFile().getID3v2Tag().getLeadArtist();
      }
      if (artist == null
          || (getFile().hasID3v1Tag() && !artist.equals(getFile().getID3v1Tag()
              .getArtist()))) {
        artist = getFile().getID3v1Tag().getArtist();
      }
    }
    return artist;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#getComment()
   */
  public String getComment() {
    if (comment == null) {
      if (getFile().hasID3v2Tag()) {
        comment = getFile().getID3v2Tag().getSongComment();
      }
      if (comment == null) {
        comment = getFile().getID3v1Tag().getComment();
      }
    }
    return comment;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#getGenre()
   */
  public String getGenre() {
    if (genre == null) {
      if (getFile().hasID3v2Tag()) {
        genre = getFile().getID3v2Tag().getSongGenre();
      }
      if (genre == null) {
        genre = getFile().getID3v1Tag().getSongGenre();
      }
    }
    return genre;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#getTitle()
   */
  public String getTitle() {
    if (title == null) {
      if (getFile().hasID3v2Tag()) {
        title = getFile().getID3v2Tag().getSongTitle();
      }
      if (title == null
          || (getFile().hasID3v1Tag() && !title.equals(getFile().getID3v1Tag()
              .getTitle()))) {
        title = getFile().getID3v1Tag().getTitle();
      }
    }
    return title;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#getTrack()
   */
  public int getTrack() {
    if (track == -1) {
      try {
        if (getFile().hasID3v2Tag()) {
          track = Integer.parseInt(getFile().getID3v2Tag()
              .getTrackNumberOnAlbum());
        }
        if (track == -1) {
          track = Integer.parseInt(getFile().getID3v1Tag()
              .getTrackNumberOnAlbum());
        }
      } catch (NumberFormatException e) {
        Context.throwAsScriptRuntimeEx(e);
        track = 0;
      }
    }
    return track;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#getYear()
   */
  public String getYear() {
    if (year == null) {
      if (getFile().hasID3v2Tag()) {
        year = getFile().getID3v2Tag().getYearReleased();
      }
      if (year == null) {
        year = getFile().getID3v1Tag().getYear();
      }
    }
    return year;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#setAlbum(java.lang.String)
   */
  public void setAlbum(String value) {
    album = value;
    initTags();
    getFile().getID3v2Tag().setAlbumTitle(album);
    getFile().getID3v1Tag().setAlbum(album);
    markDirty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#setArtist(java.lang.String)
   */
  public void setArtist(String value) {
    artist = value;
    initTags();
    getFile().getID3v2Tag().setLeadArtist(artist);
    getFile().getID3v1Tag().setArtist(artist);
    markDirty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#setComment(java.lang.String)
   */
  public void setComment(String value) {
    comment = value;
    initTags();
    getFile().getID3v2Tag().setSongComment(value);
    getFile().getID3v1Tag().setComment(value);
    markDirty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#setGenre(java.lang.String)
   */
  public void setGenre(String value) {
    genre = value;
    initTags();
    getFile().getID3v2Tag().setSongGenre(value);
    getFile().getID3v1Tag().setSongGenre(value);
    markDirty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#setTitle(java.lang.String)
   */
  public void setTitle(String value) {
    title = value;
    initTags();
    getFile().getID3v2Tag().setSongTitle(value);
    getFile().getID3v1Tag().setTitle(value);
    markDirty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#setTrack(int)
   */
  public void setTrack(int value) {
    track = value;
    initTags();
    getFile().getID3v2Tag().setTrackNumberOnAlbum("" + value);
    getFile().getID3v1Tag().setTrackNumberOnAlbum("" + value);
    markDirty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mahen.javascript.bridge.io.IID3#setYear(java.lang.String)
   */
  public void setYear(String value) {
    year = value;
    initTags();
    getFile().getID3v2Tag().setYearReleased(value);
    getFile().getID3v1Tag().setYear(value);
    markDirty();
  }

  /**
   * Saves the changes made to the ID3 tags back to the file
   * @throws Exception 
   * 
   */
  public void jsFunction_save() throws Exception{
    if (dirty) {
      getFile().save();
    }
  }

  /**
   * 
   */
  private void initTags() {
    if (!getFile().hasID3v2Tag()) {
      getFile().setID3v2Tag(new ID3v2_2());
      markDirty();
    }
    if (!getFile().hasID3v1Tag()) {
      getFile().setID3v1Tag(new ID3v1());
      markDirty();
    }
  }
}
