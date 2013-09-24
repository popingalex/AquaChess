package com.alex.aqua.framework.util;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class InputManger implements MouseListener, MouseMotionListener, KeyListener, IConstant {
	private final int keyUp = 1;
	private final int keyDown = 2;
	private final int keyLeft = 4;
	private final int keyRight = 8;

//	private final int up_left = keyUp|keyLeft;
//	private final int up_right = keyUp|keyRight;
//	private final int down_left = keyDown|keyLeft;
//	private final int down_right = keyDown|keyRight;

	private int valid = 0;
	private int cache = 0;

	Point last;
	boolean dragSwitch = true;
	@Override
	public final void mouseDragged(MouseEvent event) {
		if(dragSwitch){
			Point curr = event.getLocationOnScreen();
			if(curr==null)return;
			postDragShift(curr.x - last.x, curr.y - last.y);
			last = curr;
		}
	}
	@Override
	public void mouseMoved(MouseEvent event) {
		//dealInputWorks(INPUT_MOUSE_MOVE, event.getButton(), event.getX(), event.getY());
	}
	@Override
	public void mouseClicked(MouseEvent event) {
		//dealInputWorks(INPUT_MOUSE_CLICK, event.getButton(), event.getX(), event.getY());
	}
	@Override
	public void mouseEntered(MouseEvent event) {
		//dealInputWorks(INPUT_MOUSE_CLICK, event.getButton(), event.getX(), event.getY());
	}
	@Override
	public void mouseExited(MouseEvent event) {
		//dealInputWorks(INPUT_MOUSE_EXIT, event.getButton(), event.getX(), event.getY());
	}
	@Override
	public void mousePressed(MouseEvent event) {
		//dealInputWorks(INPUT_MOUSE_PRESS, event.getButton(), event.getX(), event.getY());
		last = event.getLocationOnScreen();
	}
	@Override
	public void mouseReleased(MouseEvent event) {
		//dealInputWorks(INPUT_MOUSE_RELEASE, event.getButton(), event.getX(), event.getY());
	}
	@Override
	public void keyPressed(KeyEvent event) {
		dealInputWorks(INPUT_KEY_PRESS, event.getKeyCode(), 0, 0);
		int key = 0;
		switch (event.getKeyCode()) {
		case KeyEvent.VK_UP:
			key = keyUp;
			break;
		case KeyEvent.VK_DOWN:
			key = keyDown;
			break;
		case KeyEvent.VK_LEFT:
			key = keyLeft;
			break;
		case KeyEvent.VK_RIGHT:
			key = keyRight;
			break;
		default:
			break;
		}
		if(0 != (cache & key)) return;
		cache = cache | key;
		//System.out.println(cache);
		if((key & (keyUp | keyDown)) ==0) {
			int temp = valid & (keyUp|keyDown);
			if(valid != temp) {
				//System.out.println(temp==keyUp?"up ":"down ");
			}
			valid = temp;
		} else if((key & (keyLeft | keyRight)) == 0){
			int temp = valid & (keyLeft|keyRight);
			if(valid != temp) {
				//System.out.println(temp==keyLeft?"left ":"right ");
			}
			valid = temp;
		}
		valid = valid | key;

		if((valid & keyUp) != 0) {
			//			System.out.print("up ");
		}
		if((valid & keyDown) != 0) {
			//			System.out.print("down ");
		}
		if((valid & keyLeft) != 0) {
			//			System.out.print("left ");
		}
		if((valid & keyRight) != 0) {
			//			System.out.print("right ");
		}
		//System.out.println();
		System.out.println("press");
	}
	@Override
	public void keyReleased(KeyEvent event) {
		System.out.println("release");
		dealInputWorks(INPUT_KEY_RELEASE, event.getKeyCode(), 0, 0);
		int key = 0;
		switch (event.getKeyCode()) {
		case KeyEvent.VK_UP:
			key = keyUp;
			break;
		case KeyEvent.VK_DOWN:
			key = keyDown;
			break;
		case KeyEvent.VK_LEFT:
			key = keyLeft;
			break;
		case KeyEvent.VK_RIGHT:
			key = keyRight;
			break;
		default:
			break;
		}
		cache = cache &~ key;
		valid = valid &~ key;
	}
	@Override
	public void keyTyped(KeyEvent event) {}
	public abstract void dealInputWorks(int type, int key, int param1, int param2);
	public abstract void postDragShift(int shiftX, int shiftY);
}
