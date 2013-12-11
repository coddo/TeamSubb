package com.coddotech.teamsubb.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.jobs.JobManager;
import com.coddotech.teamsubb.jobs.UserInformation;
import com.coddotech.teamsubb.settings.AppSettings;

/**
 * Windows gadget window.<br>
 * This is the main window for the application.
 * 
 * @author Coddo
 */
public class DesktopGadget extends CustomWindow {

	private JobManager jobManager;
	private AppSettings settings;
	private UserInformation userInfo;

	private Label label;

	/*
	 * Class constructor
	 */
	public DesktopGadget() {
		super();
		initializeComponents();
	}

	/**
	 * Clear the memory from this class and its resources
	 */
	private void dispose() {
		// user classes
		jobManager.dispose();
		jobManager = null;
		settings.dispose();
		settings = null;

		// user controls
		label.dispose();
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
	 * Read the user information (also contains job details and stuff)
	 */
	private void readUserDetails() {
	
	}

	/**
	 * Listens for when the shell (GUI) closes and clears memory from this class
	 * and its resources
	 */
	private Listener shellClosingListener = new Listener() {
		public void handleEvent(Event e) {
			dispose();
		}
	};

	/*
	 * Listens for when the shell (GUI) for this class is displayed
	 */
	private Listener gadgetShownListener = new Listener() {
		@Override
		public void handleEvent(Event e) {
			label.setText(getShell().getLocation().x + ","
					+ getShell().getLocation().y);
			label.pack();

			readUserDetails();
		}
	};

	/**
	 * Listens for location changes done to the shell of this class.<br>
	 * Practically, listens for when the form is moved into a new position
	 */
	private Listener gadgetPositionChangedListener = new Listener() {
		@Override
		public void handleEvent(Event e) {
			if (settings.getGadgetAutosaveLocation()) {
				settings.setGadgetLocation(getShell().getLocation());
			}
		}
	};

	/**
	 * Creates the graphical contents for application and defines all the fields
	 */
	private void initializeComponents () {
		// object definitions
		userInfo = new UserInformation();
		jobManager = new JobManager(this, this.userInfo);
		
		settings = new AppSettings();
		label = new Label(getShell(), SWT.None);
	
		// object properties
		label.setLocation(10, 10);
	
		// this window's properties
		this.getShell().setText("Team Subb");
		this.getShell().setLocation(settings.getGadgetLocation());
		this.getShell().setSize(400, 400);
	
		// listeners
		this.getShell().addListener(SWT.Show, gadgetShownListener);
		this.getShell().addListener(SWT.Close, shellClosingListener);
		this.getShell().addListener(SWT.Move, gadgetPositionChangedListener);
	
		// object packing
		label.pack();
	}
}
