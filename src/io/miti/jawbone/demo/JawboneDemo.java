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

package io.miti.jawbone.demo;

import io.miti.jawbone.Dictionary;
import io.miti.jawbone.IndexTerm;
import io.miti.jawbone.PartOfSpeech;
import io.miti.jawbone.Pointer;
import io.miti.jawbone.Synset;
import io.miti.jawbone.filter.TermFilter;
import io.miti.jawbone.filter.WildcardFilter;

import java.util.Iterator;

/**
 * This class shows a sample of how to use the Jawbone
 * API for accessing the WordNet data files.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class JawboneDemo
{
  /**
   * Default constructor.
   */
  private JawboneDemo()
  {
    super();
  }
  
  
  /**
   * Main entry point for the application.
   * 
   * @param args arguments to the application
   */
  public static void main(final String[] args)
  {
    // Set the path to the data files
    Dictionary.initialize("c:/Program Files/WordNet/2.1/dict");
    
    // Get an instance of the Dictionary object
    Dictionary dict = Dictionary.getInstance();
    
    // Declare a filter for all terms starting with "car", ignoring case
    TermFilter filter = new WildcardFilter("car*", true);
    
    // Get an iterator to the list of nouns
    Iterator<IndexTerm> iter = 
      dict.getIndexTermIterator(PartOfSpeech.NOUN, 1, filter);
    
    // Go over the list items
    while (iter.hasNext())
    {
      // Get the next object
      IndexTerm term = iter.next();
      
      // Write out the object
      System.out.println(term.toString());
      
      // Write out the unique pointers for this term
      int nNumPtrs = term.getPointerCount();
      if (nNumPtrs > 0)
      {
        // Print out the number of pointers
        System.out.println("\nThis term has " + Integer.toString(nNumPtrs) +
                           " pointers");
        
        // Print out all of the pointers
        String[] ptrs = term.getPointers();
        for (int i = 0; i < nNumPtrs; ++i)
        {
          String p = Pointer.getPointerDescription(term.getPartOfSpeech(), ptrs[i]);
          System.out.println(ptrs[i] + ": " + p);
        }
      }
      
      // Get the definitions for this term
      int nNumSynsets = term.getSynsetCount();
      if (nNumSynsets > 0)
      {
        // Print out the number of synsets
        System.out.println("\nThis term has " + Integer.toString(nNumSynsets) +
                           " synsets");
        
        // Print out all of the synsets
        Synset[] synsets = term.getSynsets();
        for (int i = 0; i < nNumSynsets; ++i)
        {
          System.out.println(synsets[i].toString());
        }
      }
    }
    
    System.out.println("Demo processing finished.");
  }
}
