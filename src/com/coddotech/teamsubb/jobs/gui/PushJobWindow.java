package com.coddotech.teamsubb.jobs.gui;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.chat.model.StaffManager;
import com.coddotech.teamsubb.jobs.model.Job;
import com.coddotech.teamsubb.main.CustomWindow;

public class PushJobWindow extends CustomWindow {

	private Job job;

	private PushJobController controller;

	private Composite panel;

	private Label nameLabel;
	private Text name;
	private Label typeLabel;
	private Combo type;
	private Label commentsLabel;
	private Text comments;
	private Label nextStaffLabel;
	private Combo nextStaff;

	private Button finish;
	private Button cancel;

	/**
	 * Class constructor
	 * 
	 * @param job
	 *            The Job instance representing the job that needs to be pushed
	 *            back to the server
	 */
	public PushJobWindow(Job job) {
		this.job = job;

		this.setShell(new Shell(Display.getDefault(), SWT.SHELL_TRIM));

		this.initializeComponents();
	}

	@Override
	public void dispose() {
		try {
			controller.dispose();

			nameLabel.dispose();
			name.dispose();

			typeLabel.dispose();
			type.dispose();

			commentsLabel.dispose();
			comments.dispose();

			nextStaffLabel.dispose();
			nextStaff.dispose();

			finish.dispose();
			cancel.dispose();

			panel.dispose();

			this.logDispose();

		}
		catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	/**
	 * Get the controller used by this class
	 * 
	 * @return A JobPushController instance
	 */
	public PushJobController getController() {
		return this.controller;
	}

	/**
	 * Get the ID of the job
	 * 
	 * @return An integer value
	 */
	public int getID() {
		return this.job.getID();
	}

	/**
	 * Get the job type entered by the user
	 * 
	 * @return An integer value
	 */
	public int getType() {
		return this.type.getSelectionIndex();
	}

	/**
	 * Get the next staff member selected by the user
	 * 
	 * @return A String value
	 */
	public String getNextStaff() {
		return this.nextStaff.getText().split(":")[0];
	}

	/**
	 * Get the comments entered by the user
	 * 
	 * @return A String value
	 */
	public String getComments() {
		return this.comments.getText();
	}

	/**
	 * Verify if the fields are not empty or contain invalid data
	 * 
	 * @return A logical value
	 */
	public boolean verifyFields() {
		MessageBox message = new MessageBox(this.getShell(), SWT.ICON_ERROR);

		message.setText("Empty fields");
		message.setMessage("There cannot be any empty fields !");

		if (type.getText() == null || type.getText().equals("")) {
			message.open();

			return false;
		}

		if (nextStaff.getText() == null || nextStaff.getText().equals("")) {
			message.open();

			return false;
		}

		return true;
	}

	@Override
	protected void updateGUI(final Observable obs, final Object obj) {

		Runnable update = new Runnable() {

			@Override
			public void run() {
				String[] data = obj.toString().split(CustomWindow.NOTIFICATION_SEPARATOR);

				if (data[0].equals("push")) {
					MessageBox message;

					if (Boolean.parseBoolean(data[1])) {
						message = new MessageBox(getShell(), SWT.ICON_INFORMATION);
						message.setText("Success");
						message.setMessage("The job has been successfully sent back to the server !");

						message.open();

						// close the window on successful push
						close();
					}

					else {
						message = new MessageBox(getShell(), SWT.ERROR);
						message.setText("Error");
						message.setMessage("The job could not be finished !\n The server may have refused your request...");

						message.open();
					}

				}
			}
		};

		Display.getDefault().syncExec(update);
	}

	@Override
	protected void performInitializations() {
		controller = new PushJobController(this);

		panel = new Composite(this.getShell(), SWT.BORDER);

		nameLabel = new Label(panel, SWT.None);
		name = new Text(panel, SWT.READ_ONLY);

		typeLabel = new Label(panel, SWT.None);
		type = new Combo(panel, SWT.READ_ONLY);

		commentsLabel = new Label(panel, SWT.None);
		comments = new Text(panel, SWT.BORDER);

		nextStaffLabel = new Label(panel, SWT.None);
		nextStaff = new Combo(panel, SWT.READ_ONLY);

		finish = new Button(this.getShell(), SWT.PUSH);
		cancel = new Button(this.getShell(), SWT.PUSH);
	}

	@Override
	protected void createObjectProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		panel.setLayout(layout);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		panel.pack();

		nameLabel.setFont(CustomWindow.BOLD_FONT);
		nameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		nameLabel.setText("Job name:");
		nameLabel.pack();

		name.setFont(CustomWindow.DEFAULT_FONT);
		name.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		name.setText(this.job.getName());

		typeLabel.setFont(CustomWindow.BOLD_FONT);
		typeLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		typeLabel.setText("Job type:");
		typeLabel.pack();

		type.setFont(CustomWindow.DEFAULT_FONT);
		type.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		for (String jobType : Job.DEFAULT_JOB_TYPES)
			type.add(jobType);

		commentsLabel.setFont(CustomWindow.BOLD_FONT);
		commentsLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		commentsLabel.setText("Comments:");
		commentsLabel.pack();

		comments.setFont(CustomWindow.DEFAULT_FONT);
		comments.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		nextStaffLabel.setFont(CustomWindow.BOLD_FONT);
		nextStaffLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		nextStaffLabel.setText("Next staff member:");
		nextStaffLabel.pack();

		nextStaff.setFont(CustomWindow.DEFAULT_FONT);
		nextStaff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		nextStaff.add(Job.DEFAULT_NEXT_STAFF);
		nextStaff.setItems(StaffManager.getStaffList());
		nextStaff.add(Job.DEFAULT_NEXT_STAFF, 0);
		nextStaff.select(0);

		finish.setFont(CustomWindow.DEFAULT_FONT);
		finish.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));
		finish.setText("Finish job");
		finish.pack();

		cancel.setFont(CustomWindow.DEFAULT_FONT);
		cancel.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		cancel.setText("Cancel");
		cancel.pack();
	}

	@Override
	protected void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;

		this.getShell().setLayout(layout);
		this.getShell().setText("Finish job");
		this.getShell().setSize(400, 310);

		this.placeToCenter();
	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);

		this.finish.addSelectionListener(controller.finishButtonClicked);
		this.cancel.addSelectionListener(controller.cancelButtonClicked);
	}
}
