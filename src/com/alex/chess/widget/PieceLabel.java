package com.alex.chess.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import javax.swing.JLabel;

import com.alex.chess.core.ChessConstant;
import com.alex.chess.util.ShapeUtil;

public class PieceLabel extends JLabel implements ChessConstant{
	private static final long serialVersionUID = 1L;
	private int type;
	private Shape character;
	private Area outter;
	private Area inner;
	public PieceLabel(int type, int radius, MouseAdapter adapter) {
		this.type = type;
		this.setSize(radius*2+1, radius*2+1);
		this.addMouseListener(adapter);
		this.addMouseMotionListener(adapter);
		character = ShapeUtil.getCharacterShape(ShapeUtil.pieceShapeSegs[type], ShapeUtil.pieceShapeCoords[type]);
		outter = new Area();
		outter.add(new Area(new Ellipse2D.Double(0, 0, getWidth()-1, getHeight()-1)));
		int gap = 4;
		inner = new Area(new Ellipse2D.Double(gap, gap, getWidth()-gap*2-1, getHeight()-gap*2-1));
		outter.exclusiveOr(inner);
	}
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.gray);
		g2d.fill(outter);
		g2d.setColor(Color.lightGray);
		{
			//g2d.setColor(Color.white);
		}
		g2d.fill(inner);
		if(type<7) {
			g2d.setColor(Color.red.darker());
			{
				//g2d.setColor(Color.gray);
			}
		} else {
			g2d.setColor(Color.black);
		}
		g2d.fill(character);
	}
	public int getType() {
		return type;
	}
}
