package com.coddotech.teamsubb.settings;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.main.CustomWindow;

/**
 * This class represents the GUI wrapped around the Settings class and is solely
 * used in order for the user to be able to interact with the settings of the
 * application
 * 
 * @author Coddo
 * 
 */
public final class AppSettingsWindow extends CustomWindow implements Observer {

	private Button cancel;
	private Button apply;
	private Button autosaveLocation;
	private Label searchIntervalLabel;
	private Text searchInterval;

	private AppSettingsController controller;

	/**
	 * Class constructor
	 */
	public AppSettingsWindow() {
		super();
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		autosaveLocation.dispose();
		autosaveLocation = null;
	}

	/**
	 * Tells the user is the gadget is set to automatically save its own
	 * location on the screen
	 * 
	 * @return A logical value indicating the result
	 */
	public boolean isGadgetAutosaveLocation() {
		return this.autosaveLocation.getSelection();
	}

	/**
	 * Get the search interval entered by the user in the box
	 * 
	 * @return A String representing the interval (written in numbers)
	 */
	public int getSearchInterval() {
		return Integer.parseInt(this.searchInterval.getText());
	}

	/**
	 * Get the settings class used to manage the application specific settings
	 * (from the XML file)
	 * 
	 * @return A AppSettings class instance
	 */
	public AppSettings getModel() {
		return controller.getModel();
	}

	/**
	 * Updates the interface based on the way the model has changed
	 */
	@Override
	public void update(Observable obs, Object obj) {
		String[] data = ((String) obj)
				.split(CustomWindow.NOTIFICATION_SEPARATOR);

		switch (data[0]) {
		case AppSettings.MESSAGE_AUTOSAVE_LOCATION: {
			this.autosaveLocation.setSelection(Boolean.parseBoolean(data[1]));
		}
			break;
		case AppSettings.MESSAGE_SEARCH_INTERVAL: {
			this.searchInterval.setText(data[1]);
		}
			break;
		case AppSettings.MESSAGE_SAVE: {
			MessageBox message;
			
			if(Boolean.parseBoolean(data[1])) {
				message = new MessageBox(this.getShell(), SWT.ICON_INFORMATION);
				message.setText("Success");
				message.setMessage("The settings have been successfully applied !");
			} else {
				message = new MessageBox(this.getShell(), SWT.ICON_ERROR);
				message.setText("Error");
				message.setMessage("An error has been encountered while saving the changes !");
			}
			
			message.open();
		}
			break;
		}
	}

	@Override
	protected void performInitializations() {
		controller = new AppSettingsController(this);

		cancel = new Button(this.getShell(), SWT.PUSH);
		apply = new Button(this.getShell(), SWT.PUSH);
		autosaveLocation = new Button(this.getShell(), SWT.CHECK);
		searchIntervalLabel = new Label(this.getShell(), SWT.None);
		searchInterval = new Text(this.getShell(), SWT.BORDER);
	}

	@Override
	protected void createObjectProperties() {
		autosaveLocation.setText("Automatically save the gadget's location");
		autosaveLocation.setLocation(10, 10);
		autosaveLocation.pack();

		searchIntervalLabel.setText("Job search interval (minutes):");
		searchIntervalLabel.setLocation(10, 40);
		searchIntervalLabel.pack();

		searchInterval.setSize(67, 20);
		searchInterval.setLocation(175, 38);

		cancel.setText("Cancel");
		cancel.setLocation(195, 70);
		cancel.pack();

		apply.setText("Apply");
		apply.setLocation(10, 70);
		apply.pack();
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("Application settings");
		this.getShell().setSize(258, 128);
		this.placeToCenter();
	}

	@Override
	protected void createListeners() {
		apply.addSelectionListener(controller.applyClicked);
		cancel.addSelectionListener(controller.cancelClicked);
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);
	}

}
