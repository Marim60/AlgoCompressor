package com.compress.algocompressor.algorithms.implementation.BinaryArthmatic;

import com.compress.algocompressor.algorithms.interfaces.IEncoder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class BinaryArithmeticEncoder implements IEncoder {
    private String text;
    private Map<Character, Double> freq = new TreeMap<>();
    private Map<Character, PairLowHigh> lowHigh = new TreeMap<>();
    public BinaryArithmeticEncoder(String text) {
        this.text = text;
    }
    public void encode(String encodeFile) throws FileNotFoundException {

        PrintWriter printWriter = new PrintWriter(encodeFile);
        Vector<Double> v = new Vector<>();
        int len = text.length();
        //Frequency
        for (int i = 0; i < len; i++) {
            freq.put(text.charAt(i), freq.get(text.charAt(i)) == null ? 1 : freq.get(text.charAt(i)) + 1);
        }
        //Probability
        for (Character key : freq.keySet()) {
            double value = freq.get(key) / len;
            freq.put(key, value);
            v.add(value);
        }
        //Prefix sum
        int size = v.size() + 1;
        double prefix[] = new double[size];
        prefix[0] = 0;
        prefix[1] = v.get(0);
        double smallestRange = prefix[1];
        System.out.println(prefix[0]);
        System.out.println(prefix[1]);
        for (int i = 2; i < size; i++) {
            prefix[i] = prefix[i - 1] + v.get(i-1);
            System.out.println(prefix[i]);
            smallestRange = Math.min(smallestRange, prefix[i] - prefix[i - 1]);
        }
        //Vector<Pair> symbolTable = new Vector<>();
        int j = 0;
        for (Character key : freq.keySet()) {
            freq.put(key, prefix[j]);
            //symbolTable.add(new Pair(key, prefix[j]));
            lowHigh.put(key, new PairLowHigh(prefix[j], prefix[j+1]));
            j++;
        }
        PrintWriter printWriter1 = new PrintWriter("Range table");
        for (Character key : lowHigh.keySet())
        {
            System.out.println(key + " " + lowHigh.get(key).getLow() + " " + lowHigh.get(key).getHigh());
            printWriter1.println(key + " " + lowHigh.get(key).getLow() + " " + lowHigh.get(key).getHigh());
        }
        printWriter1.close();
        // number of bits ->> k 100
        int k = 0;
        for (int i = 0; i < 100; i++) {
            if (1 / Math.pow(2, i) <= smallestRange) {
                k = i;
                break;
            }
        }
        System.out.println(k);
        double lower = 0;
        double higher = 1;
        String textCode = "";
        for (int i = 0; i < text.length(); i++) {
            double prevLower = lower;
            lower = symbolProb_Low_High(lower, higher, lowHigh.get(text.charAt(i)).getLow());
            higher =  symbolProb_Low_High(prevLower, higher, lowHigh.get(text.charAt(i)).getHigh());
            if(lower > 0.5)
            {
                lower = E2Scale(lower);
                higher = E2Scale(higher);
                textCode += "1";
            }
            else if(higher < 0.5)
            {
                lower = E1Scale(lower);
                higher = E1Scale(higher);
                textCode+="0";
            }
        }
        textCode+="1";
        for (int i = 0; i < k - 1; i++) {
            textCode+="0";
        }
        printWriter.println(textCode);
        printWriter.println( k + " bits is th min number of Bits required to store the Code ");
        printWriter.println("Original Size = " + text.length() * 8 + " bits");
        printWriter.println("Compressed size = " + textCode.length() + " bits");
        printWriter.close();
    }

    private double E2Scale(double prob) {
        return (prob - 0.5) * 2;
    }

    private double E1Scale(double prob) {
        return (prob * 2);
    }

    private double symbolProb_Low_High(double lower, double higher, double symbolProb) {
        return (lower + (higher - lower) * symbolProb);
    }
}
