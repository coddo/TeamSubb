package com.coddotech.teamsubb.jobs;

import java.io.File;
import java.util.ArrayList;

import javax.lang.model.element.Element;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Class used for managing jobs and their details
 * 
 * @author Coddo
 * 
 */
public final class JobManager {

	private static final String[] DEFAULT_JOB_HEADERS = { 
		"Traducator",
		"Verificator", 
		"Encoder", 
		"Typesetter", 
		"Manga", 
		"Stiri",
		"Postator"
	};
	private static final boolean[] DEFAULT_JOB_INFO_ARRAY = { 
		false, 
		false, 
		false,	
		false, 
		false, 
		false, 
		false
	};
	private static final String DEFAULT_USER_INFORMATION = "new user";

	private ArrayList<Job> jobs;
	private Element element; // used for reading document data

	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document jobDetailsFile;

	/**
	 * Class constructor
	 */
	public JobManager() {
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
		jobDetailsFile = null;
		element = null;
		jobs = null;
	}
	
	public String getUserName(){
		return null;
	}
	
	public void setUserName(String name){
		
	}
	
	public String getUserEmail(){
		return null;
	}
	
	public void setUserEmail(String email){
		
	}
	
	public String getUserRank(){
		return null;
	}
	
	public void setUserRank(String rank){
		
	}

	public boolean isSubber(){
		return false;
	}
	
	public boolean isVerifier(){
		return false;
	}
	
	public boolean isEncoder(){
		return false;
	}
	
	public boolean isTypesetter(){
		return false;
	}
	
	public boolean isManga(){
		return false;
	}
	
	public boolean isNews(){
		return false;
	}
	
	public boolean isPoster(){
		return false;
	}
	
	private boolean[] getUserJobsArray() {
		return null;
	}
	
	private void setUserJobsArray(boolean[] userJobInfo) {
		
	}

	/**
	 * Restore all the settings to their default values
	 */
	public void restoreDefaultSettings() {
		// user information
		
		
		// jobs information

	}

	/**
	 * Save all the changes to the actual file on the file system.
	 * 
	 * @return A boolean value indicating if the action was finished
	 *         successfully or not
	 */
	private boolean commitChangesToFile() {
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			StreamResult output = new StreamResult(new File("Settings.xml"));
			Source input = new DOMSource(jobDetailsFile);

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
			jobDetailsFile = dBuilder.parse("JobDetails.xml");
		} catch (Exception e) {
			// ignore any exceptions
		} finally { // All the other components
			jobs = new ArrayList<Job>();
		}
	}
}
