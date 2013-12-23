package com.coddotech.teamsubb.main;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;

import com.coddotech.teamsubb.jobs.JobManager;
import com.coddotech.teamsubb.settings.AppSettings;

public class GadgetWindow extends CustomWindow implements Observer {

	private boolean[] jobs;
	private String[] userInfo;

	private AppSettings settings;
	private GadgetController controller;

	/**
	 * Class constructor
	 */
	public GadgetWindow(String[] userInfo, boolean[] jobs) {
		super();

		this.userInfo = userInfo;
		this.jobs = jobs;
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		// user classes
		settings.dispose();
		settings = null;

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
				userJobs[counter] = JobManager.DEFAULT_JOBS_INFO_HEADERS[i];
				counter++;
			}
		}

		return userJobs;
	}

	@Override
	public void update(Observable obs, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void performInitializations() {
		controller = new GadgetController(this);
		settings = new AppSettings();
	}

	@Override
	protected void createObjectProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("TeamSubb");

	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Show, controller.shellShownListener);
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
	}
}
