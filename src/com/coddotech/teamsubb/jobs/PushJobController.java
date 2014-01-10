package com.coddotech.teamsubb.jobs;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.main.CustomWindow;

public class PushJobController {

	private PushJobWindow view;

	private JobManager model;

	/**
	 * Class constructor
	 * 
	 * @param view
	 *            The view that uses this controller
	 */
	public PushJobController(PushJobWindow view) {
		this.view = view;
		this.model = JobManager.getInstance();
		this.model.addObserver(view);
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		model.deleteObserver(view);
	}

	/**
	 * Listener for when the finish button is clicked. This verifies for an
	 * internet connection, checks if the fields are not empty and proceeds with
	 * pushing the job files to the server if everything is ok.
	 */
	public SelectionListener finishButtonClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			if (CustomWindow.isConnected(true)) {

				if (view.verifyFields()) {
					boolean result = model.pushJob(view.getID(), view.getNextStaff(),
							view.getType(), view.getComments());
					
					if(result) {
						//if the job was marked as "end", then send an end request too
						if(view.getType() == Job.DEFAULT_JOB_TYPES.length - 1)
							model.endJob(view.getID());
						
						view.close();
					}
						
				}
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the cancel button is clicked. This closes the view.
	 */
	public SelectionListener cancelButtonClicked = new SelectionListener() {

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
	 * Listener for when the view is closing. Disposes of all the components in
	 * use
	 */
	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.dispose();

		}
	};
}
