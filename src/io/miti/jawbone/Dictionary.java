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

import io.miti.jawbone.filter.TermFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class encapsulates the functionality needed to access
 * the WordNet data files via a Java API.
 * 
 * @author mwallace
 * @version 1.0
 * @version CHANGELOG: April 24 2007, LIM Lian Tze added getSynset methods
 */

public final class Dictionary
{
  /**
   * The internal Dictionary object.
   */
  private static Dictionary dict = new Dictionary();
  
  /**
   * The path to the WordNet data files.
   */
  private static String path = null;
  
  /**
   * Whether the user can changed the path.
   */
  private static boolean pathChanged = true;
  
  /**
   * The validity of the path.
   */
  private static boolean pathValid = false;
  
  
  /**
   * Default constructor. 
   */
  private Dictionary()
  {
    super();
  }
  
  
  /**
   * Set the path to the WordNet data files.
   * 
   * @param pathData the path to the data
   */
  public static void initialize(final String pathData)
  {
    // We're changing the path
    pathChanged = true;
    
    // Set the path
    path = pathData;
    
    // Verify the path is valid
    pathValid = pathIsValid();
    
    // The path has been validated
    pathChanged = false;
  }
  
  
  /**
   * Factory method for this class.
   * 
   * @return an instance of Dictionary
   */
  public static Dictionary getInstance()
  {
    return dict;
  }
  
  
  /**
   * Return the path to the data files.
   * 
   * @return the path to the data files
   */
  public static String getDataFilesPath()
  {
    return path;
  }
  
  
  /**
   * Return whether the path variable is set to the directory.
   * 
   * @return whether the path variable is set to the directory
   */
  private static boolean pathIsValid()
  {
    // If the path hasn't changed since it was last checked,
    // return the cached result
    if ((!pathChanged))
    {
      return pathValid;
    }
    
    // The path has been validated
    pathChanged = false;
    
    // Default value
    pathValid = true;
    
    // Check the path variable
    if ((path == null) || (path.length() < 1))
    {
      // The string is null or empty
      pathValid = false;
    }
    else
    {
      // Check that it's a path
      File dir = new File(path);
      if (!dir.isDirectory())
      {
        dir = null;
        pathValid = false;
      }
      else
      {
        // See if it contains the expected data file
        dir = new File(path, "data.noun");
        if ((!dir.equals(dir)) || (!dir.isFile()))
        {
          // Either the file doesn't exist or it's not a file
          return false;
        }
      }
    }
    
    // Return the value
    return pathValid;
  }
  
  
  /**
   * Get an iterator to terms of the specified part of speech.
   * 
   * @param pos the part of speech
   * @param maxLimit the maximum number of results to return
   * @return an iterator to terms
   */
  public Iterator<IndexTerm> getIndexTermIterator(final PartOfSpeech pos,
                                                  final int maxLimit)
  {
    return getIndexTermIterator(pos, maxLimit, null);
  }
  
  
  /**
   * Get an iterator to terms of the specified part of speech.
   * 
   * @param pos the part of speech
   * @param filter the filter to apply to the search results
   * @return an iterator to terms
   */
  public Iterator<IndexTerm> getIndexTermIterator(final PartOfSpeech pos,
                                                  final TermFilter filter)
  {
    return getIndexTermIterator(pos, -1, filter);
  }
  
  
  /**
   * Get an iterator to terms of the specified part of speech.
   * 
   * @param pos the part of speech
   * @param maxLimit the maximum number of results to return
   * @param filter the filter to apply to the search results
   * @return an iterator to terms
   */
  public Iterator<IndexTerm> getIndexTermIterator(final PartOfSpeech pos,
                                                  final int maxLimit,
                                                  final TermFilter filter)
  {
    if (!pathIsValid())
    {
      throw new RuntimeException("The data path is either not set or is invalid");
    }
    
    // Parse the file and save the contents
    List<IndexTerm> listData = ParseIndexFile.parseFile(pos, maxLimit, filter);
    
    // Return an iterator to the list
    return listData.iterator();
  }
  
  
  /**
   * Get an iterator to terms of the specified part of speech.
   * 
   * @param pos the part of speech
   * @return an iterator to terms
   */
  public Iterator<IndexTerm> getIndexTermIterator(final PartOfSpeech pos)
  {
    return getIndexTermIterator(pos, -1);
  }
  
  
  /**
   * Get an iterator to terms for all parts of speech.
   * 
   * @param maxLimit the maximum number of results to return (-1 for no limit)
   * @param filter a filter for the search results (null for no filter)
   * @return an iterator to the search results
   */
  public Iterator<IndexTerm> getIndexTermIterator(final int maxLimit,
                                                  final TermFilter filter)
  {
    // Declare a list of all parts of speech
    PartOfSpeech[] pos = {PartOfSpeech.ADJECTIVE, PartOfSpeech.ADVERB,
                          PartOfSpeech.NOUN, PartOfSpeech.VERB};
    
    // Compute the maximum number of elements
    final int maxSize = (maxLimit > 0) ? (maxLimit * 4) : 100;
    
    // Declare the array that will hold all of the results
    List<IndexTerm> terms = new ArrayList<IndexTerm>(maxSize);
    
    // Iterate over the parts of speech
    for (int i = 0; i < pos.length; ++i)
    {
      // Get the iterator for the current POS
      Iterator<IndexTerm> iter = getIndexTermIterator(pos[i], maxLimit, filter);
      
      // Add all of the elements to the output array
      while (iter.hasNext())
      {
        terms.add(iter.next());
      }
    }
    
    // Sort the list
    java.util.Collections.sort(terms);
    
    // See if the size is larger than wanted
    if (terms.size() > maxLimit)
    {
      // It is, so return an iterator for the sublist
      return terms.subList(0, maxLimit).iterator();
    }
    
    // Return the iterator
    return terms.iterator();
  }
  
  
  /**
   * Convenience method for getting a synset directly using the part-of-speech
   * and 8-digit offset. (Added by LLT on April 24)
   * 
   * @param offset
   *            The 8-digit ID of the synset to access.
   * @param pos
   *            The part-of-speech of the sysnet to access.
   * @return The synset uniquely identified by <code>offset</code> and
   *         <code>pos</code>.
   */
  public Synset getSynset(final long offset, final char pos)
  {
    return this.getSynset(offset, PartOfSpeech.getInstance(pos));
  }
  
  
  /**
   * Convenience method for getting a synset directly using the part-of-speech
   * and 8-digit offset. (Added by LLT on April 24)
   * 
   * @param offset
   *            The 8-digit ID of the synset to access.
   * @param pos
   *            The part-of-speech of the sysnet to access.
   * @return The synset uniquely identified by <code>offset</code> and
   *         <code>pos</code>.
   */
  public Synset getSynset(final long offset, final PartOfSpeech pos)
  {
    return new Synset(offset, pos);
  }
  
  
  /**
   * Convenience method for getting a synset directly using the 9-digit ID.
   * (Added by LLT on April 24)
   * 
   * @param synsetID
   *            The 9-digit ID of the synset to access.
   * @return The synset uniquely identified by <code>synsetID</code>.
   *         <code>NULL</code> is returned if <code>synsetID</code> is not
   *         9-digit.
   */
  public Synset getSynset(final long synsetID)
  {
    int posPrefix = (int) (synsetID / 100000000);
    char pos;
    switch (posPrefix)
    {
      case 1:
          pos = 'n';
          break;
      case 2:
          pos = 'v';
          break;
      case 3:
          pos = 'a';
          break;
      case 4:
          pos = 'r';
          break;
      default:
          return null;
    }
    
    return this.getSynset(synsetID % 100000000, pos);
  }
}
