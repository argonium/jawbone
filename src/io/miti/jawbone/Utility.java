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
import java.io.IOException;

/**
 * This class contains some utility methods used by other
 * classes.
 * 
 * @author mwallace
 */
public final class Utility
{
  /**
   * The names of the data files.
   */
  private static final String[] dataFiles = {"data.adv",
           "data.adj", "data.noun", "data.verb"};
  
  /**
   * The names of the index files.
   */
  private static final String[] indexFiles = {"index.adv",
           "index.adj", "index.noun", "index.verb"};
  
  /**
   * Whether to delay loading synset numbers for synonyms
   * in a synset list.
   */
  public static final boolean DELAY_LOADING_SYNSET_NUM = true;
  
  
  /**
   * The default constructor.
   */
  private Utility()
  {
    super();
  }
  
  
  /**
   * Return the filename for the part of speech and
   * type of file (index or data).
   * 
   * @param pos the part of speech
   * @param indexFile whether the filename is for the index file
   * @return the name of the input file for the part of speech
   */
  public static String getFilename(final PartOfSpeech pos,
                                   final boolean indexFile)
  {
    // Check the part of speech
    int index = -1;
    if (pos.equals(PartOfSpeech.NOUN))
    {
      index = 2;
    }
    else if (pos.equals(PartOfSpeech.ADVERB))
    {
      index = 0;
    }
    else if (pos.equals(PartOfSpeech.ADJECTIVE))
    {
      index = 1;
    }
    else if (pos.equals(PartOfSpeech.VERB))
    {
      index = 3;
    }
    
    // Generate a File object pointing to the file
    File file = new File(Dictionary.getDataFilesPath(),
        ((indexFile) ? indexFiles[index] : dataFiles[index]));
    
    // This will hold the file name
    String name = null;
    
    try
    {
      // Save the file name
      name = file.getCanonicalPath();
    }
    catch (IOException e)
    {
      System.err.println("Exception getting the file name: " + e.getMessage());
    }
    
    // Clear the File variable
    file = null;
    
    // Return the file name
    return name;
  }
}
