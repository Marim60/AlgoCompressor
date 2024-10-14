package com.compress.algocompressor.algorithms.implementation.Huffman;
import com.compress.algocompressor.algorithms.interfaces.IDecoder;

import java.io.*;
import java.util.*;

public class HuffmanDecoder implements IDecoder {
    private Map<String, String> HuffmanTable = new LinkedHashMap<>();
    private PrintWriter printWriter;
    private String encodedText;
    public HuffmanDecoder(String encodedText) {
        this.encodedText = encodedText;
    }

    // Public method to handle decoding process
    public void decode() throws IOException {
        loadHuffmanTableFromFile();
        decodeEncodedText(encodedText);
    }

    // Private method to load Huffman table from file
    private void loadHuffmanTableFromFile() throws FileNotFoundException {
        File file = new File("EncodingCode");
        Scanner scan = new Scanner(file);

        while (scan.hasNext()) {
            String ch = scan.next();

            // Stop when we reach the "Code =" line, signaling the start of the encoded string
            if (ch.equals("Code")) {
                break;
            }

            // Make sure there's a next token (the code)
            if (scan.hasNext()) {
                String code = scan.next();
                HuffmanTable.put(code, ch); // Add to Huffman table (code as key, character as value)
            } else {
                System.err.println("Invalid file format: missing code for character " + ch);
                break;
            }
        }

        scan.close();
    }

    // Private method to decode the encoded text
    private void decodeEncodedText(String encodedText) throws IOException {
        StringBuilder decodedText = new StringBuilder();
        StringBuilder substring = new StringBuilder();
        for (int i = 0; i < encodedText.length(); i++) {
            substring.append(encodedText.charAt(i));
            String character = HuffmanTable.get(substring.toString());
            if (character != null) {
                decodedText.append(character);
                substring.setLength(0); // Reset substring
            }
        }

        printWriter = new PrintWriter("DecodeResult");
        printWriter.println("Original Text = " + decodedText);
        printWriter.close();
        System.out.println("Original Text = " + decodedText);
    }
}
