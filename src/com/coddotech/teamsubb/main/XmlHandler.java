package com.coddotech.teamsubb.main;

import java.io.File;
import java.util.Observable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;

public abstract class XmlHandler extends Observable {

	protected DocumentBuilderFactory dbFactory;
	protected DocumentBuilder dBuilder;
	protected Document xmlFile;

	/**
	 * Initialize the XML builders and files in which to persist the application settings
	 */
	protected void createXMLComponents(String fileName) {
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			xmlFile = dBuilder.parse(System.getProperty("user.dir") + File.separator + fileName);

			ActivityLogger.logActivity(this.getClass().getName(), "XML components creation");

		}

		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "XML components creation", ex);

		}
	}
}
