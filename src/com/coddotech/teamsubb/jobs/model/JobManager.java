package com.coddotech.teamsubb.jobs.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.appmanage.model.AppManager;
import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.settings.model.AppSettings;

/**
 * Class used for realizing the communication between this client and the target
 * server for job management (www.anime4fun.ro).
 * 
 * The server implementation is done separately in python and javascript.
 * 
 * @author Coddo
 * 
 */
public class JobManager extends Observable {

	public static final String SEPARATOR_FIELDS = "#!#";
	public static final String SEPARATOR_JOBS = "&!&";

	public static final File WORKING_DIRECTORY = new File("Jobs");

	private List<Job> jobs;
	private List<Job> acceptedJobs;

	private AppSettings settings;

	private Thread jobFinderThread = null;

	private static JobManager instance = null;

	/**
	 * Main class construcotr
	 * 
	 * @param userName
	 *            The name of the user logged in to this app
	 * 
	 * @param userJobs
	 *            The jobs that can be done by this user
	 */
	private JobManager() {
		jobs = new ArrayList<Job>();
		acceptedJobs = new ArrayList<Job>();

		settings = AppSettings.getInstance();

		initializeWorkingDirectory();

	}

	/**
	 * Get the instance created for this class
	 * 
	 * @return
	 */
	public static JobManager getInstance() {
		if (instance == null)
			instance = new JobManager();

		return instance;
	}

	/**
	 * Clear memory from this class and its resources
	 */
	public void dispose() {
		try {

			// clear lists
			this.clearJobList(jobs);
			this.clearJobList(acceptedJobs);

			jobs = null;
			acceptedJobs = null;

			ActivityLogger.logActivity(this.getClass().getName(), "Dispose");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Dispose", ex);

		}
	}

	/**
	 * Get the list of jobs
	 * 
	 * @return A List of type Job representing all the found jobs
	 */
	public List<Job> getJobs() {
		return this.jobs;
	}

	/**
	 * Get the job from the accepted jobs list, which has the specified ID
	 * 
	 * @param jobID
	 *            The ID of the job to be fetched
	 * 
	 * @return A Job instance
	 */
	public Job getAcceptedJob(int jobID) {
		for (Job job : acceptedJobs) {
			if (job.getID() == jobID) {
				return job;
			}
		}

		return null;
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
	 * Notifies the observers with the details of the job that has the entered ID. This sends the
	 * following fields: <br>
	 * 
	 * -> Type<br>
	 * 
	 * -> The staff which previously worked on it<br>
	 * 
	 * -> The staff to which this is intended<br>
	 * 
	 * -> The staff who currently works on this job
	 * 
	 * @param jobID
	 *            The ID of the job for which to send the information
	 */
	public void notifyJobInformation(int jobID) {

		Job job = null;

		// seach for the job in the main jobs list
		for (int i = 0; i < jobs.size(); i++) {

			if (jobs.get(i).getID() == jobID) {
				job = jobs.get(i);

				break;
			}
		}

		// if not found, search for it in the accepted jobs list
		if (job == null) {

			for (int i = 0; i < acceptedJobs.size(); i++) {

				if (acceptedJobs.get(i).getID() == jobID) {
					job = acceptedJobs.get(i);

					break;
				}
			}
		}

		String message = "jobinformation" + CustomWindow.NOTIFICATION_SEPARATOR;

		// If the job could not be found, it means that the list is out of date and
		// tell the user that the list needs to be refreshed.
		// Otherwise, append the job information to the message
		if (job == null) {
			String errmsg = "No job selected";

			for (int i = 0; i < 6; i++)
				message += errmsg + CustomWindow.NOTIFICATION_SEPARATOR;

		}
		else {
			message += Job.DEFAULT_JOB_TYPES[job.getType()] + CustomWindow.NOTIFICATION_SEPARATOR;

			message += job.getStartDate() + CustomWindow.NOTIFICATION_SEPARATOR;

			message += job.getPreviousStaffMember() + CustomWindow.NOTIFICATION_SEPARATOR;

			message += job.getIntendedTo() + CustomWindow.NOTIFICATION_SEPARATOR;

			message += job.getBookedBy() + CustomWindow.NOTIFICATION_SEPARATOR;

			message += (message == null) ? " " : job.getDescription() + " ";
		}

		this.setChanged();
		notifyObservers(message);
	}

	/**
	 * Create a new job and try to add it to the server. <br>
	 * This notifies the observers with the response that has been received from the server
	 * 
	 * @param name
	 *            The name of the job
	 * 
	 * @param type
	 *            The type of the job
	 * 
	 * @param description
	 *            The description/comments for the job
	 * 
	 * @param nextStaff
	 *            The name of the user that will be obligated to take over this job
	 * 
	 * @param subFile
	 *            The main file of the job (file to be subbed)
	 * 
	 * @param fonts
	 *            The font files that are needed in order to finish this job
	 */
	public void createJob(String name, int type, String description, String nextStaff, String subFile,
			String[] fonts) {

		try {

			boolean response = ConnectionManager.sendJobCreateRequest(settings.getUserName(), name, type,
					description, nextStaff, subFile, fonts);

			this.setChanged();
			notifyObservers("create" + CustomWindow.NOTIFICATION_SEPARATOR + response);

			// after the job is created, start a new search in order to update the job list
			this.findJobs();

			ActivityLogger.logActivity(this.getClass().getName(), "Create job");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Create job", ex);

		}
	}

	/**
	 * Mark a job as ended. This method also tells the server to remove it from the pending list.<br>
	 * This notifies the observers with the response that has been received from the server.
	 * 
	 * @param jobID
	 *            The ID of the job to be marked as finished
	 * 
	 * @param user
	 *            The name of the user that
	 * 
	 * @return A logical value indicating if the job was ended successfully
	 */
	public void endJob(int jobID) {
		try {

			boolean response = ConnectionManager.sendJobEndRequest(jobID, settings.getUserName());

			if (response) {
				for (int i = 0; i < jobs.size(); i++) {
					if (jobs.get(i).getID() == jobID) {
						jobs.get(i).dispose(true);
						jobs.remove(i);
					}
				}

				this.removeJob(jobID);
			}

			// notify all the observers about the change
			this.setChanged();
			notifyObservers("end" + CustomWindow.NOTIFICATION_SEPARATOR + response);

			ActivityLogger.logActivity(this.getClass().getName(), "End job");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "End job", ex);

		}
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

		Runnable jobFinder = new Runnable() {

			String message = "normal";

			@Override
			public void run() {

				if (search())
					Display.getDefault().syncExec(notifier);

			}

			private boolean search() {
				try {

					// clear the jobs list and reinstantiate the message
					clearJobList(jobs);

					// send the jobs request to the server
					String response = ConnectionManager.sendJobSearchRequest(settings.getUserName());

					// in case everything is ok, start processing the response that was received
					// from the server
					if (!response.equals("false") && !response.equals("")) {

						String[] jobFragments = response.split(JobManager.SEPARATOR_JOBS);

						// take each job and wrap it in a Job entity
						for (String fragment : jobFragments) {

							message = wrapJob(message, fragment);
						}
					}

					message = "find" + CustomWindow.NOTIFICATION_SEPARATOR + message;

					ActivityLogger.logActivity(this.getClass().getName(), "Find jobs");

					return true;

				}
				catch (Exception ex) {
					ActivityLogger.logException(this.getClass().getName(), "Find jobs", ex);

					return false;
				}
			}

			// send the according notification to the observers
			private Runnable notifier = new Runnable() {

				@Override
				public void run() {

					setChanged();

					notifyObservers(message);

				}

			};

		};

		if (jobFinderThread == null)
			jobFinderThread = new Thread(jobFinder);

		if (!jobFinderThread.isAlive()) {
			jobFinderThread.start();

			jobFinderThread = new Thread(jobFinder);
		}
	}

	/**
	 * Accepts a certain job for this user.<br>
	 * 
	 * This notifies the observers with the response that has been received from the server
	 * 
	 * @param jobID
	 */
	public void acceptJob(int jobID) {
		try {

			boolean response = false;

			for (int i = 0; i < jobs.size(); i++) {
				Job job = jobs.get(i);

				if (job.getID() == jobID) {
					response = job.accept();

					if (response) {
						jobs.remove(job);

						acceptedJobs.add(job);

						job.setBookedBy("Yourself");

						this.findJobs();
					}

				}
			}

			// notify all the observers about the change
			this.setChanged();
			notifyObservers("accept" + CustomWindow.NOTIFICATION_SEPARATOR + response);

			ActivityLogger.logActivity(this.getClass().getName(), "Accept job");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Accept job", ex);

		}
	}

	/**
	 * Cancel a certain job that has been previously accepted by the user.<br>
	 * This notifies the observers with the response that has been received from the server
	 * 
	 * @param jobID
	 *            The ID of the job to be canceled
	 */
	public void cancelJob(int jobID) {
		try {

			boolean response = false;

			for (int i = 0; i < acceptedJobs.size(); i++) {
				Job job = acceptedJobs.get(i);

				if (job.getID() == jobID) {
					response = job.cancel();

					if (response) {
						job.dispose(true);

						acceptedJobs.remove(job);
					}

				}
			}

			// notify all the observers about the change
			this.setChanged();
			notifyObservers("cancel" + CustomWindow.NOTIFICATION_SEPARATOR + response);

			ActivityLogger.logActivity(this.getClass().getName(), "Cancel job");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Cancel job", ex);

		}
	}

	/**
	 * Removes the directory and all the files for a job.
	 * 
	 * @param jobID
	 *            The ID of the job (integer value)
	 */
	public void removeJob(int jobID) {
		try {

			for (int i = 0; i < acceptedJobs.size(); i++) {

				if (acceptedJobs.get(i).getID() == jobID) {

					try {
						FileUtils.deleteDirectory(acceptedJobs.get(i).getDirectoryInstance());

					}

					catch (IOException e) {

					}

					acceptedJobs.get(i).dispose(true);
					acceptedJobs.remove(i);
				}
			}

			ActivityLogger.logActivity(this.getClass().getName(), "Remove job");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Remove job", ex);

		}
	}

	/**
	 * Send all the data that has been worked on for a certains job to the server.<br>
	 * This notifies the observers with the response that has been received from the server.
	 * 
	 * @param jobID
	 *            The ID of the job to be sent back to the server
	 */
	public boolean pushJob(int jobID, String nextStaff, int type, String comments) {
		try {

			boolean response = false;

			for (int i = 0; i < acceptedJobs.size(); i++) {
				Job job = acceptedJobs.get(i);

				if (job.getID() == jobID) {

					job.setType(type);
					job.setNextStaffMember(nextStaff);
					job.setDescription(comments);

					response = job.push();

					if (response) {
						job.dispose(true);

						acceptedJobs.remove(job);

					}

				}
			}

			// notify all the observers about the change
			this.setChanged();
			notifyObservers("push" + CustomWindow.NOTIFICATION_SEPARATOR + response);

			ActivityLogger.logActivity(this.getClass().getName(), "Push job");

			return response;

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Push job", ex);

			return false;
		}
	}

	/**
	 * Open the directory for a certain job
	 * 
	 * @param jobID
	 *            The ID of the job
	 */
	public void openJobDirectory(int jobID) {
		for (Job job : acceptedJobs) {

			if (jobID == job.getID()) {
				job.openDirectory();

				break;

			}
		}
	}

	private String wrapJob(String message, String fragment) {

		// split the String into bits representing specific job data
		String[] data = fragment.split(JobManager.SEPARATOR_FIELDS);

		// ignore this job if it already in the accepted list
		if (!isAccepted(Integer.parseInt(data[0]))) {

			// create variables representing this job's folder
			String dirPath = JobManager.WORKING_DIRECTORY.getAbsolutePath() + File.separator + data[1];

			// create a new Job entity with the data
			Job job = createJobEntity(data, dirPath);

			if (!message.equals("important")) {

				if (job.getIntendedTo().equals(settings.getUserName()))
					message = "important";

				else if (job.isAcceptable(settings.getUserJobs())) {
					message = "acceptable";

				}
			}

			// add it to the list
			jobs.add(job);
		}
		return message;
	}

	/**
	 * Create and get a new Job entity that contains the entered data
	 * 
	 * @param data
	 *            The entire data that represents this new job that will be created
	 * 
	 * @param dirPath
	 *            The directory where this job's data files will be stored
	 * 
	 * @return The new Job entity that is created with the entered data
	 */
	private Job createJobEntity(String[] data, String dirPath) {

		// basic job information
		Job job = new Job();
		job.setID(Integer.parseInt(data[0]));
		job.setName(data[1]);
		job.setType(Integer.parseInt(data[2]));
		job.setDescription(data[3]);
		job.setBookedBy(data[4]);
		job.setPreviousStaffMember(data[5]);
		job.setIntendedTo(data[6]);
		job.setStartDate(data[7]);
		job.setDirectoryPath(dirPath);
		job.setCurrentStaffMember(settings.getUserName());

		// sub file
		job.setSubFileLink(data[8]);

		// font files
		String[] fontLinks = null;

		if (data.length - 9 > 0) {

			fontLinks = new String[data.length - 9];

			for (int i = 9; i < data.length; i++)
				fontLinks[i - 9] = data[i];

		}

		fontLinks = FontsManager.excludeSystemFonts(fontLinks);

		job.setFontLinks(fontLinks);

		return job;
	}

	/**
	 * Find out if there is already a job with this ID accepted by the user
	 * 
	 * @param id
	 *            The ID of the job to be verified
	 * 
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
			job.dispose(false);
		}
		list.clear();
	}

	/**
	 * If the working directory doesn't exist, this method creates it.
	 * 
	 * If it exists, then it tries to reload all the jobs that are still present
	 * in it (accepted jobs)
	 * 
	 * @throws Exception
	 */
	private void initializeWorkingDirectory() {
		if (!JobManager.WORKING_DIRECTORY.exists())
			JobManager.WORKING_DIRECTORY.mkdir();

		else {

			// get all the jobs that are accepted by this user and add them to
			// the "accepted jobs" list
			for (String jobDir : JobManager.WORKING_DIRECTORY.list()) {
				File jobFolder = new File(JobManager.WORKING_DIRECTORY.getAbsolutePath() + File.separator
						+ jobDir);

				try {

					if (jobFolder.isDirectory()) {

						if (jobFolder.list().length == 0)
							jobFolder.delete();

						else {
							// read the data from the file and place it in the Job entity
							File cfgFile = new File(jobFolder.getAbsolutePath() + ".cfg");

							Job job = new Job();
							job.readConfigFile(cfgFile);

							// add the Job entity to the "accepted list"
							acceptedJobs.add(job);
						}
					}

					ActivityLogger.logActivity(this.getClass().getName(), "Initialize working directory");

				}
				catch (Exception ex) {
					ActivityLogger
							.logException(this.getClass().getName(), "Initialize working directory", ex);

					// also create a dump file for this situation
					ActivityLogger.dumpAppErrorStack(ex);

					// Force the app to exit
					AppManager.exitApp();
				}
			}
		}
	}

}