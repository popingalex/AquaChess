package com.alex.aqua.sprite;

import java.awt.Graphics2D;

import com.alex.aqua.bundle.Bundle;

public abstract class AbstractSprite  extends Bundle{
	public int x;
	public int y;
	public int width;
	public int height;

	public int layer;
	public int alpha;
	public int rotate;
	public int speed;
	public boolean visible;
	
	public Drawable drawable;

	public void render(Graphics2D graph) {}
	public AbstractSprite clone() {
		return this;
	}
}
