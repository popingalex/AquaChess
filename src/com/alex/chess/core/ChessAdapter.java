package com.alex.chess.core;

import java.util.Stack;

import com.alex.chess.robot.ChessUtil;

public class ChessAdapter implements ChessConstant{
    protected int player;                       // current player
    protected int distance;                     // steps from root
    protected int bestMove;                     // move of robot
    protected int valueRed, valueBlack;         // value of each side
    protected int[] square=new int[256];        // piece on board
    protected int[] historyTable=new int[65536];// history
    protected Stack<ChessMove> stack=new Stack<ChessMove>();
    protected ChessMoveRoot root;
    public void initChessGame(int starter, int[] chessBoard) { // init piece board
        int loc, piece;
        player = starter;
        distance = 0;
        valueRed = 0;
        valueBlack = 0;
        for(int i=0;i<256;i++) {
            square[i]=0;
        }
        for (loc = 0; loc < 256; loc ++) {
            piece = chessBoard[loc];  
            if (piece != 0) {
                putPiece(loc, piece);
            }
        }
        root = new ChessMoveRoot(square);
    }
    
    protected void changeSide() {               // turn player
        player = 1 - player;
    }
    
    protected void putPiece(int loc, int piece) {   // put piece on board
        square[loc] = piece;
        if (piece < 16) {   // red piece, black ++ ; red --
            valueRed += pieceValue[piece - 8][loc];
        } else {            // black piece, red ++ ; black --
            valueBlack += pieceValue[piece - 16][RuleUtil.reverseSide(loc)];
        }
    }
   
    protected void dropPiece(int loc, int piece) {  // drop piece from board
        square[loc] = 0;
        if (piece < 16) {   // black -- (red ++)    //0 for red (up red/ balck down)
            valueRed -= pieceValue[piece - 8][loc];
        } else {            // red -- (black ++)
            valueBlack -= pieceValue[piece - 16][RuleUtil.reverseSide(loc)];
        }
    }
    
    
    protected int evaluate() {  // evaluate such situation
        return (player == PLAYER_RED ? valueRed - valueBlack : valueBlack - valueRed) + ADVANCED_VALUE;
    }
   
    protected int movePiece(int move) { // do piece move
        int squareSrc, squareDst, piece, pcCaptured;
        squareSrc = RuleUtil.getSrc(move);
        squareDst = RuleUtil.getDst(move);
        pcCaptured = square[squareDst]; 
        if (pcCaptured != 0) {              // captured some piece
            dropPiece(squareDst, pcCaptured);   // drop the captive
        }
        piece = square[squareSrc];          // get piece at source
        dropPiece(squareSrc, piece);            // drop piece at source
        putPiece(squareDst, piece);             // put piece at 
        return pcCaptured;                  // return the captive or zero
    }
   
    public void undoMove(int move, int pcCaptured) {
        int squareSrc, squareDst, piece;
        squareSrc = RuleUtil.getSrc(move);
        squareDst = RuleUtil.getDst(move);
        piece = square[squareDst];
        dropPiece(squareDst, piece);
        putPiece(squareSrc, piece);
        if (pcCaptured != 0) {
            putPiece(squareDst, pcCaptured);
        }
    }
    public ChessMoveNode getNode() {
        return root;
    }
    
    public int[] getSquare() {
        return square;
    }
    
    public int getPlayer() {
        return player;
    }
   
    public void setPlayer(int player) {
        this.player = player;
    }

    public boolean isMate() {
        int i, nGenMoveNum, pcCaptured ;
        Integer mvs[]=new Integer[MAX_GEN_MOVES];
        nGenMoveNum = ChessUtil.generateMoves(player, square, mvs);
        for (i = 0; i < nGenMoveNum; i ++) {
            pcCaptured = movePiece(mvs[i]);
            if (!ChessUtil.isChecked(player, square)) {    // any possible to live
                undoMove(mvs[i], pcCaptured);
                return false;
            } else {
                undoMove(mvs[i], pcCaptured);
            }
        }
        return true;
    }
}
