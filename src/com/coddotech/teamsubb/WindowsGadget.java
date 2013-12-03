package com.coddotech.teamsubb;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Windows gadget window.<br>
 * This is the main window for the application.
 * 
 * @author Coddo
 */
public class WindowsGadget extends CustomWindow {
	
	private CommunicationManager jobManager;
	private Label label;
	private AppSettings settings;
	
	/*
	 * Class constructor
	 */
	public WindowsGadget() {
		createContents();
	}

	/**
	 * Read the user information (also contains job details and stuff)
	 * */
	private void readUserDetails() {

	}

	/**
	 * Notify the user with visual effects and messages that a new job is
	 * available for him
	 */
	public void startNotifications() {

	}

	/**
	 * Stop all currently active notifications
	 * */
	public void stopNotifications() {

	}

	/**
	 * Read the application's position preference from the its settings
	 * 
	 * @return An org.eclipse.swt.graphics.Point value indicating 
	 * the default position that the user has set for this window
	 */
	private Point readLocationSettings() {
		return new Point(200, 200);
	}
	
	/**
	 * Clear memory from this class and its resources
	 */
	private Listener disposeListener = new Listener() {
		public void handleEvent(Event e) {
			// user classes
			jobManager.dispose();
			jobManager = null;
			settings.dispose();
			settings = null;
	
			// user controls
			label.dispose();
	
			// current instance
			Display.getCurrent().dispose();
		}
	};
	/*
	 * Listens for when the shell for this class is displayed
	 */
	private Listener gadgetShownListener = new Listener(){
		public void handleEvent(Event e) {
			Point location = readLocationSettings();
			getShell().setLocation(location);
	
			label.setText(location.x + "," + location.y);
			label.pack();
			
			readUserDetails();
			jobManager.sendJobRequest();
		}
	};

	/**
	 * Creates the graphical contents for application and defines all the fields
	 */
	private void createContents() {
		// object definitions
		jobManager = new CommunicationManager(this);
		settings = new AppSettings();
		label = new Label(getShell(), SWT.None);
	
		// object properties
		label.setLocation(10, 10);
	
		// this window's properties
		this.getShell().setText("Team Subb");
	
		// event handlers
		this.getShell().addListener(SWT.Show, gadgetShownListener);
		
		this.getShell().addListener(SWT.Close, disposeListener);
		
		// object packing
		label.pack();
	}
}
