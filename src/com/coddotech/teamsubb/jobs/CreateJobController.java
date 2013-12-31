package com.coddotech.teamsubb.jobs;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class CreateJobController {
	
	private CreateJobWindow view;
	private JobManager model;
	
	public CreateJobController(CreateJobWindow view) {
		this.view = view;
	}
	
	public void dispose() {
		model.deleteObserver(this.view);
	}
	
	public void setModel(JobManager model) {
		this.model = model;
	}
	
	public Listener shellClosingListener = new Listener() {
		
		@Override
		public void handleEvent(Event arg0) {
			view.dispose();
			
		}
	};
	
}