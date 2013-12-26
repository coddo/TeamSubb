package com.coddotech.teamsubb.jobs;

public class JobController {

	private JobWindow view;
	private JobManager model;
	
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
