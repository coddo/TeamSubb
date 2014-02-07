package com.coddotech.teamsubb.settings.gui;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.gadget.model.GadgetProfiler;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;
import com.coddotech.teamsubb.settings.model.Settings;

/**
 * This class represents the GUI wrapped around the Settings class and is solely
 * used in order for the user to be able to interact with the settings of the
 * application
 * 
 * @author Coddo
 * 
 */
public final class SettingsWindow extends CustomWindow {

	private Composite panel;

	private Button close;
	private Button apply;
	private Button autosaveLocation;
	private Button automaticLogin;
	private Button restoreDefaults;

	private Label searchIntervalLabel;
	private Label gadgetProfileLabel;

	private Text searchInterval;

	private Combo gadgetProfile;

	private boolean changed = false;

	private SettingsController controller;

	/**
	 * Class constructor
	 */
	public SettingsWindow() {
		super();

		// make the window a modal one
		this.setShell(new Shell(Display.getDefault(), SWT.APPLICATION_MODAL | SWT.SHELL_TRIM));

		this.initializeComponents();
	}

	@Override
	public void dispose() {
		try {

			// user classes
			controller.dispose();

			// GUI objects
			apply.dispose();
			close.dispose();
			restoreDefaults.dispose();

			autosaveLocation.dispose();
			automaticLogin.dispose();

			searchIntervalLabel.dispose();
			searchInterval.dispose();

			gadgetProfileLabel.dispose();
			gadgetProfile.dispose();

			panel.dispose();

			// log the disposal of this class
			this.logDispose();

		}
		catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	/**
	 * Get the value indicating whether the gadget shoudl automatically save its location
	 * 
	 * @return A logical value
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
	 * Get the ID of the selected profile (the index of the selected item)
	 * 
	 * @return An integer value
	 */
	public int getSelectedProfile() {
		return gadgetProfile.getSelectionIndex();
	}
	
	/**
	 * Get the automatic login selection valeu
	 * 
	 * @return A logical value
	 */
	public boolean isAutomaticLogin() {
		return automaticLogin.getSelection();
	}

	/**
	 * Get the value indicating whether the settings have been altered
	 * 
	 * @return A logical value
	 */
	public boolean isChanged() {
		return this.changed;
	}

	/**
	 * Mark the settings as having been changed
	 */
	public void setAsChanged(boolean value) {
		this.changed = value;
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

	/**
	 * Display a message asking the user whether to save all the changes
	 * 
	 * @return A Logical value indicating if the user has pressed YES(true) or NO(false)
	 */
	public boolean displaySaveChangesQBox() {
		MessageBox message = new MessageBox(this.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);

		message.setText("Unsaved changes");
		message.setMessage("The are unsaved changes. Do you want to save them now ?");

		return (message.open() == SWT.YES);
	}

	@Override
	protected void updateGUI(final Observable obs, final Object obj) {
		try {

			Runnable update = new Runnable() {

				@Override
				public void run() {
					NotificationEntity notif = (NotificationEntity) obj;

					switch (notif.getMessage()) {

						case Settings.AUTOSAVE_LOCATION: {
							autosaveLocation.setSelection(notif.getBoolean());

						}
							break;

						case Settings.AUTOMATIC_LOGIN: {
							automaticLogin.setSelection(notif.getBoolean());
						}
							break;

						case Settings.SEARCH_INTERVAL: {
							searchInterval.setText(notif.getString());

						}
							break;

						case Settings.GADGET_PROFILE: {
							gadgetProfile.select(notif.getInteger());

						}
							break;

						case Settings.SAVE: {
							MessageBox message;

							if (notif.getBoolean()) {
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
		controller = new SettingsController(this);

		panel = new Composite(this.getShell(), SWT.BORDER);

		autosaveLocation = new Button(this.panel, SWT.CHECK);
		automaticLogin = new Button(this.panel, SWT.CHECK);

		searchIntervalLabel = new Label(this.panel, SWT.None);
		searchInterval = new Text(this.panel, SWT.BORDER);

		gadgetProfileLabel = new Label(this.panel, SWT.None);
		gadgetProfile = new Combo(this.panel, SWT.READ_ONLY);

		apply = new Button(this.getShell(), SWT.PUSH);
		restoreDefaults = new Button(this.getShell(), SWT.PUSH);
		close = new Button(this.getShell(), SWT.PUSH);

	}

	@Override
	protected void createObjectProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 3));
		panel.setLayout(layout);

		autosaveLocation.setFont(CustomWindow.DEFAULT_FONT);
		autosaveLocation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		autosaveLocation.setText("Automatically save the gadget's location");
		autosaveLocation.pack();
		
		automaticLogin.setFont(CustomWindow.DEFAULT_FONT);
		automaticLogin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true ,false, 2, 1));
		automaticLogin.setEnabled(Settings.getInstance().isAutomaticLogin());
		automaticLogin.setText("Login automatically");
		automaticLogin.pack();

		searchIntervalLabel.setFont(CustomWindow.DEFAULT_FONT);
		searchIntervalLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		searchIntervalLabel.setText("Job search interval (minutes):");
		searchIntervalLabel.pack();

		searchInterval.setFont(CustomWindow.DEFAULT_FONT);
		searchInterval.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		gadgetProfileLabel.setFont(CustomWindow.DEFAULT_FONT);
		gadgetProfileLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		gadgetProfileLabel.setText("Gadget size:");
		gadgetProfileLabel.pack();

		gadgetProfile.setFont(CustomWindow.DEFAULT_FONT);
		gadgetProfile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		gadgetProfile.setItems(GadgetProfiler.getInstance().getProfiles());

		close.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		close.setFont(CustomWindow.DEFAULT_FONT);
		close.setText("Close");
		close.pack();

		apply.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		apply.setFont(CustomWindow.DEFAULT_FONT);
		apply.setText("Apply");
		apply.pack();

		restoreDefaults.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		restoreDefaults.setFont(CustomWindow.DEFAULT_FONT);
		restoreDefaults.setText("Restore defaults");
		restoreDefaults.pack();
	}

	@Override
	protected void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = true;

		this.getShell().setLayout(layout);
		this.getShell().setText("Settings");
		this.getShell().setSize(400, 205);
		this.placeToCenter();
	}

	@Override
	protected void createListeners() {
		apply.addSelectionListener(controller.applyClicked);

		close.addSelectionListener(controller.closeClicked);

		restoreDefaults.addSelectionListener(controller.restoreDefaultsClicked);

		autosaveLocation.addListener(SWT.Selection, controller.modificationListener);
		automaticLogin.addListener(SWT.Selection, controller.modificationListener);
		searchInterval.addListener(SWT.KeyDown, controller.modificationListener);

		gadgetProfile.addListener(SWT.Selection, controller.modificationListener);
		gadgetProfile.addSelectionListener(controller.profileSelected);

		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);
	}

}
