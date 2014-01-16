package com.coddotech.teamsubb.gadget.gui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.coddotech.teamsubb.gadget.model.AnimationRenderer;
import com.coddotech.teamsubb.jobs.gui.JobWindow;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.settings.model.AppSettings;

/**
 * A window similar to a gadget that stays stationary on the workspace of the
 * user and is responsible to transmit announcements to the user and inform him
 * about different jobs that become available for him.<br>
 * 
 * The JobWindow and SettingsWindow can be opened from here.
 * 
 * @author Coddo
 * 
 */
public class GadgetWindow extends CustomWindow implements Observer {

	private static boolean[] jobs;
	private static String[] userInfo;

	private GadgetController controller;

	private Label imageContainer;

	// this lets the app be repositioned with the value stored in the settings
	// file only once
	private boolean first = true;

	/**
	 * Class constructor
	 */
	public GadgetWindow(String[] userInfo, boolean[] jobs) {
		super();

		GadgetWindow.userInfo = userInfo;
		GadgetWindow.jobs = jobs;

		this.setShell(new Shell(Display.getCurrent(), SWT.NO_TRIM | SWT.ON_TOP));

		this.initializeComponents();
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		try {

			// user classes
			controller.dispose();
			controller = null;

			// fields
			jobs = null;
			userInfo = null;

			this.logDispose();

		} catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	/**
	 * Retrieve the user name for the currently logged in staff
	 * 
	 * @return A String value
	 */
	public static String getUserName() {
		return userInfo[0];
	}

	/**
	 * Get the user information for the currently logged in staff
	 * 
	 * @return A String collection
	 */
	public static String[] getUserInfo() {
		return userInfo;
	}

	/**
	 * Retrieve the jobs that the user currently can do
	 * 
	 * @return A String collection with job type names
	 */
	public static String[] getUserJobs() {
		int available = 0;
		for (int i = 0; i < jobs.length; i++) {
			if (jobs[i])
				available++;
		}

		int counter = 0;
		String[] userJobs = new String[available];
		for (int i = 0; i < jobs.length; i++) {
			if (jobs[i]) {
				userJobs[counter] = JobWindow.DEFAULT_JOBS_INFO_HEADERS[i];
				counter++;
			}
		}

		return userJobs;
	}

	@Override
	public void update(Observable obs, Object obj) {
		if (!controller.isDisposed()) {

			if (obs instanceof AnimationRenderer) {
				imageContainer.setBackgroundImage((Image) obj);
			}

			else if (obs instanceof AppSettings) {

				String[] data = ((String) obj)
						.split(CustomWindow.NOTIFICATION_SEPARATOR);

				if (data[0].equals(AppSettings.MESSAGE_LOCATION) && first) {
					int x = Integer.parseInt(data[1].split(",")[0]);
					int y = Integer.parseInt(data[1].split(",")[1]);

					this.getShell().setLocation(x, y);
					first = false;

				}

				else if (data[0].equals(AppSettings.MESSAGE_SEARCH_INTERVAL)) {

					controller.setSearchInterval(Integer.parseInt(data[1]));
				}
			}

		}
	}

	@Override
	protected void performInitializations() {
		controller = new GadgetController(this);
		imageContainer = new Label(getShell(), SWT.NO_TRIM);
	}

	@Override
	protected void createObjectProperties() {
		imageContainer.setLocation(-10, -11);
		imageContainer.setSize(110, 110);
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("Gadget");
		this.getShell().setSize(200, 200);
	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);
		this.imageContainer.addMouseListener(controller.shellClicked);
		this.imageContainer.addMouseMoveListener(controller.shellMoved);
		this.getShell().addPaintListener(controller.shellPaint);
	}
}
