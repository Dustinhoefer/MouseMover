package com.github.mousemover;

import java.awt.AWTException;
import java.awt.CheckboxGroup;
import java.awt.CheckboxMenuItem;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TrayUi {

	private MenuItem menuItemStartStop;
	private CheckboxMenuItem oneMinuteItem;
	private CheckboxMenuItem fiveMinuteItem;
	private CheckboxMenuItem thirtyMinuteItem;

	public TrayUi(Logic logic) throws IOException {

		final PopupMenu popup = new PopupMenu();
		Image image = ImageIO.read(TrayUi.class.getResource("Cursor.png"));
		int targetIconWidth = SystemTray.getSystemTray().getTrayIconSize().width;
		TrayIcon trayIcon = new TrayIcon(image.getScaledInstance(targetIconWidth, -1, Image.SCALE_SMOOTH));
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a pop-up menu components
		MenuItem aboutItem = new MenuItem("About");
		menuItemStartStop = new MenuItem(I18NHelper.getMessage("start"));
		menuItemStartStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logic.startStop();
			}
		});

		Menu timeMenu = new Menu(I18NHelper.getMessage("time_setting"));

		oneMinuteItem = new CheckboxMenuItem(I18NHelper.getMessage("one_minute"));
		oneMinuteItem.addActionListener(e -> logic.setDelay(60));

		fiveMinuteItem = new CheckboxMenuItem(I18NHelper.getMessage("five_minutes"));
		fiveMinuteItem.addActionListener(e -> logic.setDelay(60 * 5));

		thirtyMinuteItem = new CheckboxMenuItem(I18NHelper.getMessage("thirty_minutes"));
		thirtyMinuteItem.addActionListener(e -> logic.setDelay(60 * 30));

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(e -> logic.exit());

		// Add components to pop-up menu
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(menuItemStartStop);
		popup.addSeparator();
		popup.add(timeMenu);
		timeMenu.add(oneMinuteItem);
		timeMenu.add(fiveMinuteItem);
		timeMenu.add(thirtyMinuteItem);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	}

	public void updateState(boolean running, int delayInSeconds) {
		EventQueue.invokeLater(() -> {
			if (running) {
				menuItemStartStop.setLabel(I18NHelper.getMessage("start"));
			} else {
				menuItemStartStop.setLabel(I18NHelper.getMessage("stop"));
			}

			oneMinuteItem.setState(delayInSeconds == 60);
			fiveMinuteItem.setState(delayInSeconds == 60 * 5);
			thirtyMinuteItem.setState(delayInSeconds == 60 * 30);
		});
	}

}
