package com.coddotech.teamsubb.main;

import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

import com.coddotech.teamsubb.jobs.JobManager;

public class GadgetController {

	private GadgetWindow view;
	private JobManager model;

	public GadgetController(GadgetWindow view) {
		this.view = view;
	}

	/**
	 * Listener for when the window is displayed. <br>
	 * Creates the model and add the view as its observer
	 */
	public Listener shellShownListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			model = new JobManager(view.getUserName(), view.getUserJobs());
			model.addObserver(view);
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
