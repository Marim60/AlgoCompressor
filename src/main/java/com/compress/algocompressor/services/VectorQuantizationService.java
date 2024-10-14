package com.compress.algocompressor.services;


import com.compress.algocompressor.algorithms.VectorQuantization;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VectorQuantizationService {

    public void compressImage(String inputFilePath, String outputFilePath, int vectorHeight, int vectorWidth, int numVectors) throws IOException {
        VectorQuantization.vectorHeight = vectorHeight;
        VectorQuantization.vectorWidth = vectorWidth;
        VectorQuantization.numVectors = numVectors;

        // Read the image
        VectorQuantization.originalImage = VectorQuantization.readImage(inputFilePath);

        // Perform compression
        VectorQuantization.compression();

        // Write the compressed data to a file
        VectorQuantization.writeToFile(outputFilePath);
    }

    public void decompressImage(String inputFilePath, String outputFilePath) throws IOException {
        VectorQuantization.readFromFile(inputFilePath);
        VectorQuantization.decompression();
        VectorQuantization.writeImage(VectorQuantization.NewImage, outputFilePath);
    }
}