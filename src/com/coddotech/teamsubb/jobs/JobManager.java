package com.coddotech.teamsubb.jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.coddotech.teamsubb.connection.ConnectionManager;
import com.coddotech.teamsubb.main.CustomWindow;

/**
 * Class used for realizing the communication between this client and the target
 * server for job management (@www.anime4fun.ro).
 * 
 * The server implementation is done separately in python and javascript.
 * 
 * @author Coddo
 * 
 */
public class JobManager extends Observable {

	private static final String SEPARATOR_FIELDS = "#!#";
	private static final String SEPARATOR_JOBS = "&!&";

	private static final File WORKING_DIRECTORY = new File("Jobs");

	private List<Job> jobs;
	private List<Job> acceptedJobs;

	private String userName;
	private String[] userJobs;

	/**
	 * Get the list of jobs
	 * 
	 * @return A List of type Job representing all the found jobs
	 */
	public List<Job> getJobs() {
		return this.jobs;
	}

	/**
	 * Get the list of jobs that have been accepted by the user
	 * 
	 * @return A List of type Job representing all the accepted jobs
	 */
	public List<Job> getAcceptedJobs() {
		return this.acceptedJobs;
	}

	/**
	 * Main class construcotr
	 * 
	 * @param userName
	 *            The name of the user logged in to this app
	 * @param userJobs
	 *            The jobs that can be done by this user
	 */
	public JobManager(String userName, String[] userJobs) {
		this.userName = userName;
		this.userJobs = userJobs;

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

		// fields
		this.userJobs = null;
		this.userName = null;
	}

	/**
	 * Create a new job and try to add it to the server. <br>
	 * This notifies the observers with the response that has been received from
	 * the server
	 * 
	 * @param name
	 *            The name of the job
	 * @param type
	 *            The type of the job
	 * @param description
	 *            The description/comments for the job
	 * @param nextStaff
	 *            The name of the user that will be obligated to take over this
	 *            job
	 * @param subFile
	 *            The main file of the job (file to be subbed)
	 * @param fonts
	 *            The font files that are needed in order to finish this job
	 */
	public void createJob(String name, int type, String description,
			String nextStaff, String subFile, String[] fonts) {

		boolean response = ConnectionManager.sendJobCreateRequest(
				this.userName, name, type, description, nextStaff, subFile,
				fonts);

		this.setChanged();
		notifyObservers("create" + CustomWindow.NOTIFICATION_SEPARATOR
				+ response);

		// after the job is created, start a new search in order to update the
		// job list
		this.findJobs();
	}

	/**
	 * Mark a job as ended. This method also tells the server to remove it from
	 * the pending list.<br>
	 * This notifies the observers with the response that has been received from
	 * the server
	 * 
	 * @param jobID
	 *            The ID of the job to be marked as finished
	 * @param user
	 *            The name of the user that
	 * @return A logical value indicating if the job was ended successfully
	 */
	public void endJob(int jobID) {
		boolean response = ConnectionManager.sendJobEndRequest(jobID,
				this.userName);

		if (response) {
			for (Job job : jobs) {
				if (job.getID() == jobID)
					jobs.remove(job);
			}
		}

		// notify all the observers about the change
		this.setChanged();
		notifyObservers("end" + CustomWindow.NOTIFICATION_SEPARATOR + response);
	}

	/**
	 * Sends a request to the server in order to receive a list with all the
	 * jobs that currently exist in the servers database. Based on the response,
	 * thos method tells the observers whether there are jobs that can be
	 * accepted by this user or if there are jobs which are intended especially
	 * for him (important) <br>
	 * 
	 * The messages are: "important", "acceptable" or "normal"
	 */
	public void findJobs() {

		String message = "normal";

		// clear the jobs list
		this.clearJobList(jobs);

		// send the jobs request to the server
		String response = ConnectionManager.sendJobSearchRequest(this.userName);

		if (!response.equals("error") && !response.equals("false")) {
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

					// note if the job is suitable or is it actually intended to
					// this user
					if (!message.equals("important")) {

						if (job.getIntendedTo().equals(this.userName))
							message = "important";
						else if (job.isAcceptable(this.userJobs)) {
							message = "acceptable";

						}
					}

					// add it to the list
					this.jobs.add(job);
				}
			}
		}

		// send the according notification to the observers
		this.setChanged();
		this.notifyObservers(message);
	}

	/**
	 * Accepts a certain job for this user.<br>
	 * This notifies the observers with the response that has been received from
	 * the server
	 * 
	 * @param jobID
	 */
	public void acceptJob(int jobID) {
		boolean response = false;

		for (Job job : jobs) {

			if (job.getID() == jobID) {
				response = job.accept();

				if (response) {
					jobs.remove(job);
					acceptedJobs.add(job);
				}

			}
		}

		// notify all the observers about the change
		this.setChanged();
		notifyObservers("accept" + CustomWindow.NOTIFICATION_SEPARATOR
				+ response);
	}

	/**
	 * Cancel a certain job that has been previously accepted by the user.<br>
	 * This notifies the observers with the response that has been received from
	 * the server
	 * 
	 * @param jobID
	 *            The ID of the job to be canceled
	 */
	public void cancelJob(int jobID) {
		boolean response = false;
		for (Job job : acceptedJobs) {

			if (job.getID() == jobID) {
				response = job.cancel();
				;

				if (response) {
					acceptedJobs.remove(job);
				}

			}
		}

		// notify all the observers about the change
		this.setChanged();
		notifyObservers("cancel" + CustomWindow.NOTIFICATION_SEPARATOR
				+ response);
	}

	/**
	 * Send all the data that has been worked on for a certains job to the
	 * server.<br>
	 * This notifies the observers with the response that has been received from
	 * the server
	 * 
	 * @param jobID
	 *            The ID of the job to be sent back to the server
	 */
	public void pushJob(int jobID) {
		boolean response = false;
		for (Job job : acceptedJobs) {

			if (job.getID() == jobID) {
				response = job.push();

				if (response) {
					acceptedJobs.remove(job);
				}

			}
		}

		// notify all the observers about the change
		this.setChanged();
		notifyObservers("push" + CustomWindow.NOTIFICATION_SEPARATOR + response);
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
		job.setBooked(Boolean.parseBoolean(data[4]));
		job.setPreviousStaffMember(data[5]);
		job.setIntendedTo(data[6]);
		job.setStartDate(data[7]);
		job.setDirectoryPath(dirPath);
		job.setCurrentStaffMember(this.userName);

		// sub file
		job.setSubFileData(data[8]);

		// font files
		String[] fontsData = null;
		if (data.length - 9 > 0) {
			fontsData = new String[data.length - 9];
			for (int i = 9; i < data.length; i++)
				fontsData[i - 9] = data[i];
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
		for (Job job : list) {
			job.dispose();
		}
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