package com.alex.chess;

import java.util.Stack;

import com.alex.chess.core.ChessConstant;
import com.alex.chess.core.ChessMove;
import com.alex.chess.core.ChessMoveNode;

public class StateBundle implements Constant, ChessConstant{
	int timeA;
	int timeB;
	int current;
	int timeLimit;
	int depthLimit;
	int[] squre;
	int[] player;
	State state;
	ChessMoveNode root;
	Stack<ChessMove> stack;
	public void turn() {
		current = 1-current;
	}
}
