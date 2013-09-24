package com.alex.chess.robot;


public class JNIUtil {
    static {
       // System.loadLibrary("robot");
    }
    private int[] square = new int[256];
    public native void init();
    public native int get();
}
