package com.coddotech.teamsubb.main;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

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
	
	private Button test;

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
		//user classes
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
			
		}
	}

	@Override
	protected void performInitializations() {
		controller = new GadgetController(this);
		
		test = new Button(this.getShell(), SWT.PUSH);
	}

	@Override
	protected void createObjectProperties() {
		test.setText("hello");
		test.setLocation(10, 10);
		test.pack();
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("TeamSubb");
		this.getShell().setSize(100, 100);
	}

	@Override
	protected void createListeners() {
		test.addSelectionListener(controller.settingsClicked);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
	}
}
