package com.coddotech.teamsubb.jobs;

import java.util.ArrayList;

import com.coddotech.teamsubb.main.DesktopGadget;

/**
 * Class used for realizing the communication between this client and the target
 * server for job management (@www.anime4fun.ro).
 * 
 * The server implementation is done separately in python and javascript.
 * 
 * @author Coddo
 * 
 */
public class JobManager {
	
	private static final String SEPARATOR_FIELDS = "&?&";
	private static final String SEPARATOR_JOBS = "¬|¬";
	
	private ArrayList<Job> jobs;
	
	private DesktopGadget _gadget;

	/**
	 * Main class construcotr
	 * 
	 * @param gadget
	 *            The main form for this application
	 */
	public JobManager(DesktopGadget gadget, UserInformation userInfo) {
		this._gadget = gadget;
		
		jobs = new ArrayList<Job>();
	} 

	/**
	 * Clear memory from this class and its resources
	 */
	public void dispose() {
		// other classes
		_gadget = null;
	}

	/*
	 * Send a request to the server in order to receive a job if any available
	 */
	public void sendJobRequest() {
		//CODE HERE
	}

	/*
	 * Accept a certain job
	 */
	public void acceptJob(Job job) {

	}

	/*
	 * Decline a certain job. This also makes the selected job unavailable for
	 * the user
	 */
	public void declineJob(Job job) {

	}

	/*
	 * Notify the user that he/she has a job available for her
	 */
	private void notifyJobsAvailable() {
		_gadget.startNotifications();
	}
}
