package com.coddotech.teamsubb.jobs;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
public final class UserDetails {

	private static final String[] DEFAULT_USER_INFO_HEADERS = { "name",
			"email", "rank" };
	private static final String[] DEFAULT_USER_INFO_VALUES = { "no user",
			"no user", "no user" };
	private static final String[] DEFAULT_JOB_INFO_HEADERS = { "Traducator",
			"Verificator", "Encoder", "Typesetter", "Manga", "Stiri",
			"Postator" };
	private static final boolean[] DEFAULT_JOB_INFO_VALUES = { false, false,
			false, false, false, false, false };

	private Element element; // used for reading document data

	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document jobDetailsFile;

	/**
	 * Class constructor
	 */
	public UserDetails() {
		initializeComponents();
	}

	/**
	 * Clear memory from this class and its resources
	 */
	public void dispose() {
		// save changes to the user info file
		this.commitChangesToFile();

		// clear flieds
		dbFactory = null;
		dBuilder = null;
		jobDetailsFile = null;
		element = null;
	}
	
	/**
	 * Get the user's name
	 * 
	 * @return A string indicating the name of the user of the staff
	 */
	public String getUserName() {
		element = (Element) jobDetailsFile.getElementsByTagName("name").item(0);
		return element.getAttribute("value");
	}

	/**
	 * Set the user's name
	 * 
	 * @param name
	 *            A string indicating the name for this user
	 */
	public void setUserName(String name) {
		element = (Element) jobDetailsFile.getElementsByTagName("name").item(0);
		element.setAttribute("name", name);
	}

	/**
	 * Get the user's email
	 * 
	 * @return A string containing the email for this user
	 */
	public String getUserEmail() {
		element = (Element) jobDetailsFile.getElementsByTagName("email")
				.item(0);
		return element.getAttribute("value");
	}

	/**
	 * Set the user's email
	 * 
	 * @param email
	 *            A string containing the email for this user
	 */
	public void setUserEmail(String email) {
		element = (Element) jobDetailsFile.getElementsByTagName("email")
				.item(0);
		element.setAttribute("email", email);
	}

	/**
	 * Get the user's rank in the fansub
	 * 
	 * @return A string representing the rank that the user has in the community
	 */
	public String getUserRank() {
		element = (Element) jobDetailsFile.getElementsByTagName("rank").item(0);
		return element.getAttribute("rank");
	}

	/**
	 * Set the user's rank
	 * 
	 * @param rank
	 *            A string representing the rank for the user
	 */
	public void setUserRank(String rank) {
		element = (Element) jobDetailsFile.getElementsByTagName("name").item(0);
		element.setAttribute("rank", rank);
	}

	/**
	 * Applies the job details for this user
	 * 
	 * @param userJobsInfo
	 *            An array (boolean values) indicating which jobs are active for
	 *            this user
	 */
	public void setUserJobsArray(boolean[] userJobsInfo) {
		for (int i = 0; i < UserDetails.DEFAULT_JOB_INFO_HEADERS.length; i++) {
			element = (Element) jobDetailsFile.getElementsByTagName(
					UserDetails.DEFAULT_JOB_INFO_HEADERS[i]).item(0);
			element.setAttribute("value", Boolean.toString(userJobsInfo[i]));
		}
	}

	/**
	 * Verify the user works as a subber
	 * 
	 * @return A boolean value indicating whether he is a subber or not
	 */
	public boolean isSubber() {
		element = (Element) jobDetailsFile
				.getElementsByTagName(UserDetails.DEFAULT_USER_INFO_HEADERS[0]);
		return Boolean.parseBoolean(element.getAttribute("value"));
	}

	/**
	 * Verify the user works as a verifier
	 * 
	 * @return A boolean value indicating whether he is a verifier or not
	 */
	public boolean isVerifier() {
		element = (Element) jobDetailsFile
				.getElementsByTagName(UserDetails.DEFAULT_USER_INFO_HEADERS[1]);
		return Boolean.parseBoolean(element.getAttribute("value"));
	}

	/**
	 * Verify the user works as a encoder
	 * 
	 * @return A boolean value indicating whether he is a encoder or not
	 */
	public boolean isEncoder() {
		element = (Element) jobDetailsFile
				.getElementsByTagName(UserDetails.DEFAULT_USER_INFO_HEADERS[2]);
		return Boolean.parseBoolean(element.getAttribute("value"));
	}

	/**
	 * Verify the user works as a typesetter
	 * 
	 * @return A boolean value indicating whether he is a typesetter or not
	 */
	public boolean isTypesetter() {
		element = (Element) jobDetailsFile
				.getElementsByTagName(UserDetails.DEFAULT_USER_INFO_HEADERS[3]);
		return Boolean.parseBoolean(element.getAttribute("value"));
	}

	/**
	 * Verify the user works as a manga manager
	 * 
	 * @return A boolean value indicating whether he is a manga manager or not
	 */
	public boolean isManga() {
		element = (Element) jobDetailsFile
				.getElementsByTagName(UserDetails.DEFAULT_USER_INFO_HEADERS[4]);
		return Boolean.parseBoolean(element.getAttribute("value"));
	}

	/**
	 * Verify the user works as a news manager
	 * 
	 * @return A boolean value indicating whether he is a new manager or not
	 */
	public boolean isNews() {
		element = (Element) jobDetailsFile
				.getElementsByTagName(UserDetails.DEFAULT_USER_INFO_HEADERS[5]);
		return Boolean.parseBoolean(element.getAttribute("value"));
	}

	/**
	 * Verify the user works as a poster
	 * 
	 * @return A boolean value indicating whether he is a poster or not
	 */
	public boolean isPoster() {
		element = (Element) jobDetailsFile
				.getElementsByTagName(UserDetails.DEFAULT_USER_INFO_HEADERS[6]);
		return Boolean.parseBoolean(element.getAttribute("value"));
	}

	/**
	 * Restore all the settings to their default values
	 */
	public void restoreDefaultSettings() {
		// personal user information
		for (int i = 0; i < UserDetails.DEFAULT_USER_INFO_HEADERS.length; i++) {
			element = (Element) jobDetailsFile
					.getElementsByTagName(UserDetails.DEFAULT_USER_INFO_HEADERS[i]);
			element.setAttribute("value",
					UserDetails.DEFAULT_USER_INFO_VALUES[i]);
		}

		// user's jobs information
		for (int i = 0; i < UserDetails.DEFAULT_JOB_INFO_HEADERS.length; i++) {
			element = (Element) jobDetailsFile
					.getElementsByTagName(UserDetails.DEFAULT_JOB_INFO_HEADERS[i]);
			element.setAttribute("value",
					Boolean.toString(UserDetails.DEFAULT_JOB_INFO_VALUES[i]));
		}
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
		} finally { 
			// All the other components
		}
	}
}
