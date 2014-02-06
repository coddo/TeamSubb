package com.coddotech.teamsubb.jobs.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.main.CustomController;

public class PushJobController extends CustomController {

	private PushJobWindow view;
	private JobManager model;

	private FileDialog browseSub;

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

		browseSub = new FileDialog(view.getShell(), SWT.OPEN);
		browseSub.setText("Select sub file");
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		try {
			model.deleteObserver(view);

			this.logDispose();

		}
		catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	/**
	 * Listener for when the finish button is clicked. This verifies for an internet connection,
	 * checks if the fields are not empty and proceeds with pushing the job files to the server if
	 * everything is ok.
	 */
	public SelectionListener finishButtonClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {

			if (view.verifyFields()) {

				model.pushJob(view.getID(), view.getNextStaff(), view.getType(), view.getTorrent(),
						view.getComments(), view.getSubFile());

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
	 * Listener for when the sub file checkbox check state has changed
	 */
	public SelectionListener subFileChecked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (view.hasNewSubFile()) {
				String result = browseSub.open();

				if (result != null)
					view.setSubFile(result);

				else
					view.setSubFile("");
			}

			else
				view.setSubFile("");
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the torrent checkbox check state has changed
	 */
	public SelectionListener torrentChecked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			view.changeTorrentState();

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
