package com.coddotech.teamsubb.main;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.coddotech.teamsubb.jobs.JobManager;
import com.coddotech.teamsubb.settings.AppSettings;

public class GadgetWindow extends CustomWindow implements Observer {

	public static final String[] DEFAULT_JOBS_INFO_HEADERS = { "Traducator",
			"Verificator", "Encoder", "Typesetter", "Manga", "Stiri",
			"Postator" };
	public static final String[] DEFAULT_USER_INFORMATION = { "Name", "Email",
			"Rank" };

	private boolean[] jobs;
	private String[] userInfo;

	private GadgetController controller;

	// this lets the app be repositioned with the value stored in the settings
	// file only once
	private boolean first = true;

	/**
	 * Class constructor
	 */
	public GadgetWindow(String[] userInfo, boolean[] jobs) {
		super();

		this.userInfo = userInfo;
		this.jobs = jobs;

		this.setShell(new Shell(Display.getCurrent(), SWT.NO_TRIM));

		this.initializeComponents();
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		// user classes
		controller.dispose();
		controller = null;

		// fields
		jobs = null;
		userInfo = null;
	}

	/**
	 * Retrieve the user name for the currently logged in staff
	 * 
	 * @return A String value
	 */
	public String getUserName() {
		return this.userInfo[0];
	}

	/**
	 * Get the user information for the currently logged in staff
	 * 
	 * @return A String collection
	 */
	public String[] getUserInfo() {
		return this.userInfo;
	}

	/**
	 * Retrieve the jobs that the user currently can do
	 * 
	 * @return A String collection with job type names
	 */
	public String[] getUserJobs() {
		int available = 0;
		for (int i = 0; i < jobs.length; i++) {
			if (jobs[i])
				available++;
		}

		int counter = 0;
		String[] userJobs = new String[available];
		for (int i = 0; i < jobs.length; i++) {
			if (jobs[i]) {
				userJobs[counter] = GadgetWindow.DEFAULT_JOBS_INFO_HEADERS[i];
				counter++;
			}
		}

		return userJobs;
	}

	int k = 0;

	@Override
	public void update(Observable obs, Object obj) {
		if (obs instanceof JobManager) {

		} else if (obs instanceof AppSettings) {
			String[] data = ((String) obj)
					.split(CustomWindow.NOTIFICATION_SEPARATOR);

			if (data[0].equals(AppSettings.MESSAGE_LOCATION) && first) {
				int x = Integer.parseInt(data[1].split(",")[0]);
				int y = Integer.parseInt(data[1].split(",")[1]);

				this.getShell().setLocation(x, y);
				first = false;
			} else if (data[0].equals(AppSettings.MESSAGE_SEARCH_INTERVAL)) {
				controller.setInterval(Integer.parseInt(data[1]));
			}
		}
	}

	@Override
	protected void performInitializations() {
		controller = new GadgetController(this);
	}

	@Override
	protected void createObjectProperties() {
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("TeamSubb");
		this.getShell().setSize(200, 200);
	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);
		this.getShell().addMouseListener(controller.shellClicked);
		this.getShell().addMouseMoveListener(controller.shellMoved);
	}
}
