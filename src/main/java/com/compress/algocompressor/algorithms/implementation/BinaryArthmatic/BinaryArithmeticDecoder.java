package com.compress.algocompressor.algorithms.implementation.BinaryArthmatic;

import com.compress.algocompressor.algorithms.interfaces.IDecoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class BinaryArithmeticDecoder implements IDecoder {
    private Map<Character, PairLowHigh> lowHigh = new TreeMap<>();
    private String code;
    int k = 5;
    public BinaryArithmeticDecoder(String code) {
        this.code = code;
    }
    public void decode() throws FileNotFoundException {
        File file = new File("Range table");
        int len = code.length() - k;
        double decimal=Integer.parseInt(code.substring(0, k),2) / Math.pow(2,k);
        System.out.println(decimal);
        Scanner scan = new Scanner(file);
        int cnt = 0;
        char ch = 0;
        double low = 0;
        double high = 0;
        while (scan.hasNext()) {
            if(cnt % 3 == 0)
            {
                ch = scan.next().charAt(0);
            }
            else if(cnt % 3 == 1)
            {
                low = scan.nextDouble();
            }
            else
            {
                high = scan.nextDouble();
                lowHigh.put(ch, new PairLowHigh(low,high));
            }
            cnt++;
        }
        String text = "";

        for (Character key: lowHigh.keySet()) {
            System.out.println(key + " " + lowHigh.get(key).getLow() + " " + lowHigh.get(key).getHigh());
        }
        double lower = 0;
        double higher = 1;
        double codecode = decimal;
        int cntShifting = 0;
        for (int i = 0; i < code.length(); i++) {
            char c = originalChar(codecode);
            text += c;
            if(decimal == 0.5)
                break;
            low = lowHigh.get(c).getLow();
            high = lowHigh.get(c).getHigh();
            double prevLower = lower;
            lower = symbolProb_Low_High(lower, higher, low);
            higher =  symbolProb_Low_High(prevLower, higher, high);
            while (lower > 0.5 || higher < 0.5) {
                if (lower > 0.5) {
                    lower = E2Scale(lower);
                    higher = E2Scale(higher);
                    cntShifting++;
                    decimal = Integer.parseInt(code.substring(cntShifting, k+cntShifting), 2) / Math.pow(2, k);
                } else if (higher < 0.5) {
                    lower = E1Scale(lower);
                    higher = E1Scale(higher);
                    cntShifting++;

                    decimal = Integer.parseInt(code.substring(cntShifting, k + cntShifting), 2) / Math.pow(2, k);
                    System.out.println(decimal);
                }
            }
            codecode = calcCode(decimal, lower, higher);
        }
        System.out.println(text);
        PrintWriter printWriter = new PrintWriter("BinaryDecoderOutput");
        printWriter.println("Text decode = " + text);
        printWriter.close();
    }

    private char originalChar(double dec)
    {
        for (Character key: lowHigh.keySet()) {
            if(dec >= lowHigh.get(key).getLow() && dec <  lowHigh.get(key).getHigh())
            {
                return key;
            }
        }
        return 0;
    }
    private double symbolProb_Low_High(double lower, double higher, double symbolProb) {
        return (lower + (higher - lower) * symbolProb);
    }
    private double E2Scale(double prob) {
        return (prob - 0.5) * 2;
    }

    private double E1Scale(double prob) {
        return (prob * 2);
    }
    private double calcCode(double decimal, double low, double high)
    {
        return ((decimal - low) / (high - low));
    }
}
