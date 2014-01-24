package com.coddotech.teamsubb.settings.gui;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.settings.model.AppSettings;

/**
 * This class represents the GUI wrapped around the Settings class and is solely
 * used in order for the user to be able to interact with the settings of the
 * application
 * 
 * @author Coddo
 * 
 */
public final class AppSettingsWindow extends CustomWindow {

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

		// make the window a modal one
		this.setShell(new Shell(Display.getCurrent(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM));

		this.initializeComponents();
	}

	@Override
	public void dispose() {
		try {

			// user classes
			controller.dispose();
			controller = null;

			// GUI objects
			apply.dispose();
			apply = null;

			cancel.dispose();
			cancel = null;

			autosaveLocation.dispose();
			autosaveLocation = null;

			searchIntervalLabel.dispose();
			searchIntervalLabel = null;

			searchInterval.dispose();
			searchInterval = null;

			this.logDispose();

		}
		catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	/**
	 * Tells the user is the gadget is set to automatically save its own location on the screen
	 * 
	 * @return A logical value indicating the result
	 */
	public boolean isGadgetAutosaveLocation() {
		return this.autosaveLocation.getSelection();
	}

	/**
	 * Get the search interval set by the user
	 * 
	 * @return An integer representing the number of minutes set by the user
	 */
	public int getSearchInterval() {
		return Integer.parseInt(this.searchInterval.getText());
	}

	/**
	 * Verify the entered settings
	 * 
	 * @return A logical value indicating if the entered settings are correct or
	 *         not
	 */
	public boolean verifySettings() {
		MessageBox message = new MessageBox(this.getShell(), SWT.ICON_ERROR);
		int interval;

		try {
			interval = Integer.parseInt(this.searchInterval.getText());

			if (interval < 1 || interval > 60) {
				message.setText("Number error");
				message.setMessage("The entered number is out of bounds. The search interval must be an integer between 1 and 60");

				message.open();

				return false;
			}

			return true;
		}
		catch (Exception ex) {
			message.setText("Format error");
			message.setMessage("The search interval must be an INTEGER between 1 and 60");

			message.open();

			return false;
		}
	}

	@Override
	protected void updateGUI(final Observable obs, final Object obj) {
		try {

			Runnable update = new Runnable() {

				@Override
				public void run() {
					String[] data = ((String) obj).split(CustomWindow.NOTIFICATION_SEPARATOR);

					switch (data[0]) {

					case AppSettings.MESSAGE_AUTOSAVE_LOCATION: {
						autosaveLocation.setSelection(Boolean.parseBoolean(data[1]));

					}
						break;

					case AppSettings.MESSAGE_SEARCH_INTERVAL: {
						searchInterval.setText(data[1]);

					}
						break;

					case AppSettings.MESSAGE_SAVE: {
						MessageBox message;

						if (Boolean.parseBoolean(data[1])) {
							message = new MessageBox(getShell(), SWT.ICON_INFORMATION);
							message.setText("Success");
							message.setMessage("The settings have been successfully applied !");
						}

						else {
							message = new MessageBox(getShell(), SWT.ICON_ERROR);
							message.setText("Error");
							message.setMessage("An error has been encountered while saving the changes !");
						}

						message.open();

					}
						break;
					}
				}
			};

			Display.getDefault().syncExec(update);
		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "GUI Update", ex);

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
		this.getShell().setText("Settings");
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
