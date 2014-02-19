package com.coddotech.teamsubb.jobs.gui;

import java.util.List;
import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.chat.model.LoggedUser;
import com.coddotech.teamsubb.jobs.gui.JobController;
import com.coddotech.teamsubb.jobs.model.Job;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.notifications.gui.PopUpMessages;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;

/**
 * Main window that is used by the user to manage his/her jobs and communicate
 * with the main server about them.
 * 
 * @author Coddo
 * 
 */
public class JobWindow extends CustomWindow {

	private static final Color COLOR_ACCEPTED = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
	private static final Color COLOR_ACCEPTABLE = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
	private static final Color COLOR_IMPORTANT = Display.getDefault().getSystemColor(SWT.COLOR_MAGENTA);

	private static boolean open = false;

	// auxiliary data
	private boolean exiting = false;
	private boolean isTestUser = true;

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
	private MenuItem separator1;
	private MenuItem separator2;
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
	private MenuItem forceCancelJobMenuItem;
	private MenuItem finishJobMenuItem;
	private MenuItem endJobMenuItem;
	private MenuItem configureFontsMenuItem;
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
	private Label jobStartDateLabel;
	private Label jobStartDate;
	private Label jobTorrentLabel;
	private Link jobTorrent;
	private Label jobPreviousStaffLabel;
	private Label jobPreviousStaff;
	private Label jobIntendedToLabel;
	private Label jobIntendedTo;
	private Label jobBookedByLabel;
	private Label jobBookedBy;
	private Label jobCommentsLabel;
	private Text jobComments;

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
	 * 
	 * @param userJobs
	 *            Information about the jobs that the user can take
	 */
	public JobWindow() {
		super();
		this.setShell(new Shell(Display.getDefault(), SWT.SHELL_TRIM));

		this.isTestUser = LoggedUser.getInstance().getName().equals("testcoddo");

		this.initializeComponents();
		this.exiting = false;

		JobWindow.open = true;
	}

	@Override
	public void dispose() {
		try {
			JobWindow.open = false;

			// controller
			controller.dispose();
			this.exiting = true;

			// job information
			jobTypeLabel.dispose();
			jobType.dispose();
			jobStartDateLabel.dispose();
			jobStartDate.dispose();
			jobTorrentLabel.dispose();
			jobTorrent.dispose();
			jobPreviousStaffLabel.dispose();
			jobPreviousStaff.dispose();
			jobIntendedToLabel.dispose();
			jobIntendedTo.dispose();
			jobBookedByLabel.dispose();
			jobBookedBy.dispose();
			jobCommentsLabel.dispose();
			jobComments.dispose();

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
			forceCancelJobMenuItem.dispose();
			finishJobMenuItem.dispose();
			endJobMenuItem.dispose();
			actionsMenu.dispose();
			configureFontsMenuItem.dispose();
			openJobDirectoryMenuItem.dispose();

			// menu bar objects
			applicationMenuItem.dispose();
			jobsMenuItem.dispose();
			aboutMenuItem.dispose();
			menuBar.dispose();
			separator1.dispose();
			separator2.dispose();

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

			this.logDispose();

		}
		catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	public static boolean isOpen() {
		return JobWindow.open;
	}

	public boolean isExiting() {
		return this.exiting;
	}

	/**
	 * Get the ID of the selected job in the list
	 * 
	 * @return An integer representing the ID of the selected job, or the value (-1) if there are no
	 *         jobs in the list
	 */
	public int getSelectedJobID() {
		try {
			return (int) this.jobsList.getSelection()[0].getData();
		}
		catch (Exception ex) {
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
	 * Get the torrent link for the selected job
	 * 
	 * @return A String value
	 */
	public String getTorrentLink() {
		return this.jobTorrent.getText().replace("<a>", "").replace("</a>", "");
	}

	/**
	 * Change the active items in the actions menu based on the type of the
	 * selected job. <br>
	 * <br>
	 * 
	 * All items are disabled if there are no jobs in the list<br>
	 * <br>
	 * 
	 * If the job is an accepted job, then the user can do most of the actions,
	 * except ending the job.<br>
	 * <br>
	 * 
	 * If the job is of normal status, then the user can only accept it, or
	 * depending on the booked status, end it.
	 */
	public void morphActionsMenu() {
		try {

			if (this.getSelectedJobID() == -1) {
				acceptJobMenuItem.setEnabled(false);
				cancelJobMenuItem.setEnabled(false);
				forceCancelJobMenuItem.setEnabled(false);
				finishJobMenuItem.setEnabled(false);
				endJobMenuItem.setEnabled(false);
				configureFontsMenuItem.setEnabled(false);
				openJobDirectoryMenuItem.setEnabled(false);
			}

			else if (this.getSelectedJobColor().equals(COLOR_ACCEPTED)) {
				acceptJobMenuItem.setEnabled(false);
				cancelJobMenuItem.setEnabled(true);
				forceCancelJobMenuItem.setEnabled(true);
				finishJobMenuItem.setEnabled(true);
				endJobMenuItem.setEnabled(false);
				configureFontsMenuItem.setEnabled(true);
				openJobDirectoryMenuItem.setEnabled(true);
			}

			// if the user is a test user with view-only privileges, then
			// he/she cannot interact with the jobs
			else if (this.isTestUser) {
				acceptJobMenuItem.setEnabled(false);
				cancelJobMenuItem.setEnabled(false);
				forceCancelJobMenuItem.setEnabled(false);
				finishJobMenuItem.setEnabled(false);
				endJobMenuItem.setEnabled(false);
				configureFontsMenuItem.setEnabled(false);
				openJobDirectoryMenuItem.setEnabled(false);
			}

			else {
				cancelJobMenuItem.setEnabled(false);
				finishJobMenuItem.setEnabled(false);
				endJobMenuItem.setEnabled(true);
				configureFontsMenuItem.setEnabled(false);
				openJobDirectoryMenuItem.setEnabled(false);
				forceCancelJobMenuItem.setEnabled(!this.jobBookedBy.getText().equals("-"));
				acceptJobMenuItem.setEnabled(this.jobBookedBy.getText().equals("-"));
			}
		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Morph actions menu", ex);

		}
	}

	@Override
	protected void updateGUI(final Observable obs, final Object obj) {
		Runnable update = new Runnable() {

			@Override
			public void run() {
				// the only observable that this class has is the JobManager class
				try {
					NotificationEntity notif = (NotificationEntity) obj;

					switch (notif.getMessage()) {

						case JobManager.JOB_FIND: {
							createJobList(notif);

						}
							break;

						case JobManager.JOB_INFORMATION: {
							updateJobInfo(notif.getJob());
						}
							break;

						case JobManager.JOB_END: {
							handleEndJobResult(obs, notif.getBoolean());

						}
							break;

						case JobManager.JOB_ACCEPT: {
							handleAcceptJobResult(obs, notif.getBoolean());

						}
							break;

						case JobManager.JOB_CANCEL: {
							handleCancelJobResult(obs, notif.getBoolean());

						}
							break;

					}
				}
				catch (Exception ex) {
					ActivityLogger.logException(this.getClass().getName(), "GUI update", ex);

				}

			}
		};

		Display.getDefault().asyncExec(update);

	}

	@Override
	public void performInitializations() {
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
		separator1 = new MenuItem(menuBar, SWT.NONE);
		jobsMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		separator2 = new MenuItem(menuBar, SWT.NONE);
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
		forceCancelJobMenuItem = new MenuItem(actionsMenu, SWT.PUSH);
		finishJobMenuItem = new MenuItem(actionsMenu, SWT.PUSH);
		endJobMenuItem = new MenuItem(actionsMenu, SWT.PUSH);
		configureFontsMenuItem = new MenuItem(actionsMenu, SWT.PUSH);
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
		jobStartDateLabel = new Label(this.jobInfoGroup, SWT.None);
		jobStartDate = new Label(this.jobInfoGroup, SWT.None);
		jobTorrentLabel = new Label(this.jobInfoGroup, SWT.None);
		jobTorrent = new Link(this.jobInfoGroup, SWT.None);
		jobPreviousStaffLabel = new Label(this.jobInfoGroup, SWT.None);
		jobPreviousStaff = new Label(this.jobInfoGroup, SWT.None);
		jobIntendedToLabel = new Label(this.jobInfoGroup, SWT.None);
		jobIntendedTo = new Label(this.jobInfoGroup, SWT.None);
		jobBookedByLabel = new Label(this.jobInfoGroup, SWT.None);
		jobBookedBy = new Label(this.jobInfoGroup, SWT.None);
		jobCommentsLabel = new Label(this.jobInfoGroup, SWT.None);
		jobComments = new Text(this.jobInfoGroup, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY | SWT.TRANSPARENT | SWT.V_SCROLL);

		// help chart items
		itemAcceptedColor = new Label(this.helpChartGroup, SWT.None);
		itemAcceptedLabel = new Label(this.helpChartGroup, SWT.None);
		itemAcceptableColor = new Label(this.helpChartGroup, SWT.None);
		itemAcceptableLabel = new Label(this.helpChartGroup, SWT.None);
		itemImportantColor = new Label(this.helpChartGroup, SWT.None);
		itemImportantLabel = new Label(this.helpChartGroup, SWT.None);
	}

	@Override
	public void createObjectProperties() {
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
		userInfoGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		userInfoGroup.setText("User information");

		userJobsGroup.setLayout(userJobsLayout);
		userJobsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		userJobsGroup.setText("User abilities");

		jobsGroup.setLayout(jobsLayout);
		jobsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4));
		jobsGroup.setText("Job lists");

		jobInfoGroup.setLayout(helpLayout);
		jobInfoGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		jobInfoGroup.setText("Job information");

		helpChartGroup.setLayout(helpLayout);
		helpChartGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		helpChartGroup.setText("Help chart");

		// menu bar items
		applicationMenuItem.setText("Application");
		applicationMenuItem.setMenu(applicationMenu);

		jobsMenuItem.setText("Jobs");
		jobsMenuItem.setMenu(jobsMenu);

		aboutMenuItem.setText("About TeamSubb");

		separator1.setText("|");
		separator1.setEnabled(false);

		separator2.setText("|");
		separator2.setEnabled(false);

		// application menu objects
		openSettingsMenuItem.setText("Settings");
		closeWindowMenuItem.setText("Close");
		exitApplicationMenuItem.setText("Quit");

		// jobs menu objects
		createJobMenuItem.setText("Create a new job");
		createJobMenuItem.setEnabled(!this.isTestUser);

		refreshJobListMenuItem.setText("Refresh job list");

		// actions menu objects
		acceptJobMenuItem.setText("Accept job");
		cancelJobMenuItem.setText("Cancel job");
		forceCancelJobMenuItem.setText("Forcibly cancel this job");
		finishJobMenuItem.setText("Finish job");
		endJobMenuItem.setText("End job");
		configureFontsMenuItem.setText("Configure fonts");
		openJobDirectoryMenuItem.setText("Open storage");

		// user information objects
		userNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		userNameLabel.setFont(CustomWindow.DEFAULT_FONT);

		userRankLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		userRankLabel.setFont(CustomWindow.DEFAULT_FONT);

		userEmailLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		userEmailLabel.setFont(CustomWindow.DEFAULT_FONT);

		// jobs list
		jobsList.setFont(CustomWindow.DEFAULT_FONT);
		jobsList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobsList.setBackground(this.getShell().getBackground());
		jobsList.setMenu(actionsMenu);

		// job information
		jobTypeLabel.setFont(CustomWindow.DEFAULT_FONT);
		jobTypeLabel.setText("Type:");
		jobTypeLabel.pack();

		jobType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		jobType.setFont(CustomWindow.DEFAULT_FONT);

		jobStartDateLabel.setFont(CustomWindow.DEFAULT_FONT);
		jobStartDateLabel.setText("Start date:");
		jobStartDateLabel.pack();

		jobStartDate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		jobStartDate.setFont(CustomWindow.DEFAULT_FONT);

		jobTorrentLabel.setFont(CustomWindow.DEFAULT_FONT);
		jobTorrentLabel.setText("Torrent:");
		jobTorrentLabel.pack();

		jobTorrent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		jobTorrent.setFont(CustomWindow.DEFAULT_FONT);

		jobPreviousStaffLabel.setFont(CustomWindow.DEFAULT_FONT);
		jobPreviousStaffLabel.setText("Worked on by:");
		jobPreviousStaffLabel.pack();

		jobPreviousStaff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		jobPreviousStaff.setFont(CustomWindow.DEFAULT_FONT);

		jobIntendedToLabel.setFont(CustomWindow.DEFAULT_FONT);
		jobIntendedToLabel.setText("Intended to:");
		jobIntendedToLabel.pack();

		jobIntendedTo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		jobIntendedTo.setFont(CustomWindow.DEFAULT_FONT);

		jobBookedByLabel.setFont(CustomWindow.DEFAULT_FONT);
		jobBookedByLabel.setText("Taken by:");
		jobBookedByLabel.pack();

		jobBookedBy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		jobBookedBy.setFont(CustomWindow.DEFAULT_FONT);

		jobCommentsLabel.setFont(CustomWindow.DEFAULT_FONT);
		jobCommentsLabel.setText("Comments:");
		jobCommentsLabel.pack();

		jobComments.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5));
		jobComments.setFont(CustomWindow.DEFAULT_FONT);
		jobComments.setBackground(this.getShell().getBackground());
		// jobComments.setEnabled(false);

		// help chart item
		itemAcceptedColor.setText("          ");
		itemAcceptedColor.setBackground(COLOR_ACCEPTED);
		itemAcceptedColor.setFont(CustomWindow.DEFAULT_FONT);
		itemAcceptedColor.pack();

		itemAcceptableColor.setText("          ");
		itemAcceptableColor.setBackground(COLOR_ACCEPTABLE);
		itemAcceptableColor.setFont(CustomWindow.DEFAULT_FONT);
		itemAcceptableColor.pack();

		itemImportantColor.setText("          ");
		itemImportantColor.setBackground(COLOR_IMPORTANT);
		itemImportantColor.setFont(CustomWindow.DEFAULT_FONT);
		itemImportantColor.pack();

		itemAcceptableLabel.setText("Jobs that can be accepted by you");
		itemAcceptableLabel.setFont(CustomWindow.DEFAULT_FONT);
		itemAcceptableLabel.pack();

		itemAcceptedLabel.setText("Jobs that have been accepted by you");
		itemAcceptedLabel.setFont(CustomWindow.DEFAULT_FONT);
		itemAcceptedLabel.pack();

		itemImportantLabel.setText("Jobs that are only for you (important)");
		itemImportantLabel.setFont(CustomWindow.DEFAULT_FONT);
		itemImportantLabel.pack();

		// generate the user information
		this.generateUserInfo();
	}

	@Override
	public void createShellProperties() {
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
	public void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);

		openSettingsMenuItem.addSelectionListener(controller.openSettingsClicked);

		closeWindowMenuItem.addSelectionListener(controller.closeWindowClicked);

		exitApplicationMenuItem.addSelectionListener(controller.exitApplicationClicked);

		createJobMenuItem.addSelectionListener(controller.createJobClicked);

		refreshJobListMenuItem.addSelectionListener(controller.refreshJobListClicked);

		acceptJobMenuItem.addSelectionListener(controller.acceptJobClicked);
		cancelJobMenuItem.addSelectionListener(controller.cancelJobClicked);

		forceCancelJobMenuItem.addSelectionListener(controller.forceCancelJobCLicked);

		finishJobMenuItem.addSelectionListener(controller.finishJobClicked);
		endJobMenuItem.addSelectionListener(controller.endJobClicked);

		openJobDirectoryMenuItem.addSelectionListener(controller.openJobDirectoryClicked);

		configureFontsMenuItem.addSelectionListener(controller.configureFontsClicked);

		aboutMenuItem.addSelectionListener(controller.aboutClicked);

		jobTorrent.addSelectionListener(controller.jobTorrentClicked);

		jobsList.addSelectionListener(controller.jobsListItemSelected);
		jobsList.addMenuDetectListener(controller.jobsListMenuOpened);
		jobsList.addListener(SWT.KeyDown, controller.jobListButtonPress);
	}

	@SuppressWarnings ("unchecked")
	private void createJobList(NotificationEntity notif) {
		this.jobsList.clearAll();
		this.jobsList.removeAll();

		List<Job> acceptedJobs = null;
		List<Job> jobs = null;

		try {

			acceptedJobs = (List<Job>) notif.getExtraArguments()[0];
			jobs = (List<Job>) notif.getExtraArguments()[1];

		}

		catch (Exception ex) {
			return;
		}

		finally {
			if (jobs != null)
				if (jobs.size() == 0)
					this.clearJobInformation();
		}

		for (Job job : acceptedJobs) {

			if (!isInList(job)) {
				TableItem item = new TableItem(this.jobsList, SWT.None);

				item.setText(job.getName());
				item.setData(job.getID());

				item.setBackground(COLOR_ACCEPTED);

			}
		}

		for (Job job : jobs) {

			if (!isInList(job)) {
				TableItem item = new TableItem(this.jobsList, SWT.None);

				item.setText(job.getName());
				item.setData(job.getID());

				if (job.getIntendedTo().equals(LoggedUser.getInstance().getName()))
					item.setBackground(COLOR_IMPORTANT);

				else if (job.isAcceptable())
					item.setBackground(COLOR_ACCEPTABLE);
			}

		}
	}

	private boolean isInList(Job job) {
		for (TableItem item : jobsList.getItems()) {

			if ((int) item.getData() == job.getID())
				return true;

		}

		return false;
	}

	private void updateJobInfo(Job job) {
		if (job == null) {
			this.clearJobInformation();

			return;
		}

		this.jobType.setText(Job.DEFAULT_JOB_TYPES[job.getType()]);
		this.jobStartDate.setText(job.getStartDate());
		this.jobTorrent.setText("<a>" + job.getTorrent() + "</a>");
		this.jobPreviousStaff.setText(job.getPreviousStaffMember());
		this.jobIntendedTo.setText(job.getIntendedTo());
		this.jobBookedBy.setText(job.getBookedBy());
		this.jobComments.setText(job.getComments());

		this.jobType.pack();
		this.jobStartDate.pack();
		this.jobTorrent.pack();
		this.jobPreviousStaff.pack();
		this.jobIntendedTo.pack();
		this.jobBookedBy.pack();
	}

	private void handleEndJobResult(Observable obs, boolean succeeded) {
		if (succeeded) {
			((JobManager) obs).findJobs();

		}

		else {
			PopUpMessages.getInstance().jobEndError();

		}
	}

	private void handleAcceptJobResult(Observable obs, boolean succeeded) {
		if (succeeded) {
			((JobManager) obs).findJobs();

			PopUpMessages.getInstance().jobAcceptSuccess();
		}

		else {
			PopUpMessages.getInstance().jobAcceptError();

		}

	}

	private void handleCancelJobResult(Observable obs, boolean succeeded) {
		if (succeeded) {
			((JobManager) obs).findJobs();

		}

		else {
			PopUpMessages.getInstance().jobCancelError();

		}

	}

	/**
	 * Generate the user information objects and display the on the GUI for the user to see
	 */
	private void generateUserInfo() {
		LoggedUser user = LoggedUser.getInstance();

		userNameLabel.setText(user.getName());
		userEmailLabel.setText(user.getEmail());
		userRankLabel.setText(user.getRank());

		userNameLabel.pack();
		userEmailLabel.pack();
		userRankLabel.pack();

		String[] userJobs = user.getJobNames();

		userJobsLabels = new Label[userJobs.length];

		for (int i = 0; i < userJobs.length; i++) {
			userJobsLabels[i] = new Label(this.userJobsGroup, SWT.None);
			userJobsLabels[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			userJobsLabels[i].setText(userJobs[i]);
			userJobsLabels[i].setFont(CustomWindow.DEFAULT_FONT);
			userJobsLabels[i].pack();
		}
	}

	/**
	 * Clears the fields that display the information for a selected job in the list
	 */
	private void clearJobInformation() {
		this.jobType.setText("");
		this.jobStartDate.setText("");
		this.jobTorrent.setText("");
		this.jobPreviousStaff.setText("");
		this.jobIntendedTo.setText("");
		this.jobBookedBy.setText("");
		this.jobComments.setText("");
	}
}
