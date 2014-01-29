package com.coddotech.teamsubb.settings.gui;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.main.CustomController;
import com.coddotech.teamsubb.settings.model.Settings;

public class SettingsController extends CustomController {

	private SettingsWindow view;
	private Settings settings;

	/**
	 * The constructor for this view controller class
	 * 
	 * @param view
	 *            The view which uses this controller
	 */
	public SettingsController(SettingsWindow view) {
		this.view = view;
		settings = Settings.getInstance();
		settings.addObserver(view);
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		try {
			settings.deleteObserver(view);

			view = null;
			settings = null;

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
				applySettings();

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
	 * Listener for when the restore default settings button is pressed
	 */
	public SelectionListener restoreDefaultsClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			settings.restoreDefaultSettings();

			view.setAsChanged(true);

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when changes occur in the settings
	 */
	public Listener modificationListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.setAsChanged(true);

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
			settings.readSettings();
		}

	};

	/**
	 * Listener for when the shell is closing in order to know when to start
	 * disposing all the components for this class
	 */
	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			if (view.isChanged()) {

				if (view.displaySaveChangesQBox())
					applySettings();

			}

			view.dispose();
		}
	};

	private void applySettings() {
		settings.setGadgetAutosaveLocation(view.isGadgetAutosaveLocation());

		settings.setSearchInterval(view.getSearchInterval());

		settings.commitChangesToFile();

		view.setAsChanged(false);
	}

}
