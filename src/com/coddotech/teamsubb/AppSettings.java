package com.coddotech.teamsubb;

import org.eclipse.swt.graphics.Point;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class used for managing the settings for this application
 * 
 * @author Coddo
 * 
 */
public final class AppSettings {

	private static final Point LOCATION_DEFAULT = new Point(200, 200);
	private static final boolean AUTOSAVE_LOCATION_DEFAULT = false;

	private Element element; // used for reading document data

	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document settingsFile;

	/**
	 * Class constructor
	 */
	public AppSettings() {
		initializeComponents();
	}

	/**
	 * Clear memory from this class and its resources
	 */
	public void dispose() {
		dbFactory = null;
		dBuilder = null;
		settingsFile = null;
		element = null;
	}

	/**
	 * Get the default location for the gadget, saved in the application
	 * settings (configuration file: Settings.xml)
	 * 
	 * @return An org.eclipse.swt.graphics.Point indicating the position for the
	 *         gadget
	 */
	public Point getGadgetLocation() {
		element = (Element) settingsFile.getElementsByTagName("location").item(
				0);
		int x = Integer.parseInt(element.getAttribute("location_x"));
		int y = Integer.parseInt(element.getAttribute("location_y"));

		if (x < 0 || y < 0) {
			this.setGadgetLocation(AppSettings.LOCATION_DEFAULT);

			return AppSettings.LOCATION_DEFAULT;
		} else
			return new Point(x, y);
	}

	/**
	 * Sets the default position for the gadget
	 * 
	 * @param gadgetLocation
	 *            An org.eclipse.graphics.Point determining the default position
	 *            for the gadget
	 */
	public void setGadgetLocation(Point gadgetLocation) {
		element = (Element) settingsFile.getElementsByTagName("location").item(
				0);

		element.setAttribute("location_x", Integer.toString(gadgetLocation.x));
		element.setAttribute("location_y", Integer.toString(gadgetLocation.y));
	}

	/**
	 * Get a value indicating whether the application automatically saves its
	 * own location on the screen
	 * 
	 * @return A boolean value representing the save statement
	 */
	public boolean getGadgetAutosaveLocation() {
		element = (Element) settingsFile.getElementsByTagName(
				"autosave_location").item(0);
		return Boolean.parseBoolean(element.getAttribute("value"));
	}

	/**
	 * Set the value that indicates whether the application should automatically
	 * save its own location on the screen or not
	 * 
	 * @param value
	 *            A boolean value representing the save statement
	 */
	public void setGadgetAutosaveLocation(boolean value) {
		element = (Element) settingsFile.getElementsByTagName(
				"autosave_location").item(0);
		element.setAttribute("value", Boolean.toString(value));
	}

	/**
	 * Restore all the settings to their default values
	 */
	public void restoreDefaultSettings() {
		// gadget position
		setGadgetLocation(AppSettings.LOCATION_DEFAULT);

		// gadget autosave position
		setGadgetAutosaveLocation(AppSettings.AUTOSAVE_LOCATION_DEFAULT);
	}

	/**
	 * Initialize all the components for this class
	 */
	private void initializeComponents() {
		try { // XML builders and documents
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			settingsFile = dBuilder.parse("Settings.xml");
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // All the other components

		}
	}

}
