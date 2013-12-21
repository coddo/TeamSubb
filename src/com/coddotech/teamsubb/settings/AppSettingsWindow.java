package com.coddotech.teamsubb.settings;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;

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

	private Button gadgetAutosaveLocation;

	private AppSettingsController controller;

	private Point gadgetLocation;

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
		gadgetAutosaveLocation.dispose();
		gadgetAutosaveLocation = null;
	}

	/**
	 * Tells the user is the gadget is set to automatically save its own
	 * location on the screen
	 * 
	 * @return A logical value indicating the result
	 */
	public boolean isGadgetAutosaveLocation() {
		return this.gadgetAutosaveLocation.getSelection();
	}

	/**
	 * Retrieve the location the is stored for the gadget
	 * 
	 * @return A Point variable indicating the saved location for the gadget
	 *         window
	 */
	public Point getGadgetLocation() {
		return this.gadgetLocation;
	}

	/**
	 * Updates the interface based on the way the model has changed
	 */
	@Override
	public void update(Observable obs, Object obj) {
		if (obj instanceof Boolean) {
			this.gadgetAutosaveLocation.setSelection((Boolean) obj);
		} else if (obj instanceof Point) {
			this.gadgetLocation = (Point) obj;
		}
	}

	@Override
	protected void performInitializations() {
		controller = new AppSettingsController(this);
		gadgetAutosaveLocation = new Button(this.getShell(), SWT.CHECK);
	}

	@Override
	protected void createObjectProperties() {
		gadgetAutosaveLocation
				.setText("Automatically save the gadget's location");
		gadgetAutosaveLocation.setLocation(10, 10);
		gadgetAutosaveLocation.pack();
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("Application settings");
		this.placeToCenter();
		this.getShell().setSize(260, 70);
	}

	@Override
	protected void createListeners() {
		gadgetAutosaveLocation.addSelectionListener(controller.autosaveChecked);
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);
	}

}
