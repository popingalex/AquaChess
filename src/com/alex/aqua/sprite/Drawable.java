package com.alex.aqua.sprite;

import java.awt.Image;

import com.alex.aqua.bundle.Bundle;
import com.alex.aqua.bundle.BundleFactory;

public class Drawable extends Bundle{
	public long starttime;
	public int count = 0;
	public int duration;
	public int[] cliptime;
	public Image[] clipimage;
	
	public static Drawable extarct(Image image) {
		Drawable drawable = BundleFactory.extract(Drawable.class);
		if(null != drawable) {
			drawable.count = 1;
			drawable.clipimage = new Image[]{image};
		}
		return drawable;
	}
	public static Drawable extarct(Image[] images) {
		return extarct(images, 1);
	}
	public static Drawable extarct(Image[] images, int delay) {
		Drawable drawable = BundleFactory.extract(Drawable.class);
		if(null != drawable && null != images) {
			drawable.count = images.length;
			drawable.clipimage = images;
			drawable.duration = drawable.count * delay;
			drawable.starttime = 0;
			drawable.cliptime = new int[drawable.count];
			for(int i=0;i<drawable.cliptime.length;i++)
				drawable.cliptime[i] = delay;
		}
		return drawable;
	}
}
