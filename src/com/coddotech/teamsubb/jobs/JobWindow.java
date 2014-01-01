package com.coddotech.teamsubb.jobs;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.coddotech.teamsubb.jobs.JobController;
import com.coddotech.teamsubb.main.CustomWindow;

/**
 * Main window that is used by the user to manage his/her jobs and communicate
 * with the main server about them.
 * 
 * @author Coddo
 * 
 */
public class JobWindow extends CustomWindow implements Observer {

	public static final String[] DEFAULT_JOBS_INFO_HEADERS = { "Traducator",
			"Verificator", "Encoder", "Typesetter", "Manga", "Stiri",
			"Postator" };

	private static final Color COLOR_ACCEPTED = Display.getCurrent()
			.getSystemColor(SWT.COLOR_GREEN);
	private static final Color COLOR_ACCEPTABLE = Display.getCurrent()
			.getSystemColor(SWT.COLOR_YELLOW);
	private static final Color COLOR_IMPORTANT = Display.getCurrent()
			.getSystemColor(SWT.COLOR_MAGENTA);

	// auxiliary data
	private String[] tempUserInfo;
	private String[] tempUserJobs;

	// font used by some components
	private Font defaultFont;

	// controller used for interpreting user actions
	private JobController controller;

	// window objects
	private Group userInfoGroup;
	private Group userJobsGroup;
	private Group jobsGroup;
	private Group jobInfoGroup;
	private Group helpChartGroup;
	private Menu menuBar;

	// menu bar objects
	private MenuItem applicationMenuItem;
	private MenuItem aboutMenuItem;
	private MenuItem jobsMenuItem;

	// application menu objects
	private Menu applicationMenu;
	private MenuItem openSettingsMenuItem;
	private MenuItem closeWindowMenuItem;
	private MenuItem exitApplicationMenuItem;

	// jobs menu objects
	private Menu jobsMenu;
	private MenuItem createJobMenuItem;
	private MenuItem refreshJobListMenuItem;

	// actions menu objects
	private Menu actionsMenu;
	private MenuItem acceptJobMenuItem;
	private MenuItem cancelJobMenuItem;
	private MenuItem finishJobMenuItem;
	private MenuItem endJobMenuItem;
	private MenuItem openJobDirectoryMenuItem;

	// user information
	private Label userNameLabel;
	private Label userEmailLabel;
	private Label userRankLabel;
	private Label[] userJobsLabels;

	// job list (table)
	private Table jobsList;

	// job information
	private Label jobTypeLabel;
	private Label jobType;
	private Label jobPreviousStaffLabel;
	private Label jobPreviousStaff;
	private Label jobIntendedToLabel;
	private Label jobIntendedTo;
	private Label jobBookedByLabel;
	private Label jobBookedBy;

	// help chart objects
	private Label itemAcceptedColor;
	private Label itemAcceptedLabel;
	private Label itemAcceptableColor;
	private Label itemAcceptableLabel;
	private Label itemImportantColor;
	private Label itemImportantLabel;

	/**
	 * Class constructor
	 * 
	 * @param userInfo
	 *            Information about the user: name, rank, email
	 * @param userJobs
	 *            Information about the jobs that the user can take
	 */
	public JobWindow(String[] userInfo, String[] userJobs) {
		super();
		tempUserInfo = userInfo;
		tempUserJobs = userJobs;

		// make the shell resizable
		this.setShell(new Shell(Display.getCurrent(), SWT.SHELL_TRIM));

		this.initializeComponents();
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		// controller
		controller.dispose();

		// job information
		jobTypeLabel.dispose();
		jobType.dispose();
		jobPreviousStaffLabel.dispose();
		jobPreviousStaff.dispose();
		jobIntendedToLabel.dispose();
		jobIntendedTo.dispose();
		jobBookedByLabel.dispose();
		jobBookedBy.dispose();

		// application menu objects
		openSettingsMenuItem.dispose();
		closeWindowMenuItem.dispose();
		exitApplicationMenuItem.dispose();
		applicationMenu.dispose();

		// jobs menu objects
		createJobMenuItem.dispose();
		refreshJobListMenuItem.dispose();
		jobsMenu.dispose();

		// actions menu objects
		acceptJobMenuItem.dispose();
		cancelJobMenuItem.dispose();
		finishJobMenuItem.dispose();
		endJobMenuItem.dispose();
		actionsMenu.dispose();
		openJobDirectoryMenuItem.dispose();

		// menu bar objects
		applicationMenuItem.dispose();
		jobsMenuItem.dispose();
		aboutMenuItem.dispose();
		menuBar.dispose();

		// user information objects
		userNameLabel.dispose();
		userEmailLabel.dispose();
		userRankLabel.dispose();

		// jobs list
		if (jobsList.getItemCount() > 0) {
			jobsList.clearAll();
			jobsList.removeAll();
		}
		jobsList.dispose();

		for (int i = 0; i < userJobsLabels.length; i++)
			userJobsLabels[i].dispose();

		// fonts and other stuff
		defaultFont.dispose();

		// window objects
		userInfoGroup.dispose();
		userJobsGroup.dispose();
		jobsGroup.dispose();
		jobInfoGroup.dispose();
		helpChartGroup.dispose();

		// help chart
		itemAcceptableColor.dispose();
		itemAcceptableLabel.dispose();
		itemAcceptedColor.dispose();
		itemAcceptedLabel.dispose();
		itemImportantColor.dispose();
		itemImportantLabel.dispose();

	}

	/**
	 * Retrieve the controller used by this class
	 * 
	 * @return A JobController class instance
	 */
	public JobController getController() {
		return this.controller;
	}

	/**
	 * Get the ID of the selected job in the list
	 * 
	 * @return An integer representing the ID of the selected job, or the value
	 *         -1 if there are no jobs in the list
	 */
	public int getSelectedJobID() {
		try {
			return (int) this.jobsList.getSelection()[0].getData();
		} catch (Exception ex) {
			return -1;
		}
	}

	/**
	 * Get the background color that the selected job from the list has
	 * 
	 * @return A Color instance
	 */
	public Color getSelectedJobColor() {
		return this.jobsList.getSelection()[0].getBackground();
	}

	/**
	 * Change the active items in the actions menu based on the type of the
	 * selected job. <br><br>
	 * 
	 * All items are disabled if there are no jobs in the list<br><br>
	 * If the job is an accepted job, then the user can do most of the actions,
	 * except ending the job.<br><br>
	 * If the job is of normal status, then the user can only accept it, or
	 * depending on the booked status, end it.
	 */
	public void morphActionsMenu() {
		if (this.getSelectedJobID() == -1) {
			acceptJobMenuItem.setEnabled(false);
			cancelJobMenuItem.setEnabled(false);
			finishJobMenuItem.setEnabled(false);
			endJobMenuItem.setEnabled(false);
			openJobDirectoryMenuItem.setEnabled(false);
		} else if (this.getSelectedJobColor().equals(JobWindow.COLOR_ACCEPTED)) {
			acceptJobMenuItem.setEnabled(false);
			cancelJobMenuItem.setEnabled(true);
			finishJobMenuItem.setEnabled(true);
			endJobMenuItem.setEnabled(false);
			openJobDirectoryMenuItem.setEnabled(true);
		} else {
			acceptJobMenuItem.setEnabled(true);
			cancelJobMenuItem.setEnabled(false);
			finishJobMenuItem.setEnabled(false);
			endJobMenuItem.setEnabled(Boolean.parseBoolean(jobBookedBy
					.getText()));
			openJobDirectoryMenuItem.setEnabled(false);
		}
	}

	@Override
	public void update(Observable obs, Object obj) {
		// the only observable that this class has is the JobManager class

		String[] data = obj.toString().split(
				CustomWindow.NOTIFICATION_SEPARATOR);

		switch (data[0]) {
		case "find": {
			this.jobsList.clearAll();
			this.jobsList.removeAll();
			this.clearJobInformation();

			for (Job job : ((JobManager) obs).getAcceptedJobs()) {
				TableItem item = new TableItem(this.jobsList, SWT.None);

				item.setText(job.getName());
				item.setData(job.getID());

				if (job.getIntendedTo().equals(this.tempUserInfo[0]))
					item.setBackground(JobWindow.COLOR_IMPORTANT);
				else
					item.setBackground(JobWindow.COLOR_ACCEPTED);
			}

			for (Job job : ((JobManager) obs).getJobs()) {
				TableItem item = new TableItem(this.jobsList, SWT.None);

				item.setText(job.getName());
				item.setData(job.getID());

				if (job.getIntendedTo().equals(this.tempUserInfo[0]))
					item.setBackground(JobWindow.COLOR_IMPORTANT);
				else if (job.isAcceptable(this.tempUserJobs))
					item.setBackground(JobWindow.COLOR_ACCEPTABLE);
			}

			if (jobsList.getItemCount() > 0)
				jobsList.select(0);
		}
			break;
		case "jobinformation": {
			this.jobType.setText(data[1]);
			this.jobPreviousStaff.setText(data[2]);
			this.jobIntendedTo.setText(data[3]);
			this.jobBookedBy.setText(data[4]);
		}
			break;
		case "end": {
			MessageBox message;

			if (Boolean.parseBoolean(data[1])) {
				((JobManager) obs).findJobs();

				message = new MessageBox(this.getShell(), SWT.ICON_INFORMATION);
				message.setText("Success");
				message.setMessage("The job has been successfully ended");
			} else {
				message = new MessageBox(this.getShell(), SWT.ICON_ERROR);
				message.setText("Error");
				message.setMessage("There was an error while ending the job");
			}

			message.open();
		}
			break;
		case "accept": {
			MessageBox message;

			if (Boolean.parseBoolean(data[1])) {
				((JobManager) obs).findJobs();

				message = new MessageBox(this.getShell(), SWT.ICON_INFORMATION);
				message.setText("Success");
				message.setMessage("The job has been successfully accepted");
			} else {
				message = new MessageBox(this.getShell(), SWT.ICON_ERROR);
				message.setText("Error");
				message.setMessage("There was an error while accepting the job");
			}

			message.open();
		}
			break;
		case "cancel": {
			MessageBox message;

			if (Boolean.parseBoolean(data[1])) {
				((JobManager) obs).findJobs();

				message = new MessageBox(this.getShell(), SWT.ICON_INFORMATION);
				message.setText("Success");
				message.setMessage("The job has been successfully canceled");
			} else {
				message = new MessageBox(this.getShell(), SWT.ICON_ERROR);
				message.setText("Error");
				message.setMessage("There was an error while cancelling the job");
			}

			message.open();
		}
			break;
		case "push": {
			MessageBox message;

			if (Boolean.parseBoolean(data[1])) {
				((JobManager) obs).findJobs();

				message = new MessageBox(this.getShell(), SWT.ICON_INFORMATION);
				message.setText("Success");
				message.setMessage("The job has been successfully pushed back to the server");
			} else {
				message = new MessageBox(this.getShell(), SWT.ICON_ERROR);
				message.setText("Error");
				message.setMessage("There was an error while pushing the job back to the server");
			}

			message.open();
		}
			break;
		}
	}

	@Override
	protected void performInitializations() {
		defaultFont = new Font(Display.getCurrent(), "Calibri", 12, SWT.NORMAL);
		controller = new JobController(this);

		// window objects
		userInfoGroup = new Group(this.getShell(), SWT.None);
		userJobsGroup = new Group(this.getShell(), SWT.None);
		jobsGroup = new Group(this.getShell(), SWT.None);
		jobInfoGroup = new Group(this.getShell(), SWT.None);
		helpChartGroup = new Group(this.getShell(), SWT.None);
		menuBar = new Menu(this.getShell(), SWT.BAR);

		// menu bar objects
		applicationMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		jobsMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		aboutMenuItem = new MenuItem(menuBar, SWT.CASCADE);

		// application menu objects
		applicationMenu = new Menu(this.getShell(), SWT.DROP_DOWN);
		openSettingsMenuItem = new MenuItem(applicationMenu, SWT.PUSH);
		closeWindowMenuItem = new MenuItem(applicationMenu, SWT.PUSH);
		exitApplicationMenuItem = new MenuItem(applicationMenu, SWT.PUSH);

		// jobs menu objects
		jobsMenu = new Menu(this.getShell(), SWT.DROP_DOWN);
		createJobMenuItem = new MenuItem(jobsMenu, SWT.PUSH);
		refreshJobListMenuItem = new MenuItem(jobsMenu, SWT.PUSH);

		// actions menu objects
		actionsMenu = new Menu(this.getShell(), SWT.MENU);
		acceptJobMenuItem = new MenuItem(actionsMenu, SWT.PUSH);
		cancelJobMenuItem = new MenuItem(actionsMenu, SWT.PUSH);
		finishJobMenuItem = new MenuItem(actionsMenu, SWT.PUSH);
		endJobMenuItem = new MenuItem(actionsMenu, SWT.PUSH);
		openJobDirectoryMenuItem = new MenuItem(actionsMenu, SWT.PUSH);

		// user information objects
		userNameLabel = new Label(this.userInfoGroup, SWT.None);
		userRankLabel = new Label(this.userInfoGroup, SWT.None);
		userEmailLabel = new Label(this.userInfoGroup, SWT.None);

		// jobs list
		jobsList = new Table(this.jobsGroup, SWT.VIRTUAL | SWT.V_SCROLL);

		// job information
		jobTypeLabel = new Label(this.jobInfoGroup, SWT.None);
		jobType = new Label(this.jobInfoGroup, SWT.None);
		jobPreviousStaffLabel = new Label(this.jobInfoGroup, SWT.None);
		jobPreviousStaff = new Label(this.jobInfoGroup, SWT.None);
		jobIntendedToLabel = new Label(this.jobInfoGroup, SWT.None);
		jobIntendedTo = new Label(this.jobInfoGroup, SWT.None);
		jobBookedByLabel = new Label(this.jobInfoGroup, SWT.None);
		jobBookedBy = new Label(this.jobInfoGroup, SWT.None);

		// help chart items
		itemAcceptedColor = new Label(this.helpChartGroup, SWT.None);
		itemAcceptedLabel = new Label(this.helpChartGroup, SWT.None);
		itemAcceptableColor = new Label(this.helpChartGroup, SWT.None);
		itemAcceptableLabel = new Label(this.helpChartGroup, SWT.None);
		itemImportantColor = new Label(this.helpChartGroup, SWT.None);
		itemImportantLabel = new Label(this.helpChartGroup, SWT.None);
	}

	@Override
	protected void createObjectProperties() {
		// create layouts for different sectiions
		GridLayout userInfoLayout = new GridLayout();
		userInfoLayout.numColumns = 2;
		userInfoLayout.makeColumnsEqualWidth = true;

		GridLayout userJobsLayout = new GridLayout();
		userJobsLayout.numColumns = 3;
		userJobsLayout.makeColumnsEqualWidth = true;

		GridLayout jobsLayout = new GridLayout();
		jobsLayout.numColumns = 1;

		GridLayout helpLayout = new GridLayout();
		helpLayout.numColumns = 2;

		// groups
		userInfoGroup.setLayout(userInfoLayout);
		userInfoGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		userInfoGroup.setText("User information");

		userJobsGroup.setLayout(userJobsLayout);
		userJobsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		userJobsGroup.setText("User abilities");
		// userJobsGroup.setOrientation(SWT.RIGHT_TO_LEFT);

		jobsGroup.setLayout(jobsLayout);
		jobsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));
		jobsGroup.setText("Job lists");

		jobInfoGroup.setLayout(userInfoLayout);
		jobInfoGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		jobInfoGroup.setText("Job information");

		helpChartGroup.setLayout(helpLayout);
		helpChartGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		helpChartGroup.setText("Help chart");

		// menu bar items
		applicationMenuItem.setText("Application");
		applicationMenuItem.setMenu(applicationMenu);

		jobsMenuItem.setText("Jobs");
		jobsMenuItem.setMenu(jobsMenu);

		aboutMenuItem.setText("About");

		// application menu objects
		openSettingsMenuItem.setText("Settings");
		closeWindowMenuItem.setText("Close");
		exitApplicationMenuItem.setText("Exit TeamSubb");

		// jobs menu objects
		createJobMenuItem.setText("Create a new job");
		refreshJobListMenuItem.setText("Refresh job list");

		// actions menu objects
		acceptJobMenuItem.setText("Accept job");
		cancelJobMenuItem.setText("Cancel job");
		finishJobMenuItem.setText("Finish job");
		endJobMenuItem.setText("End job");
		openJobDirectoryMenuItem.setText("Open storage");

		// user information objects
		userNameLabel
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		userNameLabel.setFont(defaultFont);

		userRankLabel
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		userRankLabel.setFont(defaultFont);

		userEmailLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1));
		userEmailLabel.setFont(defaultFont);

		// jobs list
		jobsList.setFont(defaultFont);
		jobsList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobsList.setBackground(this.getShell().getBackground());
		jobsList.setMenu(actionsMenu);

		// job information
		jobTypeLabel
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobTypeLabel.setFont(this.defaultFont);
		jobTypeLabel.setText("Type:");
		jobTypeLabel.pack();

		jobType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobType.setFont(this.defaultFont);

		jobPreviousStaffLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true));
		jobPreviousStaffLabel.setFont(this.defaultFont);
		jobPreviousStaffLabel.setText("Worked on by:");
		jobPreviousStaffLabel.pack();

		jobPreviousStaff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		jobPreviousStaff.setFont(this.defaultFont);

		jobIntendedToLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		jobIntendedToLabel.setFont(this.defaultFont);
		jobIntendedToLabel.setText("Intended to:");
		jobIntendedToLabel.pack();

		jobIntendedTo
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobIntendedTo.setFont(this.defaultFont);

		jobBookedByLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		jobBookedByLabel.setFont(this.defaultFont);
		jobBookedByLabel.setText("Taken by:");
		jobBookedByLabel.pack();

		jobBookedBy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobBookedBy.setFont(this.defaultFont);

		// help chart item
		itemAcceptedColor.setText("          ");
		itemAcceptedColor.setBackground(JobWindow.COLOR_ACCEPTED);
		itemAcceptedColor.setFont(this.defaultFont);
		itemAcceptedColor.pack();

		itemAcceptableColor.setText("          ");
		itemAcceptableColor.setBackground(JobWindow.COLOR_ACCEPTABLE);
		itemAcceptableColor.setFont(this.defaultFont);
		itemAcceptableColor.pack();

		itemImportantColor.setText("          ");
		itemImportantColor.setBackground(JobWindow.COLOR_IMPORTANT);
		itemImportantColor.setFont(this.defaultFont);
		itemImportantColor.pack();

		itemAcceptableLabel.setText("Jobs that can be accepted by you");
		itemAcceptableLabel.setFont(this.defaultFont);
		itemAcceptableLabel.pack();

		itemAcceptedLabel.setText("Jobs that have been accepted by you");
		itemAcceptedLabel.setFont(this.defaultFont);
		itemAcceptedLabel.pack();

		itemImportantLabel.setText("Jobs that are only for you (important)");
		itemImportantLabel.setFont(this.defaultFont);
		itemImportantLabel.pack();

		// generate the user information
		this.generateUserInfo();
	}

	@Override
	protected void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;

		this.getShell().setText("TeamSubb - Jobs window");
		this.getShell().setLayout(layout);
		this.getShell().setMenuBar(menuBar);
		this.getShell().setSize(800, 600);
		this.placeToCenter();
	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);

		openSettingsMenuItem
				.addSelectionListener(controller.openSettingsClicked);
		closeWindowMenuItem.addSelectionListener(controller.closeWindowClicked);
		exitApplicationMenuItem
				.addSelectionListener(controller.exitApplicationClicked);
		createJobMenuItem.addSelectionListener(controller.createJobClicked);
		refreshJobListMenuItem
				.addSelectionListener(controller.refreshJobListClicked);
		acceptJobMenuItem.addSelectionListener(controller.acceptJobClicked);
		cancelJobMenuItem.addSelectionListener(controller.cancelJobClicked);
		finishJobMenuItem.addSelectionListener(controller.finishJobClicked);
		endJobMenuItem.addSelectionListener(controller.endJobClicked);
		openJobDirectoryMenuItem
				.addSelectionListener(controller.openJobDirectoryClicked);
		aboutMenuItem.addSelectionListener(controller.aboutClicked);

		jobsList.addSelectionListener(controller.jobsListItemSelected);
		jobsList.addMenuDetectListener(controller.jobsListMenuOpened);
	}

	/**
	 * Generate the user information objects and display the on the GUI for the
	 * user to see
	 */
	private void generateUserInfo() {
		userNameLabel.setText(tempUserInfo[0]);
		userEmailLabel.setText(tempUserInfo[1]);
		userRankLabel.setText(tempUserInfo[2]);

		userNameLabel.pack();
		userEmailLabel.pack();
		userRankLabel.pack();

		userJobsLabels = new Label[tempUserJobs.length];
		for (int i = 0; i < tempUserJobs.length; i++) {
			userJobsLabels[i] = new Label(this.userJobsGroup, SWT.None);
			userJobsLabels[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL,
					true, true));
			userJobsLabels[i].setText(tempUserJobs[i]);
			userJobsLabels[i].setFont(this.defaultFont);
			userJobsLabels[i].pack();
		}
	}

	/**
	 * Clears the fields that display the information for a selected job in the list
	 */
	private void clearJobInformation() {
		this.jobType.setText("");
		this.jobPreviousStaff.setText("");
		this.jobIntendedTo.setText("");
		this.jobBookedBy.setText("");
	}
}
