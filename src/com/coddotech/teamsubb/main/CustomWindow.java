package com.coddotech.teamsubb.main;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.connection.model.ConnectionManager;

public abstract class CustomWindow implements Observer {

	public static final Font DEFAULT_FONT = new Font(Display.getDefault(), "Calibri", 12, SWT.NORMAL);
	public static final Font BOLD_FONT = new Font(Display.getDefault(), "Calibri", 12, SWT.BOLD);

	public static final Image APP_ICON = new Image(Display.getDefault(), System.getProperty("user.dir")
			+ File.separator + "resources" + File.separator + "icon.png");

	private Shell shell;
	private boolean disposed;

	/**
	 * Class constructor. Initializez all the components for the GUI class
	 */
	public CustomWindow() {
		createShell();
	}

	/**
	 * Get a value indicating whether the shell has been disposed or not
	 * 
	 * @return A logical value
	 */
	public boolean isDisposed() {
		return this.disposed;
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
		this.shell.removeListener(SWT.Close, this.disposeListener);

		this.shell.dispose();

		this.shell = shell;

		this.shell.addListener(SWT.Close, this.disposeListener);
	}

	/**
	 * Display the GUI (shell) for this class
	 */
	public void open() {
		this.disposed = false;

		shell.open();

		while (!shell.isDisposed()) {

			if (!Display.getDefault().readAndDispatch())
				Display.getDefault().sleep();

		}
	}

	/**
	 * Close the GUI for this class and dispose of its contents
	 */
	public void close() {
		shell.close();

		this.disposed = true;
	}

	/**
	 * Position the window at the center of the desktop
	 */
	public void placeToCenter() {
		int x = Display.getDefault().getMonitors()[0].getClientArea().width / 2;
		int y = Display.getDefault().getMonitors()[0].getClientArea().height / 2;

		shell.setLocation(x - getShell().getSize().x / 2, y - getShell().getSize().y / 2);
	}

	/**
	 * Displays an error message telling the user that the connection to the server was unsuccessful
	 * if the app cannot establish a a connection to the server.
	 */
	public static boolean isConnected(boolean displayMessage) {
		boolean connected = ConnectionManager.isConnected();

		if (!connected && displayMessage) {
			MessageBox message = new MessageBox(Display.getDefault().getShells()[0], SWT.ICON_ERROR);

			message.setMessage("A connection error has occured.\nPlease try again later...");
			message.setText("Connection failed");

			message.open();
		}

		return connected;
	}

	@Override
	public void update(Observable obs, Object obj) {
		try {
			this.updateGUI(obs, obj);
			
		}

		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "GUI Update", ex);
		}

	}

	/*
	 * Create the contents for this shell instance
	 */
	private void createShell() {

		// Prevent the window from being resized
		this.shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM ^ SWT.RESIZE ^ SWT.DIALOG_TRIM);

		this.shell.addListener(SWT.Close, this.disposeListener);
	}

	private Listener disposeListener = new Listener() {

		@Override
		public void handleEvent(Event e) {

			disposed = true;

			if (!shell.isDisposed() && !disposed)
				shell.dispose();

		}

	};

	protected void logDispose() {
		ActivityLogger.logActivity(this.getClass().getName(), "GUI dispose");

	}

	protected void logDiposeFail(Exception ex) {
		ActivityLogger.logException(this.getClass().getName(), "GUI dispose", ex);
	}

	/**
	 * Initializez all the components that are used in this GUI
	 */
	protected void initializeComponents() {

		// initializations
		try {
			this.performInitializations();

			ActivityLogger.logActivity(this.getClass().getName(), "GUI Initialization");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "GUI Initialization", ex);

		}

		// object properties
		try {
			this.createObjectProperties();

			ActivityLogger.logActivity(this.getClass().getName(), "Create object properties");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Create object properties", ex);

		}

		// shell properties
		try {
			this.createShellProperties();

			ActivityLogger.logActivity(this.getClass().getName(), "Create shell properties");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Create shell properties", ex);

		}

		// listeners
		try {
			this.createListeners();

			ActivityLogger.logActivity(this.getClass().getName(), "Create listeners");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Create listeners", ex);

		}

		// set the icon for the shell
		try {
			this.getShell().setImage(CustomWindow.APP_ICON);

			ActivityLogger.logActivity(this.getClass().getName(), "Set shell type (super class)");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Set shell type (super class)", ex);

		}
	}

	/**
	 * Updates the GUI based on the way the models have changed
	 */
	protected abstract void updateGUI(final Observable obs, final Object obj);

	/**
	 * Dispose all the components for this class
	 */
	public abstract void dispose();

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
