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
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JToggleButton;

public class SwitchButton extends JToggleButton {
	private static final long serialVersionUID = 1L;
	private int fontsize;
	private String onText;
	private String offText;
	public SwitchButton(String on, String off, String command, int width, int height, int fontsize) {
		super();
		this.fontsize = fontsize;
		this.onText = on;
		this.offText = off;
		this.setActionCommand(command);
		this.setFocusable(false);
		this.setOpaque(false);
		this.setSize(width, height);
		setPreferredSize(new Dimension(width, height));
	}
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int pad = 5;
		Polygon polyLeft = new Polygon(
				new int[]{pad, getWidth()/2, getWidth()/2, pad}, 
				new int[]{pad, pad, getHeight()-pad, getHeight()-pad}, 4);
		Polygon polyRight = new Polygon(
				new int[]{getWidth()/2, getWidth()-pad, getWidth()-pad, getWidth()/2}, 
				new int[]{pad, pad, getHeight()-pad, getHeight()-pad}, 4);
		g2d.setColor(Color.red.darker());
		if(isSelected())
			g2d.setColor(new Color(172, 225, 175));
		else
			g2d.setColor(Color.gray);
		g2d.fill(polyLeft);
		if(isSelected())
			g2d.setColor(Color.gray);
		else
			g2d.setColor(new Color(172, 225, 175));
		g2d.fill(polyRight);

		Area round1 = new Area(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));
		Area round2 = new Area(new RoundRectangle2D.Double(4, 4, getWidth() - 1 - 8, getHeight() - 1 - 8, 6, 6));
		Area round3 = new Area(new RoundRectangle2D.Double(6, 6, getWidth() - 1 - 12, getHeight() - 1 - 12, 5, 5));
		Area round4 = new Area(new Ellipse2D.Float(-getWidth()/2, -getHeight(), getWidth()*2, getHeight()*3/2));

		round4.intersect(round2);
		round1.exclusiveOr(round2);
		round2.exclusiveOr(round3);
		int width = getWidth();
		int height = getHeight();
		int d1 = 6;
		int d2 = 8;
		Area outBorder = new Area(new Polygon(new int[]{
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
		g2d.setColor(new Color(0x993333));
		g2d.fill(outBorder);
		int d3 = 3;
		int d4 = 5;
		Area midBorder = new Area(new Rectangle2D.Double(d3, d3, width - 1 - d3*2, height - 1 - d3*2));
		midBorder.subtract(new Area(new Rectangle2D.Double(d4, d4, width - 1 - d4*2, height - 1 - d4*2)));
		g2d.fill(midBorder);
		GlyphVector glyphVectorLeft = new Font("宋体", Font.PLAIN, fontsize).createGlyphVector(new FontRenderContext(null, true, true), onText);
		Shape shapeTextLeft = glyphVectorLeft.getOutline(
				-(float)(glyphVectorLeft.getVisualBounds().getMinX() - d3 - (getWidth()/2 - glyphVectorLeft.getVisualBounds().getWidth())/2 + 1), 
				-(float)(glyphVectorLeft.getVisualBounds().getMinY() - (getHeight() - glyphVectorLeft.getVisualBounds().getHeight())/2) - 1);
		GlyphVector glyphVectorRight = new Font("宋体", Font.PLAIN, fontsize).createGlyphVector(new FontRenderContext(null, true, true), offText);
		Shape shapeTextRight = glyphVectorRight.getOutline(
				-(float)(glyphVectorRight.getVisualBounds().getMinX() + d3 - (getWidth()*3/2 - glyphVectorRight.getVisualBounds().getWidth())/2 + 1), 
				-(float)(glyphVectorRight.getVisualBounds().getMinY() - (getHeight() - glyphVectorRight.getVisualBounds().getHeight())/2) - 1);
		g2d.setColor(new Color(23, 54, 93));
		g2d.draw(shapeTextLeft);
		g2d.setColor(new Color(23, 54, 93));
		g2d.draw(shapeTextRight);
	}
}
