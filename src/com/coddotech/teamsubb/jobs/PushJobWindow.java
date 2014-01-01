package com.coddotech.teamsubb.jobs;

import java.util.Observable;
import java.util.Observer;

import com.coddotech.teamsubb.main.CustomWindow;

public class PushJobWindow extends CustomWindow implements Observer {

	private Job job;
	
	private PushJobController controller;
	
	public PushJobWindow(Job job) {
		this.job = job;
		
		this.initializeComponents();
	}
	
	public void dispose() {
		controller.dispose();
	}
	
	public PushJobController getController() {
		return this.controller;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void performInitializations() {
		controller = new PushJobController(this);
		
	}

	@Override
	protected void createObjectProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("Finish job");
		this.getShell().setSize(300, 300);
		this.placeToCenter();
	}

	@Override
	protected void createListeners() {
		// TODO Auto-generated method stub
		
	}
}
