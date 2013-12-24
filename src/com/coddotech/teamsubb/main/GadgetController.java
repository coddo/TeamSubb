package com.coddotech.teamsubb.main;

import java.awt.MouseInfo;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

import com.coddotech.teamsubb.jobs.JobManager;
import com.coddotech.teamsubb.jobs.JobsWindow;
import com.coddotech.teamsubb.settings.AppSettings;
import com.coddotech.teamsubb.settings.AppSettingsWindow;

public class GadgetController {

	private GadgetWindow gadget;

	private JobManager jobs;
	private JobsWindow jobsWindow;

	private AppSettings settings;
	private AppSettingsWindow settingsWindow;

	// 1 min = 60000 ms
	int interval = AppSettings.DEFAULT_SEARCH_INTERVAL * 60000;

	boolean isDisposed = false;

	// data used for moving the form around
	boolean move = false;
	int x;
	int y;

	/**
	 * Class constructor
	 * 
	 * @param view
	 *            The view which uses this controller
	 */
	public GadgetController(GadgetWindow view) {
		this.gadget = view;

		// initialize the components
		this.initializeController();

		// start the timer in order for it to search for new jobs
		Display.getCurrent().timerExec(this.interval, timer);
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		this.isDisposed = true;

		timer = null;
		gadget = null;

		jobsWindow = null;
		jobs.dispose();

		settingsWindow = null;
		settings.dispose();
	}

	/**
	 * Set the interval in which
	 * 
	 * @param mins
	 */
	public void setInterval(int mins) {
		this.interval = mins * 60000;
	}

	/**
	 * Timer used to search for new jobs
	 */
	Runnable timer = new Runnable() {

		@Override
		public void run() {
			jobs.findJobs();

			if (!isDisposed)
				Display.getCurrent().timerExec(interval, this);
		}

	};

	/**
	 * Listener for when the gadget is clicked. <br>
	 * Left double-click opens the JobsWindow while left double-click opens the
	 * ApplicationSettings. The left click is used for moving the window on the
	 * screen
	 */
	public MouseListener shellClicked = new MouseListener() {

		@Override
		public void mouseUp(MouseEvent e) {
			if (e.button == 1)
				move = false;
		}

		@Override
		public void mouseDown(MouseEvent e) {
			if (e.button == 1) {
				move = true;

				x = MouseInfo.getPointerInfo().getLocation().x;
				y = MouseInfo.getPointerInfo().getLocation().y;
			}
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			if (e.button == 1) {
				jobsWindow = new JobsWindow(gadget.getUserInfo(),
						gadget.getUserJobs());
				jobsWindow.getController().setModel(jobs);
				jobs.addObserver(jobsWindow);
				jobsWindow.open();
			} else if (e.button == 3) {
				settingsWindow = new AppSettingsWindow();
				settingsWindow.getController().setModel(settings);
				settings.addObserver(settingsWindow);
				settingsWindow.open();
			}
			
			//don't let the window be moved because it was a double-click
			move = false;
		}
	};

	/**
	 * Detects when the mouse is moved over the shell. <br>
	 * This helps with the repositioning of the application by the user.
	 */
	public MouseMoveListener shellMoved = new MouseMoveListener() {

		@Override
		public void mouseMove(MouseEvent arg0) {
			if (move) {
				int difx = MouseInfo.getPointerInfo().getLocation().x - x;
				int dify = MouseInfo.getPointerInfo().getLocation().y - y;

				int curx = gadget.getShell().getLocation().x;
				int cury = gadget.getShell().getLocation().y;

				gadget.getShell().setLocation(curx + difx, cury + dify);

				x = MouseInfo.getPointerInfo().getLocation().x;
				y = MouseInfo.getPointerInfo().getLocation().y;
			}
		}
	};

	/**
	 * Listener for when the window is shown. <br>
	 * Reads the settings and updates the interface.
	 */
	public Listener shellShownListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			// read the app settings to get the position for the gadget
			settings.readSettings();
		}

	};

	/**
	 * Listener for when the window is closed. <br>
	 * Saves the current position for the app and disposes of all the window's
	 * components
	 */
	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			// save the position of the gadget is necessary
			if (settings.isGadgetAutosaveLocation()) {
				settings.setGadgetLocation(gadget.getShell().getLocation());
				settings.commitChangesToFile();
			}

			gadget.dispose();
		}
	};

	/**
	 * Creates all the components for this class
	 */
	private void initializeController() {
		// create the models
		jobs = new JobManager(this.gadget.getUserName(),
				this.gadget.getUserJobs());
		settings = new AppSettings();

		// set the observers for the models
		settings.addObserver(this.gadget);
		jobs.addObserver(this.gadget);
	}

}
