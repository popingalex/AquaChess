package com.alex.aqua.framework.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Arrays;
import java.util.Comparator;

import com.alex.aqua.bundle.EchoBundle;
import com.alex.aqua.core.Aqua;
import com.alex.aqua.framework.IViewAdapter;
import com.alex.aqua.framework.util.EchoCenter;
import com.alex.aqua.sprite.AbstractSprite;
import com.alex.aqua.sprite.Drawable;

public abstract class ViewAdapter implements IViewAdapter {
	private boolean dirty = true;
	private AbstractSprite[] spriteArray;
	@Override
	public final void callDisplay() {
		clearComponents();
		beforeDisplay();
		AquaCanvas.getInastance().setCurrentView(this);
	}
	@Override
	public final void callRepaint(){//need improve
		dirty = true;
		AquaCanvas.getInastance().freshDesktop();
	}
	@Override
	public final void callReturn(){
	}
	@Override
	public final boolean isDirty() {
		return this.dirty;
	}
	@Override
	public final void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	public final void submit(int parm, EchoBundle source, EchoBundle result) {
		EchoCenter.getInstance().offerEcho(parm, ECHO_TO_LOGIC, source, result);
	}
	public final void startLoop() {
		AquaCanvas.getInastance().startLoop();
	}
	public final void pauseLoop() {
		AquaCanvas.getInastance().pauseLoop();
	}
	public final int getFps() {
		return AquaCanvas.getInastance().getFps();
	}
	public final void setFps(int fps) {
		AquaCanvas.getInastance().setFps(fps);
	}
	protected final int getWidth() {
		return AquaCanvas.getInastance().getWidth();
	}
	protected final int getHeight() {
		return AquaCanvas.getInastance().getHeight();
	}
	protected final void clearComponents() {
		AquaCanvas.getInastance().clearComponents();
	}
	protected final void addComponent(Component component, int layer) {
		AquaCanvas.getInastance().addComponent(component, layer);
	}
	protected final void dropComponent(Component component) {
		AquaCanvas.getInastance().dropComponent(component);
	}
	protected final void layerComponent(Component component, int layer) {
		AquaCanvas.getInastance().layerComponent(component, layer);
	}
	//=======================================

	@Override
	public void init() {}
	@Override
	public void beforeDisplay() {}
	@Override
	public void beforeDispose() {}

	boolean flag = true;
	@Override
	public void processCanvasPaint(long timestamp, Graphics2D graph) {
		if(null != spriteArray) {
			AbstractSprite[] spriteArray = this.spriteArray;
			for(int i=0; i<spriteArray.length; i++) {
				AbstractSprite sprite = spriteArray[i];
				processSpritePaint(timestamp, graph,  sprite);
			}
		}
	}
	@Override
	public void processSpritePaint(long timestamp, Graphics2D graph, AbstractSprite sprite) {
		Drawable drawable = sprite.drawable;
		if(null != drawable) {
			Image image = null;
			if(drawable.count == 1) {
				image = drawable.clipimage[0];
			} else if(drawable.count > 1) {
				int goal =  (int)(timestamp - drawable.starttime)%drawable.duration;
				int cursor, time;
				for(time = 0, cursor = 0;time<=goal;time = time + drawable.cliptime[cursor], cursor+=1);
				image = drawable.clipimage[cursor-1];
			}
			graph.drawImage(image, sprite.x, sprite.y, null);
		}
		if(Aqua.DEBUG_PAINT_BOARD) {
			graph.setColor(Color.gray);
			graph.drawRect(sprite.x, sprite.y, sprite.width-1, sprite.height-1);
		}
	}
	private static Comparator<AbstractSprite> layerComparator = new Comparator<AbstractSprite>() {
		@Override
		public int compare(AbstractSprite s1, AbstractSprite s2) {
			return s1.layer - s2.layer;
		}
	};
	@Override
	public int handleEcho(EchoBundle source, EchoBundle result) {
		if(source.param<ECHO_AQUA_USER_MIN) {
			switch (source.param) {
			case ECHO_VIEW_SPRITE_FRESH:
				AbstractSprite[] spriteJunior = AbstractSprite[].class.cast(source.bundle);
				Arrays.sort(spriteJunior, layerComparator);
				spriteArray = spriteJunior;
				break;
			case ECHO_AQUA_START:
				startLoop();
				break;
			case ECHO_VIEW_SHOW:
				this.callDisplay();
				break;
			default:
				break;
			}
		}
		return 0;
	}
	@Override
	public void processInputWorks(long timestamp, int type, int key, int param1, int param2) {
		if(type==INPUT_MOUSE_MOVE) {
			for(AbstractSprite sprite : spriteArray) {
				if(sprite.visible);
				//sprite
			}
		}
		this.submit(0, EchoBundle.extract(0, null), null);
	}
}
