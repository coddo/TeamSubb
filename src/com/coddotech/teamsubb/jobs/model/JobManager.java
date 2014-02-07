package com.coddotech.teamsubb.jobs.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.apache.commons.io.FileUtils;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.appmanage.model.AppManager;
import com.coddotech.teamsubb.chat.model.LoggedUser;
import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;

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

	public static final String SEPARATOR_DATA = "#!#";
	public static final String SEPARATOR_ENTITY = "&!&";

	public static final String JOB_PRIORITY_NORMAL = "normal";
	public static final String JOB_PRIORITY_ACCEPTABLE = "acceptable";
	public static final String JOB_PRIORITY_IMPORTANT = "important";

	public static final String JOB_FIND = "job find";
	public static final String JOB_INFORMATION = "job information";
	public static final String JOB_END = "job end";
	public static final String JOB_ACCEPT = "job accept";
	public static final String JOB_CANCEL = "job cancel";
	public static final String JOB_CREATE = "job create";
	public static final String JOB_PUSH = "job push";

	public static final File WORKING_DIRECTORY = new File("Jobs");

	private List<Job> jobs;
	private List<Job> acceptedJobs;

	// variables used in order to know when the threads are running
	private volatile boolean findJobsRunning = false;
	private volatile boolean acceptJobRunning = false;
	private volatile boolean endJobRunning = false;
	private volatile boolean createJobRunning = false;
	private volatile boolean cancelJobRunning = false;
	private volatile boolean pushJobRunning = false;

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

		NotificationEntity notif = new NotificationEntity(JobManager.JOB_INFORMATION, job);

		this.setChanged();
		notifyObservers(notif);
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
	public void createJob(final String name, final int type, final String description, final String nextStaff,
			final String torrent, final String subFile, final String[] fonts) {

		class CreateJob extends Thread {

			@Override
			public void run() {

				if (!CustomWindow.isConnected(true))
					return;

				waitForThreadsToComplete();

				createJobRunning = true;

				NotificationEntity notif = null;

				try {

					boolean response = ConnectionManager.sendJobCreateRequest(LoggedUser.getInstance().getName(), name,
							type, description, nextStaff, torrent, subFile, fonts);

					notif = new NotificationEntity(JobManager.JOB_CREATE, response);

					ActivityLogger.logActivity(this.getClass().getName(), "Create job");

				}

				catch (Exception ex) {
					notif = new NotificationEntity(JobManager.JOB_CREATE, false);

					ActivityLogger.logException(this.getClass().getName(), "Create job", ex);

				}

				finally {
					setChanged();
					notifyObservers(notif);

					createJobRunning = false;

					findJobs();
				}
			}
		}

		if (!createJobRunning) {
			CreateJob create = new CreateJob();

			create.start();
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
	public void endJob(final int jobID) {

		class EndJob extends Thread {

			@Override
			public void run() {

				// check for an internet connection
				if (!CustomWindow.isConnected(true))
					return;

				waitForThreadsToComplete();

				endJobRunning = true;

				NotificationEntity notif = null;

				try {

					boolean response = ConnectionManager.sendJobEndRequest(jobID, LoggedUser.getInstance().getName());

					if (response) {
						for (int i = 0; i < jobs.size(); i++) {

							if (jobs.get(i).getID() == jobID) {

								jobs.get(i).dispose(true);

								jobs.remove(i);
							}

						}

						removeJob(jobID);
					}

					notif = new NotificationEntity(JobManager.JOB_END, response);

					ActivityLogger.logActivity(this.getClass().getName(), "End job");

				}

				catch (Exception ex) {
					notif = new NotificationEntity(JobManager.JOB_END, false);

					ActivityLogger.logException(this.getClass().getName(), "End job", ex);

				}

				finally {
					// notify all the observers about the change
					setChanged();
					notifyObservers(notif);

					endJobRunning = false;
				}
			}
		}

		if (!endJobRunning) {
			EndJob end = new EndJob();

			end.start();
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

		class FindJobs extends Thread {

			String message = JOB_PRIORITY_NORMAL;

			@Override
			public void run() {

				// check for an internet connection
				if (!CustomWindow.isConnected(false))
					return;

				waitForThreadsToComplete();

				try {

					// clear the jobs list and reinstantiate the message
					clearJobList(jobs);

					// send the jobs request to the server
					String response = ConnectionManager.sendJobSearchRequest(LoggedUser.getInstance().getName());

					// in case everything is ok, start processing the response that was received
					// from the server
					if (!response.equals("false") && !response.equals("")) {

						String[] jobFragments = response.split(JobManager.SEPARATOR_ENTITY);

						// take each job and wrap it in a Job entity
						for (String fragment : jobFragments) {

							message = wrapJob(message, fragment);
						}
					}

					// get rid of jobs that no longer exist
					removeInactiveJobs();

					NotificationEntity notif = new NotificationEntity(JobManager.JOB_FIND, message, new Object[] {
							acceptedJobs, jobs });

					// send the according notification to the observers
					setChanged();
					notifyObservers(notif);

					ActivityLogger.logActivity(this.getClass().getName(), "Find jobs");
				}

				catch (Exception ex) {
					ActivityLogger.logException(this.getClass().getName(), "Find jobs", ex);
				}

				finally {
					findJobsRunning = false;
				}
			}

		};

		FindJobs find = new FindJobs();
		find.start();
	}

	/**
	 * Accepts a certain job for this user.<br>
	 * 
	 * This notifies the observers with the response that has been received from the server
	 * 
	 * @param jobID
	 */
	public void acceptJob(final int jobID) {

		class AcceptJob extends Thread {

			@Override
			public void run() {

				if (!CustomWindow.isConnected(true))
					return;

				waitForThreadsToComplete();

				acceptJobRunning = true;

				NotificationEntity notif = null;

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

							}

						}
					}

					notif = new NotificationEntity(JobManager.JOB_ACCEPT, response);

					ActivityLogger.logActivity(this.getClass().getName(), "Accept job");

				}

				catch (Exception ex) {
					notif = new NotificationEntity(JobManager.JOB_ACCEPT, false);

					ActivityLogger.logException(this.getClass().getName(), "Accept job", ex);

				}

				finally {
					// notify all the observers about the change
					setChanged();
					notifyObservers(notif);

					acceptJobRunning = false;

					findJobs();

				}
			}
		}

		if (!acceptJobRunning) {
			AcceptJob accept = new AcceptJob();

			accept.start();
		}
	}

	/**
	 * Cancel a certain job that has been previously accepted by the user.<br>
	 * This notifies the observers with the response that has been received from the server
	 * 
	 * @param jobID
	 *            The ID of the job to be canceled
	 */
	public void cancelJob(final int jobID) {

		class CancelJob extends Thread {

			@Override
			public void run() {

				if (!CustomWindow.isConnected(true))
					return;

				waitForThreadsToComplete();

				cancelJobRunning = true;

				NotificationEntity notif = null;

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

					notif = new NotificationEntity(JobManager.JOB_CANCEL, response);

					ActivityLogger.logActivity(this.getClass().getName(), "Cancel job");

				}

				catch (Exception ex) {
					notif = new NotificationEntity(JobManager.JOB_CANCEL, false);

					ActivityLogger.logException(this.getClass().getName(), "Cancel job", ex);

				}

				finally {
					// notify all the observers about the change
					setChanged();
					notifyObservers(notif);

					cancelJobRunning = false;
				}
			}
		}

		if (!cancelJobRunning) {
			CancelJob cancel = new CancelJob();

			cancel.start();
		}
	}

	/**
	 * Send all the data that has been worked on for a certains job to the server.<br>
	 * This notifies the observers with the response that has been received from the server.
	 * 
	 * @param jobID
	 *            The ID of the job to be sent back to the server
	 */
	public void pushJob(final int jobID, final String nextStaff, final int type, final String torrent,
			final String comments, final File subFile) {

		class PushJob extends Thread {

			@Override
			public void run() {

				if (!CustomWindow.isConnected(true))
					return;

				waitForThreadsToComplete();

				pushJobRunning = true;

				NotificationEntity notif = null;

				try {

					boolean response = false;

					for (int i = 0; i < acceptedJobs.size(); i++) {
						Job job = acceptedJobs.get(i);

						if (job.getID() == jobID) {

							job.setType(type);
							job.setNextStaffMember(nextStaff);
							job.setComments(comments);

							if (subFile != null)
								job.setSubFile(subFile);

							if (!torrent.equals(""))
								job.setTorrent(torrent);

							response = job.push();

							if (response) {
								job.dispose(true);

								acceptedJobs.remove(job);

							}

						}
					}

					notif = new NotificationEntity(JobManager.JOB_PUSH, response);

					ActivityLogger.logActivity(this.getClass().getName(), "Push job");
				}

				catch (Exception ex) {
					notif = new NotificationEntity(JobManager.JOB_PUSH, false);

					ActivityLogger.logException(this.getClass().getName(), "Push job", ex);

				}

				finally {
					// notify all the observers about the change
					setChanged();
					notifyObservers(notif);

					pushJobRunning = false;

				}
			}
		}

		if (!pushJobRunning) {
			PushJob push = new PushJob();

			push.start();
		}
	}

	/**
	 * Removes the directory and all the files for a job.
	 * 
	 * @param jobID
	 *            The ID of the job (integer value)
	 */
	public void removeJob(final int jobID) {

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
		String[] data = fragment.split(JobManager.SEPARATOR_DATA);
		int jobID = Integer.parseInt(data[0]);

		// ignore this job if it already in the accepted list
		if (!isAccepted(jobID)) {

			// create variables representing this job's folder
			String dirPath = JobManager.WORKING_DIRECTORY.getAbsolutePath() + File.separator + data[1];

			// create a new Job entity with the data
			Job job = createJobEntity(data, dirPath);

			if (!message.equals(JOB_PRIORITY_IMPORTANT)) {

				if (job.getIntendedTo().equals(LoggedUser.getInstance().getName()))
					message = JOB_PRIORITY_IMPORTANT;

				else if (job.isAcceptable()) {
					message = JOB_PRIORITY_ACCEPTABLE;

				}
			}

			// add it to the list
			jobs.add(job);
		}
		else { // mark the job as being valid (still active in the database on the server)

			for (Job job : acceptedJobs) {

				if (job.getID() == jobID)
					job.valid = true;

			}

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
		job.setComments(data[3]);
		job.setBookedBy(data[4]);
		job.setPreviousStaffMember(data[5]);
		job.setIntendedTo(data[6]);
		job.setStartDate(data[7]);
		job.setTorrent(data[8]);
		job.setDirectoryPath(dirPath);
		job.setCurrentStaffMember(LoggedUser.getInstance().getName());

		// sub file
		job.setSubFileLink(data[9]);

		// font files
		String[] fontLinks = null;

		if (data.length - 10 > 0) {

			fontLinks = new String[data.length - 10];

			for (int i = 10; i < data.length; i++)
				fontLinks[i - 10] = data[i];

		}

		fontLinks = FontsManager.excludeSystemFonts(fontLinks);

		job.setFontLinks(fontLinks);

		return job;
	}

	/**
	 * Deletes all the jobs that are marked as being invalid from the user's HDD
	 */
	private void removeInactiveJobs() {
		for (int i = 0; i < acceptedJobs.size(); i++) {

			if (!acceptedJobs.get(i).valid)
				removeJob(acceptedJobs.get(i).getID());

		}
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
	 * Wait for all the threads to complete. Times out after 30 seconds
	 */
	private void waitForThreadsToComplete() {
		boolean active;

		do {

			try {
				Thread.sleep(100);
			}

			catch (InterruptedException e) {
				e.printStackTrace();

			}

			finally {
				active = findJobsRunning || acceptJobRunning || endJobRunning || createJobRunning || cancelJobRunning
						|| pushJobRunning;
			}

		} while (active);

	}

	/**
	 * Clears a list of all the Job entiteis that it contains
	 * 
	 * @param list
	 *            The list that needs to be disposed
	 */
	private void clearJobList(List<Job> list) {
		if (list == null)
			return;

		if (list.size() == 0)
			return;

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
				File jobFolder = new File(JobManager.WORKING_DIRECTORY.getAbsolutePath() + File.separator + jobDir);

				try {

					if (jobFolder.isDirectory()) {

						if (jobFolder.list().length == 0)
							jobFolder.delete();

						else {
							// read the data from the file and place it in the Job entity
							File cfgFile = new File(jobFolder.getAbsolutePath() + ".cfg");

							if (!cfgFile.exists())
								FileUtils.deleteDirectory(jobFolder);

							else {

								Job job = new Job();
								job.readConfigFile(cfgFile);

								// add the Job entity to the "accepted list"
								acceptedJobs.add(job);
							}

						}
					}

					ActivityLogger.logActivity(this.getClass().getName(), "Initialize working directory");

				}
				catch (Exception ex) {
					ActivityLogger.logException(this.getClass().getName(), "Initialize working directory", ex);

					// also create a dump file for this situation
					ActivityLogger.dumpAppErrorStack(ex);

					// Force the app to exit
					AppManager.exitApp();
				}
			}
		}
	}

}