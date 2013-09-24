package com.alex.aqua.framework;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;


public interface IAquaCanvas{
	public final static int CONTAINER_WINDOW = 0;
	public final static int CONTAINER_FRAME = 1;
	public void init();
	public JPanel getRootPanel();
	public JLayeredPane getLayeredPane();
	public void freshDesktop();
	public void setCurrentView(IViewAdapter currentView);
	public void reBounds(int left, int top, int width, int height);
	public void reSize(int width, int height);
	public void display(int beginView);
	public void startLoop();
	public void pauseLoop();
	public void registerView(int param, IViewAdapter adapter);
	public void registerMovableContainer(Container container);
	public void setFps(int fps);
	public int getFps();
	public int getWidth();
	public int getHeight();
	public void clearComponents();
	public void addComponent(Component component, int layer);
	public void dropComponent(Component component);
	void layerComponent(Component component, int layer);
}
