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

	private int jobID;
	private String jobName;
	private String jobType;
	private String jobDescription;
	private String previousStaffMember;
	private String nextStaffMember;
	private String jobStartDate; // store the date as a String value for now
	private File jobSubFile;
	private File[] jobFontArchive;

	/**
	 * Class constructor
	 */
	public Job() {
		// initialize the next staff member with the default value
		this.setNextStaffMember(Job.DEFAULT_NEXT_STAFF);
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
	}

	/**
	 * Get the ID that this job instance has
	 * 
	 * @return A string representing the ID
	 */
	public int getJobID() {
		return jobID;
	}

	/**
	 * Set the ID for this job instance
	 * 
	 * @param jobID
	 *            A String value representing the ID
	 */
	public void setJobID(int jobID) {
		this.jobID = jobID;
	}

	/**
	 * Get the name of this job (here the name of the video to be subbed)
	 * 
	 * @return A String containing the job's name
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * Set the name that this job has
	 * 
	 * @param jobName
	 *            The string containing this job's name
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * Get the type of work that this job needs done
	 * 
	 * @return A String value indicating the type of work (modification) needed
	 */
	public String getJobType() {
		return jobType;
	}

	/**
	 * Set the type of work that this job needs done
	 * 
	 * @param jobType
	 *            A String value representing the type of modifications that
	 *            need to be done for this job
	 */
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	/**
	 * Get the description and comments that come along with this job
	 * 
	 * @return A String containing the entire description that comes along with
	 *         the job
	 */
	public String getJobDescription() {
		return jobDescription;
	}

	/**
	 * Set the description/comments for this job instance
	 * 
	 * @param jobDescription
	 *            A String containing the description and comments that the job
	 *            will have
	 */
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	/**
	 * Get the start date of the job in the Date format
	 * 
	 * @return A Date type variable telling the date in which the job was first
	 *         created
	 */
	public String getJobStartDate() {
		return jobStartDate;
	}

	/**
	 * Set the start date for this job
	 * 
	 * @param jobDate
	 *            A Date type variable representing the start date that this job
	 *            has
	 */
	public void setJobStartDate(String jobDate) {
		this.jobStartDate = jobDate;
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
	public File getJobSubFile() {
		return jobSubFile;
	}

	/**
	 * Set the sub file that comes with this sub
	 * 
	 * @param jobSubFile
	 *            The File entity that represents the sub
	 */
	public void setJobSubFile(File jobSubFile) {
		this.jobSubFile = jobSubFile;
	}

	/**
	 * Get the font collection needed to finish this job
	 * 
	 * @return A collection (File[]) containing the File entities for each font
	 */
	public File[] getJobFontArchive() {
		return jobFontArchive;
	}

	/**
	 * Set the font collection needed to finish this job
	 * 
	 * @param jobFontArchive
	 *            A collection (File[]) representing the File entities for the
	 *            fonts
	 */
	public void setJobFontArchive(File[] jobFontArchive) {
		this.jobFontArchive = jobFontArchive;
	}
}
