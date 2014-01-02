package com.coddotech.teamsubb.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ApplicationInformation extends CustomWindow {

	public ApplicationInformation() {
		this.setShell(new Shell(Display.getCurrent(), SWT.APPLICATION_MODAL | SWT.SHELL_TRIM));
		
		this.initializeComponents();
	}
	
	@Override
	protected void performInitializations() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createObjectProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("About TeamSubb");
		this.getShell().setSize(300, 300);
		this.placeToCenter();
		
	}

	@Override
	protected void createListeners() {
		// TODO Auto-generated method stub
		
	}
	
}
