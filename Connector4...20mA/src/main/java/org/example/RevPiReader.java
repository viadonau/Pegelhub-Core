package org.example;

public class RevPiReader {

    static
    {
        System.loadLibrary("RevPiReader");
    }
    public native int readFromOffset(int offset);
}
