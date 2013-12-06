package com.coddotech.teamsubb.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.jobs.ConnectionManager;
import com.coddotech.teamsubb.settings.AppSettings;

/**
 * Windows gadget window.<br>
 * This is the main window for the application.
 * 
 * @author Coddo
 */
public class DesktopGadget extends CustomWindow {

	private ConnectionManager jobManager;
	private AppSettings settings;

	private Label label;

	/*
	 * Class constructor
	 */
	public DesktopGadget() {
		createContents();
	}

	/**
	 * Read the user information (also contains job details and stuff)
	 */
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
	 * Listens for when the shell (GUI) for this class is displayed
	 */
	private Listener gadgetShownListener = new Listener() {
		@Override
		public void handleEvent(Event e) {
			label.setText(getShell().getLocation().x + ","
					+ getShell().getLocation().y);
			label.pack();

			readUserDetails();
			jobManager.sendJobRequest();
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
	private void createContents() {
		// object definitions
		jobManager = new ConnectionManager(this);
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
		this.getShell().addListener(SWT.Move, gadgetPositionChangedListener);

		// object packing
		label.pack();
	}
}
