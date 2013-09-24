package com.alex.aqua.framework.util;

import java.util.HashMap;

import com.alex.aqua.bundle.EchoBundle;
import com.alex.aqua.core.Aqua;
import com.alex.aqua.framework.IEchoHandler;

public class EchoCenter{
	private HashMap<Integer, IEchoHandler> viewMap = new HashMap<Integer, IEchoHandler>();
	private HashMap<Integer, IEchoHandler> logicMap = new HashMap<Integer, IEchoHandler>();
	
	/**
	 * offer an echo to echoCenter
	 * @param parm receiver id
	 * @param type logic->view / view->logic / logic->logic
	 * @param source 
	 * @param result 
	 * @return
	 */
	public int offerEcho(int parm, int type, EchoBundle source, EchoBundle result) {
		IEchoHandler handler = null;
		switch (type) {
		case Aqua.ECHO_TO_LOGIC:
			handler = logicMap.get(parm);
			break;
		case Aqua.ECHO_TO_VIEW:
			handler = viewMap.get(parm);
			break;
		}
		if(null == handler)
			return 0;
		else
			return handler.handleEcho(source, result);
	}
	/**
	 * register an echo handler to echoCenter
	 * @param parm
	 * @param type
	 * @param handler
	 */
	public void registerHandler(int param, int type, IEchoHandler handler) {
		switch (type) {
		case Aqua.MODULE_VIEW:
			viewMap.put(param, handler);
			break;
		case Aqua.MODULE_LOGIC:
			logicMap.put(param, handler);
			break;
		}
	}
	private EchoCenter() {}
	private static EchoCenter instance = new EchoCenter();
	public static EchoCenter getInstance() {
		return instance;
	}
}
