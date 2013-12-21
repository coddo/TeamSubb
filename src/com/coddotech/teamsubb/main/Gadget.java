package com.coddotech.teamsubb.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.jobs.JobManager;
import com.coddotech.teamsubb.settings.AppSettings;

/**
 * Windows gadget window.<br>
 * This is the main window for the application.
 * 
 * @author Coddo
 */
public class Gadget extends CustomWindow {

	public static final String[] DEFAULT_JOBS_INFO_HEADERS = { "Traducator",
			"Verificator", "Encoder", "Typesetter", "Manga", "Stiri",
			"Postator" };
	public static final String[] DEFAULT_USER_INFORMATION = { "Name", "Email",
			"Rank" };

	private boolean[] jobs;
	private String[] userInfo;

	private JobManager jobManager;
	private AppSettings settings;

	private Label label;

	/*
	 * Class constructor
	 */
	public Gadget(String[] userInfo, boolean[] jobs) {
		super();
		
		this.userInfo = userInfo;
		this.jobs = jobs;
	}

	/**
	 * Clear the memory from this class and its resources
	 */
	private void dispose() {
		// user classes
		jobManager.dispose();
		jobManager = null;
		settings.dispose();
		settings = null;

		// user controls
		label.dispose();
	}
	
	public String getUserName() {
		return this.userInfo[0];
	}

	public boolean[] getJobs() {
		return jobs;
	}

	public void setJobs(boolean[] jobs) {
		this.jobs = jobs;
	}

	/**
	 * Notify the user with visual effects and messages that a new job is
	 * available for him
	 */
	public void startNotifications() {

	}

	/**
	 * Stop all currently active notifications
	 * */
	public void stopNotifications() {

	}

	/**
	 * Listens for when the shell (GUI) closes and clears memory from this class
	 * and its resources
	 */
	private Listener shellClosingListener = new Listener() {
		public void handleEvent(Event e) {
			dispose();
		}
	};

	/*
	 * Listens for when the shell (GUI) for this class is displayed
	 */
	private Listener gadgetShownListener = new Listener() {
		
		@Override
		public void handleEvent(Event e) {
			/*label.setText(getShell().getLocation().x + ","
					+ getShell().getLocation().y);
			label.pack();*/
		}
		
	};

	/**
	 * Listens for location changes done to the shell of this class.<br>
	 * Practically, listens for when the form is moved into a new position
	 */
	private Listener gadgetPositionChangedListener = new Listener() {
		@Override
		public void handleEvent(Event e) {
			if (settings.isGadgetAutosaveLocation()) {
				settings.setGadgetLocation(getShell().getLocation());
			}
		}
	};

	@Override
	protected void performInitializations() {
		jobManager = new JobManager(this);
		
		settings = new AppSettings();
		label = new Label(getShell(), SWT.None);

	}

	@Override
	protected void createObjectProperties() {
		settings.readSettings();
		
		label.setLocation(10, 10);
		label.pack();

	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("Team Subb");
		this.getShell().setLocation(settings.getGadgetLocation());
		this.getShell().setSize(400, 400);

	}

	@Override
	protected void createListeners() {
		//this.getShell().addListener(SWT.Show, gadgetShownListener);
		//this.getShell().addListener(SWT.Close, shellClosingListener);
		//this.getShell().addListener(SWT.Move, gadgetPositionChangedListener);

	}
}
