package com.alex.aqua.source;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageSourceDao extends AbstractSourceDao{

	@Override
	public String getDaoName() {
		return "default_image_dao";
	}

	@Override
	public String getSourceType() {
		return SourceFactory.TYPE_IMAGE;
	}

	@Override
	protected Object loadSource(File file) {
		try {
			Image readImage = ImageIO.read(file);
			if(file == null) {
				ImageFilter filter = new ColorFilter();
				FilteredImageSource src = new FilteredImageSource(readImage.getSource(), filter);
				readImage = Toolkit.getDefaultToolkit().createImage(src);
			}
			return readImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	class ColorFilter extends RGBImageFilter {
		int mask = 0;
		@Override
		public int filterRGB(int x, int y, int rgb) {
			if(x==0&&y==0)
				mask = rgb;
			if(rgb==mask)
				return 0;
			else 
				return rgb;
		}
	}
}