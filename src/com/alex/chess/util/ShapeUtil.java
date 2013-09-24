package com.alex.chess.util;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

public class ShapeUtil implements ShapeConstant{
	public static Shape getEdgeShape(int marginLeft, int marginTop, int boardWidth, int boardHeight, int edge) {
		Path2D.Double chessEdge = new Path2D.Double();
		chessEdge.append(new Rectangle2D.Double(
				marginLeft - edge, 
				marginTop - edge, 
				boardWidth + edge*2, 
				boardHeight + edge*2), false);
		return chessEdge;
	}
	public static Shape getBoardShape(int marginLeft, int marginTop, int boardWidth, int boardHeight, int radius, int pad, int edge) {
		Path2D.Double chessBoard = new Path2D.Double();
		chessBoard.append(new Rectangle2D.Double(
				marginLeft, 
				marginTop, 
				boardWidth, 
				boardHeight), false);
		//row
		for(int i=0;i<8;i++) {
			chessBoard.append(new Line2D.Double(
					marginLeft, 
					marginTop+(radius*2+pad)*(i+1), 
					marginLeft+boardWidth, 
					marginTop+(radius*2+pad)*(i+1)), false);
		}
		//colume
		for(int j=0;j<7;j++) {
			chessBoard.append(new Line2D.Double(
					marginLeft+(radius*2+pad)*(j+1), 
					marginTop, 
					marginLeft+(radius*2+pad)*(j+1), 
					marginTop+(boardHeight-pad)/2-radius), false);
			chessBoard.append(new Line2D.Double(
					marginLeft+(radius*2+pad)*(j+1), 
					marginTop+boardHeight/2+radius+pad, 
					marginLeft+(radius*2+pad)*(j+1), 
					marginTop+boardHeight), false);
		}
		//gurad route
		chessBoard.append(new Line2D.Double(
				marginLeft + 3*(radius*2+pad),
				marginTop,
				marginLeft + 5*(radius*2+pad),
				marginTop + 2*(radius*2+pad)), false);
		chessBoard.append(new Line2D.Double(
				marginLeft + 5*(radius*2+pad),
				marginTop,
				marginLeft + 3*(radius*2+pad),
				marginTop + 2*(radius*2+pad)), false);
		chessBoard.append(new Line2D.Double(
				marginLeft + 3*(radius*2+pad),
				marginTop + 7*(radius*2+pad),
				marginLeft + 5*(radius*2+pad),
				marginTop + 9*(radius*2+pad)), false);
		chessBoard.append(new Line2D.Double(
				marginLeft + 5*(radius*2+pad),
				marginTop + 7*(radius*2+pad),
				marginLeft + 3*(radius*2+pad),
				marginTop + 9*(radius*2+pad)), false);
		//sign
		chessBoard.append(new Rectangle2D.Double(
				marginLeft, 
				marginTop, 
				boardWidth, 
				boardHeight), false);
		shapeSign(chessBoard, 0, 3, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 1, 2, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 2, 3, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 4, 3, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 6, 3, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 7, 2, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 8, 3, marginLeft, marginTop, radius, pad);

		shapeSign(chessBoard, 0, 9 - 3, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 1, 9 - 2, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 2, 9 - 3, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 4, 9 - 3, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 6, 9 - 3, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 7, 9 - 2, marginLeft, marginTop, radius, pad);
		shapeSign(chessBoard, 8, 9 - 3, marginLeft, marginTop, radius, pad);
		return chessBoard;
	}
	private static void shapeSign(Path2D.Double board, int x, int y, int marginLeft, int marginTop, int radius, int pad) {
		int centerX = marginLeft + x*(radius*2+pad);
		int centerY = marginTop + y*(radius*2+pad);
		int len = pad*2;
		if(x>0) {
			board.append(new Line2D.Double(centerX - pad - len, centerY - pad, centerX - pad, centerY - pad), false);
			board.append(new Line2D.Double(centerX - pad, centerY - pad - len, centerX - pad, centerY - pad), false);
			board.append(new Line2D.Double(centerX - pad - len, centerY + pad, centerX - pad, centerY + pad), false);
			board.append(new Line2D.Double(centerX - pad, centerY + pad + len, centerX - pad, centerY + pad), false);
		}
		if(x<8) {
			board.append(new Line2D.Double(centerX + pad + len, centerY - pad, centerX + pad, centerY - pad), false);
			board.append(new Line2D.Double(centerX + pad, centerY - pad - len, centerX + pad, centerY - pad), false);
			board.append(new Line2D.Double(centerX + pad + len, centerY + pad, centerX + pad, centerY + pad), false);
			board.append(new Line2D.Double(centerX + pad, centerY + pad + len, centerX + pad, centerY + pad), false);
		}
	}
	public static Shape getChessBack(int left, int top, int width, int height,
			int pad) {
		return new Rectangle2D.Double(pad, pad, 400-pad*2, height-pad*2);
	}
	public static Shape getFeatureBack(int left, int top, int width, int height,
			int pad) {
		return new Rectangle2D.Double(400, 0, 200, height);
	}
	public static Shape getCanvasBack(int left, int top, int width, int height,
			int pad) {
		Path2D.Double canvas = new Path2D.Double();
		canvas.append(new Rectangle2D.Double(0, 0, width, height), false);
		return canvas;
	}
	public static Shape getCharacterShape(int[] segTypes, double[] segCoords) {
		Path2D.Double path = new Path2D.Double();
		int cursor = 0;
		for(int i=0;i<segTypes.length;i++) {
			int segType = segTypes[i];
			switch (segType) {
			case PathIterator.SEG_MOVETO:
				path.moveTo(segCoords[cursor], segCoords[cursor+1]);
				cursor = cursor+2;
				break;
			case PathIterator.SEG_LINETO:
				path.lineTo(segCoords[cursor], segCoords[cursor+1]);
				cursor = cursor+2;
				break;
			case PathIterator.SEG_QUADTO:
				path.quadTo(segCoords[cursor], segCoords[cursor+1], segCoords[cursor+2], segCoords[cursor+3]);
				cursor = cursor+4;
				break;
			case PathIterator.SEG_CUBICTO:
				path.curveTo(segCoords[cursor], segCoords[cursor+1], segCoords[cursor+2], segCoords[cursor+3], segCoords[cursor+4], segCoords[cursor+5]);
				cursor = cursor+6;
				break;
			case PathIterator.SEG_CLOSE:
				path.closePath();
				break;
			}
		}
		return path;
	}
	public static Shape getChessRiver(int marginLeft, int marginTop, int boardWidth, int boardHeight) {
		int offX = marginLeft + boardWidth/2;
		int offY = marginTop + boardHeight/2;
		//GlyphVector glyphVector = new Font("隶书", Font.PLAIN, 36).createGlyphVector(new FontRenderContext(null, true, true), "楚");
		AffineTransform leftRotator = AffineTransform.getRotateInstance(-Math.PI/2);
		AffineTransform rightRotator = AffineTransform.getRotateInstance(Math.PI/2);
		Shape shapeText;
		
		Area riverShape = new Area();

		shapeText = ShapeUtil.getCharacterShape(ShapeUtil.riverShapeSegs[0], ShapeUtil.riverShapeCoords[0]);
		shapeText = leftRotator.createTransformedShape(shapeText);
		shapeText = AffineTransform.getScaleInstance(2, 1).createTransformedShape(shapeText);
		shapeText = AffineTransform.getTranslateInstance(offX-boardWidth*3/10, offY).createTransformedShape(shapeText);
		riverShape.add(new Area(shapeText));
		shapeText = ShapeUtil.getCharacterShape(ShapeUtil.riverShapeSegs[1], ShapeUtil.riverShapeCoords[1]);
		shapeText = leftRotator.createTransformedShape(shapeText);
		shapeText = AffineTransform.getScaleInstance(2, 1).createTransformedShape(shapeText);
		shapeText = AffineTransform.getTranslateInstance(offX-boardWidth*1/10, offY).createTransformedShape(shapeText);
		riverShape.add(new Area(shapeText));
		shapeText = ShapeUtil.getCharacterShape(ShapeUtil.riverShapeSegs[2], ShapeUtil.riverShapeCoords[2]);
		shapeText = rightRotator.createTransformedShape(shapeText);
		shapeText = AffineTransform.getScaleInstance(2, 1).createTransformedShape(shapeText);
		shapeText = AffineTransform.getTranslateInstance(offX+boardWidth*3/10, offY).createTransformedShape(shapeText);
		riverShape.add(new Area(shapeText));
		shapeText = ShapeUtil.getCharacterShape(ShapeUtil.riverShapeSegs[3], ShapeUtil.riverShapeCoords[3]);
		shapeText = rightRotator.createTransformedShape(shapeText);
		shapeText = AffineTransform.getScaleInstance(2, 1).createTransformedShape(shapeText);
		shapeText = AffineTransform.getTranslateInstance(offX+boardWidth*1/10, offY).createTransformedShape(shapeText);
		riverShape.add(new Area(shapeText));
		
		return riverShape;
	}
}
