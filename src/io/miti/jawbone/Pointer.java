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
 * This class represents pointer data from the data files
 * used by WordNet.
 * 
 * @author mwallace
 */
public final class Pointer
{
  /**
   * The pointer symbol.
   */
  private String pointerSymbol;
  
  /**
   * The byte offset of the target synset in the data file
   * corresponding to the part of speech.
   */
  private Synset synset = null;
  
  /**
   * The part of speech.
   */
  private PartOfSpeech partOfSpeech;
  
  /**
   * The word number in the current (source) synset.
   */
  private int sourceSynsetWordNumber = 0;
  
  /**
   * The word number in the target synset.
   */
  private int targetSynsetWordNumber = 0;
  
  
  /**
   * The default constructor.
   */
  @SuppressWarnings("unused")
  private Pointer()
  {
    super();
  }
  
  
  /**
   * A constructor that takes all fields at once.
   * 
   * @param sPointerSymbol the pointer symbol
   * @param lSynsetOffset the byte offset of the target synset
   * @param cPartOfSpeech the part of speech
   * @param nSourceSynsetWordNumber the word number in the source synset
   * @param nTargetSynsetWordNumber the word number in the target synset
   */
  public Pointer(final String sPointerSymbol,
                 final long lSynsetOffset,
                 final char cPartOfSpeech,
                 final int nSourceSynsetWordNumber,
                 final int nTargetSynsetWordNumber)
  {
    pointerSymbol = sPointerSymbol;
    partOfSpeech = PartOfSpeech.getInstance(cPartOfSpeech);
    sourceSynsetWordNumber = nSourceSynsetWordNumber;
    targetSynsetWordNumber = nTargetSynsetWordNumber;
    
    // this.synsetOffset = lSynsetOffset;
    synset = new Synset(lSynsetOffset, partOfSpeech);
  }
  
  
  /**
   * @return Returns the partOfSpeech.
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
    this.partOfSpeech = PartOfSpeech.getInstance(cPartOfSpeech);
  }
  
  
  /**
   * @return Returns the pointer symbol.
   */
  public String getPointerSymbol()
  {
    return pointerSymbol;
  }
  
  
  /**
   * @param sPointerSymbol The pointer symbol to set.
   */
  protected void setPointerSymbol(final String sPointerSymbol)
  {
    pointerSymbol = sPointerSymbol;
  }
  
  
  /**
   * @return Returns the sourceSynsetWordNumber.
   */
  public int getSourceSynsetWordNumber()
  {
    return sourceSynsetWordNumber;
  }
  
  
  /**
   * @param nSourceSynsetWordNumber The sourceSynsetWordNumber to set.
   */
  protected void setSourceSynsetWordNumber(final int nSourceSynsetWordNumber)
  {
    this.sourceSynsetWordNumber = nSourceSynsetWordNumber;
  }
  
  
  /**
   * @return Returns the synset.
   */
  public Synset getSynset()
  {
    return synset;
  }
  
  
  /**
   * @param sSynset The synset to set.
   */
  protected void setSynset(final Synset sSynset)
  {
    synset = sSynset;
  }
  
  
  /**
   * @return Returns the targetSynsetWordNumber.
   */
  public int getTargetSynsetWordNumber()
  {
    return targetSynsetWordNumber;
  }
  
  
  /**
   * @param nTargetSynsetWordNumber The targetSynsetWordNumber to set.
   */
  protected void setTargetSynsetWordNumber(final int nTargetSynsetWordNumber)
  {
    this.targetSynsetWordNumber = nTargetSynsetWordNumber;
  }
  
  
  /**
   * Return a description of the pointer string for the specified
   * part of speech.
   * 
   * @param pos the part of speech
   * @param pointer the pointer string
   * @return a description of the pointer
   */
  public static String getPointerDescription(final PartOfSpeech pos,
                                             final String pointer)
  {
    // Check the input
    if ((pointer == null) || (pointer.length() < 1))
    {
      return "";
    }
    
    // Check the part of speech
    if (pos.equals(PartOfSpeech.ADJECTIVE))
    {
      if (pointer.equals("!"))
      {
        return "Antonym";
      }
      else if (pointer.equals("&"))
      {
        return "Similar to";
      }
      else if (pointer.equals("<"))
      {
        return "Participle of verb";
      }
      else if (pointer.equals("\\"))
      {
        return "Partainym (pertains to noun)";
      }
      else if (pointer.equals("="))
      {
        return "Attribute";
      }
      else if (pointer.equals("^"))
      {
        return "Also see";
      }
      else if (pointer.equals(";c"))
      {
        return "Domain of synset - Topic";
      }
      else if (pointer.equals(";r"))
      {
        return "Domain of synset - Region";
      }
      else if (pointer.equals(";u"))
      {
        return "Domain of synset - Usage";
      }
    }
    else if (pos.equals(PartOfSpeech.ADVERB))
    {
      if (pointer.equals("!"))
      {
        return "Antonym";
      }
      else if (pointer.equals("\\"))
      {
        return "Derived from adjective";
      }
      else if (pointer.equals(";c"))
      {
        return "Domain of synset - Topic";
      }
      else if (pointer.equals(";r"))
      {
        return "Domain of synset - Region";
      }
      else if (pointer.equals(";u"))
      {
        return "Domain of synset - Usage";
      }
    }
    else if (pos.equals(PartOfSpeech.NOUN))
    {
      if (pointer.equals("!"))
      {
        return "Antonym";
      }
      else if (pointer.equals("@"))
      {
        return "Hypernym";
      }
      else if (pointer.equals("@i"))
      {
        return "Instance Hypernym";
      }
      else if (pointer.equals("~"))
      {
        return "Hyponym";
      }
      else if (pointer.equals("~i"))
      {
        return "Instance Hyponym";
      }
      else if (pointer.equals("#m"))
      {
        return "Member holonym";
      }
      else if (pointer.equals("#s"))
      {
        return "Substance holonym";
      }
      else if (pointer.equals("#p"))
      {
        return "Part holonym";
      }
      else if (pointer.equals("%m"))
      {
        return "Member meronym";
      }
      else if (pointer.equals("%s"))
      {
        return "Substance meronym";
      }
      else if (pointer.equals("%p"))
      {
        return "Part meronym";
      }
      else if (pointer.equals("="))
      {
        return "Attribute";
      }
      else if (pointer.equals("+"))
      {
        return "Derivationally related form";
      }
      else if (pointer.equals(";c"))
      {
        return "Domain of synset - Topic";
      }
      else if (pointer.equals("-c"))
      {
        return "Member of this domain - Topic";
      }
      else if (pointer.equals(";r"))
      {
        return "Domain of synset - Region";
      }
      else if (pointer.equals("-r"))
      {
        return "Member of this domain - Region";
      }
      else if (pointer.equals(";u"))
      {
        return "Domain of synset - Usage";
      }
      else if (pointer.equals("-u"))
      {
        return "Member of this domain - Usage";
      }
    }
    else if (pos.equals(PartOfSpeech.VERB))
    {
      if (pointer.equals("!"))
      {
        return "Antonym";
      }
      else if (pointer.equals("@"))
      {
        return "Hypernym";
      }
      else if (pointer.equals("~"))
      {
        return "Hyponym";
      }
      else if (pointer.equals("*"))
      {
        return "Entailment";
      }
      else if (pointer.equals(">"))
      {
        return "Cause";
      }
      else if (pointer.equals("^"))
      {
        return "Also see";
      }
      else if (pointer.equals("$"))
      {
        return "Verb Group";
      }
      else if (pointer.equals("+"))
      {
        return "Derivationally related form";
      }
      else if (pointer.equals(";c"))
      {
        return "Domain of synset - Topic";
      }
      else if (pointer.equals(";r"))
      {
        return "Domain of synset - Region";
      }
      else if (pointer.equals(";u"))
      {
        return "Domain of synset - Usage";
      }
    }
    
    // The part of speech was not found, so return
    return "[Unknown]";
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
    buf.append("Symbol: ").append(pointerSymbol).append("  Synset-Offset: ")
       .append(synset.getSynsetOffset()).append("  POS: ").append(partOfSpeech)
       .append("  Source-Word-Number: ").append(sourceSynsetWordNumber)
       .append("  Target-Word-Number: ").append(targetSynsetWordNumber);
    
    // Return the string
    return buf.toString();
  }
}
