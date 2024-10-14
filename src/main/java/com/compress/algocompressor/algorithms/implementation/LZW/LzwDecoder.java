package com.compress.algocompressor.algorithms.implementation.LZW;

import com.compress.algocompressor.algorithms.interfaces.IDecoder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class LzwDecoder implements IDecoder {
    private Map<Integer, String> dictionary = new LinkedHashMap<>();
    private Vector<Integer> compressed = new Vector<>();

    // Modified constructor to take a String
    public LzwDecoder(String encodedText) {
        // Parse the encodedText (comma-separated integers) into a Vector<Integer>
        this.compressed = parseEncodedText(encodedText);
    }

    // Method to parse the string into a vector of integers
    private Vector<Integer> parseEncodedText(String encodedText) {
        Vector<Integer> vector = new Vector<>();
        Scanner scanner = new Scanner(encodedText);
        scanner.useDelimiter("\\s+"); // Delimiter is whitespace between numbers

        while (scanner.hasNextInt()) {
            vector.add(scanner.nextInt());
        }
        return vector;
    }

    @Override
    public void decode() throws IOException {
        // Initialize the dictionary
        for (int i = 0; i < 256; i++) {
            String symbol = "";
            symbol += (char) i;
            dictionary.put(i, symbol);
        }

        String text = "";
        int oldIndex = this.compressed.get(0);
        int id = 256;
        String subString = dictionary.get(oldIndex);
        char currentChar = subString.charAt(0);
        text += (subString);

        for (int i = 1; i < this.compressed.size(); i++) {
            int currentIndex = this.compressed.get(i);
            if (dictionary.get(currentIndex) == null) {
                subString = dictionary.get(oldIndex) + currentChar;
            } else {
                subString = dictionary.get(currentIndex);
            }

            text += (subString);
            currentChar = subString.charAt(0);
            dictionary.put(id, dictionary.get(oldIndex) + currentChar);
            id++;
            oldIndex = currentIndex;
        }

        // Write the decompressed text to a file
        FileWriter fileWriter = new FileWriter("DecompressedText");
        fileWriter.write(text);
        fileWriter.close();
    }
}
