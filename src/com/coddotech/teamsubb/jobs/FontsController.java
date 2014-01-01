package com.coddotech.teamsubb.jobs;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class FontsController {
	
	private Job job;
	
	private FontsWindow view;
	
	public FontsController(FontsWindow view) {
		this.view = view;
	}
	
	public void dispose() {
		
	}
	
	public void setJob(Job job) {
		this.job = job;
	}
	
	public Listener shellClosingListener = new Listener() {
		
		@Override
		public void handleEvent(Event arg0) {
			view.dispose();
			
		}
	};
}
