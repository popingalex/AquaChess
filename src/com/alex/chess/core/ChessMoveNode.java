package com.alex.chess.core;

import java.util.ArrayList;

public class ChessMoveNode extends ChessMove {
    public int depth;
    public ChessMove parent;
    public ArrayList<ChessMoveNode> childs = new ArrayList<ChessMoveNode>();
    public ChessMoveNode(int move, int captured, int piece, int sort, int depth) {
        super(move, captured, piece, sort);
        this.depth = depth;
    }
}
