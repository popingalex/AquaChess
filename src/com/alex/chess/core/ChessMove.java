package com.alex.chess.core;

import com.alex.chess.Constant;

public class ChessMove implements Constant, ChessConstant{
	public int sort;
	public int move;
	public int piece;
	public int captured;
	public ChessMove(int move, int captured, int piece, int sort) {
		super();
		this.sort = sort;
		this.move = move;
		this.piece = piece;
		this.captured = captured;
	}
	public static String getManual(int move, int piece, int sort) {
		int src = RuleUtil.getSrc(move);
		int dst = RuleUtil.getDst(move);
		int x1 = RuleUtil.getX(src)-2;
		int y1 = RuleUtil.getY(src)-2;
		int x2 = RuleUtil.getX(dst)-2;
		int y2 = RuleUtil.getY(dst)-2;
		String index, icon, action, source, sstep;
		int istep;
		icon = pieceChar[piece-8];
		//TODO need fix the manual
		if(sort!=0) {
			index = (sort==6)?manualNumberCharS[0]:sort>6?manualNumberCharS[sort-6]:(piece-15<0?manualNumberNum[sort-1]:manualNumberChar[sort-1]);
			icon = index+icon;
		}
		if(sort>0 && (piece!=22&&piece!=14)) {	//counts > 0 & piece not pawn
			source = "";
		} else {
			source = (piece<15?manualNumberNum[x1-1]:manualNumberChar[x1-1]);
		}
		action = (y1==y2)?manualActChar[0]:(((y2-y1)*(piece-15)>0)?manualActChar[1]:manualActChar[2]);
		istep = (y1==y2||(piece<15?(piece-8==PIECE_HORSE||piece-8==PIECE_ELEPHANT):(piece-16==PIECE_HORSE||piece-16==PIECE_ELEPHANT)))?x2:(y2>y1)?(y2-y1):(y1-y2);
		
		sstep = (piece-15<0?manualNumberNum[istep-1]:manualNumberChar[istep-1]);
		return icon+source+action+sstep;
	}
	public String getManual() {
	    return getManual(move, piece, sort);
	}
	public void printManual() {
		System.out.println(getManual());
	}
}
