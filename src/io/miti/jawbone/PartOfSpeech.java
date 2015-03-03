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
 * This class represents the parts of speech recognized
 * by WordNet.
 * 
 * @author mwallace
 * @version CHANGELOG: April 16 2007, LIM Lian Tze 
 * added numerical prefix for each parts of speech
 */
public final class PartOfSpeech
{
  /**
   * The character for the Noun part of speech.
   */
  private static final char NOUN_CHAR = 'n';
  
  /**
   * The character for the verb part of speech.
   */
  private static final char VERB_CHAR = 'v';
  
  /**
   * The character for the adjective part of speech.
   */
  private static final char ADJECTIVE_CHAR = 'a';
  
  /**
   * The character for the adj. satellite part of speech.
   */
  private static final char ADJECTIVE_SATELLITE_CHAR = 's';
  
  /**
   * The character for the adverb part of speech.
   */
  private static final char ADVERB_CHAR = 'r';

  
  // The following 4 offsets are added by LLT on 16 April
  
  /**
   * The prefix for the noun part of speech.
   */
  private static final byte NOUN_PREFIX = 1;
  
  /**
   * The prefix for the verb part of speech.
   */
  private static final byte VERB_PREFIX = 2;
  
  /**
   * The prefix for the adjective part of speech.
   */
  private static final byte ADJECTIVE_PREFIX = 3;
  
  /**
   * The prefix for the adverb part of speech.
   */
  private static final byte ADVERB_PREFIX = 4;
  

  // The following four factory constructors are modified by LLT on
  // 16 April 2007 to add in the 9-digit offsets.
  
  /**
   * The character for the Noun part of speech.
   */
  public static final PartOfSpeech NOUN =
    new PartOfSpeech("noun", NOUN_CHAR, NOUN_PREFIX);
  
  /**
   * The character for the verb part of speech.
   */
  public static final PartOfSpeech VERB =
    new PartOfSpeech("verb", VERB_CHAR, VERB_PREFIX);
  
  /**
   * The character for the adjective part of speech.
   */
  public static final PartOfSpeech ADJECTIVE =
    new PartOfSpeech("adj", ADJECTIVE_CHAR, ADJECTIVE_PREFIX);
  
  /**
   * The character for the adj. satellite part of speech.
   */
  public static final PartOfSpeech ADJECTIVE_SATELLITE =
    new PartOfSpeech("adj sat", ADJECTIVE_SATELLITE_CHAR, ADJECTIVE_PREFIX);
  
  /**
   * The character for the adverb part of speech.
   */
  public static final PartOfSpeech ADVERB =
    new PartOfSpeech("adv", ADVERB_CHAR, ADVERB_PREFIX);
  
  /**
   * The part of speech.
   */
  private String key = null;
  
  /**
   * The abbreviation for the part of speech.
   */
  private char shortKey = 0;
  
  /**
   * The numerical prefix/code for the part of speech.
   * (Added by LLT on 16 April 2007)
   */
  private byte prefix = -1;
  
  /**
   * This class should not be instantiated, so make the
   * default constructor private.
   */
  private PartOfSpeech()
  {
    super();
  }
  
  
  /**
   * Constructor taking the key.
   * Modified by LLT on 16 April 2007 to add in the 9-digit offset.
   * 
   * @param keyData the key for this part of speech
   * @param shortKeyData the character for this part of speech
   * @param prefixData the 9-digit prefix code for this part of speech
   */
  private PartOfSpeech(final String keyData, final char shortKeyData,
                       final byte prefixData)
  {
    key = keyData;
    shortKey = shortKeyData;
    prefix = prefixData;
  }
  
  
  /**
   * Returns the part of speech abbreviation.
   * 
   * @return the part of speech
   */
  public String getKey()
  {
    return key;
  }
  
  
  /**
   * Returns the abbreviation for this part of speech.
   * 
   * @return the part of speech abbreviation
   */
  public char getShortKey()
  {
    return shortKey;
  }
  
  
  /**
   * Returns the numberical prefix for this part of speech.
   * (Added by LLT on 16 April 2007)
   * 
   * @return the numerical part-of-speech prefix
   */
  public byte getPrefix()
  {
    return prefix;
  }
  
  
  /**
   * Return whether the object equals this.
   * 
   * @param obj the object to compare to this
   * @return whether the object equals this
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
    else if (!(obj instanceof PartOfSpeech))
    {
      // It's not of the same class
      return false;
    }
    
    // Cast away
    final PartOfSpeech pos = (PartOfSpeech) (obj);
    
    // Return the comparison of the two strings
    return key.equals(pos.key);
  }
  
  
  /**
   * Return the hashcode for the key.
   * 
   * @return the hashcode for the key
   */
  @Override
  public int hashCode()
  {
    return key.hashCode();
  }
  
  
  /**
   * Return this as a string.
   * 
   * @return this as a string
   */
  @Override
  public String toString()
  {
    return key;
  }
  
  
  /**
   * Return whether this character is the right one for this part
   * of speech.
   * 
   * @param posType the part of speech
   * @param pos the part of speech
   * @return whether posType equals pos
   */
  protected static boolean equals(final char posType,
                                  final PartOfSpeech pos)
  {
    return (pos.getShortKey() == posType);
  }
  
  
  /**
   * Returns the part of speech instance for the specified character.
   * 
   * @param partOfSpeech the part of speech character
   * @return the PartOfSpeech instance for the character
   */
  public static PartOfSpeech getInstance(final char partOfSpeech)
  {
    switch (partOfSpeech)
    {
      case NOUN_CHAR:
        return NOUN;
      
      case VERB_CHAR:
        return VERB;
        
      case ADJECTIVE_CHAR:
        return ADJECTIVE;
        
      case ADJECTIVE_SATELLITE_CHAR:
        return ADJECTIVE_SATELLITE;
        
      case ADVERB_CHAR:
        return ADVERB;
        
      default:
        throw new RuntimeException("Unknown type of part of speech: " +
            Character.toString(partOfSpeech));
    }
  } 
}
