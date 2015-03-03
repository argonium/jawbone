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
 * Utility class to parse a line using a single character as
 * the delimiter.  This is used instead of a StringTokenizer
 * because the application needs the additional ability to
 * get the rest of the line at any point.
 * 
 * @author mwallace
 */
public final class LineSplitter
{
  /**
   * The current position within the string.
   */
  private int nCurrIndex = 0;
  
  /**
   * The string to split.
   */
  private String str = null;
  
  /**
   * The delimiter character.
   */
  private char delim = ' ';
  
  
  /**
   * Default constructor.
   */
  public LineSplitter()
  {
    super();
  }
  
  
  /**
   * Constructor that takes the input string and the
   * delimiter to use.
   * 
   * @param sStr the input string
   * @param cDelim the delimiter character
   */
  public LineSplitter(final String sStr, final char cDelim)
  {
    this.str = sStr;
    this.delim = cDelim;
  }
  
  
  /**
   * Constructor that takes the delimiter character
   * to use.
   * 
   * @param sStr the input string
   */
  public LineSplitter(final String sStr)
  {
    this.str = sStr;
  }
  
  
  /**
   * Returns the next token in the string, if there
   * is one.  Otherwise, returns an empty string.
   * 
   * @return the next token in the string
   */
  public String nextToken()
  {
    // Check the input
    if ((str == null) || (str.length() < 1))
    {
      return "";
    }
    
    // Save the length
    final int nLen = str.length();
    
    // Go through the string while the current character
    // is the delimiter
    while ((nCurrIndex < nLen) && (str.charAt(nCurrIndex) == delim))
    {
      ++nCurrIndex;
    }
    
    // Check the index
    if (nCurrIndex >= nLen)
    {
      // We're at the end, so return an empty string
      return "";
    }
    
    // Save the current spot
    final int nStartIndex = nCurrIndex;
    
    // Find the next delimiter
    while ((nCurrIndex < nLen) && (str.charAt(nCurrIndex) != delim))
    {
      ++nCurrIndex;
    }
    
    // Save the current string
    String outStr = str.substring(nStartIndex, nCurrIndex);
    
    // Return the selected substring
    return outStr;
  }
  
  
  /**
   * Returns the remainder of the line (past the delimiter).
   * 
   * @return the remainder of the line
   */
  public String restOfString()
  {
    // Check the input
    if ((str == null) || (str.length() < 1))
    {
      return "";
    }
    
    // Save the length
    final int nLen = str.length();
    
    // Go through the string while the current character
    // is the delimiter
    while ((nCurrIndex < nLen) && (str.charAt(nCurrIndex) == delim))
    {
      ++nCurrIndex;
    }
    
    // Check the index
    if (nCurrIndex >= nLen)
    {
      // We're at the end, so return an empty string
      return "";
    }
    
    // Save the string to retrieve
    String outStr = str.substring(nCurrIndex);
    
    // Update the index to point to the end
    nCurrIndex = nLen;
    
    return outStr;
  }
  
  
  /**
   * Returns whether the input string has more tokens.
   * 
   * @return whether the input string has more tokens
   */
  public boolean hasMoreTokens()
  {
    // Check the input
    if ((str == null) || (str.length() < 1))
    {
      return false;
    }
    
    // Save the length
    final int nLen = str.length();
    
    // Go through the string while the current character
    // is the delimiter
    while ((nCurrIndex < nLen) && (str.charAt(nCurrIndex) == delim))
    {
      ++nCurrIndex;
    }
    
    // Check the index
    if (nCurrIndex >= nLen)
    {
      // We're at the end, so return an empty string
      return false;
    }
    
    // If we reached this point, and we're not at the
    // end of the line, we must have reached a non-delimiter
    // character, so there is at least one more token
    // that could be returned.
    return true;
  }
}
