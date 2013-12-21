package com.coddotech.teamsubb.main;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public abstract class CustomWindow {

	private Shell shell;

	/**
	 * Class constructor
	 */
	public CustomWindow() {
		createContents();
	}

	/**
	 * Get the shell for this class
	 * 
	 * @return The shell for this class
	 */
	public Shell getShell() {
		return shell;
	}

	/**
	 * Display the GUI (shell) for this class
	 */
	public void open() {
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
		shell.close();
	}

	/**
	 * Position the window at the center of the desktop
	 */
	public void placeToCenter() {
		int x = Display.getCurrent().getMonitors()[0].getClientArea().width / 2;
		int y = Display.getCurrent().getMonitors()[0].getClientArea().height / 2;

		shell.setLocation(x - getShell().getSize().x / 2, y
				- getShell().getSize().y / 2);
	}

	/*
	 * creates the contents for this shell instance
	 */
	private void createContents() {
		// Prevent the window from being resized
		shell = new Shell(Display.getCurrent(), SWT.SHELL_TRIM ^ SWT.RESIZE);

		shell.setSize(300, 300);
		this.placeToCenter();

		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event e) {
				shell.dispose();
			}
		});
	}
	
	/**
	 * Displays an error message telling the user that the connection to the
	 * server was unsuccessful
	 */
	public void showConnectionErrorMessage() {
		MessageBox message = new MessageBox(
				Display.getCurrent().getShells()[0], SWT.ICON_ERROR);
		message.setMessage("A connection error has occured.\nPlease try again later...");
		message.setText("Connection failed");
		message.open();
	}
}
