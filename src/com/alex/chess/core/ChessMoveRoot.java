package com.alex.chess.core;

public class ChessMoveRoot extends ChessMoveNode{
    public int[] square = new int[256];
    public ChessMoveRoot(int[] square) {
        super(0, 0, 0, 0, 0);
        for(int i=0;i<256;i++)
            this.square[i] = square[i];
    }
}
