package com.coddotech.teamsubb.main;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

import com.coddotech.teamsubb.jobs.JobManager;
import com.coddotech.teamsubb.settings.AppSettings;
import com.coddotech.teamsubb.settings.AppSettingsWindow;

public class GadgetController {

	private GadgetWindow view;
	private JobManager manager;
	private AppSettings settings;

	// 1 min = 60000 ms
	int interval = AppSettings.DEFAULT_SEARCH_INTERVAL * 60000; 
	
	boolean isDisposed = false;

	/**
	 * Class constructor
	 * 
	 * @param view The view which uses this controller
	 */
	public GadgetController(GadgetWindow view) {
		this.view = view;
		
		settings = new AppSettings();
		settings.addObserver(view);
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		this.isDisposed = true;
		timer = null;
		view = null;
		
		manager.dispose();
		manager = null;
		
		settings.dispose();
		settings = null;
	}
	
	/**
	 * 
	 * @param mins
	 */
	public void setInterval(int mins) {
		this.interval = mins * 60000;
	}

	Runnable timer = new Runnable() {
	
		@Override
		public void run() {
			manager.findJobs();
	
			if (!isDisposed)
				Display.getCurrent().timerExec(interval, this);
		}
	
	};
	
	public SelectionListener settingsClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			AppSettingsWindow settingsWindow = new AppSettingsWindow();
			settingsWindow.setModel(settings);
			settingsWindow.open();
			settingsWindow = null;
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**
	 * Listener for when the window is displayed. <br>
	 * Creates the model and add the view as its observer
	 */
	public Listener shellShownListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			manager = new JobManager(view.getUserName(), view.getUserJobs());
			manager.addObserver(view);
			
			// start the timer for searching jobs
			timer.run();
		}

	};

	/**
	 * Listener for when the window is closed. <br>
	 * Disposes of all the window's components
	 */
	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			view.dispose();
		}
	};

}
