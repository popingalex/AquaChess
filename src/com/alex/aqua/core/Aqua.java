package com.alex.aqua.core;

import java.awt.Container;

import javax.swing.JPanel;

import com.alex.aqua.bundle.EchoBundle;
import com.alex.aqua.framework.IEchoHandler;
import com.alex.aqua.framework.ILogicProcessor;
import com.alex.aqua.framework.IViewAdapter;
import com.alex.aqua.framework.impl.AquaCanvas;
import com.alex.aqua.framework.impl.LogicProcessor;
import com.alex.aqua.framework.impl.ViewAdapter;
import com.alex.aqua.framework.util.AquaExitListener;
import com.alex.aqua.framework.util.EchoCenter;
import com.alex.aqua.framework.util.IConstant;
import com.alex.aqua.source.ImageSourceDao;
import com.alex.aqua.source.SourceFactory;

public class Aqua implements IConstant{
	public static boolean DEBUG = false;
	public static boolean DEBUG_PAINT_BOARD = false;
	public static void initAqua() {
		AquaCanvas.getInastance().init();
	}
	private static AquaExitListener exitListener;

	public static boolean registerModule(int param, String moduleName) {
		try {
			Class<?> handlerClass = Class.forName(moduleName);
			registerModule(param, handlerClass);
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean registerModule(int param, Class<?> moduleClass) {
		int type = 0;
		try {
			Object moduleObject = null;
			moduleObject = moduleClass.newInstance();
			if(moduleClass.getSuperclass().equals(ViewAdapter.class)) {
				type = MODULE_VIEW;
				IViewAdapter adapter = IViewAdapter.class.cast(moduleObject);
				AquaCanvas.getInastance().registerView(param, adapter);
			} else if (moduleClass.getSuperclass().equals(LogicProcessor.class)) {
				ILogicProcessor professer = ILogicProcessor.class.cast(moduleObject);
				professer.init();
				type = MODULE_LOGIC;
			}
			IEchoHandler handler = IEchoHandler.class.cast(moduleObject);
			EchoCenter.getInstance().registerHandler(param, type, handler);
			return true;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void launchAqua() {
		launchAqua(0);
	}
	public static JPanel getContentPanel() {
		return AquaCanvas.getInastance().getRootPanel();
	}
	public static void launchAqua(int launchView) {
		//AquaDesktop.getInastance().startLoop();
		AquaCanvas.getInastance().display(launchView);
		//may send some important info
		EchoCenter.getInstance().offerEcho(0, ECHO_TO_VIEW, EchoBundle.extract(ECHO_AQUA_START, null), null);
		EchoCenter.getInstance().offerEcho(0, ECHO_TO_LOGIC, EchoBundle.extract(ECHO_AQUA_START, null), null);
	}
	public static void debug(String module, String message) {
		if(DEBUG) {
			System.out.println(module+":"+message);
		}
	}
	public static void registerMovableContainer(Container container) {
		AquaCanvas.getInastance().registerMovableContainer(container);
	}

	public static void registerDao(String name) {
		SourceFactory.registerDao(ImageSourceDao.class.getName());
	}
	public static void loadSource() {
		SourceFactory.loadSource();
	}
	public static void registerExitListener(AquaExitListener aquaExitListener) {
		exitListener = aquaExitListener;
	}
	public static void exitAqua(int param) {
		if(null != exitListener)exitListener.exit(param);
	}
}
