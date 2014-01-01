package com.coddotech.teamsubb.jobs;

public class PushJobController {

	private PushJobWindow view;
	
	private JobManager model;
	
	public PushJobController(PushJobWindow view) {
		this.view = view;
	}
	
	public void dispose() {
		model.deleteObserver(view);
	}
	
	public void setModel(JobManager model) {
		this.model = model;
	}
}
