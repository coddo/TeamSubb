package com.coddotech.teamsubb.settings.model;

import java.io.File;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.graphics.Point;
import org.w3c.dom.Element;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.gadget.model.GadgetProfiler;
import com.coddotech.teamsubb.main.XmlHandler;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;

/**
 * Class used for managing the settings for this application.
 * 
 * <br>
 * This class is a singleton
 * 
 * @author Coddo
 * 
 */
public final class Settings extends XmlHandler {

	/*
	 * Defautl values for each all the settings
	 */
	public static final Point DEFAULT_LOCATION = new Point(200, 200);

	public static final boolean DEFAULT_AUTOMATIC_LOGIN = false;
	public static final boolean DEFAULT_AUTOSAVE_LOCATION = true;

	public static final int DEFAULT_SEARCH_INTERVAL = 1; // one minute
	public static final int DEFAULT_GADGET_PROFILE = 2;

	public static final String SAVE = "save changes";
	public static final String LOCATION = "location";
	public static final String AUTOSAVE_LOCATION = "autosave_location";
	public static final String AUTOMATIC_LOGIN = "automatic_login";
	public static final String SEARCH_INTERVAL = "search_interval";
	public static final String GADGET_PROFILE = "gadget_profile";

	/*
	 * All the settings are retained in the apps memory
	 */
	private Point gadgetLocation;
	private boolean gadgetAutosaveLocation;
	private boolean automaticLogin;
	private int searchInterval;
	private int gadgetProfile;

	private static Settings instance = null;

	/**
	 * Class constructor
	 */
	private Settings() {
		createXMLComponents("Settings.xml");
	}

	/**
	 * Get the existing instance for this class
	 */
	public static Settings getInstance() {
		if (instance == null)
			instance = new Settings();

		return instance;
	}

	/**
	 * Clear memory from this class and its resources
	 */
	public void dispose() {
		try {

			// clear flieds
			dbFactory = null;
			dBuilder = null;
			xmlFile = null;
			gadgetLocation = null;

			ActivityLogger.logActivity(this.getClass().getName(), "Dispose");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Dispose", ex);

		}
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

	public boolean isAutomaticLogin() {
		return this.automaticLogin;
	}

	public void setAutomaticLogin(boolean automaticLogin) {
		this.automaticLogin = automaticLogin;
	}

	public int getSearchInterval() {
		return this.searchInterval;
	}

	public void setSearchInterval(int searchInterval) {
		this.searchInterval = searchInterval;
	}

	public int getGadgetProfile() {
		return this.gadgetProfile;
	}

	public void setGadgetProfile(int gadgetProfile) {
		this.gadgetProfile = gadgetProfile;

		// generate a preview of the new size by notifying the gadget with the new size
		notifyGadgetProfile();
	}

	/**
	 * Restore all the settings to their default values
	 */
	public void restoreDefaultSettings() {
		// gadget position
		this.gadgetLocation = Settings.DEFAULT_LOCATION;

		// gadget autosave position
		this.gadgetAutosaveLocation = Settings.DEFAULT_AUTOSAVE_LOCATION;

		// job search interval
		this.searchInterval = Settings.DEFAULT_SEARCH_INTERVAL;

		// gadget profile
		this.gadgetProfile = Settings.DEFAULT_GADGET_PROFILE;

		// notify the observers about this
		notifyCompleteSettings();
	}

	/**
	 * Save all the changes to the actual file on the file system.
	 * 
	 * @return A boolean value indicating if the action was finished successfully or not
	 */
	public void saveSettings() {
		NotificationEntity notif = null;

		try {
			saveGadgetLocation(this.gadgetLocation);
			saveGadgetAutosaveLocation(this.gadgetAutosaveLocation);
			saveAutomaticLogin(this.automaticLogin);
			saveSearchInterval(this.searchInterval);
			saveGadgetProfile(this.gadgetProfile);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StreamResult output = new StreamResult(new File("Settings.xml"));

			Source input = new DOMSource(xmlFile);

			transformer.transform(input, output);

			notif = new NotificationEntity(Settings.SAVE, true);

			ActivityLogger.logActivity(this.getClass().getName(), "Commit changes to file");

		}

		catch (Exception ex) {
			notif = new NotificationEntity(Settings.SAVE, false);

			ActivityLogger.logException(this.getClass().getName(), "Commit changes to file", ex);
		}

		finally {
			this.setChanged();
			notifyObservers(notif);

		}
	}

	/**
	 * Reads all the settings from the XML file and stores them in the HEAP
	 */
	public void readSettings() {
		try {
			readGadgetLocation();
			readGadgetAutosaveLocation();
			readAutomaticLogin();
			readSearchInterval();
			readGadgetProfile();

			ActivityLogger.logActivity(this.getClass().getName(), "Read settings");

		}

		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Read settings", ex);

			restoreDefaultSettings();
		}

		finally {
			notifyCompleteSettings();

		}
	}

	/**
	 * Save the new position for the gadget into the XML settings file
	 * 
	 * @param gadgetLocation
	 *            An org.eclipse.graphics.Point determining the default position for the gadget
	 */
	private void saveGadgetLocation(Point gadgetLocation) {
		Element element = (Element) xmlFile.getElementsByTagName(Settings.LOCATION).item(0);

		element.setAttribute("location_x", Integer.toString(gadgetLocation.x));
		element.setAttribute("location_y", Integer.toString(gadgetLocation.y));
	}

	/**
	 * Save the setting that tells the app whether to save the gadget's location
	 * automatically into the XML settings file
	 * 
	 * @param gadgetAutosaveLocation
	 *            A boolean value representing the save statement
	 */
	private void saveGadgetAutosaveLocation(boolean gadgetAutosaveLocation) {
		Element element = (Element) xmlFile.getElementsByTagName(Settings.AUTOSAVE_LOCATION).item(0);
		element.setAttribute("value", Boolean.toString(gadgetAutosaveLocation));
	}

	/**
	 * Save the logical value which tells the app whether the user has chosen the login process to
	 * be automatic or not
	 * 
	 * @param automaticLogin
	 *            The logical value to be stored
	 */
	private void saveAutomaticLogin(boolean automaticLogin) {
		Element element = (Element) xmlFile.getElementsByTagName(Settings.AUTOMATIC_LOGIN).item(0);
		element.setAttribute("value", Boolean.toString(automaticLogin));
	}

	/**
	 * Save the interval used for searching new jobs into the XML settings file
	 * 
	 * @param searchInterval
	 *            The interval to be written to the file
	 */
	private void saveSearchInterval(int searchInterval) {
		Element element = (Element) xmlFile.getElementsByTagName(Settings.SEARCH_INTERVAL).item(0);
		element.setAttribute("value", Integer.toString(searchInterval));
	}

	/**
	 * Save the gadget profile ID that has been selected by the user.
	 * 
	 * @param gadgetProfile2
	 *            The ID of the profile (Integer)
	 */
	private void saveGadgetProfile(int gadgetProfile2) {
		Element element = (Element) xmlFile.getElementsByTagName(Settings.GADGET_PROFILE).item(0);
		element.setAttribute("value", Integer.toString(this.gadgetProfile));
	}

	/**
	 * Read the default location for the gadget from the application's settings
	 * XML file
	 * 
	 * @return An org.eclipse.swt.graphics.Point indicating the position for the gadget
	 */
	private void readGadgetLocation() {
		try {
			Element element = (Element) xmlFile.getElementsByTagName(Settings.LOCATION).item(0);

			int x = Integer.parseInt(element.getAttribute("location_x"));
			int y = Integer.parseInt(element.getAttribute("location_y"));

			if (x < 0 || y < 0)
				this.gadgetLocation = DEFAULT_LOCATION;

			else
				this.gadgetLocation = new Point(x, y);

		}

		catch (Exception ex) {
			this.gadgetLocation = DEFAULT_LOCATION;

		}
	}

	/**
	 * Read the value indicating whether the application automatically saves its own location on the
	 * screen from the XML settings file
	 */
	private void readGadgetAutosaveLocation() {
		Element element = (Element) xmlFile.getElementsByTagName(Settings.AUTOSAVE_LOCATION).item(0);

		try {
			this.gadgetAutosaveLocation = Boolean.parseBoolean(element.getAttribute("value"));

		}

		catch (Exception ex) {
			this.gadgetAutosaveLocation = Settings.DEFAULT_AUTOSAVE_LOCATION;

		}
	}

	/**
	 * Read the value telling the app whether to login the user automatically using the data stored
	 * in the login file
	 */
	private void readAutomaticLogin() {
		Element element = (Element) xmlFile.getElementsByTagName(Settings.AUTOMATIC_LOGIN).item(0);

		try {
			this.automaticLogin = Boolean.parseBoolean(element.getAttribute("value"));
		}

		catch (Exception ex) {
			this.automaticLogin = Settings.DEFAULT_AUTOMATIC_LOGIN;
		}
	}

	/**
	 * Read the value representing the search interval used by the app in order
	 * to find new jobs for the user from the XML settings file
	 */
	private void readSearchInterval() {
		Element element = (Element) xmlFile.getElementsByTagName(Settings.SEARCH_INTERVAL).item(0);

		try {
			this.searchInterval = Integer.parseInt(element.getAttribute("value"));

		}

		catch (Exception ex) {
			this.searchInterval = Settings.DEFAULT_SEARCH_INTERVAL;

		}
	}

	/**
	 * Read the value representing the ID of the profile used for the gadget size.
	 */
	private void readGadgetProfile() {
		Element element = (Element) xmlFile.getElementsByTagName(Settings.GADGET_PROFILE).item(0);

		try {
			this.gadgetProfile = Integer.parseInt(element.getAttribute("value"));

			GadgetProfiler.getInstance().select(this.gadgetProfile);

		}

		catch (Exception ex) {
			this.gadgetProfile = Settings.DEFAULT_GADGET_PROFILE;
		}
	}

	/**
	 * Notifies the registered GUI about all the settings that are available for
	 * the application
	 */
	private void notifyCompleteSettings() {
		notifyAutosaveLocation();

		notifyAutomaticLogin();

		notifyLocation();

		notifySearchInterval();

		notifyGadgetProfile();
	}

	private void notifyAutosaveLocation() {
		NotificationEntity notif = new NotificationEntity(Settings.AUTOSAVE_LOCATION, this.gadgetAutosaveLocation);

		this.setChanged();
		notifyObservers(notif);
	}

	private void notifyAutomaticLogin() {
		NotificationEntity notif = new NotificationEntity(Settings.AUTOMATIC_LOGIN, this.automaticLogin);

		this.setChanged();
		notifyObservers(notif);
	}

	private void notifyLocation() {
		NotificationEntity notif = new NotificationEntity(Settings.LOCATION, this.gadgetLocation.x + ","
				+ gadgetLocation.y);

		this.setChanged();
		notifyObservers(notif);
	}

	private void notifySearchInterval() {
		NotificationEntity notif = new NotificationEntity(Settings.SEARCH_INTERVAL, this.searchInterval);

		this.setChanged();
		notifyObservers(notif);
	}

	private void notifyGadgetProfile() {
		NotificationEntity notif = new NotificationEntity(Settings.GADGET_PROFILE, this.gadgetProfile);

		this.setChanged();
		notifyObservers(notif);
	}

}
