package com.alex.aqua.framework.impl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.alex.aqua.framework.IAquaCanvas;
import com.alex.aqua.framework.ILoopable;
import com.alex.aqua.framework.IViewAdapter;
import com.alex.aqua.framework.util.InputManger;
import com.alex.aqua.framework.util.LoopAdapter;

public final class AquaCanvas implements IAquaCanvas, ILoopable{
	private IViewAdapter currentView;
	private JPanel contentPanel;
	private JLayeredPane layeredPane;
	private JPanel canvasPanel;
	private int panelWidth, panelHeight;
	private HashMap<Integer, IViewAdapter> viewMap = new HashMap<Integer, IViewAdapter>();
	private LoopAdapter loopAdapter = new LoopAdapter(this);
	@Override
	public final void init() {
		setFps(20);
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		layeredPane = new JLayeredPane();
		contentPanel.add(layeredPane, 0);
		canvasPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			BufferedImage bufferImage;
			BufferedImage lastBuffer;
			@Override
			public void paint(Graphics g) {
				if(currentView==null||!currentView.isDirty())return;
				bufferImage = lastBuffer;
				if(null == bufferImage) {
					bufferImage = new BufferedImage(layeredPane.getWidth(), layeredPane.getHeight(), BufferedImage.TYPE_INT_ARGB);
				}
				Graphics2D g2d = bufferImage.createGraphics();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.clearRect(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
				currentView.processCanvasPaint(loopAdapter.getTimestamp(), g2d);
				//if(loopAdapter.isLooping()) { //make sure the view will still be display
				super.paint(g);
				g.drawImage(bufferImage, 0, 0, null);
				//}
				lastBuffer = bufferImage;
			}
		};
		layeredPane.setLayer(canvasPanel, 0);
		layeredPane.add(canvasPanel);
		InputManger inputListner = new InputManger() {
			@Override
			public void dealInputWorks(int type, int mask, int param1, int param2) {
				if(currentView!=null) {
					currentView.processInputWorks(loopAdapter.getTimestamp(), type, mask, param1, param2);
				}
			}
			@Override
			public void postDragShift(int shiftX, int shiftY) {
				if(null != container) {
					container.setLocation(container.getX()+shiftX, container.getY()+shiftY);
				}
			}
		};
		contentPanel.addKeyListener(inputListner);
		contentPanel.addMouseListener(inputListner);
		contentPanel.addMouseMotionListener(inputListner);
	}
	private Container container;
	@Override
	public void registerMovableContainer(Container container) {
		this.container = container;
	}
	@Override
	public final void freshDesktop() {	//maybe it would supply plural window in the future
		canvasPanel.repaint();
	}
	@Override
	public final void setCurrentView(IViewAdapter currentView) {
		this.currentView = currentView;
	}
	@Override
	public void reSize(int width, int height) {
		Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		reBounds((maxBounds.width - width)/2, (maxBounds.height - height)/2, width, height);
	}
	@Override
	public final void reBounds(int left, int top, int width, int height) {
		this.panelWidth = width;
		this.panelHeight = height;
		canvasPanel.setBounds(left, top, this.panelWidth, this.panelHeight);
	}
	@Override
	public void display(int beginView) {
		canvasPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
		contentPanel.setFocusable(true);
		contentPanel.requestFocus();
		if(viewMap.get(beginView)!=null) {
			viewMap.get(beginView).callDisplay();
		}
	}
	@Override
	public final void registerView(int param, IViewAdapter adapter) {
		viewMap.put(param, adapter);
		adapter.init();
	}
	@Override
	public final JPanel getRootPanel() {
		return contentPanel;
	}
	@Override
	public final JLayeredPane getLayeredPane() {
		return layeredPane;
	}
	@Override
	public final void startLoop() {
		loopAdapter.startLoop();
	}
	@Override
	public final void pauseLoop() {
		loopAdapter.pauseLoop();
	}
	@Override
	public final int getFps() {
		return loopAdapter.getFps();
	}
	@Override
	public final void setFps(int fps) {
		loopAdapter.setFps(fps);
	}
	@Override
	public final int getWidth() {
		return layeredPane.getWidth();
	}
	@Override
	public final int getHeight() {
		return layeredPane.getHeight();
	}
	@Override
	public final void clearComponents() {
		layeredPane.removeAll();
		layeredPane.add(canvasPanel);
	}
	@Override
	public final void addComponent(Component component, int layer) {
		layeredPane.setLayer(component, layer);
		layeredPane.add(component, 0);
		layeredPane.validate();
	}
	@Override
	public void dropComponent(Component component) {
		layeredPane.remove(component);
	}
	@Override
	public final void layerComponent(Component component, int layer) {
		layeredPane.setLayer(component, layer, 0);
		//layeredPane.add(component, 0);
		//layeredPane.validate();
	}
	//====================
	private AquaCanvas() {}
	private static IAquaCanvas instance = new AquaCanvas();
	public static IAquaCanvas getInastance() {
		return instance;
	}
	//====================
	@Override
	public final boolean processLoop(int stampID, long timestamp) {
		freshDesktop();
		return true;
	}
}
