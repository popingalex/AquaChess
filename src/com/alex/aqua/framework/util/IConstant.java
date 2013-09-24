package com.alex.aqua.framework.util;

public interface IConstant {
	/**view module*/
	public static final short MODULE_VIEW = 1;
	/**logic module*/
	public static final short MODULE_LOGIC = 2;

	/**handle EchoBundle to logic*/
	public static final short ECHO_TO_LOGIC = 1;
	/**handle EchoBundle to view*/
	public static final short ECHO_TO_VIEW = 2;


	public static final short ECHO_AQUA_START = 1;
	public static final short ECHO_VIEW_SHOW = 50;
	public static final short ECHO_VIEW_INFO = 51;
	public static final short ECHO_VIEW_INPUT = 100;
	public static final short ECHO_VIEW_SPRITE_FRESH = 200;
	public static final short ECHO_AQUA_USER_MIN = 1000;
	
	public final static short INPUT_MOUSE_DRAG = 0;
	public final static short INPUT_MOUSE_MOVE = 1;
	public final static short INPUT_MOUSE_CLICK = 2;
	public final static short INPUT_MOUSE_ENTER = 4;
	public final static short INPUT_MOUSE_EXIT = 8;
	public final static short INPUT_MOUSE_PRESS = 16;
	public final static short INPUT_MOUSE_RELEASE = 32;
	
	public final static short INPUT_KEY_PRESS = 64;
	public final static short INPUT_KEY_RELEASE = 128;
	
	public final static short SCHEDULE_MAIN = 0;
}
