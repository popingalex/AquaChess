package com.alex.aqua.framework;

import java.awt.Graphics2D;

import com.alex.aqua.framework.util.IConstant;
import com.alex.aqua.sprite.AbstractSprite;


public interface IViewAdapter extends IEchoHandler, IConstant{
	
	public void callRepaint();
	public void callReturn();
	public void callDisplay();
	public boolean isDirty();
	public void setDirty(boolean dirty);
	
	public void init();
	public void beforeDisplay();
	public void beforeDispose();
	
	public void processCanvasPaint(long timestamp, Graphics2D graph);
	public void processSpritePaint(long timestamp, Graphics2D graph, AbstractSprite sprite);
	public void processInputWorks(long timestamp, int type, int key, int param1, int param2);
}
