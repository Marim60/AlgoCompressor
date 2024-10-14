package com.compress.algocompressor.controllers;
import com.compress.algocompressor.services.CompressionAlgorithm;
import com.compress.algocompressor.services.CompressionService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/compression")
public class CompressionController {

    private final CompressionService compressionService;

    public CompressionController(CompressionService compressionService) {
        this.compressionService = compressionService;
    }

    @PostMapping("/encode")
    public ResponseEntity<Resource> encodeFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("algorithm") CompressionAlgorithm algorithm) throws IOException {

        Resource resource = compressionService.encodeFile(file, algorithm);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"encoded.txt\"")
                .body(resource);
    }

    @PostMapping("/decode")
    public ResponseEntity<Resource> decodeFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("algorithm") CompressionAlgorithm algorithm) throws IOException {

        Resource resource = compressionService.decodeFile(file, algorithm);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"decoded.txt\"")
                .body(resource);
    }
}