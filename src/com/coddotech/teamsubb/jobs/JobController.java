package com.coddotech.teamsubb.jobs;

import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.jobs.JobWindow;
import com.coddotech.teamsubb.main.ApplicationInformation;
import com.coddotech.teamsubb.main.GadgetWindow;
import com.coddotech.teamsubb.settings.AppSettings;
import com.coddotech.teamsubb.settings.AppSettingsWindow;

/**
 * Controller class used by the JobWindow in order to complete the job actions
 * that are requested by the user
 * 
 * NOTE: THIS CLASS IS STILL INCOMPLETE. NOT ALL THE COMPONENTS HAVE BEEN
 * CREATED YET
 * 
 * @author Coddo
 * 
 */
public class JobController {

	private JobWindow view;
	private JobManager model;

	private AppSettings settings;
	private GadgetWindow mainWindow;

	/**
	 * Class constructor
	 * 
	 * @param view
	 *            The view that user this controller
	 */
	public JobController(JobWindow view) {
		this.view = view;
	}

	/**
	 * Clear the memory from this class and its conponents
	 */
	public void dispose() {
		model.deleteObserver(this.view);
	}

	/**
	 * Set the model to be used by this controller
	 * 
	 * @param model
	 *            A JobManager instance representing the model
	 */
	public void setModel(JobManager model) {
		this.model = model;
		this.model.addObserver(this.view);
	}

	/**
	 * Set the settings model to be used by this controller
	 * 
	 * @param settings
	 *            An AppSettings class instance
	 */
	public void setSettingsModel(AppSettings settings) {
		this.settings = settings;
	}

	/**
	 * Set main thread for the application (used for closing the app)
	 * 
	 * @param gadget
	 *            A GadgetWindow class instance
	 */
	public void setMainWindow(GadgetWindow gadget) {
		this.mainWindow = gadget;
	}

	/**
	 * Listener for when the open settings button is clicked from the actions
	 * menu This opens the settings windows.
	 */
	public SelectionListener openSettingsClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			AppSettingsWindow set = new AppSettingsWindow();
			set.getController().setModel(settings);
			settings.addObserver(set);
			set.open();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the close window button is clicked from the application
	 * menu. This closes only the jobs window
	 */
	public SelectionListener closeWindowClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			view.close();

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the exit application button is clicked from the
	 * application menu. This closes the entire application.
	 */
	public SelectionListener exitApplicationClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			view.close();
			mainWindow.close();

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the create job button is clicked from the jobs menu.
	 * This opens the view responsible for creating new jobs.
	 */
	public SelectionListener createJobClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			CreateJobWindow creator = new CreateJobWindow(model);
			model.addObserver(creator);
			creator.open();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the refresh job list button is clicked from the jobs
	 * menu menu. This refreshes the list of jobs by reloading all the data from
	 * the server.
	 */
	public SelectionListener refreshJobListClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			if (view.isConnected(true)) {
				model.findJobs();
			}

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the accept job button is clicked from the actions menu.
	 * This accepts the selected job.
	 */
	public SelectionListener acceptJobClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			if (view.isConnected(true)) {
				model.acceptJob(view.getSelectedJobID());
			}

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the cancel job button is clicked from the actions menu.
	 * This cancels a jobs which was previously accepted by the user.
	 */
	public SelectionListener cancelJobClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			if (view.isConnected(true)) {
				model.cancelJob(view.getSelectedJobID());
			}

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the finish job button is clicked from the actions menu.
	 * This finishes a job previously accepted by the user, by sending the data
	 * back to the server and marking it for the next job. This is done through
	 * another window which has the task of finishing a job.
	 */
	public SelectionListener finishJobClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			if (view.isConnected(true)) {
				PushJobWindow push = new PushJobWindow(
						model.getAcceptedJob(view.getSelectedJobID()));
				push.getController().setModel(model);
				push.open();
			}

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the end job button is clicked from the actions menu.
	 * This ends a job completely and deletes it from the server's list of jobs.
	 */
	public SelectionListener endJobClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			if (view.isConnected(true)) {
				model.endJob(view.getSelectedJobID());
			}

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the open job directory button is clicked from the
	 * actions menu. This opens the directory where the selected job's files are
	 * stored, using the default file explorer in the operating system.
	 */
	public SelectionListener openJobDirectoryClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			model.getAcceptedJob(view.getSelectedJobID()).openDirectory();

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the about button is clicked from the menu bar. This
	 * opens a new window containing information about the application.
	 */
	public SelectionListener aboutClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			ApplicationInformation appInfo = new ApplicationInformation();
			appInfo.open();

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the actions menu is opened on the jobs list. This
	 * changes the availability of the menu items in order no to let the user
	 * perform illegal actions on different jobs.
	 */
	public MenuDetectListener jobsListMenuOpened = new MenuDetectListener() {

		@Override
		public void menuDetected(MenuDetectEvent arg0) {
			view.morphActionsMenu();

		}
	};

	/**
	 * Listener for when an item is selected in the jobs list. This updates the
	 * view with relevant information about the selected job in the list.
	 */
	public SelectionListener jobsListItemSelected = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			int ID = view.getSelectedJobID();

			if (ID != -1)
				model.notifyJobInformation(ID);

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the shell is shown for the first time. This prompts the
	 * model to do an initial search for jobs in order to populate the view with
	 * data
	 */
	public Listener shellShownListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			model.findJobs();

		}

	};

	/**
	 * Listener for when the shell is closing. Responsible for disposing of
	 * components before the app is completely closed
	 */
	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.dispose();

		}

	};

}
