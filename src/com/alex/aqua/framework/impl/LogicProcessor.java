package com.alex.aqua.framework.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import com.alex.aqua.bundle.EchoBundle;
import com.alex.aqua.core.Aqua;
import com.alex.aqua.framework.ILogicProcessor;
import com.alex.aqua.framework.ILoopable;
import com.alex.aqua.framework.util.EchoCenter;
import com.alex.aqua.sprite.AbstractSprite;

public abstract class LogicProcessor implements ILogicProcessor, ILoopable{
	private static Comparator<AbstractSprite> layerComparator = new Comparator<AbstractSprite>() {
		@Override
		public int compare(AbstractSprite o1, AbstractSprite o2) {
			return o1.layer - o2.layer;
		}
	};
	protected final void offer(int view, EchoBundle source, EchoBundle result) {
		EchoCenter.getInstance().offerEcho(view, Aqua.ECHO_TO_VIEW, source, result);
	}
	protected final void share(int logic, EchoBundle source, EchoBundle result) {
		EchoCenter.getInstance().offerEcho(logic, Aqua.ECHO_TO_LOGIC, source, result);
	}
	protected final void updateSprite(int view, AbstractSprite[] freshMeat) {
		AbstractSprite[] toastMeat = new AbstractSprite[freshMeat.length];
		for(int i=0; i<freshMeat.length; i++) {
			toastMeat[i] = freshMeat[i].clone();
		}
		Arrays.sort(toastMeat, layerComparator);
		offer(view, EchoBundle.extract(ECHO_VIEW_SPRITE_FRESH, toastMeat), null);
	}
	protected final void updateSprite(int view, Collection<AbstractSprite> freshMeat) {
		AbstractSprite[] freshMeatArray = new AbstractSprite[freshMeat.size()];
		freshMeat.toArray(freshMeatArray);
		updateSprite(view, freshMeatArray);
	}
	protected final void updateSprite(int view, AbstractSprite demoSprite) {
		updateSprite(view, new AbstractSprite[]{demoSprite});
	}
	protected int processLogic(EchoBundle source, EchoBundle result) {
		return 0;
	}
	@Override
	public final int handleEcho(EchoBundle source, EchoBundle result) {
		if(source.param<ECHO_AQUA_USER_MIN) {
			switch (source.param) {
			case ECHO_AQUA_START:
				break;
			default:
				break;
			}
		}
		return processLogic(source, result);// TODO Auto-generated method stub
	}
	@Override
	public void init() {}
	@Override
	public boolean processLoop(int stampID, long timestamp) {return false;}
}
