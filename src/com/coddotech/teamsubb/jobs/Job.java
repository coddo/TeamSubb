package com.coddotech.teamsubb.jobs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;

import com.coddotech.teamsubb.connection.ConnectionManager;
import com.coddotech.teamsubb.main.GadgetWindow;

/**
 * Entity used by the JobManager class. This class stores information about a
 * job and can passed thoughout the user network
 * 
 * @author Coddo
 * 
 */
public final class Job {

	private static final String DEFAULT_NEXT_STAFF = "anyone";
	private static final String CONFIG_FILE_NAME = "\\config.cfg";

	private int id;
	private String name;
	private int type;
	private String description;
	private boolean booked;
	private String currentStaffMember;
	private String previousStaffMember;
	private String nextStaffMember;
	private String startDate; // store the date as a String value for now
	private String directoryPath;
	private File subFile;
	private File[] fonts;
	private String subFileData;
	private String[] fontsData;

	/**
	 * Class constructor
	 */
	public Job() {
		// initialize the next staff member with the default value
		this.setNextStaffMember(Job.DEFAULT_NEXT_STAFF);
	}

	/**
	 * Clear the memory from this class and its components This also deletes any
	 * files that are asociated with this job entity (including its specific
	 * folder)
	 */
	public void dispose() {
		this.name = null;
		this.description = null;
		this.previousStaffMember = null;
		this.nextStaffMember = null;
		this.startDate = null;

		// delete files
		if (subFile.exists())
			this.subFile.delete();

		for (File file : this.fonts) {
			if (file.exists())
				file.delete();
		}

		this.subFile = null;
		this.fonts = null;
	}

	/**
	 * Get the ID that this job instance has
	 * 
	 * @return A string representing the ID
	 */
	public int getID() {
		return id;
	}

	/**
	 * Set the ID for this job instance
	 * 
	 * @param jobID
	 *            A String value representing the ID
	 */
	public void setID(int jobID) {
		this.id = jobID;
	}

	/**
	 * Get the name of this job (here the name of the video to be subbed)
	 * 
	 * @return A String containing the job's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name that this job has
	 * 
	 * @param jobName
	 *            The string containing this job's name
	 */
	public void setName(String jobName) {
		this.name = jobName;
	}

	/**
	 * Get the type of work that this job needs done
	 * 
	 * @return A String value indicating the type of work (modification) needed
	 */
	public int getType() {
		return type;
	}

	/**
	 * Set the type of work that this job needs done
	 * 
	 * @param jobType
	 *            A String value representing the type of modifications that
	 *            need to be done for this job
	 */
	public void setType(int jobType) {
		this.type = jobType;
	}

	/**
	 * Get the description and comments that come along with this job
	 * 
	 * @return A String containing the entire description that comes along with
	 *         the job
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description/comments for this job instance
	 * 
	 * @param jobDescription
	 *            A String containing the description and comments that the job
	 *            will have
	 */
	public void setDescription(String jobDescription) {
		this.description = jobDescription;
	}

	/**
	 * Verifiy if this job is currently booked by someone else
	 * 
	 * @return A logical value
	 */
	public boolean isBooked() {
		return booked;
	}

	/**
	 * Set whether this job is booked or not
	 * 
	 * @param booked
	 *            A logical value to be set for this field
	 */
	public void setBooked(boolean booked) {
		this.booked = booked;
	}

	/**
	 * Get the start date of the job in the Date format
	 * 
	 * @return A Date type variable telling the date in which the job was first
	 *         created
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * Set the start date for this job
	 * 
	 * @param jobDate
	 *            A Date type variable representing the start date that this job
	 *            has
	 */
	public void setStartDate(String jobDate) {
		this.startDate = jobDate;
	}

	/**
	 * Get the name of the user that currently works on this project
	 * 
	 * @return A String representing the name of the staff member
	 */
	public String getCurrentStaffMember() {
		return currentStaffMember;
	}

	/**
	 * Set the name of the user that currently works on this project
	 * 
	 * @param currentStaffMember
	 *            A String representing the name of the staff member
	 */
	public void setCurrentStaffMember(String currentStaffMember) {
		this.currentStaffMember = currentStaffMember;
	}

	/**
	 * Get the name of the staff member that worked on this job before the
	 * current one
	 * 
	 * @return A String containing the name of the person
	 */
	public String getPreviousStaffMember() {
		return previousStaffMember;
	}

	/**
	 * Set the name of the staff member that worked on this job before the
	 * current one
	 * 
	 * @param previousStaffMember
	 *            A String value containing the name of the person
	 */
	public void setPreviousStaffMember(String previousStaffMember) {
		this.previousStaffMember = previousStaffMember;
	}

	/**
	 * Get the name of the staff member that should work on this job after it is
	 * pushed back to the server
	 * 
	 * @return A String value containing the name of the person
	 */
	public String getNextStaffMember() {
		return nextStaffMember;
	}

	/**
	 * Set the name of the staff member that should work on this job after it is
	 * puched back to the server
	 * 
	 * @param nextStaffMember
	 *            A String value containing the name of the person
	 */
	public void setNextStaffMember(String nextStaffMember) {
		this.nextStaffMember = nextStaffMember;
	}

	/**
	 * Get the path for the directory containing the data files for this job
	 * 
	 * @return A String indicating the files' location
	 */
	public String getDirectoryPath() {
		return directoryPath;
	}

	/**
	 * Set the path for the directory containing the data files for this job
	 * 
	 * @param directoryPath
	 *            A String indicating the files' location
	 */
	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	/**
	 * Get the instance of the directory in which this job operates
	 * 
	 * @return A File entity representing its working directory
	 */
	public File getDirectoryInstance() {
		return new File(this.directoryPath);
	}

	/**
	 * Get the sub file that comes with this sub
	 * 
	 * @return A File entity which contains the file information for the sub
	 */
	public File getSubFile() {
		return subFile;
	}

	/**
	 * Set the sub file that comes with this sub
	 * 
	 * @param jobSubFile
	 *            The File entity that represents the sub
	 */
	public void setSubFile(File jobSubFile) {
		this.subFile = jobSubFile;
	}

	/**
	 * Get the font collection needed to finish this job
	 * 
	 * @return A collection (File[]) containing the File entities for each font
	 */
	public File[] getFonts() {
		return fonts;
	}

	/**
	 * Set the font collection needed to finish this job
	 * 
	 * @param jobFontArchive
	 *            A collection (File[]) representing the File entities for the
	 *            fonts
	 */
	public void setFonts(File[] jobFontArchive) {
		this.fonts = jobFontArchive;
	}

	/**
	 * Get the raw data about the sub file and its location on the web, which
	 * has been extracted from the response string from the server
	 * 
	 * @return A String containing the raw data about the file
	 */
	public String getSubFileData() {
		return subFileData;
	}

	/**
	 * Set the raw data about the sub file and its location on the web, which
	 * has been extracted from the response string from the server
	 * 
	 * @param subFileData
	 *            A String containing the raw data about the file
	 */
	public void setSubFileData(String subFileData) {
		this.subFileData = subFileData;
	}

	/**
	 * Get the raw data about the font files and their location on the web, data
	 * that has been extracted from the response string from the server
	 * 
	 * @return A String collection containing the raw data about the fonts
	 */
	public String[] getFontsData() {
		return fontsData;
	}

	/**
	 * Verifiy in the job type that this job needs is within the skill list that
	 * the user has and determines if this job is acceptable by this user or not
	 * 
	 * @param possible
	 *            A collection of jobs that can be done by the user
	 * @return A logical value indicating whether this job can be accepted by
	 *         the user of not
	 */
	public boolean isAcceptable(String[] possible) {

		if (this.booked)
			return false;
		
		for (String pos : possible) {
			if (GadgetWindow.DEFAULT_JOBS_INFO_HEADERS[this.type].equals(pos))
				return true;
		}

		return false;
	}

	/**
	 * Set the raw data about the font files and their location on the web, data
	 * that has been extracted from the response string from the server
	 * 
	 * @param fontsData
	 *            A String collection containing the raw data about the fonts
	 */
	public void setFontsData(String[] fontsData) {
		this.fontsData = fontsData;
	}

	/**
	 * Accept a certain job.<br>
	 * This method displays a popup message in case of an error
	 */
	public boolean accept() {
		try {
			// send the accept message request to the server
			boolean response = ConnectionManager.sendJobAcceptRequest(this.id,
					this.currentStaffMember);

			// continue only if the server accepted the request
			if (!response)
				return false;

			// create a directory for this this
			File dir = new File(this.directoryPath);
			if (!dir.exists())
				dir.mkdir();

			// sub file (download + add to the Job entity)
			this.setSubFile(ConnectionManager.downloadFile(this.subFileData,
					this.directoryPath));

			// font files (download + add to the Job entity)
			File[] fonts = new File[this.fontsData.length];

			for (int i = 0; i < fonts.length; i++) {
				fonts[i] = ConnectionManager.downloadFile(this.fontsData[i],
						this.directoryPath);
			}
			this.setFonts(fonts);

			// create the configuration file containing the this data
			// This is for later use (in case of a this pause)
			this.generateConfigFile();

			return true;

		} catch (Exception ex) { // diplay error message
			return false;
		}
	}

	/**
	 * Cancel/Abort a certain job on which the user is currently working.<br>
	 * This method displays a popup message in case of an error
	 */
	public boolean cancel() {
		// send the cancel message request to the server
		boolean response = ConnectionManager.sendJobCancelRequest(this,
				this.currentStaffMember);

		// continue only if the server has accepted the request
		if (!response)
			return false;

		try {
			FileUtils.deleteDirectory(this.getDirectoryInstance());

			return true;

		} catch (Exception ex) {
			return false;
		}

	}

	/**
	 * Send all the user's work for a job back to the server and remove its data
	 * from the disk
	 * 
	 * @return A logical value indicating if the server accepted the data
	 */
	public boolean push() {
		// send the request and wait for the servers response
		boolean response = ConnectionManager.sendJobPushRequest(this,
				this.currentStaffMember, false);

		// continue only if the server has accepted the request
		if (!response)
			return false;

		// if the response is ok, then delete the data from the disk
		try {
			FileUtils.deleteDirectory(this.getDirectoryInstance());

			return true;

		} catch (Exception ex) {
			return false;
		}
	}

	public void readConfigFile(File jobFolder) throws Exception {
		File config = new File(jobFolder.getAbsolutePath() + File.separator
				+ Job.CONFIG_FILE_NAME);

		BufferedReader reader = new BufferedReader(new FileReader(
				config.getAbsoluteFile()));

		this.setID(Integer.parseInt(reader.readLine()));
		this.setName(reader.readLine());
		this.setType(Integer.parseInt(reader.readLine()));
		this.setDescription(reader.readLine());
		this.setPreviousStaffMember(reader.readLine());
		this.setNextStaffMember(reader.readLine());
		this.setStartDate(reader.readLine());
		this.setDirectoryPath(reader.readLine());
		this.setSubFile(new File(reader.readLine()));

		File[] fonts = new File[Integer.parseInt(reader.readLine())];
		for (int i = 0; i < fonts.length; i++)
			fonts[i] = new File(reader.readLine());

		this.setFonts(fonts);

		// close the reader
		reader.close();
	}

	/**
	 * Creates a configuration file for a certain job in its own directory and
	 * fills it with data
	 * 
	 * @param job
	 *            The Job entity for which to create this file
	 * @throws Exception
	 */
	private void generateConfigFile() throws Exception {
		File file = new File(this.directoryPath + Job.CONFIG_FILE_NAME);

		// delete the file if it already exists
		if (file.exists())
			file.delete();

		// recreate it (void from data)
		file.createNewFile();

		// fill it with data (exclude raw data about the subfile and font files)
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				file.getAbsoluteFile()));
		writer.write(this.id + "\n");
		writer.write(this.name + "\n");
		writer.write(this.type + "\n");
		writer.write(this.description + "\n");
		writer.write(this.currentStaffMember + "\n");
		writer.write(this.previousStaffMember + "\n");
		writer.write(this.nextStaffMember + "\n");
		writer.write(this.startDate + "\n");
		writer.write(this.directoryPath + "\n");
		writer.write(this.subFile.getAbsolutePath() + "\n");

		writer.write(this.fonts.length + "\n");
		for (int i = 0; i < this.fonts.length; i++) {
			writer.write(this.fonts[i].getAbsolutePath() + "\n");
		}

		// close the writer
		writer.close();

	}
}
