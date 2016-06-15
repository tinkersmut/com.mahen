package org.mahen.javascript.bridge.io.id3;

/**
 * Any object with common ID3 tags
 * 
 * @author Andrew Mahen
 *
 */
public interface IID3 {
	/**
	 * @return The album field of this ID3
	 */
	public String getAlbum();
	/**
	 * @return The artist field of this ID3
	 */
	public String getArtist();
	/**
	 * @return The comment field of this ID3
	 */
	public String getComment();
	/**
	 * @return The genre field of this ID3
	 */
	public String getGenre();
	/**
	 * @return The title field of this ID3
	 */
	public String getTitle();
	/**
	 * @return The track field of this ID3
	 */
	public int getTrack();
	/**
	 * @return The year field of this ID3
	 */
	public String getYear();
	/**
	 * Sest the album field
	 * @param value
	 */
	public void setAlbum(String value);
	/**
	 * Sets the artist field
	 * @param value
	 */
	public void setArtist(String value);
	/**
	 * Sets the comment field
	 * @param value
	 */
	public void setComment(String value);
	/**
	 * Sets the genre field
	 * @param value
	 */
	public void setGenre(String value);
	/**
	 * Sets the title field
	 * @param value
	 */
	public void setTitle(String value);
	/**
	 * Sets the track number
	 * @param value
	 */
	public void setTrack(int value);
	/**
	 * Sets the year field
	 * @param value
	 */
	public void setYear(String value);
}
