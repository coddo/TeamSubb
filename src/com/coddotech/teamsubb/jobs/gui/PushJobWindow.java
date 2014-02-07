package com.coddotech.teamsubb.jobs.gui;

import java.io.File;
import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.chat.model.StaffManager;
import com.coddotech.teamsubb.jobs.model.Job;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.notifications.gui.PopUpMessages;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;

public class PushJobWindow extends CustomWindow {

	private Job job;

	private PushJobController controller;

	private Composite panel;

	private Label nameLabel;
	private Label typeLabel;
	private Label commentsLabel;
	private Label nextStaffLabel;

	private Text name;
	private Text comments;
	private Text subFile;
	private Text torrent;

	private Combo type;
	private Combo nextStaff;

	private Button torrentCheck;
	private Button finish;
	private Button cancel;
	private Button subFileCheck;

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

			torrentCheck.dispose();
			torrent.dispose();

			commentsLabel.dispose();
			comments.dispose();

			nextStaffLabel.dispose();
			nextStaff.dispose();

			finish.dispose();
			cancel.dispose();

			subFile.dispose();
			subFileCheck.dispose();

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
	 * Get the torrent link entered by the user
	 * 
	 * @return A String value
	 */
	public String getTorrent() {
		return this.torrent.getText();
	}

	/**
	 * Set the path to the selected sub file.
	 * 
	 * Enter an empty string in order to disable the textbox
	 * 
	 * @param filePath
	 *            A String value
	 */
	public void setSubFile(String filePath) {
		this.subFile.setText(filePath);

		this.subFile.setEnabled(!filePath.equals(""));
		this.subFileCheck.setSelection(!filePath.equals(""));
	}

	/**
	 * Get the newly selected sub file.
	 * 
	 * @return A File entity or null if the user didn't select a new file
	 */
	public File getSubFile() {
		if (!this.subFileCheck.getSelection())
			return null;

		return new File(this.subFile.getText());
	}

	/**
	 * Verify if the user has decided to use a new subfile
	 * 
	 * @return A Logical value
	 */
	public boolean hasNewSubFile() {
		return this.subFileCheck.getSelection();
	}
	
	/**
	 * Verify if the user has decided to use a new torrent link
	 * 
	 * @return A Logical value
	 */
	public boolean hasNewTorrent() {
		return this.torrentCheck.getSelection();
	}

	/**
	 * Change the state of the torrent TextBox according to the state of its corresponding checkbox
	 */
	public void changeTorrentState() {
		this.torrent.setEnabled(this.torrentCheck.getSelection());

		if (!torrent.getEnabled())
			torrent.setText("");
	}

	@Override
	protected void updateGUI(final Observable obs, final Object obj) {

		Runnable update = new Runnable() {

			@Override
			public void run() {
				NotificationEntity notif = (NotificationEntity) obj;

				if (notif.getMessage().equals(JobManager.JOB_PUSH)) {

					if (notif.getBoolean()) {
						PopUpMessages.getInstance().jobPushSuccess();

						// close the window on successful push
						close();
					}

					else {
						PopUpMessages.getInstance().jobPushError();

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
		name = new Text(panel, SWT.READ_ONLY | SWT.BORDER);

		typeLabel = new Label(panel, SWT.None);
		type = new Combo(panel, SWT.READ_ONLY);

		torrentCheck = new Button(panel, SWT.CHECK);
		torrent = new Text(panel, SWT.BORDER);

		commentsLabel = new Label(panel, SWT.None);
		comments = new Text(panel, SWT.BORDER);

		nextStaffLabel = new Label(panel, SWT.None);
		nextStaff = new Combo(panel, SWT.READ_ONLY);

		subFileCheck = new Button(panel, SWT.CHECK);
		subFile = new Text(panel, SWT.BORDER);

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
		typeLabel.setText("*Job type:");
		typeLabel.pack();

		type.setFont(CustomWindow.DEFAULT_FONT);
		type.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		for (String jobType : Job.DEFAULT_JOB_TYPES)
			type.add(jobType);
		type.select(0); // make the first item selected by default

		torrentCheck.setFont(CustomWindow.BOLD_FONT);
		torrentCheck.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		torrentCheck.setText("Change the torrent link (leave unchecked for same torrent)");
		torrentCheck.pack();

		torrent.setFont(CustomWindow.DEFAULT_FONT);
		torrent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		torrent.setEnabled(false);

		commentsLabel.setFont(CustomWindow.BOLD_FONT);
		commentsLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		commentsLabel.setText("Comments:");
		commentsLabel.pack();

		comments.setFont(CustomWindow.DEFAULT_FONT);
		comments.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		nextStaffLabel.setFont(CustomWindow.BOLD_FONT);
		nextStaffLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		nextStaffLabel.setText("*Next staff member:");
		nextStaffLabel.pack();

		nextStaff.setFont(CustomWindow.DEFAULT_FONT);
		nextStaff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		nextStaff.add(Job.DEFAULT_NEXT_STAFF);
		nextStaff.setItems(StaffManager.fetchFormatedStaffList());
		nextStaff.add(Job.DEFAULT_NEXT_STAFF, 0);
		nextStaff.select(0);

		subFileCheck.setFont(CustomWindow.BOLD_FONT);
		subFileCheck.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		subFileCheck.setText("Change the subfile (leave unchecked for same sub)");
		subFileCheck.pack();

		subFile.setFont(CustomWindow.DEFAULT_FONT);
		subFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		subFile.setEnabled(false);

		finish.setFont(CustomWindow.DEFAULT_FONT);
		finish.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));
		finish.setText("Finish job");
		finish.pack();

		cancel.setFont(CustomWindow.DEFAULT_FONT);
		cancel.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		cancel.setText("Close");
		cancel.pack();
	}

	@Override
	protected void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;

		this.getShell().setLayout(layout);
		this.getShell().setText("Finish job");
		this.getShell().setSize(485, 405);

		this.placeToCenter();
	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);

		this.finish.addSelectionListener(controller.finishButtonClicked);
		this.cancel.addSelectionListener(controller.cancelButtonClicked);
		this.subFileCheck.addSelectionListener(controller.subFileChecked);
		this.torrentCheck.addSelectionListener(controller.torrentChecked);
	}
}
