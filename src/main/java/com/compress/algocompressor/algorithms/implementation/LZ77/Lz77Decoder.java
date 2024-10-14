package com.compress.algocompressor.algorithms.implementation.LZ77;

import com.compress.algocompressor.algorithms.interfaces.IDecoder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Lz77Decoder implements IDecoder {
    private Vector<Tag> tag = new Vector<Tag>();

    public Lz77Decoder(String encodedText) {
        this.tag = parseEncodedText(encodedText);
    }
    public void decode() throws IOException {
        String text = "";
        FileWriter fileWriter = new FileWriter("DecompressedText");
        for (int i = 0; i < tag.size(); i++) {
            if (tag.get(i).length == 0 && tag.get(i).position == 0) {
                text += tag.get(i).nextSymbol;
                continue;
            }
            int len = text.length();
            int start = len - tag.get(i).position;
            text += text.substring(start, tag.get(i).length + start);
            text += tag.get(i).nextSymbol;
        }
        fileWriter.write(text);
        fileWriter.close();
    }

    private Vector<Tag> parseEncodedText(String encodedText) {
        Vector<Tag> vector = new Vector<>();
        String[] tags = encodedText.split("\n");
        for (String tag : tags) {
            String[] parts = tag.split(", ");
            int position = Integer.parseInt(parts[0].substring(1));
            int length = Integer.parseInt(parts[1]);
            char nextSymbol = parts[2].charAt(1);
            vector.add(new Tag(position, length, nextSymbol));
        }
        return vector;
    }
}