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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.ArrayList;

/**
 * This class represents each line of data in the WordNet
 * data files.
 * 
 * @author mwallace
 * @version April 20 2007 LLT
 * CHANGELOG:
 * + added getting list of synsets by specific pointers (by character).
 * + added getting 9-digit ID
 */
public final class Synset
{
  /**
   * Current byte offset in the file.
   */
  private long synsetOffset = 0L;
  
  /**
   * Number corresponding to the lexicographer file name
   * containing the synset.
   */
  private int lexFilenum = 0;
  
  /**
   * The part of speech.
   */
  private PartOfSpeech pos;
  
  /**
   * The number of words in the synset.
   */
  private int numWords = 0;
  
  /**
   * The list of words.
   */
  private List<WordData> listWords = null;
  
  /**
   * The number of pointers.
   */
  private int pointerCount = 0;
  
  /**
   * The list of pointer symbols.
   */
  private List<Pointer> listPointers = null;
  
  /**
   * The number of frames (verbs only).
   */
  private int frameCount = 0;
  
  /**
   * The frames - a list of numbers corresponding to the generic
   * verb sentence frames for words in the synset.
   */
  private List<FrameData> listFrames = null;
  
  /**
   * The gloss may contain a definition, one or more example
   * sentences, or both.
   */
  private String gloss;
  
  /**
   * Whether this synset has been loaded from the data file.
   */
  private boolean loaded = false;
  
  
  /**
   * Default constructor.
   */
  protected Synset()
  {
    super();
  }
  
  
  /**
   * Constructor taking the offset to the data.
   * 
   * @param offset the offset to the data
   * @param partOfSpeech the part of speech
   */
  protected Synset(final long offset, final PartOfSpeech partOfSpeech)
  {
    synsetOffset = offset;
    pos = partOfSpeech;
  }
  
  
  /**
   * @return Returns the gloss.
   */
  public String getGloss()
  {
    // Check if this data has been loaded.  If not, load it.
    checkLoad();
    
    return gloss;
  }
  
  
  /**
   * @param sGloss The gloss to set.
   */
  protected void setGloss(final String sGloss)
  {
    this.gloss = sGloss;
  }
  
  
  /**
   * @return Returns the lexicon file number.
   */
  public int getLexFilenum()
  {
    // Check if this data has been loaded.  If not, load it.
    checkLoad();
    
    return lexFilenum;
  }
  
  
  /**
   * @param nLexFilenum The lexicon file number.
   */
  protected void setLexFilenum(final int nLexFilenum)
  {
    this.lexFilenum = nLexFilenum;
  }
  
  
  /**
   * @return Returns the number of words in the synset.
   */
  public int getNumWords()
  {
    // Check if this data has been loaded.  If not, load it.
    checkLoad();
    
    return numWords;
  }
  
  
  /**
   * @param nNumWords The number of words in the synset.
   */
  protected void setNumWords(final int nNumWords)
  {
    this.numWords = nNumWords;
    
    if ((numWords > 0) && (listWords == null))
    {
      listWords = new ArrayList<WordData>(numWords);
    }
  }
  
  
  /**
   * @return Returns the pointer count.
   */
  public int getPointerCount()
  {
    // Check if this data has been loaded.  If not, load it.
    checkLoad();
    
    return pointerCount;
  }
  
  
  /**
   * @param nPointerCount The pointer count to set.
   */
  protected void setPointerCount(final int nPointerCount)
  {
    this.pointerCount = nPointerCount;
    
    if ((pointerCount > 0) && (listPointers == null))
    {
      listPointers = new ArrayList<Pointer>(pointerCount);
    }
  }
  
  
  /**
   * Returns the list of pointers.
   * 
   * @return the list of pointers
   */
  public List<Pointer> getPointers()
  {
    // Check if this data has been loaded.  If not, load it.
    checkLoad();
    
    return listPointers;
  }
  
  /**
   * Returns the 9-digit unique ID of this synset. This is equal to this
   * synset's 8-digit offset (within its part-of-speech data file) prefixed
   * with a digit as follows:
   * <ul>
   * <li>noun: 1
   * <li>verb: 2
   * <li>adjective: 3
   * <li>adverb: 4
   * </ul>
   * (Added by LLT 20 April 2007)
   * 
   * @return The 9-digit unique ID of this synset.
   */
  public long get9DigitID()
  {
    return this.getPOS().getPrefix() * 100000000 + this.getSynsetOffset();
  }
  
  
  /**
   * Convenience method to get list of related synsets for a specific relation
   * (by relation literal strings). For example,
   * {@code getRelatedSynsets("@")} will return the list of (direct) hypernyms
   * of this synset. See the WordNet documentation for the <a
   * href="http://wordnet.princeton.edu/man/wnsearch.3WN.html#toc4">list of
   * valid pointer symbols</a>.
   * 
   * (Added by LLT 20 April 2007)
   * 
   * @param pointerSymbol
   *            The pointer symbol for the relation required.
   * @return This list of {@literal Synset}s related to this {@literal} by
   *         <code>pointerSymbol</code>.
   */
  public List<Synset> getRelatedSynsets(final String pointerSymbol)
  {
    if ((pointerSymbol == null) || (pointerSymbol.trim().length() == 0))
    {
      return null;
    }
    
    List<Synset> synsets = new ArrayList<Synset>();
    List<Pointer> allPointers = this.getPointers();

    Pointer p;
    for (int i = 0; i < allPointers.size(); i++)
    {
      p = allPointers.get(i);
      if (p.getPointerSymbol().equals(pointerSymbol))
      {
        synsets.add(p.getSynset());
      }
    }
    
    return ((synsets == null) || synsets.isEmpty()) ? null : synsets;
  }
  
  
  /**
   * Adds a pointer object to the internal list.
   * 
   * @param pointer
   *            a pointer object
   */
  protected void addPointer(final Pointer pointer)
  {
    if (listPointers == null)
    {
      listPointers = new ArrayList<Pointer>(5);
    }
    
    listPointers.add(pointer);
  }
  
  
  /**
   * @return Returns the part of speech.
   */
  public PartOfSpeech getPOS()
  {
    // Check if this data has been loaded.  If not, load it.
    checkLoad();
    
    return pos;
  }
  
  
  /**
   * @param cPOS The part of speech to set.
   */
  protected void setPosType(final char cPOS)
  {
    pos = PartOfSpeech.getInstance(cPOS);
  }
  
  
  /**
   * @return Returns the synset offset.
   */
  public long getSynsetOffset()
  {
    return synsetOffset;
  }
  
  
  /**
   * @param lSynsetOffset The synset offset to set.
   */
  protected void setSynsetOffset(final long lSynsetOffset)
  {
    this.synsetOffset = lSynsetOffset;
  }
  
  
  /**
   * @return Returns the list of words from the line (synonyms).
   */
  public List<WordData> getWord()
  {
    // Check if this data has been loaded.  If not, load it.
    checkLoad();
    
    // Return the list of synonyms in this synset
    return listWords;
  }
  
  
  /**
   * @param word The word to set.
   */
  protected void addWord(final WordData word)
  {
    // Check if the array is already allocated
    if (listWords == null)
    {
      listWords = new ArrayList<WordData>(3);
    }
    
    listWords.add(word);
  }
  
  
  /**
   * Returns the number of frames.
   * 
   * @return the number of frames
   */
  public int getFrameCount()
  {
    // Check if this data has been loaded.  If not, load it.
    checkLoad();
    
    return frameCount;
  }
  
  
  /**
   * Sets the number of frames.
   * 
   * @param nFrameCount the number of frames
   */
  protected void setFrameCount(final int nFrameCount)
  {
    this.frameCount = nFrameCount;
    
    if ((frameCount > 0) && (listFrames == null))
    {
      listFrames = new ArrayList<FrameData>(frameCount);
    }
  }
  
  
  /**
   * Returns the current list of frames.
   * 
   * @return the current list of frames
   */
  public List<FrameData> getFrames()
  {
    // Check if this data has been loaded.  If not, load it.
    checkLoad();
    
    return listFrames;
  }
  
  
  /**
   * Add a frame to our current list.
   * 
   * @param frame a frame object
   */
  protected void addFrame(final FrameData frame)
  {
    if (listFrames == null)
    {
      listFrames = new ArrayList<FrameData>(5);
    }
    
    listFrames.add(frame);
  }
  
  
  /**
   * @see java.lang.Object#toString()
   * @return A string representation of this object
   */
  @Override
  public String toString()
  {
    // Check if this data has been loaded.  If not, load it.
    checkLoad();
    
    // Declare our string buffer
    StringBuffer buf = new StringBuffer(200);
    
    // Build the string
    buf.append("Synset-Offset: ").append(synsetOffset)
       .append("  Lex-FileNum: ").append(lexFilenum)
       .append("  POS: ").append(pos)
       .append("  Num-Words: ").append(numWords)
       .append("\nNum-Ptrs: ").append(pointerCount)
       .append("  Num-Frames: ").append(frameCount)
       .append("\nGloss: ").append(gloss);
    
    int index = 0;
    if (listWords == null)
    {
      buf.append("\nList-Words: (null)");
    }
    else
    {
      buf.append("\nList of Words (").append(numWords)
         .append(")");
      for (WordData word : listWords)
      {
        buf.append("\n  #").append(index + 1).append(": ")
           .append(word.toString());
        ++index;
      }
    }
    
    index = 0;
    if (listPointers == null)
    {
      buf.append("\nList-Pointers: (null)");
    }
    else
    {
      buf.append("\nList of Pointers (").append(pointerCount)
         .append(")");
      for (Pointer ptr : listPointers)
      {
        buf.append("\n  #").append(index + 1).append(": ")
           .append(ptr.toString());
        ++index;
      }
    }
    
    index = 0;
    if (listFrames == null)
    {
      buf.append("\nList-Frames: (no frames)");
    }
    else
    {
      buf.append("\nList of Frames (").append(frameCount)
         .append(")");
      for (FrameData frame : listFrames)
      {
        buf.append("\n  #").append(index + 1).append(": ")
           .append(frame.toString());
        ++index;
      }
    }
    
    // Return the string
    return buf.toString();
  }
  
  
  /**
   * Return whether this object equals another.
   * 
   * @param obj the object to compare to
   * @return whether the two objects are equal
   */
  @Override
  public boolean equals(final Object obj)
  {
    // Check the argument
    if (obj == null)
    {
      // It's null
      return false;
    }
    else if (!(obj instanceof Synset))
    {
      // It's not of the same class
      return false;
    }
    
    Synset other = (Synset) obj;
    return this.getPOS().equals(other.getPOS()) 
      && (this.getSynsetOffset() == other.getSynsetOffset())
      && (this.getLexFilenum() == other.getLexFilenum());
  }
  
  
  /**
   * Check if the data has been loaded.  If not, load it.
   */
  private void checkLoad()
  {
    // Check if the data has been loaded already
    if (loaded)
    {
      // It has, so just return
      return;
    }
    
    // Mark the data as loaded since we're going to load it now
    loaded = true;
    
    // Check the parameters
    if ((pos == null) || (synsetOffset <= 0))
    {
      // One or both of the required fields is not set, so just return
      return;
    }
    
    // Get the input filename
    final String inputFile = Utility.getFilename(pos, false);
    
    // Check the filename
    if ((inputFile == null) || (inputFile.length() < 1))
    {
      return;
    }
    
    // Open the file
    File file = new File(inputFile);
    
    // Verify it exists
    boolean bExists = file.exists();
    if (!bExists)
    {
      return;
    }
    
    // Verify it's a file
    if (!file.isFile())
    {
      return;
    }
    
    // Read the data from the file (just one line)
    try
    {
      // Open the file
      RandomAccessFile random = new RandomAccessFile(file, "r");
      
      // Seek to the right offset
      random.seek(synsetOffset);
      
      // Read the line
      String line = random.readLine();
      
      // Parse the line and populate this
      ParseDataFile.process(line, this);
      
      // Close the file and set it to null
      random.close();
      random = null;
    }
    catch (FileNotFoundException fnfe)
    {
      System.err.println("File not found exception: " + fnfe.getMessage());
      fnfe.printStackTrace();
    }
    catch (IOException ioe)
    {
      System.err.println("IO exception: " + ioe.getMessage());
      ioe.printStackTrace();
    }
  }
}
