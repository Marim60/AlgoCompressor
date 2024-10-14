package com.compress.algocompressor.algorithms.implementation.Huffman;
import com.compress.algocompressor.algorithms.interfaces.IEncoder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class HuffmanEncoder implements IEncoder {
    private Node root;
    private Map<Character, String> HuffmanTable = new LinkedHashMap<>();
    private static int[] array;
    private PriorityQueue<Node> pq = new PriorityQueue<>(new NodeComparator());
    private PrintWriter printWriter;
    private String text;
    public HuffmanEncoder(String text) {
        this.text = text.toUpperCase();
    }

    static {
        array = new int[26];
    }

    class Node {
        public int freq;
        public Node left;
        public Node right;
        public char aChar = ' ';
        public int code;
    }

    class NodeComparator implements Comparator<Node> {
        public int compare(Node n1, Node n2) {
            return Integer.compare(n1.freq, n2.freq);
        }
    }

    // Main encode function exposed to the user
    public void encode(String file) throws FileNotFoundException {
        buildFrequencyTable(text);
        root = constructSubTree();
        generateHuffmanCode(root, "");
        saveHuffmanCodeToFile(text,file);
        printOnFile(text);
    }

    // Helper to build frequency table
    private void buildFrequencyTable(String text) {
        for (int i = 0; i < text.length(); i++) {
            int index = text.charAt(i) - 'A';
            array[index]++;
        }
        for (int i = 0; i < 26; i++) {
            if (array[i] != 0) {
                Node tempNode = new Node();
                tempNode.freq = array[i];
                tempNode.aChar = (char) (i + 'A');
                pq.add(tempNode);
            }
        }
    }

    // Constructing the Huffman Tree
    private Node constructSubTree() {
        while (!pq.isEmpty()) {
            Node sum = new Node();
            Node leftNode = pq.poll();
            if (pq.isEmpty()) {
                return leftNode;
            }
            Node rightNode = pq.poll();

            sum.freq = leftNode.freq + rightNode.freq;
            sum.left = leftNode;
            sum.left.code = 0;
            sum.right = rightNode;
            sum.right.code = 1;

            pq.add(sum);
        }
        return null;
    }

    // Recursive function to generate Huffman codes
    private void generateHuffmanCode(Node root, String s) {
        if (root == null) return;
        if (root.left == null && root.right == null && Character.isLetter(root.aChar)) {
            HuffmanTable.put(root.aChar, s);
        } else {
            generateHuffmanCode(root.left, s + "0");
            generateHuffmanCode(root.right, s + "1");
        }
    }

    // Saving Huffman code to a file and printing compression info
    private void saveHuffmanCodeToFile(String text, String file) throws FileNotFoundException {
        printWriter = new PrintWriter(file);
        StringBuilder encodedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            encodedText.append(HuffmanTable.get(c));
        }

        for (Map.Entry<Character, String> entry : HuffmanTable.entrySet()) {
            printWriter.println(entry.getKey() + " " + entry.getValue());
        }

        printWriter.println("Code = " + encodedText);
        printWriter.println("Original Size = " + text.length() * 8 + " bit");
        printWriter.println("Compression Size = " + (encodedText.length() + HuffmanTable.size() * 8) + " bit");
        printWriter.close();
    }
    private void printOnFile(String text) throws FileNotFoundException {
        printWriter = new PrintWriter("EncodingCode");
        for (Map.Entry<Character,String> entry : HuffmanTable.entrySet())
        {
            printWriter.println(entry.getKey() + " " + entry.getValue());
        }
        String textCode = "";
        for (int i = 0; i < text.length(); i++)
        {
            textCode += HuffmanTable.get(text.charAt(i));
        }
        printWriter.println("Code = " + textCode);
        System.out.println("Code = " + textCode);
        printWriter.println("Original Size = " + text.length() * 8 + " bit");
        printWriter.println("Compression Size = " + (textCode.length() + HuffmanTable.size() * 8) + " bit");
        printWriter.close();
    }
}
