package com.alex.chess;


import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JFrame;
import com.alex.aqua.core.Aqua;
import com.alex.aqua.framework.util.AquaExitListener;
import com.alex.aqua.source.ImageSourceDao;

public class MainDemo {
	static JFrame frame;
	public static void main(String[] args) {
		Aqua.initAqua();
		Aqua.DEBUG = true;
		Aqua.DEBUG_PAINT_BOARD = true;
		Aqua.registerDao(ImageSourceDao.class.getName());
		Aqua.loadSource();	//will be collected all prepare works in one function
		Aqua.registerModule(0, LogicGame.class.getName());
		Aqua.registerModule(0, ViewGame.class.getName());
		Aqua.registerExitListener(new AquaExitListener());
		
		int width = 600;
		int height = 480;
		Rectangle maximumBound = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds((maximumBound.width-width)/2, (maximumBound.height-height)/2, width, height);
		frame.add(Aqua.getContentPanel());
		frame.setVisible(true);
		
		Aqua.registerMovableContainer(frame);
		Aqua.launchAqua(0);
	}
}
