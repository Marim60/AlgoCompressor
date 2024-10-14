package com.compress.algocompressor.services;

import com.compress.algocompressor.algorithms.implementation.BinaryArthmatic.BinaryArithmeticDecoder;
import com.compress.algocompressor.algorithms.implementation.BinaryArthmatic.BinaryArithmeticEncoder;
import com.compress.algocompressor.algorithms.implementation.Huffman.HuffmanDecoder;
import com.compress.algocompressor.algorithms.implementation.Huffman.HuffmanEncoder;
import com.compress.algocompressor.algorithms.implementation.LZ77.Lz77Decoder;
import com.compress.algocompressor.algorithms.implementation.LZ77.Lz77Encoder;
import com.compress.algocompressor.algorithms.implementation.LZW.LzwDecoder;
import com.compress.algocompressor.algorithms.implementation.LZW.LzwEncoder;
import com.compress.algocompressor.algorithms.interfaces.IDecoder;
import com.compress.algocompressor.algorithms.interfaces.IEncoder;

import java.util.Vector;

public class CompressionFactory {

    public static IEncoder getEncoder(CompressionAlgorithm algorithm, String text) {
        switch (algorithm) {
            case LZW:
                return new LzwEncoder(text);
            case HUFFMAN:
                return new HuffmanEncoder(text);
            case LZ77:
                return new Lz77Encoder(text);
            case BINARY_ARITHMETIC:
                return new BinaryArithmeticEncoder(text);
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
    }

    public static IDecoder getDecoder(CompressionAlgorithm algorithm, String encodedText) {
        switch (algorithm) {
            case LZW:
                return new LzwDecoder(encodedText);
            case HUFFMAN:
                return new HuffmanDecoder(encodedText);
            case LZ77:
                return new Lz77Decoder(encodedText);
            case BINARY_ARITHMETIC:
                return new BinaryArithmeticDecoder(encodedText);
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
    }
}
