package com.coddotech.teamsubb.jobs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.coddotech.teamsubb.main.CustomWindow;

public class FontsWindow extends CustomWindow{

	private FontsController controller;
	
	public FontsWindow(Job job) {
		this.setShell(new Shell(Display.getCurrent(), SWT.APPLICATION_MODAL | SWT.SHELL_TRIM));
		
		this.initializeComponents();
		
		controller.setJob(job);
	}
	
	public void dispose() {
		controller.dispose();
	}

	@Override
	protected void performInitializations() {
		controller = new FontsController(this);
		
	}

	@Override
	protected void createObjectProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("Edit fonts");
		this.getShell().setSize(300, 300);
		this.placeToCenter();
		
	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		
	}
}
