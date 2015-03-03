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

import io.miti.jawbone.filter.ExactMatchFilter;
import io.miti.jawbone.filter.TermFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * This class demonstrates how to parse the WordNet index
 * files (e.g., index.adv).
 * 
 * @author mwallace
 */
public final class ParseIndexFile
{
  /**
   * This class should not be constructed, so make the
   * default constructor private.
   */
  private ParseIndexFile()
  {
    super();
  }
  
  
  /**
   * Simple method to write a message to standard error.
   * 
   * @param msg the string to write out
   */
  private static void writeErr(final String msg)
  {
    System.err.println(msg);
  }
  
  
  /**
   * Parse a line from the input file and store the
   * data in a structure.
   * 
   * @param line the input line to parse
   * @return the IndexTerm object for the line
   */
  private static IndexTerm process(final String line)
  {
    // Check the input
    if ((line == null) || (line.length() < 1))
    {
      return null;
    }
    
    // Declare the object we want to populate
    IndexTerm data = new IndexTerm();
    
    // Split the line based on the space characters
    LineSplitter st = new LineSplitter(line, ' ');
    
    // Get token 0
    data.setLemma(st.nextToken());
    
    // Get token 1
    data.setPartOfSpeech(st.nextToken().charAt(0));
    
    // Get token 3
    int nNumSynsets = Integer.parseInt(st.nextToken());
    data.setSynsetCount(nNumSynsets);
    
    // Get token 4
    final int nNumPointers = Integer.parseInt(st.nextToken());
    data.setPointerCount(nNumPointers);
    
    // Get token 5
    int nIndex = 0;
    while (nIndex < nNumPointers)
    {
      // Parse the pointer data
      String ptrSymbol = st.nextToken();
      data.setPointerSymbol(nIndex, ptrSymbol);
      
      // Increment the index counter
      ++nIndex;
    }
    
    // Get token 6, but ignore it
    st.nextToken();
    
    // Get token 7
    final int nTagSenseCount = Integer.parseInt(st.nextToken());
    data.setTagSenseCount(nTagSenseCount);
    
    // Get token 8
    nIndex = 0;
    while (nIndex < nNumSynsets)
    {
      final int synsetOffset = Integer.parseInt(st.nextToken());
      data.setSynsetOffset(nIndex, synsetOffset);
      
      ++nIndex;
    }
    
    return data;
  }
  
  
  /**
   * Opens the input file and calls the parser.
   * 
   * @param pos the part of speech (determines the input filename)
   * @param maxLimit the maximum number of results to return (-1 for no limit)
   * @param filter the filter to apply to the search results
   * @return the list of matching search results
   */
  protected static List<IndexTerm> parseFile(final PartOfSpeech pos,
                                             final int maxLimit,
                                             final TermFilter filter)
  {
    // Check the input parameter
    if (pos == null)
    {
      throw new RuntimeException("The part of speech is null");
    }
    else if (maxLimit == 0)
    {
      return new ArrayList<IndexTerm>(1);
    }
    
    // Get the input filename
    final String inputFile = Utility.getFilename(pos, true);
    
    // Check the filename
    if ((inputFile == null) || (inputFile.length() < 1))
    {
      throw new RuntimeException("The input file was not specified");
    }
    
    // Open the file
    File file = new File(inputFile);
    
    // Verify it exists
    boolean bExists = file.exists();
    if (!bExists)
    {
      throw new RuntimeException("The data file does not exist");
    }
    
    // Verify it's a file
    if (!file.isFile())
    {
      throw new RuntimeException("The data file is not a file");
    }
    
    // Declare our list to return
    List<IndexTerm> listData = null;
    if (maxLimit > 0)
    {
      // A maximum size was specified
      listData = new ArrayList<IndexTerm>(maxLimit);
    }
    else
    {
      // No maximum size was specified
      listData = new ArrayList<IndexTerm>(500);
    }
    
    // Read the input file
    BufferedReader reader = null;
    try
    {
      reader = new BufferedReader(new java.io.FileReader(file));
      String str;
      while ((str = reader.readLine()) != null)
      {
        // Check if we should process this line
        if ((str.length() < 1) || (str.charAt(0) == ' '))
        {
          continue;
        }
        
        IndexTerm data = process(str);
        if (data != null)
        {
          // Check the filter
          if ((filter == null) || (filter.accept(data.getLemma())))
          {
            // Save the object
            listData.add(data);
            
            // See if we've hit the limit
            if ((maxLimit > 0) && (listData.size() >= maxLimit))
            {
              break;
            }
          }
        }
      }
      
      reader.close();
      reader = null;
    }
    catch (FileNotFoundException fnfe)
    {
      // Write an error message
      writeErr("File Not Found: " + fnfe.getMessage());
    }
    catch (IOException ioe)
    {
      // Write an error message
      writeErr("Exception reading: " + ioe.getMessage());
    }
    finally
    {
      if (reader != null)
      {
        try
        {
          reader.close();
          reader = null;
        }
        catch (Exception e)
        {
          // Nothing to do here
          reader = null;
        }
      }
    }
    
    return listData;
  }
  
  
  /**
   * Return the sense number (from the index file) of the specified
   * word and part of speech.  It does this by finding a matching
   * on the offset in the list of offsets associated with this word.
   * 
   * @param word the word to search for
   * @param pos the part of speech
   * @param offset the offset to find a match on
   * @return the sense number
   */
  public static int getSenseNumber(final String word,
                                   final PartOfSpeech pos,
                                   final long offset)
  {
    // This is the variable that gets returned
    int senseNum = 0;
    
    // Find the term in the index file
    List<IndexTerm> terms = parseFile(pos, 1, new ExactMatchFilter(word, false));
    if ((terms == null) || (terms.size() < 1))
    {
      // The term was not found.  This should not happen.
      return senseNum;
    }
    
    // Get the synsets for the first term (there's only one term in the list)
    Synset[] sets = terms.get(0).getSynsets();
    for (int index = 0; index < sets.length; ++index)
    {
      if (sets[index].getSynsetOffset() == offset)
      {
        // Save the index (this is 1-based, so add 1)
        senseNum = index + 1;
        
        // We found a match, so break the loop
        break;
      }
    }
    
    // Return the sense number 
    return senseNum;
  }
}
