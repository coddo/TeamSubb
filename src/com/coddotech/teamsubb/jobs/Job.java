package com.coddotech.teamsubb.jobs;

import java.io.File;

/**
 * Entity used by the JobManager class. This class stores information about a
 * job and can passed thoughout the user network
 * 
 * @author Coddo
 * 
 */
public final class Job {

	public static final String DEFAULT_NEXT_STAFF = "anyone";

	private int id;
	private String name;
	private String type;
	private String description;
	private String previousStaffMember;
	private String nextStaffMember;
	private String startDate; // store the date as a String value for now
	private File subFile;
	private File[] fonts;
	private String subFileData;
	private String[] fontsData;
	private String directoryPath;

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
		this.type = null;
		this.description = null;
		this.previousStaffMember = null;
		this.nextStaffMember = null;
		this.startDate = null;

		// delete files
		this.subFile.delete();
		for (File file : this.fonts)
			file.delete();

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
	public String getType() {
		return type;
	}

	/**
	 * Set the type of work that this job needs done
	 * 
	 * @param jobType
	 *            A String value representing the type of modifications that
	 *            need to be done for this job
	 */
	public void setType(String jobType) {
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
	 * @param directoryPath A String indicating the files' location
	 */
	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
}
