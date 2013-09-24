package com.alex.aqua.sprite;

import java.awt.Graphics2D;

import com.alex.aqua.bundle.BundleFactory;

public class DefaultSprite extends AbstractSprite {
	
	@Override
	public void render(Graphics2D graph) {
		super.render(graph);
	}
	
	public static DefaultSprite extract(int x, int y, int width, int height, Drawable drawable) {
		DefaultSprite defaultSprite = BundleFactory.extract(DefaultSprite.class);
		if(null != defaultSprite) {
			defaultSprite.x = x;
			defaultSprite.y = y;
			defaultSprite.layer = 0;
			defaultSprite.width = width;
			defaultSprite.height = height;
			defaultSprite.visible = true;
			defaultSprite.drawable = drawable;
		}
		return defaultSprite;
	}
}
