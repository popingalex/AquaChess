package com.alex.chess.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.GrayFilter;
import javax.swing.JButton;

public class DecorButton extends JButton {
	private static final long serialVersionUID = 1L;
	private Shape shape;
	private int fontSize;
	private boolean edge;
	private Area round1;
	private Area round2;
	private Area outBorder;
	private Area midBorder;
	public DecorButton(String title, int width, int height, int fontsize) {
		this(title, title.toLowerCase(), width, height, fontsize, true);
	}
	public DecorButton(String title, String command, int width, int height, int fontsize) {
		this(title, command, width, height, fontsize, true);
	}
	public DecorButton(String title, String command, int width, int height, int fontsize, boolean edge) {
		super(title);
		this.setFocusable(false);
		this.fontSize = fontsize;
		this.edge = edge;
		setOpaque(false);
		setSize(width, height);
		setPreferredSize(new Dimension(width, height));
		setActionCommand(command);
		shape = new Rectangle2D.Double(0, 0, getWidth()-1, getHeight()-1);
		round1 = new Area(new RoundRectangle2D.Double(4, 4, getWidth() - 1 - 8, getHeight() - 1 - 8, 6, 6));
		round2 = new Area(new Ellipse2D.Float(-getWidth()/2, -getHeight(), getWidth()*2, getHeight()*3/2));
		round2.intersect(round1);
		int d1 = 6;
		int d2 = 8;
		outBorder = new Area(new Polygon(new int[]{
				d1,
				width - 1 - d1,
				width - 1 - d1,
				width - 1,
				width - 1,
				width - 1 - d1,
				width - 1 - d1,
				d1,
				d1,
				0,
				0,
				d1}, new int[]{
				0,
				0,
				d1,
				d1,
				height - 1 - d1,
				height - 1 - d1,
				height - 1,
				height - 1,
				height - 1 - d1,
				height - 1 - d1,
				d1,
				d1}, 12));
		outBorder.subtract(new Area(new Polygon(new int[]{
				d2,
				width - 1 - d2,
				width - 1 - d2,
				width - 1 - (d2-d1),
				width - 1 - (d2-d1),
				width - 1 - d2,
				width - 1 - d2,
				d2,
				d2,
				d2-d1,
				d2-d1,
				d2}, new int[]{
				d2-d1,
				d2-d1,
				d2,
				d2,
				height - 1 - d2,
				height - 1 - d2,
				height - 1 - (d2-d1),
				height - 1 - (d2-d1),
				height - 1 - d2,
				height - 1 - d2,
				d2,
				d2}, 12)));
		int d3 = 3;
		int d4 = 5;
		midBorder = new Area(new Rectangle2D.Double(d3, d3, width - 1 - d3*2, height - 1 - d3*2));
		midBorder.subtract(new Area(new Rectangle2D.Double(d4, d4, width - 1 - d4*2, height - 1 - d4*2)));
	}
	@Override
	public boolean contains(int x, int y) {
		return shape.contains(new Point2D.Double(x, y));
	}
	int i=0;
	@Override
	public void paint(Graphics g) {
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		
		g2.setColor(new Color(0x993333));
		g2.fill(outBorder);
		g2.fill(midBorder);
		
		if(getModel().isPressed())
			g2.setColor(new Color(198, 217, 240, 100));
		else if(getModel().isRollover())
			g2.setColor(new Color(198, 217, 240, 120));
		else
			g2.setColor(new Color(198, 217, 240, 50));
		g2.fill(round2);
		
		GlyphVector glyphVector = new Font("宋体", Font.PLAIN, fontSize).createGlyphVector(new FontRenderContext(null, true, true), getText());
		Shape shapeText = glyphVector.getOutline(
				-(float)(glyphVector.getVisualBounds().getMinX() - (getWidth() - glyphVector.getVisualBounds().getWidth())/2 + 1), 
				-(float)(glyphVector.getVisualBounds().getMinY() - (getHeight() - glyphVector.getVisualBounds().getHeight())/2) - 1);
		if(getModel().isPressed())
			g2.setColor(new Color(15, 36, 62));
		else if(getModel().isRollover())
			g2.setColor(new Color(84, 141, 212));
		else
			g2.setColor(new Color(23, 54, 93));
		g2.draw(shapeText);
		if(edge)
			g2.fill(shapeText);
		if(!isEnabled()) {
			g2.dispose();
		}
		g.drawImage(getModel().isEnabled()?image:GrayFilter.createDisabledImage(image), 0, 0, null);
	}
}
