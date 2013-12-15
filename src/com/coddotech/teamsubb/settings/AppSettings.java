package com.coddotech.teamsubb.settings;

import java.io.File;

import org.eclipse.swt.graphics.Point;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class used for managing the settings for this application
 * 
 * @author Coddo
 * 
 */
public final class AppSettings {

	private static final Point DEFAULT_LOCATION = new Point(200, 200);
	private static final boolean DEFAULT_AUTOSAVE_LOCATION = false;

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
		// save changes to the settings file
		this.commitChangesToFile();

		// clear flieds
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
		try {
			element = (Element) settingsFile.getElementsByTagName("location")
					.item(0);
			int x = Integer.parseInt(element.getAttribute("location_x"));
			int y = Integer.parseInt(element.getAttribute("location_y"));

			if (x < 0 || y < 0) {
				this.setGadgetLocation(AppSettings.DEFAULT_LOCATION);

				return AppSettings.DEFAULT_LOCATION;
			} else
				return new Point(x, y);
		} catch (Exception ex) {
			return AppSettings.DEFAULT_LOCATION;
		}
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
		try {
			return Boolean.parseBoolean(element.getAttribute("value"));
		} catch (Exception ex) {
			return AppSettings.DEFAULT_AUTOSAVE_LOCATION;
		}
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
		setGadgetLocation(AppSettings.DEFAULT_LOCATION);

		// gadget autosave position
		setGadgetAutosaveLocation(AppSettings.DEFAULT_AUTOSAVE_LOCATION);
	}

	/**
	 * Save all the changes to the actual file on the file system.
	 * 
	 * @return A boolean value indicating if the action was finished
	 *         successfully or not
	 */
	public boolean commitChangesToFile() {
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			StreamResult output = new StreamResult(new File("Settings.xml"));
			Source input = new DOMSource(settingsFile);

			transformer.transform(input, output);

			return true;
		} catch (TransformerConfigurationException e) {
			return false;
		} catch (TransformerFactoryConfigurationError e) {
			return false;
		} catch (TransformerException e) {
			return false;
		}
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
			// ignore any exceptions
		} finally { // All the other components

		}
	}
}
