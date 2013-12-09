package com.coddotech.teamsubb.jobs;

import java.io.File;
import java.util.Date;

/**
 * Entity used by the JobManager class. This class stores information about a
 * job and can passed thoughout the user network
 * 
 * @author Coddo
 * 
 */
public final class Job {
	
	private String jobID;
	private String jobName;
	private String jobType;
	private String jobDescription;
	private Date jobDate;
	private String previousStaffMember;
	private String nextStaffMember;
	private File jobSubFile;
	private File jobFontArchive;
	
	/**
	 * Class constructor
	 */
	public Job() {
		
	}
	
	public void dispose() {
		
	}


	public String getJobID() {
		return jobID;
	}


	public void setJobID(String jobID) {
		this.jobID = jobID;
	}


	public String getJobName() {
		return jobName;
	}


	public void setJobName(String jobName) {
		this.jobName = jobName;
	}


	public String getJobType() {
		return jobType;
	}


	public void setJobType(String jobType) {
		this.jobType = jobType;
	}


	public String getJobDescription() {
		return jobDescription;
	}


	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}


	public Date getJobDate() {
		return jobDate;
	}


	public void setJobDate(Date jobDate) {
		this.jobDate = jobDate;
	}


	public String getPreviousStaffMember() {
		return previousStaffMember;
	}

	public void setPreviousStaffMember(String previousStaffMember) {
		this.previousStaffMember = previousStaffMember;
	}

	public String getNextStaffMember() {
		return nextStaffMember;
	}


	public void setNextStaffMember(String nextStaffMember) {
		this.nextStaffMember = nextStaffMember;
	}


	public File getJobSubFile() {
		return jobSubFile;
	}


	public void setJobSubFile(File jobSubFile) {
		this.jobSubFile = jobSubFile;
	}


	public File getJobFontArchive() {
		return jobFontArchive;
	}


	public void setJobFontArchive(File jobFontArchive) {
		this.jobFontArchive = jobFontArchive;
	}
}
