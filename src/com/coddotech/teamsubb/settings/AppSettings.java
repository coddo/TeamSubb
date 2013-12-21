package com.coddotech.teamsubb.settings;

import java.io.File;
import java.util.Observable;

import org.eclipse.swt.graphics.Point;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
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
public final class AppSettings extends Observable {

	private static final Point DEFAULT_LOCATION = new Point(200, 200);
	private static final boolean DEFAULT_AUTOSAVE_LOCATION = false;

	private Point gadgetLocation;
	private boolean gadgetAutosaveLocation;

	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document settingsFile;

	/**
	 * Class constructor
	 */
	public AppSettings() {
		createXMLComponents();
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
	}

	public Point getGadgetLocation() {
		return gadgetLocation;
	}

	public void setGadgetLocation(Point gadgetLocation) {
		this.gadgetLocation = gadgetLocation;
	}

	public boolean isGadgetAutosaveLocation() {
		return gadgetAutosaveLocation;
	}

	public void setGadgetAutosaveLocation(boolean gadgetAutosaveLocation) {
		this.gadgetAutosaveLocation = gadgetAutosaveLocation;
	}

	/**
	 * Restore all the settings to their default values
	 */
	public void restoreDefaultSettings() {
		// gadget position
		this.gadgetLocation = AppSettings.DEFAULT_LOCATION;

		// gadget autosave position
		this.gadgetAutosaveLocation = AppSettings.DEFAULT_AUTOSAVE_LOCATION;

		// notify the observers about this
		notifyCompleteSettings();
	}

	/**
	 * Save all the changes to the actual file on the file system.
	 * 
	 * @return A boolean value indicating if the action was finished
	 *         successfully or not
	 */
	public boolean commitChangesToFile() {
		try {
			saveGadgetLocation(gadgetLocation);
			saveGadgetAutosaveLocation(gadgetAutosaveLocation);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			StreamResult output = new StreamResult(new File("Settings.xml"));
			Source input = new DOMSource(settingsFile);

			transformer.transform(input, output);

			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Reads all the settings from the XML file and stores them in the HEAP
	 */
	public void readSettings() {
		readGadgetLocation();
		readGadgetAutosaveLocation();
	
		notifyCompleteSettings();
	}

	/**
	 * Sets the default position for the gadget
	 * 
	 * @param gadgetLocation
	 *            An org.eclipse.graphics.Point determining the default position
	 *            for the gadget
	 */
	private void saveGadgetLocation(Point gadgetLocation) {
		Element element = (Element) settingsFile.getElementsByTagName(
				"location").item(0);

		element.setAttribute("location_x", Integer.toString(gadgetLocation.x));
		element.setAttribute("location_y", Integer.toString(gadgetLocation.y));
	}

	/**
	 * Set the value that indicates whether the application should automatically
	 * save its own location on the screen or not
	 * 
	 * @param gadgetAutosaveLocation
	 *            A boolean value representing the save statement
	 */
	private void saveGadgetAutosaveLocation(boolean gadgetAutosaveLocation) {
		Element element = (Element) settingsFile.getElementsByTagName(
				"autosave_location").item(0);
		element.setAttribute("value", Boolean.toString(gadgetAutosaveLocation));
	}

	/**
	 * Get the default location for the gadget, saved in the application
	 * settings (configuration file: Settings.xml)
	 * 
	 * @return An org.eclipse.swt.graphics.Point indicating the position for the
	 *         gadget
	 */
	private void readGadgetLocation() {
		try {
			Element element = (Element) settingsFile.getElementsByTagName(
					"location").item(0);
			int x = Integer.parseInt(element.getAttribute("location_x"));
			int y = Integer.parseInt(element.getAttribute("location_y"));

			if (x < 0 || y < 0)
				this.gadgetLocation = DEFAULT_LOCATION;
			else
				this.gadgetLocation = new Point(x, y);

		} catch (Exception ex) {
			this.gadgetLocation = DEFAULT_LOCATION;
		}
	}

	/**
	 * Get a value indicating whether the application automatically saves its
	 * own location on the screen
	 * 
	 * @return A boolean value representing the save statement
	 */
	private void readGadgetAutosaveLocation() {
		Element element = (Element) settingsFile.getElementsByTagName(
				"autosave_location").item(0);
		try {
			this.gadgetAutosaveLocation = Boolean.parseBoolean(element
					.getAttribute("value"));
		} catch (Exception ex) {
			this.gadgetAutosaveLocation = DEFAULT_AUTOSAVE_LOCATION;
		}
	}

	/**
	 * Notifies the registered GUI about all the settings that are available for
	 * the application
	 */
	private void notifyCompleteSettings() {
		this.setChanged();
		
		notifyObservers(this.gadgetAutosaveLocation);
		notifyObservers(this.gadgetLocation);
	}

	/**
	 * Initialize all the components for this class
	 */
	private void createXMLComponents() {
		try { // XML builders and documents
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			settingsFile = dBuilder.parse("Settings.xml");
		} catch (Exception e) {
			// ignore any exceptions
		}
	}
}
