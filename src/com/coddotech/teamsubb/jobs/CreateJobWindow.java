package com.coddotech.teamsubb.jobs;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.coddotech.teamsubb.main.CustomWindow;

public class CreateJobWindow extends CustomWindow implements Observer {

	private CreateJobController controller;

	public CreateJobWindow(JobManager model) {
		this.initializeComponents();

		controller.setModel(model);
	}

	public void dispose() {
		controller.dispose();
	}

	@Override
	public void update(Observable obs, Object obj) {
		String[] data = obj.toString().split(
				CustomWindow.NOTIFICATION_SEPARATOR);

		if (data[0].equals("create")) {
			MessageBox message;

			if (Boolean.parseBoolean(data[1])) {
				message = new MessageBox(this.getShell(), SWT.ICON_INFORMATION);
				message.setText("Success");
				message.setMessage("The job has been successfully created");
			} else {
				message = new MessageBox(this.getShell(), SWT.ICON_ERROR);
				message.setText("Error");
				message.setMessage("There was a problem while creating this job");
			}

			message.open();
		}

	}

	@Override
	protected void performInitializations() {
		controller = new CreateJobController(this);

	}

	@Override
	protected void createObjectProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("Create a new job");
		this.getShell().setSize(400, 400);
		this.placeToCenter();

	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);

	}
}
