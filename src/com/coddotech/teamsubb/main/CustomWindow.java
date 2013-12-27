package com.coddotech.teamsubb.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.coddotech.teamsubb.connection.ConnectionManager;

public abstract class CustomWindow {

	public static final String NOTIFICATION_SEPARATOR = "#@&!#@&!";

	private Shell shell;

	/**
	 * Class constructor. Initializez all the components for the GUI class
	 */
	public CustomWindow() {
		createShell();
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
	 * Set the shell to be used for this window
	 * 
	 * @param shell
	 *            The new desired shell
	 */
	public void setShell(Shell shell) {
		this.shell = shell;
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

	/**
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

	/**
	 * Displays an error message telling the user that the connection to the
	 * server was unsuccessful if the app cannot establish a a connection to the
	 * server
	 */
	public boolean isConnected(boolean displayMessage) {
		boolean connected = ConnectionManager.isConnected();
		if (!connected && displayMessage) {

			MessageBox message = new MessageBox(Display.getCurrent()
					.getShells()[0], SWT.ICON_ERROR);
			message.setMessage("A connection error has occured.\nPlease try again later...");
			message.setText("Connection failed");
			message.open();
		}

		return connected;
	}

	/*
	 * creates the contents for this shell instance
	 */
	private void createShell() {
		// Prevent the window from being resized
		shell = new Shell(Display.getCurrent(), SWT.SHELL_TRIM ^ SWT.RESIZE
				^ SWT.DIALOG_TRIM);

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
	 * Initializez all the components that are used in this GUI
	 */
	protected void initializeComponents() {
		// initializations
		this.performInitializations();

		// object properties
		this.createObjectProperties();

		// shell properties
		this.createShellProperties();

		// listeners
		this.createListeners();
	}

	/**
	 * Object initializations and instance creation
	 */
	protected abstract void performInitializations();

	/**
	 * Set the properties for all the components used in this GUI instance
	 */
	protected abstract void createObjectProperties();

	/**
	 * Set the shell properties for this GUI instance
	 */
	protected abstract void createShellProperties();

	/**
	 * Set the listeners that will be used in this GUI instance
	 */
	protected abstract void createListeners();
}
