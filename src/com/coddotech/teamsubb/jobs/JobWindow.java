package com.coddotech.teamsubb.jobs;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.coddotech.teamsubb.jobs.JobController;
import com.coddotech.teamsubb.main.CustomWindow;

/**
 * Main window that is used by the user to manage his/her jobs and communicate
 * with the main server about them.
 * 
 * NOTE: THIS CLASS IS STILL INCOMPLETE. NOT ALL THE COMPONENTS HAVE BEEN
 * CREATED YET
 * 
 * importante adresate tie -> visiniu ceva, acceptate - verde, acceptabile -
 * galben
 * 
 * @author Coddo
 * 
 */
public class JobWindow extends CustomWindow implements Observer {

	public static final String[] DEFAULT_JOBS_INFO_HEADERS = { "Traducator",
			"Verificator", "Encoder", "Typesetter", "Manga", "Stiri",
			"Postator" };

	//auxiliary data
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
	private MenuItem actionsMenuItem;

	// application menu objects
	private Menu applicationMenu;
	private MenuItem openSettingsMenuItem;
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

	// user information
	private Label userNameLabel;
	private Label userEmailLabel;
	private Label userRankLabel;
	private Label[] userJobsLabels;

	// job list (table)
	private List jobsList;
	
	// job information
	private Label jobTypeLabel;
	private Label jobType;
	private Label jobPreviousStaffLabel;
	private Label jobPreviousStaff;
	private Label jobIntendedToLabel;
	private Label jobIntendedTo;
	private Label jobBookedByLabel;
	private Label jobBookedBy;

	public JobWindow(String[] userInfo, String[] userJobs) {
		super();
		tempUserInfo = userInfo;
		tempUserJobs = userJobs;
		
		// make the shell resizable
		this.setShell(new Shell(Display.getCurrent(), SWT.SHELL_TRIM));

		this.initializeComponents();
	}

	public void dispose() {
		// controller
		controller.dispose();
		
		//job information
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

		// menu bar objects
		applicationMenuItem.dispose();
		jobsMenuItem.dispose();
		actionsMenuItem.dispose();
		aboutMenuItem.dispose();
		menuBar.dispose();

		// user information objects
		userNameLabel.dispose();
		userEmailLabel.dispose();
		userRankLabel.dispose();

		// jobs list
		if (jobsList.getItemCount() > 0)
			jobsList.removeAll();
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

	}

	public JobController getController() {
		return this.controller;
	}

	/**
	 * Change the active fields of the actions menu based on the selected job in
	 * the list
	 */
	public void morphActionsMenu() {

	}

	@Override
	public void update(Observable obs, Object obj) {
		// TODO Auto-generated method stub

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
		actionsMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		aboutMenuItem = new MenuItem(menuBar, SWT.CASCADE);

		// application menu objects
		applicationMenu = new Menu(this.getShell(), SWT.DROP_DOWN);
		openSettingsMenuItem = new MenuItem(applicationMenu, SWT.PUSH);
		exitApplicationMenuItem = new MenuItem(applicationMenu, SWT.PUSH);

		// jobs menu objects
		jobsMenu = new Menu(this.getShell(), SWT.DROP_DOWN);
		createJobMenuItem = new MenuItem(jobsMenu, SWT.PUSH);
		refreshJobListMenuItem = new MenuItem(jobsMenu, SWT.PUSH);

		// actions menu objects
		actionsMenu = new Menu(this.getShell(), SWT.DROP_DOWN);
		acceptJobMenuItem = new MenuItem(actionsMenu, SWT.PUSH);
		cancelJobMenuItem = new MenuItem(actionsMenu, SWT.PUSH);
		finishJobMenuItem = new MenuItem(actionsMenu, SWT.PUSH);
		endJobMenuItem = new MenuItem(actionsMenu, SWT.PUSH);

		// user information objects
		userNameLabel = new Label(this.userInfoGroup, SWT.None);
		userRankLabel = new Label(this.userInfoGroup, SWT.None);
		userEmailLabel = new Label(this.userInfoGroup, SWT.None);

		// jobs list
		jobsList = new List(this.jobsGroup, SWT.VIRTUAL | SWT.V_SCROLL);
		
		// job information
		jobTypeLabel = new Label(this.jobInfoGroup, SWT.None);
		jobType = new Label(this.jobInfoGroup, SWT.None);
		jobPreviousStaffLabel = new Label(this.jobInfoGroup, SWT.None);
		jobPreviousStaff = new Label(this.jobInfoGroup, SWT.None);
		jobIntendedToLabel = new Label(this.jobInfoGroup, SWT.None);
		jobIntendedTo = new Label(this.jobInfoGroup, SWT.None);
		jobBookedByLabel = new Label(this.jobInfoGroup, SWT.None);
		jobBookedBy = new Label(this.jobInfoGroup, SWT.None);
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

		helpChartGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		helpChartGroup.setText("Help chart");

		// menu bar items
		applicationMenuItem.setText("Application");
		jobsMenuItem.setText("Jobs");
		actionsMenuItem.setText("Actions");
		aboutMenuItem.setText("About");

		// application menu objects
		applicationMenuItem.setMenu(applicationMenu);
		openSettingsMenuItem.setText("Settings");
		exitApplicationMenuItem.setText("Exit");

		// jobs menu objects
		jobsMenuItem.setMenu(jobsMenu);
		createJobMenuItem.setText("Create a new job");
		refreshJobListMenuItem.setText("Refresh job list");

		// actions menu objects
		actionsMenuItem.setMenu(actionsMenu);
		acceptJobMenuItem.setText("Accept job");
		cancelJobMenuItem.setText("Cancel job");
		finishJobMenuItem.setText("Finish job");
		endJobMenuItem.setText("End job");

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
		
		//job information
		jobTypeLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobTypeLabel.setFont(this.defaultFont);
		jobTypeLabel.setText("Type:");
		jobTypeLabel.pack();

		jobType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobType.setFont(this.defaultFont);
		
		jobPreviousStaffLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobPreviousStaffLabel.setFont(this.defaultFont);
		jobPreviousStaffLabel.setText("Worked on by:");
		jobPreviousStaffLabel.pack();
		
		jobPreviousStaff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobPreviousStaff.setFont(this.defaultFont);
		
		jobIntendedToLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobIntendedToLabel.setFont(this.defaultFont);
		jobIntendedToLabel.setText("Intended to:");
		jobIntendedToLabel.pack();
		
		jobIntendedTo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobIntendedTo.setFont(this.defaultFont);
		
		jobBookedByLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobBookedByLabel.setFont(this.defaultFont);
		jobBookedByLabel.setText("Taken by:");
		jobBookedByLabel.pack();
		
		jobBookedBy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jobBookedBy.setFont(this.defaultFont);
		
		//generate the user information
		this.generateUserInfo();
	}

	@Override
	protected void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;

		this.getShell().setLayout(layout);
		this.getShell().setMenuBar(menuBar);
		this.getShell().setSize(800, 600);
		this.placeToCenter();
	}

	@Override
	protected void createListeners() {
		// temporary listener - used just for testing the GUI
		this.getShell().addListener(SWT.Close, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				dispose();

			}

		});

	}

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
		
		this.tempUserInfo = null;
		this.tempUserJobs = null;
	}
}
