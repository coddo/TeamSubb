package com.coddotech.teamsubb;

import org.eclipse.swt.SWT;
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
	 * Clear memory from this class and its resources
	 */
	private Listener shellClosingListener = new Listener() {
		public void handleEvent(Event e) {
			// user classes
			jobManager.dispose();
			jobManager = null;
			settings.dispose();
			settings = null;
	
			// user controls
			label.dispose();
	
			// current display instance
			Display.getCurrent().dispose();
		}
	};
	
	/*
	 * Listens for when the shell for this class is displayed
	 */
	private Listener gadgetShownListener = new Listener(){
		public void handleEvent(Event e) {	
			label.setText(getShell().getLocation().x + "," + getShell().getLocation().y);
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
		this.getShell().setLocation(settings.getGadgetLocation());
		this.getShell().setSize(400, 400);
	
		// event handlers
		this.getShell().addListener(SWT.Show, gadgetShownListener);
		
		this.getShell().addListener(SWT.Close, shellClosingListener);
		
		// object packing
		label.pack();
	}
}
