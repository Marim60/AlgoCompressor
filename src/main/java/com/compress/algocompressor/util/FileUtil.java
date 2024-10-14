package com.compress.algocompressor.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class FileUtil {
    // Read a file as byte array for download
    public static byte[] readFileToBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    // Parse encoded file (integer list)
    public static List<Integer> parseEncodedFile(MultipartFile file) throws IOException {
        List<Integer> indices = new ArrayList<>();
        Scanner scanner = new Scanner(file.getInputStream());

        while (scanner.hasNextInt()) {
            indices.add(scanner.nextInt());
        }

        return indices;
    }
}

