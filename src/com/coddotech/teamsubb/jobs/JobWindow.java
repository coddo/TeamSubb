package com.coddotech.teamsubb.jobs;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

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
	public static final String[] DEFAULT_USER_INFORMATION = { "Name", "Email",
			"Rank" };

	// controller used for interpreting user actions
	private JobController controller;

	// window objects
	private Group userInfo;
	private Group userJobs;
	private Group jobs;
	private Group jobInfo;
	private Group helpChart;
	private Menu menuBar;

	// menu bar objects
	private MenuItem applicationMenuItem;
	private MenuItem aboutMenuItem;
	private MenuItem jobsMenuItem;
	private MenuItem actionsMenuItem;
	
	// application menu objects
	private MenuItem openSettingsMenuItem;
	
	// jobs menu objects
	
	
	// actions menu objects
	private Menu actionsMenu;

	public JobWindow(String[] userInfo, String[] userJobs) {
		super();

		// make the shell resizable
		this.setShell(new Shell(Display.getCurrent(), SWT.SHELL_TRIM));

		this.initializeComponents();

		this.generateUserInfo(userInfo, userJobs);
	}

	public void dispose() {
		//controller
		controller.dispose();

		//window objects
		userInfo.dispose();
		userJobs.dispose();
		jobs.dispose();
		jobInfo.dispose();
		helpChart.dispose();
		
		//menu bar objects
		menuBar.dispose();
		applicationMenuItem.dispose();
		jobsMenuItem.dispose();
		actionsMenuItem.dispose();
		aboutMenuItem.dispose();
		
		// application menu objects
		openSettingsMenuItem.dispose();
		
		//actions menu objects
		actionsMenu.dispose();
		
		// jobs menu objects
	}

	public JobController getController() {
		return this.controller;
	}

	@Override
	public void update(Observable obs, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void performInitializations() {
		controller = new JobController(this);

		// window objects
		userInfo = new Group(this.getShell(), SWT.None);
		userJobs = new Group(this.getShell(), SWT.None);
		jobs = new Group(this.getShell(), SWT.None);
		jobInfo = new Group(this.getShell(), SWT.None);
		helpChart = new Group(this.getShell(), SWT.None);
		menuBar = new Menu(this.getShell(), SWT.BAR);
		
		//menu bar objects
		applicationMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		jobsMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		actionsMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		aboutMenuItem  = new MenuItem(menuBar, SWT.CASCADE);
		
		//applications menu objects
		openSettingsMenuItem = new MenuItem(parent, style)
	}

	@Override
	protected void createObjectProperties() {
		// groups
		userInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		userInfo.setText("User information");

		userJobs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		userJobs.setText("User abilities");
		userJobs.setOrientation(SWT.RIGHT_TO_LEFT);

		jobs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		jobs.setText("Job lists");

		jobInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		jobInfo.setText("Job information");

		helpChart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		helpChart.setText("Help chart");
		helpChart.setOrientation(SWT.RIGHT_TO_LEFT);
		
		//menu bar items
		applicationMenuItem.setText("Application");
		jobsMenuItem.setText("Jobs");
		actionsMenuItem.setText("Actions");
		aboutMenuItem.setText("About");
	}

	@Override
	public void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		this.getShell().setLayout(layout);
		this.getShell().setSize(800, 600);
		this.getShell().setMenuBar(menuBar);
		this.placeToCenter();
	}

	@Override
	protected void createListeners() {
		// TODO Auto-generated method stub

	}

	private void generateUserInfo(String[] userInfo, String[] userJob) {
		// TODO Auto-generated method stub

	}
}
