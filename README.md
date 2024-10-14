# AlgoCompressor

## Overview

AlgoCompressor is a Spring Boot API that implements efficient file compression and decompression algorithms, including Huffman, Binary Arithmetic, LZW, LZ77, and Vector Quantization. The application allows users to upload text and image files for processing, enhancing data storage efficiency.

## Features

- **File Compression**: Supports various algorithms for compressing text and image files.
- **File Decompression**: Easily decompress files compressed using supported algorithms.
- **RESTful API**: Interact with the application via HTTP requests.
- **User-Friendly**: Simple endpoints for uploading files and retrieving processed results.

## Technologies Used

- **Java**: The primary programming language for development.
- **Spring Boot**: Framework used to build the API.
- **Maven**: Dependency management tool.
- **Algorithms**: Implementation of Huffman, Binary Arithmetic, LZW, LZ77, and Vector Quantization.

## API Endpoints
1. Upload File for Compression
Endpoint: /api/compress

Method: POST

Content-Type: multipart/form-data

Input:

file: The file to compress (can be a text or image file).
algorithm (optional): The compression algorithm to use (e.g., huffman, lzw, lz77, binary_arithmetic, vector_quantization). If not specified, defaults to Huffman.
Example Request (using Postman):

URL: http://localhost:8080/api/compress
Body:
Select form-data
Key: file (Type: File) → [Upload your file]
Key: algorithm (Type: Text) → huffman (optional)
Response:

200 OK: Returns the compressed file.
Error: Returns error message if the file cannot be processed.
2. Upload File for Decompression
Endpoint: /api/decompress

Method: POST

Content-Type: multipart/form-data

Input:

file: The compressed file to decompress.
Example Request (using Postman):

URL: http://localhost:8080/api/decompress
Body:
Select form-data
Key: file (Type: File) → [Upload your compressed file]
Response:

200 OK: Returns the decompressed file.
Error: Returns error message if the file cannot be processed.
Usage Example
Compressing a File:

Use Postman to send a POST request to /api/compress with your file uploaded in the body.
Optionally specify the compression algorithm.
Decompressing a File:

Send a POST request to /api/decompress with the compressed file uploaded in the body.
   
