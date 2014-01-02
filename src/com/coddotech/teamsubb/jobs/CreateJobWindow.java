package com.coddotech.teamsubb.jobs;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.connection.ConnectionManager;
import com.coddotech.teamsubb.main.CustomWindow;

public class CreateJobWindow extends CustomWindow implements Observer {

	private CreateJobController controller;

	private Composite panel;

	private Label nameLabel;
	private Text name;
	private Label typeLabel;
	private Combo type;
	private Label commentsLabel;
	private Text comments;
	private Label nextStaffLabel;
	private Combo nextStaff;
	private Label subLabel;
	private Text sub;
	private Label fontsLabel;
	private List fonts;

	private Button browseSubButton;
	private Button browseFontsButton;

	private Button create;
	private Button cancel;

	public CreateJobWindow(JobManager model) {
		this.setShell(new Shell(Display.getCurrent(), SWT.SHELL_TRIM));

		this.initializeComponents();

		controller.setModel(model);
	}

	public void dispose() {
		controller.dispose();

		nameLabel.dispose();
		name.dispose();

		typeLabel.dispose();
		type.dispose();

		commentsLabel.dispose();
		comments.dispose();

		nextStaffLabel.dispose();
		nextStaff.dispose();

		subLabel.dispose();
		sub.dispose();

		fontsLabel.dispose();
		fonts.dispose();

		browseSubButton.dispose();
		browseFontsButton.dispose();

		create.dispose();
		cancel.dispose();

		panel.dispose();
	}

	public String getName() {
		return this.name.getText();
	}

	public int getType() {
		return this.type.getSelectionIndex();
	}

	public String getComments() {
		return this.comments.getText();
	}

	public String getNextStaff() {
		return this.nextStaff.getText().split(":")[0];
	}

	public String getSub() {
		return this.sub.getText();
	}

	public void setSub(String sub) {
		this.sub.setText(sub);
	}

	public String[] getFonts() {
		return this.fonts.getItems();
	}

	public void setFonts(String[] fonts) {
		this.fonts.removeAll();

		this.fonts.setItems(fonts);
	}

	public boolean verifFields() {
		MessageBox message = new MessageBox(this.getShell(), SWT.ICON_ERROR);
		message.setText("Empty fields");
		message.setMessage("There cannot be any empty fields except the font list!");

		boolean empty = false;

		if (name.getText() == null || name.getText().equals(""))
			empty = true;

		if (type.getText() == null || type.getText().equals(""))
			empty = true;

		if (comments.getText() == null || comments.getText().equals(""))
			empty = true;

		if (nextStaff.getText() == null || nextStaff.getText().equals(""))
			empty = true;

		if (sub.getText() == null || sub.getText().equals(""))
			empty = true;

		if (empty) {
			message.open();
			return false;
		}

		return true;
	}

	public void deleteSelectedFonts() {
		this.fonts.remove(this.fonts.getSelectionIndices());
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

		panel = new Composite(this.getShell(), SWT.BORDER);

		nameLabel = new Label(panel, SWT.None);
		name = new Text(this.panel, SWT.BORDER);

		typeLabel = new Label(panel, SWT.None);
		type = new Combo(panel, SWT.READ_ONLY);

		commentsLabel = new Label(panel, SWT.None);
		comments = new Text(panel, SWT.BORDER);

		nextStaffLabel = new Label(panel, SWT.None);
		nextStaff = new Combo(panel, SWT.READ_ONLY);

		subLabel = new Label(panel, SWT.None);
		sub = new Text(panel, SWT.BORDER);

		browseSubButton = new Button(panel, SWT.PUSH);

		fontsLabel = new Label(panel, SWT.None);
		fonts = new List(panel, SWT.BORDER | SWT.MULTI);

		browseFontsButton = new Button(panel, SWT.PUSH);

		create = new Button(this.getShell(), SWT.PUSH);
		cancel = new Button(this.getShell(), SWT.PUSH);
	}

	@Override
	protected void createObjectProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;

		panel.setLayout(layout);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		panel.pack();

		nameLabel.setFont(CustomWindow.DEFAULT_FONT);
		nameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		nameLabel.setText("Job name:");
		nameLabel.pack();

		name.setFont(CustomWindow.DEFAULT_FONT);
		name.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		typeLabel.setFont(CustomWindow.DEFAULT_FONT);
		typeLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		typeLabel.setText("Job type:");
		typeLabel.pack();

		type.setFont(CustomWindow.DEFAULT_FONT);
		type.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		for (String jobType : Job.DEFAULT_JOB_TYPES)
			type.add(jobType);

		commentsLabel.setFont(CustomWindow.DEFAULT_FONT);
		commentsLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		commentsLabel.setText("Job description/comments: ");
		commentsLabel.pack();

		comments.setFont(CustomWindow.DEFAULT_FONT);
		comments.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2,
				1));

		nextStaffLabel.setFont(CustomWindow.DEFAULT_FONT);
		nextStaffLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		nextStaffLabel.setText("Next staff member:");
		nextStaffLabel.pack();

		nextStaff.setFont(CustomWindow.DEFAULT_FONT);
		nextStaff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				2, 1));
		nextStaff.add(Job.DEFAULT_NEXT_STAFF);
		for (String staff : this.getStaffList())
			nextStaff.add(staff);

		subLabel.setFont(CustomWindow.DEFAULT_FONT);
		subLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		subLabel.setText("Sub file:");
		subLabel.pack();

		sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		sub.setFont(CustomWindow.DEFAULT_FONT);

		browseSubButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false, 1, 1));
		browseSubButton.setText("...");
		browseSubButton.pack();

		fontsLabel.setFont(CustomWindow.DEFAULT_FONT);
		fontsLabel
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		fontsLabel.setText("Font files:");
		fontsLabel.pack();

		fonts.setFont(CustomWindow.DEFAULT_FONT);
		fonts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		browseFontsButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false, 1, 1));
		browseFontsButton.setText("...");
		browseFontsButton.pack();

		create.setFont(CustomWindow.DEFAULT_FONT);
		create.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));
		create.setText("Create job");
		create.pack();

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
		this.getShell().setText("Create a new job");
		this.getShell().setSize(800, 600);
		this.placeToCenter();

	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);

		cancel.addSelectionListener(controller.cancelClicked);
		create.addSelectionListener(controller.createClicked);

		browseSubButton.addSelectionListener(controller.browseSubButtonClicked);
		browseFontsButton
				.addSelectionListener(controller.browseFontsButtonClicked);
		
		fonts.addKeyListener(controller.fontsKeyListener);
	}

	private String[] getStaffList() {
		if (this.isConnected(false)) {
			String[] staffData = ConnectionManager.sendStaffRequest().split(
					JobManager.SEPARATOR_JOBS);

			for (int i = 0; i < staffData.length; i++) {
				staffData[i] = staffData[i].replaceAll(
						JobManager.SEPARATOR_FIELDS, " | ");
				staffData[i] = staffData[i].replaceFirst(Pattern.quote(" |"),
						":");
			}

			return staffData;
		}

		return null;
	}
}
