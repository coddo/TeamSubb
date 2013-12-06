package com.coddotech.teamsubb.maingui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

abstract class CustomWindow {

	private Shell shell;

	/**
	 * @return the shell for this class
	 */
	public Shell getShell() {
		return shell;
	}

	/**
	 * Class constructor
	 */
	CustomWindow() {
		createContents();
	}

	/**
	 * Display the GUI (shell) for this class
	 */
	public void show() {
		shell.open();
		while (!shell.isDisposed()) {
			if (!Display.getCurrent().readAndDispatch())
				Display.getCurrent().sleep();
		}
	}

	/*
	 * Close the GUI for this class and dispose of its contents
	 */
	public void close() {
		shell.dispose();
		Display.getCurrent().dispose();
	}

	/*
	 * creates the contents for this shell instance
	 */
	private void createContents() {
		shell = new Shell();

		int x = Display.getCurrent().getMonitors()[0].getClientArea().width / 2;
		int y = Display.getCurrent().getMonitors()[0].getClientArea().height / 2;
		shell.setSize(300, 300);
		shell.setLocation(x - getShell().getSize().x / 2, y - getShell().getSize().y / 2); // center of screen

		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event e) {
				shell.dispose();
			}
		});
	}
}
