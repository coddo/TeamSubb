package com.coddotech.teamsubb.jobs;

import java.util.Observable;
import java.util.Observer;

import com.coddotech.teamsubb.main.CustomWindow;

/**
 * Main window that is used by the user to manage his/her jobs and communicate
 * with the main server about them.
 * 
 * @author Coddo
 * 
 */
public class JobWindow extends CustomWindow implements Observer {

	public static final String[] DEFAULT_JOBS_INFO_HEADERS = { "Traducator",
			"Verificator", "Encoder", "Typesetter", "Manga", "Stiri",
			"Postator" };
	public static final String[] DEFAULT_USER_INFORMATION = { "Name", "Email",
			"Rank" };

	private String[] userInfo;
	private String[] userJobs;

	private JobController controller;

	public JobWindow(String[] userInfo, String[] userJobs) {
		this.userInfo = userInfo;
		this.userJobs = userJobs;

		this.initializeComponents();
	}

	public void dispose() {
		controller.dispose();
	}

	public JobController getController() {
		return this.controller;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void performInitializations() {
		controller = new JobController(this);

	}

	@Override
	protected void createObjectProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createShellProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createListeners() {
		// TODO Auto-generated method stub

	}
}
