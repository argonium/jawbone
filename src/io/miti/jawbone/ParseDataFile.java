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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class demonstrates how to parse the WordNet data
 * files (e.g., data.adv).
 * 
 * @author mwallace
 */
final class ParseDataFile
{
  /**
   * This class should not be constructed, so make the
   * default constructor private.
   */
  private ParseDataFile()
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
   * @param data the Synset object we want to populate
   * @return the result of the parsing
   */
  protected static boolean process(final String line,
                                   final Synset data)
  {
    // Check the input
    if ((line == null) || (line.length() < 1) || (line.charAt(0) == ' '))
    {
      return false;
    }
    else if (data == null)
    {
      return false;
    }
    
    // Split the line based on the space characters
    LineSplitter st = new LineSplitter(line, ' ');
    
    // Get token 0
    long lSynsetOffset = Long.parseLong(st.nextToken());
    data.setSynsetOffset(lSynsetOffset);
    
    // Get token 1
    int nLexFileNum = Integer.parseInt(st.nextToken());
    data.setLexFilenum(nLexFileNum);
    
    // Get token 2
    data.setPosType(st.nextToken().charAt(0));
    
    // Get token 3
    int nNumWordsInSynset = Integer.parseInt(st.nextToken(), 16);
    data.setNumWords(nNumWordsInSynset);
    
    // Get token 4
    int nIndex = 0;
    while (nIndex < nNumWordsInSynset)
    {
      // Get the word, and change underscores to spaces
      String word = st.nextToken().replace('_', ' ');
      
      // Get the lexicon ID
      final int lexID = Integer.parseInt(st.nextToken(), 16);
      String syntacticMarker = null;
      
      // Check if this is an adjective file
      final int nWordLen = word.length();
      if (data.getPOS().equals(PartOfSpeech.ADJECTIVE))
      {
        // Check if it ends with "(?)"
        if ((nWordLen > 3) && (word.endsWith(")")))
        {
          int nStartIndex = word.lastIndexOf('(');
          if ((nStartIndex >= 0) && (nStartIndex < (nWordLen - 2)))
          {
            syntacticMarker = word.substring(nStartIndex + 1,
                                             word.length() - 1);
          }
        }
      }
      
      // Build the WordData object
      WordData wd = new WordData(word, lexID, syntacticMarker,
                                 data.getPOS(), data.getSynsetOffset());
      
      // Check if we should load the sense number now
      if (!Utility.DELAY_LOADING_SYNSET_NUM)
      {
        // Load the sense number
        int senseNum = ParseIndexFile.getSenseNumber(word, data.getPOS(),
                                                     data.getSynsetOffset());
        
        // Save it
        wd.setSenseNumber(senseNum);
      }
      
      // Add the synonym to the list
      data.addWord(wd);
      
      // Increment the index
      ++nIndex;
    }
    
    // Get token 5
    int nNumPointers = Integer.parseInt(st.nextToken());
    data.setPointerCount(nNumPointers);
    
    // Get token 6
    nIndex = 0;
    while (nIndex < nNumPointers)
    {
      // Parse the pointer data
      String ptrSymbol = st.nextToken();
      long synsetOffset = Long.parseLong(st.nextToken());
      char pos = st.nextToken().charAt(0);
      String wordNumberStr = st.nextToken();
      int wordNumCurrentSynset =
        Integer.parseInt(wordNumberStr.substring(0, 2), 16);
      int wordNumTargetSynset =
        Integer.parseInt(wordNumberStr.substring(2), 16);
      
      // Build our pointer object
      Pointer ptr = new Pointer(ptrSymbol, synsetOffset,
          pos, wordNumCurrentSynset, wordNumTargetSynset);
      data.addPointer(ptr);
      
      // Increment the index counter
      ++nIndex;
    }
    
    // Get token 7
    if (data.getPOS().equals(PartOfSpeech.VERB))
    {
      // Read the frame count
      final int nFrameCount = Integer.parseInt(st.nextToken());
      data.setFrameCount(nFrameCount);
      
      nIndex = 0;
      while (nIndex < nFrameCount)
      {
        // Read the +
        st.nextToken();
        
        // Read the frame information
        final int frameNum = Integer.parseInt(st.nextToken());
        final int wordNum = Integer.parseInt(st.nextToken(), 16);
        FrameData frame = new FrameData(frameNum, wordNum);
        data.addFrame(frame);
        
        ++nIndex;
      }
    }
    
    // Get token 8
    final String bar = st.nextToken(); // Skip the "|"
    if (bar.equals("|"))
    {
      // Get all words from this point forward
      data.setGloss(st.restOfString());
    }
    
    return true;
  }
  
  
  /**
   * Opens the input file and calls the parser.
   * 
   * @param pos the part of speech (determines the input filename)
   * @return the list of objects for the specified part of speech
   */
  protected static List<Synset> parseFile(final PartOfSpeech pos)
  {
    // Check the input parameter
    if (pos == null)
    {
      return null;
    }
    
    // Get the input filename
    final String inputFile = Utility.getFilename(pos, false);
    
    // Check the filename
    if ((inputFile == null) || (inputFile.length() < 1))
    {
      return null;
    }
    
    // Open the file
    File file = new File(inputFile);
    
    // Verify it exists
    boolean bExists = file.exists();
    if (!bExists)
    {
      return null;
    }
    
    // Verify it's a file
    if (!file.isFile())
    {
      return null;
    }
    
    // Declare our list to return
    List<Synset> listData = new ArrayList<Synset>(500);
    
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
        
        // Declare the synset to fill in
        Synset data = new Synset();
        
        // Parse the line and populate the values
        if (process(str, data))
        {
          // Add the item to the list
          listData.add(data);
        }
      }
      
      // Close the reader
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
}
