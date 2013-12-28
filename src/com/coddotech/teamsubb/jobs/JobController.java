package com.coddotech.teamsubb.jobs;

import com.coddotech.teamsubb.jobs.JobWindow;

/**
 * Controller class used by the JobWindow in order to complete the job actions
 * that are requested by the user
 * 
 * @author Coddo
 * 
 */
public class JobController {

	private JobWindow view;
	private JobManager model;
	
	public JobController() {
		
	}

	public JobController(JobWindow view) {
		this.view = view;
	}

	public void dispose() {
		model.deleteObserver(this.view);
	}

	public void setModel(JobManager model) {
		this.model = model;
		this.model.addObserver(this.view);
	}

}
