package com.coddotech.teamsubb;

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
		getShell().open();
		while (!getShell().isDisposed()) {
			if (!Display.getCurrent().readAndDispatch())
				Display.getCurrent().sleep();
		}
	}

	/*
	 * Close the GUI for this class and dispose of its contents
	 */
	public void close() {
		getShell().dispose();
	}

	/*
	 * creates the contents for this shell instance
	 */
	private void createContents() {
		shell = new Shell();

		int x = Display.getCurrent().getMonitors()[0].getClientArea().width / 2;
		int y = Display.getCurrent().getMonitors()[0].getClientArea().height / 2;
		getShell().setSize(800, 600);
		getShell().setLocation(x - getShell().getSize().x / 2, y - getShell().getSize().y / 2); // center of screen

		getShell().addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event e) {
				getShell().dispose();
			}
		});
	}
}
