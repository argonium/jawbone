/*
 * Written by Mike Wallace (mfwallace at gmail.com).  Available
 * on the web site http://mfwallace.googlepages.com/.
 * 
 * Copyright (c) 2006 Mike Wallace.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package io.miti.jawbone;

/**
 * This class represents a word in the WordNet data files.
 * 
 * @author mwallace
 */
public final class WordData
{
  /**
   * The word.
   */
  private String word = null;
  
  /**
   * A number that, when appended onto lemma, uniquely identifies
   * a sense within a lexicographer file.
   */
  private int lexID = 0;
  
  /**
   * For adjectives, a word is followed by a syntactic marker if
   * one was specified in the lexicographer file. A syntactic marker
   * is appended, in parentheses, onto word  without any intervening
   * spaces.
   */
  private String syntacticMarker = null;
  
  /**
   * The offset into the data file for the line with this word.
   */
  private long dataFileOffset = 0;
  
  /**
   * The sense number for this word.
   * 
   * If this number is positive, it's the sense number (1-based).
   * If it's -1, it has not been loaded yet; if 0, it was not found
   * in the index file (this should never happen).
   */
  private int senseNum = -1;
  
  /**
   * The part of speech for this word.
   */
  private PartOfSpeech pos;
  
  
  /**
   * Default constructor.
   */
  public WordData()
  {
    super();
  }
  
  
  /**
   * Constructor that takes the 3 fields of interest.
   * 
   * @param sWord the word
   * @param nLexID the lexicon ID
   * @param sSyntacticMarker a syntactic marker
   * @param pPos the part of speech
   * @param lOffset the offset into the data file
   */
  public WordData(final String sWord,
                  final int nLexID,
                  final String sSyntacticMarker,
                  final PartOfSpeech pPos,
                  final long lOffset)
  {
    word = sWord;
    lexID = nLexID;
    syntacticMarker = sSyntacticMarker;
    pos = pPos;
    dataFileOffset = lOffset;
  }
  
  
  /**
   * @return Returns the lexicon ID.
   */
  public int getLexID()
  {
    return lexID;
  }
  
  
  /**
   * @param nLexID The lexicon ID to set.
   */
  public void setLexID(final int nLexID)
  {
    lexID = nLexID;
  }
  
  
  /**
   * @return Returns the word.
   */
  public String getWord()
  {
    return word;
  }
  
  
  /**
   * @param sWord The word to set.
   */
  public void setWord(final String sWord)
  {
    word = sWord;
  }


  /**
   * @return Returns the syntactic marker.
   */
  public String getSyntacticMarker()
  {
    return syntacticMarker;
  }


  /**
   * @param sSyntacticMarker The syntactic marker to set.
   */
  public void setSyntacticMarker(final String sSyntacticMarker)
  {
    syntacticMarker = sSyntacticMarker;
  }
  
  
  /**
   * Return the part of speech.
   * 
   * @return the part of speech
   */
  public PartOfSpeech getPos()
  {
    return pos;
  }
  
  
  /**
   * Return the sense number.  This is 1-based.
   * If zero, it was not found (should never happen).
   * 
   * @return the sense number
   */
  public int getSenseNumber()
  {
    // Check if it's been loaded
    if (senseNum < 0)
    {
      // It has not, so load it now
      senseNum = ParseIndexFile.getSenseNumber(word, pos, dataFileOffset);
    }
    
    return senseNum;
  }
  
  
  /**
   * Set the sense number.
   * 
   * @param lSenseNumber the sense number for this word
   */
  public void setSenseNumber(final int lSenseNumber)
  {
    senseNum = lSenseNumber;
  }
  
  
  /**
   * @see java.lang.Object#toString()
   * @return A string representation of this object
   */
  @Override
  public String toString()
  {
    // Declare our string buffer
    StringBuffer buf = new StringBuffer(100);
    
    // Build the string
    buf.append("Word: ").append(word).append("  Lexicon-ID: ")
       .append(lexID).append("  Syntactic-Marker: ").append(syntacticMarker)
       .append("\nPOS: ").append(pos.toString()).append("  Offset: ")
       .append(Long.toString(dataFileOffset)).append("  Sense");
    
    if (senseNum < 0)
    {
      buf.append(": <Not loaded>");
    }
    else if (senseNum == 0)
    {
      buf.append(": <Not found>");
    }
    else
    {
      buf.append(" #").append(Integer.toString(senseNum));
    }
    
    // Return the string
    return buf.toString();
  }
}
