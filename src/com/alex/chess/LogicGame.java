package com.alex.chess;

import java.util.Stack;

import com.alex.aqua.bundle.EchoBundle;
import com.alex.aqua.framework.impl.LogicProcessor;
import com.alex.aqua.framework.util.LoopAdapter;
import com.alex.chess.core.ChessConstant;
import com.alex.chess.core.ChessMove;
import com.alex.chess.core.RuleUtil;
import com.alex.chess.robot.ChessAgent;
import com.alex.chess.robot.ChessUtil;

public class LogicGame extends LogicProcessor implements Constant, ChessConstant{
	private StateBundle stateBundle = new StateBundle();
	private ChessAgent chessAgent = ChessAgent.getInstance();
	private int defDepthLimit = 4;
	private int defTimeLimit = 10;
	@Override
	public int processLogic(EchoBundle source, EchoBundle result) {
		switch (source.param) {
		case ECHO_AQUA_START:
			loopAdapter.startLoop();
			break;
		case CHESS_INFO_SET:
			if(null == source.bundle) {
			    chessAgent.initChessGame(PLAYER_RED, defaultBoard);
				stateBundle.state = State.pause;
				stateBundle.depthLimit = defDepthLimit;
				stateBundle.timeLimit = defTimeLimit;
				stateBundle.player = new int[]{PLAYER_HUMAN, PLAYER_ROBOT};
				stateBundle.squre = chessAgent.getSquare();
				stateBundle.current = chessAgent.getPlayer();
				stateBundle.root = chessAgent.getNode();
				stateBundle.stack = new Stack<ChessMove>();
			}
			freshView();
			break;
		case CHESS_INFO_GET:
			result.bundle = stateBundle;
			break;
		case CHESS_REQUEST_PLAY:
			if(stateBundle.state!=State.play) {
				stateBundle.state = State.play;
				source.param = CHESS_REQUEST_STEP;
				processLogic(source, null);
			}
			break;
		case CHESS_REQUEST_PAUSE:
			if(stateBundle.state==State.play) {
				stateBundle.state = State.pause;
			}
			break;
		case CHESS_REQUEST_STEP:
			switch (stateBundle.player[chessAgent.getPlayer()]) {
			case PLAYER_ROBOT:
				//Robot move
				loopAdapter.registerSchedule(SCHEDULE_PONDER, loopAdapter.getTimestamp()+1);
				break;
			case PLAYER_HUMAN:
				break;
			} 
			break;
		case CHESS_REQUEST_SWITCHP:
			int player  = (Integer)source.bundle;
			stateBundle.player[player] = 1-stateBundle.player[player];
			if(stateBundle.state==State.play) {
				processLogic(EchoBundle.extract(CHESS_REQUEST_STEP, null), null);
			}
			break;
		case CHESS_REQUEST_HUMANMOVE:
			int move = (Integer) source.bundle;
			if(stateBundle.player[chessAgent.getPlayer()]==PLAYER_HUMAN) {
				switch (valueMove(move)) {
				case MOVE_MATED:
//					Aqua.debug("human", "* mate move");
					freshView();
					result.param = CHESS_RESULT_MATED;
					break;
				case MOVE_LEGAL:
//					Aqua.debug("human", "* legal move");
					stateBundle.turn();
					freshView();
					result.param = CHESS_RESULT_LEGAL;
					break;
				case MOVE_CHECK:
//					Aqua.debug("human", "* check move");
					result.param = CHESS_RESULT_CHECK;
					break;
				case MOVE_REPEAT:
//					Aqua.debug("human", "* repeat fail");
					result.param = CHESS_RESULT_REPEAT;
					break;
				case MOVE_ILLEGAL:
//					Aqua.debug("human", "* illegal move");
					result.param = CHESS_RESULT_ILLEGAL;
					break;
				case MOVE_SUICIDE:
//					Aqua.debug("human", "* suicide move");
					result.param = CHESS_RESULT_SUICIDE;
					break;
				}
			} else {//not human turn
//				Aqua.debug("logic", "not human turn");
				result.param = CHESS_RESULT_ILLEGAL;
			}
//			Aqua.debug("human", "=== end =========");
			break;
		case CHESS_REQUEST_RESIGN:
			break;
		case CHESS_REQUEST_UNMOVE:
			if(stateBundle.stack.size() > 1) {
				ChessMove chess;
				chess = stateBundle.stack.pop();
				chessAgent.undoMove(chess.move, chess.captured);
				chess = stateBundle.stack.pop();
				chessAgent.undoMove(chess.move, chess.captured);
				freshView();
			}
			break;
		default:
			break;
		}
		return 0;
	}
	private int valueMove(int move) {
		if(ChessUtil.checkRepeat(move, stateBundle.stack)) {
			return MOVE_REPEAT;
		}
		if(!ChessUtil.checkLegal(chessAgent.getPlayer(), move, chessAgent.getSquare())) {
			return MOVE_ILLEGAL;
		}
		int src = RuleUtil.getSrc(move);
		int dst = RuleUtil.getDst(move);
		int capture = chessAgent.getSquare()[dst];
		int piece = chessAgent.getSquare()[src];
		int sort = ChessUtil.sort(src, chessAgent.getSquare());
		if(!chessAgent.makeMove(move, capture)) {
			return MOVE_SUICIDE;
		}
		
		ChessMove history = new ChessMove(move, capture, piece, sort);
		//history.printManual();
		stateBundle.stack.push(history);
		return chessAgent.isMate()?MOVE_MATED:MOVE_LEGAL;
	}
	private void freshView() {
		offer(0, EchoBundle.extract(CHESS_REQUEST_FRESHSQURE, stateBundle), null);
	}
	@Override
	public void init() {
	}
	private LoopAdapter loopAdapter = new LoopAdapter(this);
	@Override
	public boolean processLoop(int stampID, long timestamp) {
		switch (stampID) {
		case SCHEDULE_PONDER:
			//Aqua.debug("robot", "=== begin ========");
			int robotMove = chessAgent.searchWork(stateBundle.depthLimit, stateBundle.timeLimit*100);
			int src = RuleUtil.getSrc(robotMove);
			int dst = RuleUtil.getDst(robotMove);
			int capture = chessAgent.getSquare()[dst];
			int piece = chessAgent.getSquare()[src];
			int sort = ChessUtil.sort(src, chessAgent.getSquare());
			chessAgent.makeMove(robotMove, capture);
			ChessMove history = new ChessMove(robotMove, capture, piece, sort);
			//history.printManual();
			stateBundle.stack.push(history);
			freshView();
			if(chessAgent.isMate()) {	//is game over
				offer(0, EchoBundle.extract(CHESS_RESULT_MATED, null), null);
			} else {
				stateBundle.turn();
				if(stateBundle.state==State.play) {
					processLogic(EchoBundle.extract(CHESS_REQUEST_STEP, null), null);
				}
			}
			return false;
		default:
			break;
		}
		return false;
	}
	
}
