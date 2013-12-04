package com.coddotech.teamsubb;

import org.eclipse.swt.graphics.Point;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
/**
 * Class used for managing the settings for this application
 * 
 * @author Coddo
 *
 */
public class AppSettings {

	private static final int X_DEFAULT = 200;
	private static final int Y_DEFAULT = 200;

	private Point gadgetLocation;
	
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document jobDetailsFile;
	private Document settingsFile;

	/**
	 * Get the default location for the gadget, saved in 
	 * the application settings (configuration file: Settings.xml)
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
		initializeComponents();
	}

	/**
	 * Clear memory from this class and its resources
	 */
	public void dispose() {
		dbFactory = null;
		dBuilder = null;
		jobDetailsFile = null;
		settingsFile = null;
	}

	/**
	 * Read the settings from the configuration file
	 */
	public void readSettings(){
		//gadget location
		 Element element = (Element) settingsFile.getElementsByTagName("location").item(0);
		 int x = Integer.parseInt(element.getAttribute("location_x"));
		 int y = Integer.parseInt(element.getAttribute("location_y"));
		 gadgetLocation = new Point(x,y);
	}

	/**
	 * Restore all the settings to their default values
	 */
	public void restoreDefaultSettings() {
		// also save them to the preferences API
		saveSettings();
	}

	/**
	 * Save the current settings to the configuration file
	 */
	public void saveSettings() {
		
	}
	
	/**
	 * Initialize all the components for this class
	 */
	private void initializeComponents(){
		try { //XML builders and documents
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			jobDetailsFile = dBuilder.parse("JobDetails.xml");
			settingsFile = dBuilder.parse("Settings.xml");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally { //All the other components
			
		}
	}

}
