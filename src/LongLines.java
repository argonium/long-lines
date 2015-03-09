package io.miti.LongLines;

import java.util.StringTokenizer;

/**
 * This class has a method for parsing an input string and
 * inserting line breaks such that no line has a length
 * greater than some value, either the default value or
 * a value supplied by the user.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class LongLines
{
  /**
   * This is the default maximum line length.
   */
  public static final int DEFAULT_MAX_LINE_LENGTH = 60;
  
  
  /**
   * Private default constructor.
   */
  private LongLines()
  {
    super();
  }
  
  
  /**
   * Add line breaks to the string, using the default
   * output line length.
   * 
   * @param sInput the input string
   * @return the input string with line breaks added in
   */
  public static String addLineBreaks(final String sInput)
  {
    return addLineBreaks(sInput, DEFAULT_MAX_LINE_LENGTH);
  }
  
  
  /**
   * Trim all trailing whitespace from a string.
   * 
   * @param sInput the input string
   * @return the input string with all trailing whitespace removed
   */
  private static String trimTrailingBlanks(final String sInput)
  {
    // Check the input string
    if (sInput == null)
    {
      // The string is null
      return null;
    }
    else if (sInput.length() < 1)
    {
      // The string is empty
      return sInput;
    }
    
    // Save the starting point
    int nIndex = sInput.length() - 1;
    
    // Iterate over the string, starting from the end, until we
    // hit a non-whitespace character
    while ((nIndex >= 0) && (sInput.charAt(nIndex) <= ' '))
    {
      --nIndex;
    }
    
    // Check if we went negative
    if (nIndex < 0)
    {
      // The input string is all whitespace
      return "";
    }
    
    // Return everything up to nIndex
    return sInput.substring(0, nIndex + 1);
  }
  
  
  /**
   * Add line breaks to a string, using the supplied
   * maximum line length.
   * 
   * @param sInput the input string
   * @param nMaxLen the maximum length of each line
   * @return the input string with line breaks added in
   */
  public static String addLineBreaks(final String sInput,
                                     final int nMaxLen)
  {
    // Check the input string
    if (sInput == null)
    {
      return "";
    }
    
    // Save the size and check against the inputs
    final int nLineLen = sInput.length();
    if ((nMaxLen < 1) || (sInput.length() <= nMaxLen))
    {
      return sInput;
    }
    
    // This will hold the value returned at the end
    StringBuffer buf = new StringBuffer(nLineLen + 20);
    
    // Declare a tokenizer for the line-ending characters
    StringTokenizer st = new StringTokenizer(sInput, "\r\n\f");
    while (st.hasMoreTokens())
    {
      // Get the next token
      String sLine = st.nextToken();
      
      // Check if the line is less than the maximum line length
      if (sLine.length() <= nMaxLen)
      {
        // This line does not exceed the max line length
        buf.append(sLine);
        buf.append('\n');
      }
      else
      {
        // This line is too long, so parse it based on max line
        // length and the space character.
        
        // Find the first space character
        if (sLine.indexOf(' ') < 0)
        {
          // The line contains no spaces, so just append it
          buf.append(sLine);
          buf.append('\n');
        }
        else
        {
          // Parse the line into smaller lines
          parseLine(sLine, buf, nMaxLen);
        }
      }
    }
    
    // Return the newly-formatted string
    return buf.toString();
  }
  
  
  /**
   * Internal method to parse the line.
   * 
   * @param line the input string to parse
   * @param buf the buffer to append the new string to
   * @param nMaxLen the maximum line length
   */
  private static void parseLine(final String line,
                                final StringBuffer buf,
                                final int nMaxLen)
  {
    // Trim whitespace from the end of the string
    final String sInput = trimTrailingBlanks(line);
    
    // Save the length of the input string
    final int nLength = sInput.length();
    if (nLength == 0)
    {
      // The string is all whitespace, so just return a newline
      buf.append('\n');
      return;
    }
    
    // Save the start of this frame
    int nFrameStartIndex = 0;
    
    // Find the start of the non-whitespace
    int nLookAheadIndex = 0;
    while ((nLookAheadIndex < nLength) && (sInput.charAt(nLookAheadIndex) == ' '))
    {
      ++nLookAheadIndex;
    }
    
    // Check if we went past the max line length
    if (nLookAheadIndex >= nMaxLen)
    {
      // There are enough spaces at the beginning that we need to
      // put them on their own line
      buf.append(sInput.substring(nFrameStartIndex, nMaxLen)).append('\n');
      
      // Update the start frame index.  They now both point to the first
      // non-whitespace character in the string.
      nFrameStartIndex = nLookAheadIndex;
    }
    
    // Find the next space
    int nNextSpaceIndex = sInput.indexOf(' ', nLookAheadIndex);
    
    // This is the index of the end of the current frame.  This is
    // used when a space is found, but the frame is still shorter
    // than the allowed maximum line length.
    int nFrameEndIndex = nFrameStartIndex;
    
    // Keep looping while there are more spaces
    while (nNextSpaceIndex > 0)
    {
      // Calculate the current line length
      final int nCurrentLineLength = nNextSpaceIndex - nFrameStartIndex;
      
      // See if the line length is equal to what we're looking for
      if (nCurrentLineLength == nMaxLen)
      {
        // Append the current substring
        buf.append(trimTrailingBlanks(sInput.substring(nFrameStartIndex,
                                                       nNextSpaceIndex)))
           .append('\n');
        
        // Update the start of the frame
        nFrameStartIndex = nNextSpaceIndex;
        
        // Advance nFrameStartIndex to point to the next non-space
        while ((nFrameStartIndex < nLength) && (sInput.charAt(nFrameStartIndex) == ' '))
        {
          ++nFrameStartIndex;
        }
        
        // Update the end of the frame index
        nFrameEndIndex = nFrameStartIndex;
      }
      else if (nCurrentLineLength > nMaxLen)
      {
        // The calculated line is too long.  See if the start and
        // end indices are the same.
        if (nFrameStartIndex == nFrameEndIndex)
        {
          // The indices are the same, so the current word is too
          // long, so just append all of it
          buf.append(trimTrailingBlanks(sInput.substring(
                         nFrameStartIndex, nNextSpaceIndex)))
             .append('\n');
          
          // Update the start of the frame
          nFrameStartIndex = nNextSpaceIndex;
        }
        else
        {
          // The indices are different, so append the current frame
          buf.append(trimTrailingBlanks(sInput.substring(nFrameStartIndex,
                                                         nFrameEndIndex)))
             .append('\n');
          
          // Update the start of the frame
          nFrameStartIndex = nFrameEndIndex;
        }
        
        // Advance nFrameStartIndex to point to the next non-space
        while ((nFrameStartIndex < nLength) && (sInput.charAt(nFrameStartIndex) == ' '))
        {
          ++nFrameStartIndex;
        }
        
        // Update the end of the frame index
        nFrameEndIndex = nFrameStartIndex;
      }
      else
      {
        // The frame is still shorter than the allowed line length, so update
        // the end of the frame with the index of the current space
        nFrameEndIndex = nNextSpaceIndex;
      }
      
      // Find the next space
      nNextSpaceIndex = sInput.indexOf(' ', nFrameEndIndex + 1);
    }
    
    // There are no more spaces at the end of the line.  Check
    // if the frame indices are the same
    if (nFrameStartIndex == nFrameEndIndex)
    {
      // They are, so append the substring starting the frame start
      buf.append(trimTrailingBlanks(sInput.substring(nFrameStartIndex)))
         .append('\n');
    }
    else
    {
      // The frame indices are different, so first append the frame
      buf.append(trimTrailingBlanks(sInput.substring(nFrameStartIndex,
                                                     nFrameEndIndex)))
         .append('\n');
      
      // Now append everything after the frame
      buf.append(sInput.substring(nFrameEndIndex).trim())
         .append('\n');
    }
  }
  
  
  /**
   * Main entry point for the application.
   * 
   * @param args arguments supplied by the user
   */
  public static void main(final String[] args)
  {
    // Show sample usage for this class
    System.out.println(addLineBreaks(
        "This long line is really not that long, but it is not short either.",
        13));
  }
}
