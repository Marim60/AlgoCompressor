package com.compress.algocompressor.services;
import com.compress.algocompressor.algorithms.interfaces.IDecoder;
import com.compress.algocompressor.algorithms.interfaces.IEncoder;
import com.compress.algocompressor.util.FileUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;
@Service
public class CompressionService {

    public Resource encodeFile(MultipartFile file, CompressionAlgorithm algorithm) throws IOException {
        // Read file content
        String text = new String(file.getBytes());

        // Use factory to get the correct encoder
        IEncoder encoder = CompressionFactory.getEncoder(algorithm, text);
        String encodedFilePath = "encoded.txt";
        encoder.encode(encodedFilePath);

        // Return encoded file as a Resource
        Path path = Path.of(encodedFilePath);
        ByteArrayResource resource = new ByteArrayResource(FileUtil.readFileToBytes(path));

        return resource;
    }

    public Resource decodeFile(MultipartFile file, CompressionAlgorithm algorithm) throws IOException {
        // Read the encoded file content as a string
        String encodedText = new String(file.getBytes());

        // Use factory to get the correct decoder
        IDecoder decoder = CompressionFactory.getDecoder(algorithm, encodedText);
        decoder.decode(); // This will create the decompressed file

        // Return decoded file as a Resource
        Path path = Path.of("DecompressedText");
        ByteArrayResource resource = new ByteArrayResource(FileUtil.readFileToBytes(path));

        return resource;
    }
}