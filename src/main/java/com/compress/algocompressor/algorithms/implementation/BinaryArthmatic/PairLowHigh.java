package com.compress.algocompressor.algorithms.implementation.BinaryArthmatic;

public class PairLowHigh {
    double low;
    double high;

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    PairLowHigh(double low, double high) {
        this.high = high;
        this.low = low;
    }
}
