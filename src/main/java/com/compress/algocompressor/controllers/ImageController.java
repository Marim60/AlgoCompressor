package com.compress.algocompressor.controllers;

import com.compress.algocompressor.services.VectorQuantizationService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final VectorQuantizationService vectorQuantizationService;

    public ImageController(VectorQuantizationService vectorQuantizationService) {
        this.vectorQuantizationService = vectorQuantizationService;
    }

    @PostMapping("/compress")
    public String compressImage(
            @RequestParam String inputFilePath,
            @RequestParam String outputFilePath,
            @RequestParam int vectorHeight,
            @RequestParam int vectorWidth,
            @RequestParam int numVectors) {
        try {
            vectorQuantizationService.compressImage(inputFilePath, outputFilePath, vectorHeight, vectorWidth, numVectors);
            return "Image compressed successfully!";
        } catch (IOException e) {
            return "Error compressing image: " + e.getMessage();
        }
    }

    @PostMapping("/decompress")
    public String decompressImage(
            @RequestParam String inputFilePath,
            @RequestParam String outputFilePath) {
        try {
            vectorQuantizationService.decompressImage(inputFilePath, outputFilePath);
            return "Image decompressed successfully!";
        } catch (IOException e) {
            return "Error decompressing image: " + e.getMessage();
        }
    }
}