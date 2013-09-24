package com.alex.aqua.framework.util;

import java.util.HashMap;
import java.util.Vector;

import com.alex.aqua.core.Aqua;
import com.alex.aqua.framework.ILoopable;

public class LoopAdapter implements Runnable, IConstant{
	private Thread thread = null;
	private ILoopable loopObject;
	private HashMap<Long, Vector<Integer>> scheduleMap = new HashMap<Long, Vector<Integer>>();
	public LoopAdapter(ILoopable loopObject) {
		if(loopObject!=null) {
			this.loopObject = loopObject;
		}
	}
	@Override
	public void run() {
		while(looping) {
			loopObject.processLoop(SCHEDULE_MAIN, timestamp);
			Vector<Integer> schedule = scheduleMap.get(timestamp);
			//System.out.println(timestamp + " "+schedule);
			if(schedule!=null) {
				Integer[] schedules = new Integer[schedule.size()];
				schedule.toArray(schedules);
				for(Integer id:schedules) {
					if(!loopObject.processLoop(id, timestamp))
						schedule.remove(timestamp);
				}
			}
			timestamp = timestamp+1;
			try {
				Thread.sleep(1000/fps);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private long timestamp = 0;
	private int fps = 1000;
	private boolean looping = false;

	public boolean startLoop() {
		if(thread!=null && thread.isAlive()) {
			Aqua.debug("loop", "error");
			return false;
		} else {
			looping = true;
			thread = new Thread(this);
			thread.start();
			return true;
		}
	}
	public boolean pauseLoop() {
		if(looping) {
			looping = false;
			while(null != thread && thread.isAlive());
			thread = null;
			return true;
		} else {
			return false;
		}
	}
	public void registerSchedule(int scheduleID, long deadline) {
		if(scheduleID==0)return;
		Vector<Integer> schedule = scheduleMap.get(deadline);
		if(schedule==null) {
			schedule = new Vector<Integer>();
			scheduleMap.put(deadline, schedule);
		}
		schedule.add(scheduleID);
	}
	public long getTimestamp() {
		return timestamp;
	}
	public boolean isLooping() {
		return looping;
	}
	public int getFps() {
		return fps;
	}
	public void setFps(int delay) {
		this.fps = delay;
	}
}
