# long-lines
The Long Lines project is a simple Java class that takes an input string and a line length value, and inserts the line break character throughout the input string so that no line (the substring between line breaks) has a length greater than the specified line length value. The LongLines class has one method:

```
  public static String addLineBreaks(final String sInput,
                                     final int nMaxLen)
```

This method takes two arguments: the input string, and the maximum line length. The return value is the input string with line breaks inserted throughout the string.

Here is an example of how to call this code:

```
    String s = LongLines.addLineBreaks(
        "This long line is really not that long, but it is not short either.",
        20);
    System.out.println(s);
```

The output of this code snippet is the following:

```
  This long line is
  really not that
  long, but it is not
  short either.
```

The source code is released under the MIT license.
