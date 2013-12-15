package com.coddotech.teamsubb.settings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.main.CustomWindow;

/**
 * This class represents the GUI wrapped around the Settings class and is solely
 * used in order for the user to be able to interact with the settings of the
 * application
 * 
 * @author Coddo
 * 
 */
public final class AppSettingsWindow extends CustomWindow {

	private Button autosaveLocation;

	private AppSettings settings;

	/**
	 * Class constructor
	 */
	public AppSettingsWindow() {
		super();
		initializeComponents();
	}

	/**
	 * Clear the memory from this class and its components
	 */
	private void dispose() {
		settings.dispose();

		autosaveLocation.dispose();
	}

	/**
	 * Listener for when the autosaveLocation button is checked or not This sets
	 * the value for autosaving the gadget's location based on the state of this
	 * checkbox
	 */
	private SelectionListener autosaveChecked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// change the autosave property value
			settings.setGadgetAutosaveLocation(autosaveLocation.getSelection());
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the shell is closing in order to know when to start
	 * disposing all the components for this class
	 */
	private Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			dispose();

		}
	};

	/**
	 * Initialize all the components for this class and set their initial
	 * properties (default ones)
	 */
	private void initializeComponents() {
		// initializations
		settings = new AppSettings();
		autosaveLocation = new Button(this.getShell(), SWT.CHECK);

		// object properties
		autosaveLocation.setText("Automatically save the gadget's location");
		autosaveLocation.setLocation(10, 10);
		autosaveLocation.setSelection(settings.getGadgetAutosaveLocation());
		autosaveLocation.pack();

		// shell properties
		this.getShell().setText("Application settings");
		this.placeToCenter();
		this.getShell().setSize(260, 70);
		this.getShell().addListener(SWT.Close, shellClosingListener);

		// listeners
		autosaveLocation.addSelectionListener(autosaveChecked);
	}

}
