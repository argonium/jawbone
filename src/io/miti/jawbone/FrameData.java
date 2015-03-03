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
 * This class represents frames, which are only relevant to verbs.
 * Frames are a list of numbers corresponding to the generic
 * verb sentence frames for words in the synset.
 * 
 * @author mwallace
 */
public final class FrameData
{
  /**
   * The frame number.
   */
  private int frameNum = 0;
  
  /**
   * The number of the word in the synset that the frame applies to.
   */
  private int wordNum = 0;
  
  
  /**
   * Default constructor.
   */
  public FrameData()
  {
    super();
  }
  
  
  /**
   * Constructor that takes all fields at once.
   * 
   * @param nFrameNum the frame number
   * @param nWordNum the word number in the synset for the frame number
   */
  public FrameData(final int nFrameNum, final int nWordNum)
  {
    this.frameNum = nFrameNum;
    this.wordNum = nWordNum;
  }
  
  
  /**
   * @return Returns the frame number.
   */
  public int getFrameNum()
  {
    return frameNum;
  }
  
  
  /**
   * @param nFrameNum The frame number to set.
   */
  public void setFrameNum(final int nFrameNum)
  {
    this.frameNum = nFrameNum;
  }
  
  
  /**
   * @return Returns the word number.
   */
  public int getWordNum()
  {
    return wordNum;
  }
  
  
  /**
   * @param nWordNum The word number to set.
   */
  public void setWordNum(final int nWordNum)
  {
    this.wordNum = nWordNum;
  }
  
  
  /**
   * @see java.lang.Object#toString()
   * @return A String representation of this object
   */
  @Override
  public String toString()
  {
    // Declare our string buffer
    StringBuffer buf = new StringBuffer(20);
    
    // Build the string
    buf.append("Frame-Number: ").append(frameNum)
       .append("  Word-Number: ").append(wordNum);
    
    // Return the string
    return buf.toString();
  }
}
