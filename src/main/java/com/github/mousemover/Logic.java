package com.github.mousemover;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class Logic {

	private static final int mouseMoveOffset = 50;

	private boolean running;

	private Point mouseLocation = new Point(0, 0);

	private long lastActivity;

	private int delay = 60;

	public Logic() {
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				moveIfRequired();
			}
		}, 1, 1);
	}

	protected void moveIfRequired() {
		PointerInfo a = MouseInfo.getPointerInfo();
		if (!mouseLocation.equals(a.getLocation())) {
			lastActivity = System.currentTimeMillis();
			mouseLocation.setLocation(a.getLocation());
		}

		if (lastActivity < System.currentTimeMillis() - delay * 1000) {
			int newOffset = (int) (-mouseMoveOffset + Math.random()*2*mouseMoveOffset);
			
			try {
				Robot robot = new Robot();
				robot.mouseMove((int) (mouseLocation.getX()+newOffset), (int) (mouseLocation.getY()+newOffset));
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}

	}

	public void setDelay(int delay) {
		this.delay = delay;
		tray.ifPresent(t -> t.updateState(running, delay));
	}

	Optional<TrayUi> tray = Optional.empty();

	public void setUi(TrayUi tray) {
		this.tray = Optional.of(tray);
		tray.updateState(running, delay);
	}

	public void exit() {
		System.exit(0);
	}

	public void startStop() {
		running = !running;
		tray.ifPresent(t -> t.updateState(running, delay));
	}

}