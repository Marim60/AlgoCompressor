package com.compress.algocompressor.algorithms.implementation.LZ77;
import com.compress.algocompressor.algorithms.interfaces.IEncoder;

import java.io.FileWriter;
import java.io.IOException;

public class Lz77Encoder implements IEncoder {
    private String text;
    public Lz77Encoder(String text) {
        this.text = text;
    }
    public void encode(String encodeFile) throws IOException {
        FileWriter fWriter = new FileWriter(encodeFile);
        int index = 0;
        String currentString = "";
        // return the char value of specific index like [i] in c++
        currentString += text.charAt(index);
        int beginningOfCurrentString = index;
        while (index < text.length()) {
            if (currentString.length() == 0) {
                currentString += text.charAt(index);
                beginningOfCurrentString = index;
            }
            // Params: the beginning index, inclusive.
            // endIndex – the ending index, exclusive.
            // return substring.
            String searchWindow = text.substring(0, beginningOfCurrentString);
            // Params: str – the substring to search for.
            // return the index of the last occurrence
            int startPosition = searchWindow.lastIndexOf(currentString);
            int position = 0, length = 0;
            char nexSymbol;
            int lengthOfBuffer = beginningOfCurrentString;
            int lastIndex = startPosition;
            while ((startPosition != -1) && (index < text.length() - 1)) {
                lastIndex = startPosition;
                currentString += text.charAt(++index);
                searchWindow = text.substring(0, ++lengthOfBuffer);
                startPosition = searchWindow.lastIndexOf(currentString);
            }
            // case 1 the character didn't appear in the search window before
            if (currentString.length() == 1) {
                nexSymbol = currentString.charAt(0);
                position = length = 0;
                index++;
                currentString = "";
            } else {
                nexSymbol = currentString.charAt(currentString.length() - 1);
                position = beginningOfCurrentString - lastIndex;
                index++;
                length = currentString.length() - 1;
                currentString = "";
            }
            fWriter.write("<" + position + ", " + length + ", '" + nexSymbol + "'>" + "\n");
        }
        fWriter.close();
    }
}