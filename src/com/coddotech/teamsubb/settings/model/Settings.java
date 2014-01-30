package com.coddotech.teamsubb.settings.model;

import java.io.File;
import java.util.Observable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.graphics.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.gadget.model.GadgetProfiler;
import com.coddotech.teamsubb.jobs.gui.JobWindow;
import com.coddotech.teamsubb.main.CustomWindow;

/**
 * Class used for managing the settings for this application.
 * 
 * <br>
 * This class is a singleton
 * 
 * @author Coddo
 * 
 */
public final class Settings extends Observable {

	/*
	 * Defautl values for each all the settings
	 */
	public static final Point DEFAULT_LOCATION = new Point(200, 200);
	public static final boolean DEFAULT_AUTOSAVE_LOCATION = true;
	public static final int DEFAULT_SEARCH_INTERVAL = 1; // one minute
	public static final int DEFAULT_GADGET_PROFILE = 2;

	public static final boolean[] DEFAULT_USER_JOBS = { false, false, false, false, false, false, false };
	public static final String[] DEFAULT_USER_INFO = { "NONE", "NONE", "NONE" };

	/*
	 * Constants representing the settings headers from the XML file and other Strings that are used
	 * in order to relay what type of notification is sent to the observers
	 */
	public static final String MESSAGE_SAVE = "save changes";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_AUTOSAVE_LOCATION = "autosave_location";
	public static final String MESSAGE_SEARCH_INTERVAL = "search_interval";
	public static final String MESSAGE_GADGET_PROFILE = "gadget_profile";

	/*
	 * All the settings are retained in the apps memory
	 */
	private Point gadgetLocation;
	private boolean gadgetAutosaveLocation;
	private int searchInterval;
	private int gadgetProfile;

	private String[] userInfo;
	private boolean[] userJobs;

	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document settingsFile;

	private static Settings instance = null;

	/**
	 * Class constructor
	 */
	private Settings() {
		createXMLComponents();
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
			settingsFile = null;
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

	public int getSearchInterval() {
		return this.searchInterval;
	}

	public void setSearchInterval(int searchInterval) {
		this.searchInterval = searchInterval;
	}

	public boolean[] getUserJobsRawData() {
		return this.userJobs;
	}

	public int getGadgetProfile() {
		return this.gadgetProfile;
	}

	public void setGadgetProfile(int gadgetProfile) {
		this.gadgetProfile = gadgetProfile;

		// generate a preview of the new size by notifying the gadget with the new size
		notifyGadgetProfile();
	}

	public String[] getUserJobs() {
		int available = 0;

		for (int i = 0; i < this.userJobs.length; i++) {

			if (this.userJobs[i])
				available++;

		}

		int counter = 0;

		String[] userJobs = new String[available];

		for (int i = 0; i < this.userJobs.length; i++) {

			if (this.userJobs[i]) {
				userJobs[counter] = JobWindow.DEFAULT_JOBS_INFO_HEADERS[i];
				counter++;

			}

		}

		return userJobs;

	}

	public void setUserJobs(boolean[] userJobs) {
		this.userJobs = userJobs;
	}

	public String[] getUserInfo() {
		return this.userInfo;
	}

	public String getUserName() {
		return Settings.getInstance().getUserInfo()[0];

	}

	public void setUserInfo(String[] userInfo) {
		this.userInfo = userInfo;
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

		// user jobs
		this.userJobs = Settings.DEFAULT_USER_JOBS;

		// user info
		this.userInfo = Settings.DEFAULT_USER_INFO;

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
	public void commitChangesToFile() {
		try {
			saveGadgetLocation(this.gadgetLocation);
			saveGadgetAutosaveLocation(this.gadgetAutosaveLocation);
			saveSearchInterval(this.searchInterval);
			saveGadgetProfile(this.gadgetProfile);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StreamResult output = new StreamResult(new File("Settings.xml"));

			Source input = new DOMSource(settingsFile);

			transformer.transform(input, output);

			this.setChanged();
			notifyObservers(Settings.MESSAGE_SAVE + CustomWindow.NOTIFICATION_SEPARATOR + true);

			ActivityLogger.logActivity(this.getClass().getName(), "Commit changes to file");

		}
		catch (Exception ex) {
			this.setChanged();
			notifyObservers(Settings.MESSAGE_SAVE + CustomWindow.NOTIFICATION_SEPARATOR + false);

			ActivityLogger.logException(this.getClass().getName(), "Commit changes to file", ex);
		}
	}

	/**
	 * Reads all the settings from the XML file and stores them in the HEAP
	 */
	public void readSettings() {
		try {
			readGadgetLocation();
			readGadgetAutosaveLocation();
			readSearchInterval();
			readGadgetProfile();

			notifyCompleteSettings();

			ActivityLogger.logActivity(this.getClass().getName(), "Read settings");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Read settings", ex);

		}
	}

	/**
	 * Save the new position for the gadget into the XML settings file
	 * 
	 * @param gadgetLocation
	 *            An org.eclipse.graphics.Point determining the default position for the gadget
	 */
	private void saveGadgetLocation(Point gadgetLocation) {
		Element element = (Element) settingsFile.getElementsByTagName("location").item(0);

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
		Element element = (Element) settingsFile.getElementsByTagName("autosave_location").item(0);
		element.setAttribute("value", Boolean.toString(gadgetAutosaveLocation));
	}

	/**
	 * Save the interval used for searching new jobs into the XML settings file
	 * 
	 * @param searchInterval
	 *            The interval to be written to the file
	 */
	private void saveSearchInterval(int searchInterval) {
		Element element = (Element) settingsFile.getElementsByTagName("search_interval").item(0);
		element.setAttribute("value", Integer.toString(searchInterval));
	}

	private void saveGadgetProfile(int gadgetProfile2) {
		Element element = (Element) settingsFile.getElementsByTagName("gadget_profile").item(0);
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
			Element element = (Element) settingsFile.getElementsByTagName("location").item(0);

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
		Element element = (Element) settingsFile.getElementsByTagName("autosave_location").item(0);

		try {
			this.gadgetAutosaveLocation = Boolean.parseBoolean(element.getAttribute("value"));

		}

		catch (Exception ex) {
			this.gadgetAutosaveLocation = Settings.DEFAULT_AUTOSAVE_LOCATION;

		}
	}

	/**
	 * Read the value representing the search interval used by the app in order
	 * to find new jobs for the user from the XML settings file
	 */
	private void readSearchInterval() {
		Element element = (Element) settingsFile.getElementsByTagName("search_interval").item(0);

		try {
			this.searchInterval = Integer.parseInt(element.getAttribute("value"));

		}

		catch (Exception ex) {
			this.searchInterval = Settings.DEFAULT_SEARCH_INTERVAL;

		}
	}

	private void readGadgetProfile() {
		Element element = (Element) settingsFile.getElementsByTagName("gadget_profile").item(0);

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

		notifyLocation();

		notifySearchInterval();

		notifyGadgetProfile();
	}

	private void notifyAutosaveLocation() {
		this.setChanged();
		notifyObservers(Settings.MESSAGE_AUTOSAVE_LOCATION + CustomWindow.NOTIFICATION_SEPARATOR
				+ this.gadgetAutosaveLocation);
	}

	private void notifyLocation() {
		this.setChanged();
		notifyObservers(Settings.MESSAGE_LOCATION + CustomWindow.NOTIFICATION_SEPARATOR
				+ this.gadgetLocation.x + "," + gadgetLocation.y);
	}

	private void notifySearchInterval() {
		this.setChanged();
		notifyObservers(Settings.MESSAGE_SEARCH_INTERVAL + CustomWindow.NOTIFICATION_SEPARATOR
				+ this.searchInterval);
	}

	private void notifyGadgetProfile() {
		this.setChanged();
		notifyObservers(Settings.MESSAGE_GADGET_PROFILE + CustomWindow.NOTIFICATION_SEPARATOR
				+ this.gadgetProfile);
	}

	/**
	 * Initialize the XML builders and files in which to persist the application settings
	 */
	private void createXMLComponents() {
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			settingsFile = dBuilder.parse(System.getProperty("user.dir") + File.separator + "Settings.xml");

			ActivityLogger.logActivity(this.getClass().getName(), "XML components creation");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "XML components creation", ex);

		}
	}
}
