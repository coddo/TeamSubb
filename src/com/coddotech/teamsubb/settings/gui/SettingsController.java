package com.coddotech.teamsubb.settings.gui;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.main.CustomController;
import com.coddotech.teamsubb.settings.model.Settings;

public class SettingsController extends CustomController {

	private SettingsWindow view;
	private Settings model;

	/**
	 * The constructor for this view controller class
	 * 
	 * @param view
	 *            The view which uses this controller
	 */
	public SettingsController(SettingsWindow view) {
		this.view = view;
		model = Settings.getInstance();
		model.addObserver(view);
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		try {
			model.deleteObserver(view);

			view = null;
			model = null;

			this.logDispose();

		}
		catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	/**
	 * Listener for when the apply button is clicked
	 */
	public SelectionListener applyClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			if (view.verifySettings()) {
				model.setGadgetAutosaveLocation(view.isGadgetAutosaveLocation());

				model.setSearchInterval(view.getSearchInterval());

				model.commitChangesToFile();
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {

		}
	};

	/**
	 * Listener for when the cancel button is clicked
	 */
	public SelectionListener cancelClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			view.close();

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {

		}
	};

	/**
	 * Listener for when the shell is shown -> reads all the settings from the XML settings file.<br>
	 * 
	 * This forces the view to update its interface based on the read settings.
	 */
	public Listener shellShownListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			model.readSettings();
		}

	};

	/**
	 * Listener for when the shell is closing in order to know when to start
	 * disposing all the components for this class
	 */
	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.dispose();
		}
	};

}
