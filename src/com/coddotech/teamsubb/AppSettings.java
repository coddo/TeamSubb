package com.coddotech.teamsubb;

import java.util.prefs.*;

import org.eclipse.swt.graphics.Point;

/**
 * Class used for managing the settings for this application
 * 
 * @author Coddo
 *
 */
public class AppSettings {

	private static final int X_DEFAULT = 200;
	private static final int Y_DEFAULT = 200;

	private Preferences prefs;
	private Point gadgetLocation;

	/**
	 * Get the default location for the gadget, saved in 
	 * the application settings (preferences API)
	 * 
	 * @return An org.eclipse.swt.graphics.Point indicating
	 * the position for the gadget
	 */
	public Point getGadgetLocation() {
		return gadgetLocation;
	}

	/**
	 * Sets the default position for the gadget
	 * 
	 * @param widgetLocation An org.eclipse.graphics.Point determining the 
	 * default position for the gadget
	 */
	public void setGadgetLocation(Point widgetLocation) {
		this.gadgetLocation = widgetLocation;
	}

	/**
	 * Class constructor
	 */
	public AppSettings() {
		prefs = Preferences.userNodeForPackage(WindowsGadget.class);
	}

	/**
	 * Clear memory from this class and its resources
	 */
	public void dispose() {
		//TO-DO: Dispose special objects and other undisposable stuff
	}

	/**
	 * Read the settings from the preferences API
	 */
	public void readSettings() throws BackingStoreException {
		// sync the file before reading
		try {
			prefs.sync();
		} catch (BackingStoreException ex) {
		} finally {
			int x = prefs.getInt("location_x", X_DEFAULT);
			int y = prefs.getInt("location_y", Y_DEFAULT);

			gadgetLocation = new Point(x, y);

			// in case of defaulted settings (or inexistent)
			// always save the settings after reading them
			saveSettings();

			// dispose of local objects
			
		}
	}

	/**
	 * Restore all the settings to their default values
	 */
	public void restoreDefaultSettings() {
		prefs.putInt("location_x", X_DEFAULT);
		prefs.putInt("location_y", Y_DEFAULT);

		// also save them to the preferences API
		saveSettings();
	}

	/**
	 * Save the current settings to the preferences API
	 */
	public void saveSettings() {
		prefs.putInt("location_x", gadgetLocation.x);
		prefs.putInt("location_y", gadgetLocation.y);
	}

}
