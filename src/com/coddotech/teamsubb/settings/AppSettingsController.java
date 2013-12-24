package com.coddotech.teamsubb.settings;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

public class AppSettingsController {

	private AppSettingsWindow view;
	private AppSettings model;

	/**
	 * The constructor for this view controller class
	 * 
	 * @param view The view which uses this controller
	 */
	public AppSettingsController(AppSettingsWindow view) {
		this.view = view;
	}
	
	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		view = null;
	}
	
	/**
	 * Get the settings class used to manage the application specific settings
	 * (from the XML file)
	 * 
	 * @return A AppSettings class instance
	 */
	public void setModel(AppSettings model) {
		this.model = model;
		this.model.addObserver(this.view);
	}
	
	/**
	 * Listener for when the apply button is clicked
	 */
	public SelectionListener applyClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			model.setGadgetAutosaveLocation(view.isGadgetAutosaveLocation());
			model.setSearchInterval(view.getSearchInterval());
			
			model.commitChangesToFile();
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
	 * Listener for when the shell is shown -> reads all the settings from the
	 * XML settings file <br>
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
			model.deleteObserver(view);
			view.dispose();
		}
	};

}
