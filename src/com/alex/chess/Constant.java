package com.alex.chess;

import com.alex.aqua.framework.util.IConstant;

public interface Constant extends IConstant{
	public static final short CHESS_REQUEST_PLAY = ECHO_AQUA_USER_MIN+1;
	public static final short CHESS_REQUEST_PAUSE = ECHO_AQUA_USER_MIN+2;
	public static final short CHESS_REQUEST_STEP = ECHO_AQUA_USER_MIN+3;
	
	public static final short CHESS_REQUEST_DRAW = ECHO_AQUA_USER_MIN+5;
	
	public static final short CHESS_REQUEST_UNMOVE = ECHO_AQUA_USER_MIN+6;
	public static final short CHESS_REQUEST_RESIGN = ECHO_AQUA_USER_MIN+7;
	
	public static final short CHESS_REQUEST_HUMANMOVE = ECHO_AQUA_USER_MIN+8;
	public static final short CHESS_REQUEST_ROBOTMOVE = ECHO_AQUA_USER_MIN+9;
	public static final short CHESS_REQUEST_FRESHSQURE = ECHO_AQUA_USER_MIN+10;
	public static final short CHESS_REQUEST_SWITCHP = ECHO_AQUA_USER_MIN+11;
	public static final short CHESS_RESULT_ROBOTOVER = ECHO_AQUA_USER_MIN+20;
	
	public static final short CHESS_RESULT_MATED = ECHO_AQUA_USER_MIN+30;
	public static final short CHESS_RESULT_LEGAL = ECHO_AQUA_USER_MIN+31;
	public static final short CHESS_RESULT_CHECK = ECHO_AQUA_USER_MIN+32;
	public static final short CHESS_RESULT_REPEAT = ECHO_AQUA_USER_MIN+33;
	public static final short CHESS_RESULT_ILLEGAL = ECHO_AQUA_USER_MIN+34;
	public static final short CHESS_RESULT_SUICIDE = ECHO_AQUA_USER_MIN+35;
	
	public static final short CHESS_INFO_GET = ECHO_AQUA_USER_MIN+40;
	public static final short CHESS_INFO_SET = ECHO_AQUA_USER_MIN+41;
	
	public static final short SCHEDULE_PONDER = SCHEDULE_MAIN+1;
}