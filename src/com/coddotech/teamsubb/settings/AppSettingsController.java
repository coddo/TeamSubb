package com.coddotech.teamsubb.settings;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

public class AppSettingsController {

	private AppSettingsWindow view;
	private AppSettings model;
	
	public AppSettingsController(AppSettingsWindow view) {
		this.view = view;
		model = new AppSettings();
		
		model.addObserver(view);
	}
	
	/**
	 * Listener for when the autosaveLocation button is checked or not This sets
	 * the value for autosaving the gadget's location based on the state of this
	 * checkbox
	 */
	public SelectionListener autosaveChecked = new SelectionListener() {
	
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// change the autosave property value
			model.setGadgetAutosaveLocation(view.isGadgetAutosaveLocation());
		}
	
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
	
		}
	};
	
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
			model.dispose();
			view.dispose();
		}
	};

}
