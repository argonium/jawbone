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
 * This class represents each line of data in the WordNet
 * index files.
 * 
 * @author mwallace
 */
public final class IndexTerm implements Comparable<IndexTerm>
{
  /**
   * The word.
   */
  private String lemma = null;
  
  /**
   * The part of speech.
   */
  private PartOfSpeech partOfSpeech;
  
  /**
   * Number of synsets that lemma is in. This is the number of
   * senses of the word in WordNet.
   */
  private int synsetCount = 0;
  
  /**
   * Number of different pointers that lemma has in all synsets
   * containing it.
   */
  private int pointerCount = 0;
  
  /**
   * The list of pointers that lemma has in all synsets containing it.
   */
  private String[] pointers;
  
  /**
   * Number of senses of lemma that are ranked according to their
   * frequency of occurrence in semantic concordance texts.
   */
  private int tagSenseCount = 0;
  
  /**
   * Byte offset in data.pos file of a synset containing lemma.
   * Each synset offset in the list corresponds to a different
   * sense of lemma in WordNet.
   */
  private Synset[] synsets;
  
  
  /**
   * Default constructor.
   */
  protected IndexTerm()
  {
    super();
  }
  
  
  /**
   * @return Returns the lemma.
   */
  public String getLemma()
  {
    return lemma;
  }
  
  
  /**
   * @param sLemma The lemma to set.
   */
  protected void setLemma(final String sLemma)
  {
    // Check if the lemma is null or has no underscores
    if ((sLemma == null) || (sLemma.indexOf('_') < 0))
    {
      lemma = sLemma;
    }
    else
    {
      // Replace each _ with a space
      lemma = sLemma.replace('_', ' ');
    }
  }
  
  
  /**
   * @return Returns the part of speech.
   */
  public PartOfSpeech getPartOfSpeech()
  {
    return partOfSpeech;
  }
  
  
  /**
   * @param cPartOfSpeech The part of speech to set.
   */
  protected void setPartOfSpeech(final char cPartOfSpeech)
  {
    partOfSpeech = PartOfSpeech.getInstance(cPartOfSpeech);
  }
  
  
  /**
   * @return Returns the pointer count.
   */
  public int getPointerCount()
  {
    return pointerCount;
  }
  
  
  /**
   * @param nPointerCount The pointer count to set.
   */
  protected void setPointerCount(final int nPointerCount)
  {
    pointerCount = nPointerCount;
    
    // Check if the count is greater than zero
    if (pointerCount > 0)
    {
      // It is, so allocate the array
      pointers = new String[pointerCount];
    }
  }
  
  
  /**
   * @return Returns the pointer symbols.
   */
  public String[] getPointers()
  {
    // Check the array length
    if (pointers == null)
    {
      // It's null or empty so return null
      return null;
    }
    
    // Make a new array
    String[] data = new String[pointers.length];
    
    // Copy the array
    System.arraycopy(pointers, 0, data, 0, pointers.length);
    
    // Return the copy
    return data;
  }
  
  
  /**
   * Sets the pointer symbol in the specified location
   * in the array.
   * 
   * @param index the index to store the symbol in the array
   * @param sPointerSymbol The pointer symbols to set
   */
  protected void setPointerSymbol(final int index,
                                  final String sPointerSymbol)
  {
    pointers[index] = sPointerSymbol;
  }
  
  
  /**
   * @return Returns the synset count.
   */
  public int getSynsetCount()
  {
    return synsetCount;
  }
  
  
  /**
   * @param nSynsetCount The synset count to set.
   */
  protected void setSynsetCount(final int nSynsetCount)
  {
    synsetCount = nSynsetCount;
    
    // Check if the count is greater than zero
    if (synsetCount > 0)
    {
      // It is, so allocate the array
      synsets = new Synset[synsetCount];
    }
  }
  
  
  /**
   * @return Returns the synsets.
   */
  public Synset[] getSynsets()
  {
    // Check the array length
    if (synsets == null)
    {
      // It's null or empty so return null
      return null;
    }
    
    // Make a new array
    Synset[] data = new Synset[synsets.length];
    
    // Copy the array
    System.arraycopy(synsets, 0, data, 0, synsets.length);
    
    // Return the copy
    return data;
  }
  
  
  /**
   * Sets the synset offset in the specified location
   * in the array.
   * 
   * @param index the index to store the offset in the array
   * @param lSynsetOffset The synset offset to set
   */
  protected void setSynsetOffset(final int index,
                              final long lSynsetOffset)
  {
    synsets[index] = new Synset(lSynsetOffset, partOfSpeech);
  }
  
  
  /**
   * @return Returns the tag sense count.
   */
  public int getTagSenseCount()
  {
    return tagSenseCount;
  }
  
  
  /**
   * @param nTagSenseCount The tag sense count to set.
   */
  protected void setTagSenseCount(final int nTagSenseCount)
  {
    tagSenseCount = nTagSenseCount;
  }


  /**
   * @see java.lang.Object#finalize()
   * @throws Throwable an exception for the parent's finalize
   */
  @Override
  protected void finalize() throws Throwable
  {
    // Call the parent's method first
    super.finalize();
    
    // Clear the stored data
    lemma = null;
    pointers = null;
    synsets = null;
  }


  /**
   * @see java.lang.Object#toString()
   * @return a string representation of this object
   */
  @Override
  public String toString()
  {
    // Declare the string buffer to hold the output
    StringBuffer buf = new StringBuffer(150);
    
    // Build the string
    buf.append("Lemma: ").append(lemma)
       .append("  POS: ").append(partOfSpeech)
       .append("  Tag-Sense-Count: ").append(tagSenseCount);
    
    // Add the list of synset offsets
    buf.append("\nList of Synsets (").append(synsetCount)
       .append(")");
    for (int index = 0; index < synsetCount; ++index)
    {
      buf.append("\n  #").append(index + 1).append(": ")
         .append(Long.toString(synsets[index].getSynsetOffset()));
    }
    
    // Add the list of pointer symbols
    buf.append("\nList of Pointers (").append(pointerCount)
       .append(")");
    for (int index = 0; index < pointerCount; ++index)
    {
      buf.append("\n  #").append(index + 1).append(": ")
         .append(pointers[index]).append(" (")
         .append(Pointer.getPointerDescription(partOfSpeech, pointers[index]))
         .append(")");
    }
    
    // Return the string
    return buf.toString();
  }
  
  
  /**
   * Implemented method for the Comparable interface.
   * 
   * @param obj the obj to compare this to
   * @return the result of the comparison
   */
  public int compareTo(final IndexTerm obj)
  {
    // Check the input
    if (obj == null)
    {
      return -1;
    }
    
    // Save the lemma from the argument
    String lemma2 = obj.getLemma();
    
    // Check for nullness
    if ((lemma == null) && (lemma2 == null))
    {
      return 0;
    }
    else if (lemma == null)
    {
      return 1;
    }
    else if (lemma2 == null)
    {
      return -1;
    }
    
    // Return the comparison value
    return (lemma.compareTo(lemma2));
  }
}
