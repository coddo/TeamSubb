package com.coddotech.teamsubb.jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.coddotech.teamsubb.connection.ConnectionManager;
import com.coddotech.teamsubb.main.DesktopGadget;

/**
 * Class used for realizing the communication between this client and the target
 * server for job management (@www.anime4fun.ro).
 * 
 * The server implementation is done separately in python and javascript.
 * 
 * @author Coddo
 * 
 */
public class JobManager {

	private static final String SEPARATOR_FIELDS = "&?&";
	private static final String SEPARATOR_JOBS = "¬|¬";
	private static final File WORKING_DIRECTORY = new File("Jobs");

	private List<Job> jobs;
	private List<Job> acceptedJobs;

	private UserInformation userInfo;

	/**
	 * Main class construcotr
	 * 
	 * @param gadget
	 *            The main form for this application
	 */
	public JobManager(DesktopGadget gadget, UserInformation userInfo) {
		this.userInfo = userInfo;
		jobs = new ArrayList<Job>();
		acceptedJobs = new ArrayList<Job>();

		initializeWorkingDirectory();
	}

	/**
	 * Clear memory from this class and its resources
	 */
	public void dispose() {
		// clear lists
		this.clearJobList(jobs);
		this.clearJobList(acceptedJobs);
		jobs = null;
		acceptedJobs = null;

		// other classes
		userInfo = null;
	}

	/**
	 * Get the list of jobs that are available for this user
	 * 
	 * @return A List containing all the available jobs
	 */
	public List<Job> getAvailableJobsList() {
		return this.jobs;
	}

	/**
	 * Get the list of jobs that have been accepted by the user
	 * 
	 * @return A List containing all the accepted jobs
	 */
	public List<Job> getAcceptedJobsList() {
		return this.acceptedJobs;
	}

	public String getUserName() {
		return this.userInfo.getUserName();
	}

	/**
	 * Create a new job and try to add it to the server
	 * 
	 * @param name
	 *            The name of the job
	 * @param type
	 *            The type of the job
	 * @param description
	 *            The description/comments for the job
	 * @param subFile
	 *            The main file of the job (file to be subbed)
	 * @param fonts
	 *            The font files that are needed in order to finish this job
	 * @return A logical value indicating if the job was successfully added to
	 *         the server or not
	 */
	public boolean createJob(String name, int type, String description,
			String subFile, String[] fonts) {
		String response = ConnectionManager.sendJobCreateRequest(
				userInfo.getUserName(), name, type, description, subFile,
				fonts, true);

		if (response.equals("error"))
			return false;

		return Boolean.parseBoolean(response);
	}

	/**
	 * Mark a job as ended. This method also tells the server to remove it from
	 * the pending list.
	 * 
	 * @param jobID
	 *            The ID of the job to be marked as finished
	 * @param user
	 *            The name of the user that
	 * @return A logical value indicating if the job was ended successfully
	 */
	public boolean endJob(int jobID) {
		String response = ConnectionManager.sendJobEndRequest(jobID,
				userInfo.getUserName(), true);

		if (response.equals("error"))
			return false;

		return Boolean.parseBoolean(response);
	}

	/**
	 * Send a request to the server in order to receive a job if any available
	 * If there are available jobs, it separates them from the response string
	 * and wraps every job in a Job entity, then they're added to the list
	 * 
	 * @param notify
	 *            A logical value telling the app whether to notify the user or
	 *            not in case new jobs are available for him
	 */
	public List<Job> findJobs(boolean notify) {

		// clear the jobs list
		this.clearJobList(jobs);

		// send the jobs request to the server
		String response = ConnectionManager.sendJobSearchRequest(
				userInfo.getUserName(), true);

		if (response.equals("false")) {
			// if no jobs are found, notify the user
			MessageBox message = new MessageBox(Display.getDefault()
					.getShells()[0], SWT.ICON_INFORMATION);
			message.setMessage("There are no available jobs for you at this time");
			message.setText("Nob jobs found");
			message.open();
		} else if (!response.equals("error")) {
			// in case everything is ok, start processing the response that was
			// received from the server
			String[] jobFragments = response.split(JobManager.SEPARATOR_JOBS);

			// take each job and wrap it in a Job entity
			for (String fragment : jobFragments) {
				// split the String into bits representing specific job data
				String[] data = fragment.split(JobManager.SEPARATOR_FIELDS);

				// ignore this job if it already in the accepted list
				if (!isAccepted(Integer.parseInt(data[0]))) {
					// create variables representing this job's folder
					String dirPath = JobManager.WORKING_DIRECTORY
							.getAbsolutePath() + File.separator + data[1];

					// create a new Job entity with the data
					Job job = createJobEntity(data, dirPath);

					// add it to the list
					jobs.add(job);
				}
			}
		}

		// return the jobs list
		return this.jobs;
	}

	public boolean acceptJob(int jobID) {
		boolean result = false;
		for (Job job : jobs) {

			if (job.getID() == jobID) {
				result = job.acceptJob();

				if (result) {
					jobs.remove(job);
					acceptedJobs.add(job);
				}

			}
		}

		return result;
	}

	public boolean cancelJob(int jobID) {
		boolean result = false;
		for (Job job : acceptedJobs) {

			if (job.getID() == jobID) {
				result = job.cancelJob();
				;

				if (result) {
					acceptedJobs.remove(job);
				}

			}
		}

		return result;
	}

	public boolean pushJob(int jobID) {
		boolean result = false;
		for (Job job : acceptedJobs) {

			if (job.getID() == jobID) {
				result = job.pushJob();
				;

				if (result) {
					acceptedJobs.remove(job);
				}

			}
		}

		return result;
	}

	/**
	 * Create and get a new Job entity that contains the entered data
	 * 
	 * @param data
	 *            The entire data that represents this new job that will be
	 *            created
	 * @param dirPath
	 *            The directory where this job's data files will be stored
	 * @return The new Job entity that is created with the entered data
	 */
	private Job createJobEntity(String[] data, String dirPath) {
		// basic job information
		Job job = new Job();
		job.setID(Integer.parseInt(data[0]));
		job.setName(data[1]);
		job.setType(Integer.parseInt(data[2]));
		job.setDescription(data[3]);
		job.setPreviousStaffMember(data[4]);
		job.setStartDate(data[5]);
		job.setDirectoryPath(dirPath);
		job.setCurrentStaffMember(this.userInfo.getUserName());

		// sub file
		job.setSubFileData(data[6]);

		// font files
		String[] fontsData = null;
		if (data.length - 7 > 0) {
			fontsData = new String[data.length - 7];
			for (int i = 7; i < data.length; i++)
				fontsData[i - 7] = data[i];
		}
		job.setFontsData(fontsData);

		return job;
	}

	/**
	 * Find out if there is already a job with this ID accepted by the user
	 * 
	 * @param id
	 *            The ID of the job to be verified
	 * @return A logical value indicating if the job is already accepted or not
	 */
	private boolean isAccepted(int id) {
		for (Job job : acceptedJobs) {
			if (job.getID() == id)
				return true;
		}

		return false;
	}

	/**
	 * Clears a list of all the Job entiteis that it contains
	 * 
	 * @param list
	 *            The list that needs to be disposed
	 */
	private void clearJobList(List<Job> list) {
		for (Job job : list)
			job.dispose();
		list.clear();
	}

	/**
	 * If the working directory doesn't exist, this method creates it.
	 * 
	 * If it exists, then it tries to reload all the jobs that are still present
	 * in it (accepted jobs)
	 */
	private void initializeWorkingDirectory() {
		if (!JobManager.WORKING_DIRECTORY.exists())
			JobManager.WORKING_DIRECTORY.mkdir();
		else {
			// get all the jobs that are accepted by this user and add them to
			// the "accepted jobs" list
			for (String jobDir : JobManager.WORKING_DIRECTORY.list()) {
				File jobFolder = new File(jobDir);
				try {

					// read the data from the file and place it in the entity
					Job job = new Job();
					job.readConfigFile(jobFolder);

					// add the Job entity to the "accepted list"
					acceptedJobs.add(job);

				} catch (Exception ex) {
					// TODO - warn the user about loading errors
					// find a way to make him save his current work on the job
					// directory that gave this error
				}
			}
		}
	}
}