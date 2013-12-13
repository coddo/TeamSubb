package com.coddotech.teamsubb.jobs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.apache.commons.io.FileUtils;

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
	private static final String WORKING_DIRECTORY_PATH = "Jobs\\";
	private static final String CONFIG_FILE_NAME = "\\config.cfg";

	private List<Job> jobs;
	private List<Job> acceptedJobs;

	private DesktopGadget _gadget;
	private UserInformation _userInfo;

	/**
	 * Main class construcotr
	 * 
	 * @param gadget
	 *            The main form for this application
	 */
	public JobManager(DesktopGadget gadget, UserInformation userInfo) {
		this._gadget = gadget;
		this._userInfo = userInfo;

		initializeWorkingDirectory();
	}

	/**
	 * Clear memory from this class and its resources
	 */
	public void dispose() {
		// clear lists
		jobs.clear();
		acceptedJobs.clear();

		// other classes
		_gadget = null;
		_userInfo = null;
		jobs = null;
	}

	public boolean createJob() {
		return true;
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
		jobs.clear();

		// send the jobs request to the server
		String response = ConnectionManager.sendJobSearchRequest(
				_userInfo.getUserName(), true);

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
			boolean jobsFound = false;
			String[] jobFragments = response.split(JobManager.SEPARATOR_JOBS);

			// take each job and wrap it in a Job entity
			for (String fragment : jobFragments) {
				// split the String into bits representing specific job data
				String[] data = fragment.split(JobManager.SEPARATOR_FIELDS);

				// ignore this job if it already in the accepted list
				if (!isAccepted(Integer.parseInt(data[0]))) {
					// create variables representing this job's folder
					String dirPath = JobManager.WORKING_DIRECTORY_PATH
							+ data[1];

					Job job = new Job();

					// basic job information
					job.setID(Integer.parseInt(data[0]));
					job.setName(data[1]);
					job.setType(data[2]);
					job.setDescription(data[3]);
					job.setPreviousStaffMember(data[4]);
					job.setStartDate(data[5]);
					job.setDirectoryPath(dirPath);

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

					jobs.add(job);
					jobsFound = true;
				}

				// start notifying the user about available jobs
				if (notify && jobsFound)
					_gadget.startNotifications();
			}
		}

		// return the jobs list
		return this.jobs;
	}

	/*
	 * Accept a certain job
	 */
	public void acceptJob(Job job) {
		try {
			File dir = new File(job.getDirectoryPath());

			if (!dir.exists()) {
				// create a directory for this job
				dir.mkdir();

				// sub file (download + add to the Job entity)
				job.setSubFile(this.downloadFile(job.getSubFileData(),
						job.getDirectoryPath()));

				// font files (download + add to the Job entity)
				File[] fonts = new File[job.getFontsData().length];

				for (int i = 0; i < fonts.length; i++) {
					fonts[i] = this.downloadFile(job.getFontsData()[i],
							job.getDirectoryPath());
				}
				job.setFonts(fonts);

				// create the configuration file containing the job data
				// This is for later use (in case of a job pause)
				this.generateConfigFile(job);

				// send the accept message request to the server
				// throw an exception if the request fails
				String response = ConnectionManager.sendJobAcceptMessage(
						job.getID(), _userInfo.getUserName(), true);
				if (response.equals("false"))
					throw new Exception();

				// add the job to the list
				if (!response.equals("error"))
					acceptedJobs.add(job);
			}
		} catch (Exception ex) {
			MessageBox message = new MessageBox(Display.getCurrent()
					.getShells()[0], SWT.ICON_ERROR);
			message.setMessage("There was an error accepting this job."
					+ "\nOr the server may have refused you this job.");
			message.setText("Error");
			message.open();
		}

	}

	/*
	 * Decline a certain job. This also makes the selected job unavailable for
	 * the user
	 */
	public void cancelJob(Job job) {

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
	 * Download a certain file from the web
	 * 
	 * @param fileData
	 *            A String containing the name of the file to be downloaded and
	 *            the url from where it can be fetched<b> The format is:
	 *            file_name=file_URL
	 * @param dir
	 *            The directory path where to save the file
	 * @return A File entity representing the downloaded file
	 * @throws Exception
	 */
	private File downloadFile(String fileData, String dir) throws Exception {
		String[] nameURLContainer = fileData.split("=");

		File file = new File(dir + "\\" + nameURLContainer[0]);
		URL fileURL = new URL(nameURLContainer[1]);

		FileUtils.copyURLToFile(fileURL, file);

		return file;
	}

	/**
	 * Creates a configuration file for a certain job in its own directory and
	 * fills it with data
	 * 
	 * @param job
	 *            The Job entity for which to create this file
	 * @throws Exception
	 */
	private void generateConfigFile(Job job) throws Exception {
		File file = new File(job.getDirectoryPath()
				+ JobManager.CONFIG_FILE_NAME);

		// delete the file if it already exists
		if (file.exists())
			file.delete();

		// recreate it (void from data)
		file.createNewFile();

		// fill it with data (exclude raw data about the subfile and font files)
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				file.getAbsoluteFile()));
		writer.write(job.getID() + "\n");
		writer.write(job.getName() + "\n");
		writer.write(job.getType() + "\n");
		writer.write(job.getDescription() + "\n");
		writer.write(job.getPreviousStaffMember() + "\n");
		writer.write(job.getNextStaffMember() + "\n");
		writer.write(job.getStartDate() + "\n");
		writer.write(job.getDirectoryPath() + "\n");
		writer.write(job.getSubFile().getAbsolutePath() + "\n");

		for (int i = 0; i < job.getFonts().length; i++) {
			writer.write(job.getFonts()[i].getAbsolutePath() + "\n");
		}

		// close the writer
		writer.close();

	}

	/**
	 * If the working directory doesn't exist, it creates it.
	 * 
	 * If it exists, then it tries to reload all the jobs that are still present
	 * in it
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
					File config = new File(jobFolder.getAbsolutePath()
							+ JobManager.CONFIG_FILE_NAME);

					BufferedReader reader = new BufferedReader(new FileReader(
							config.getAbsoluteFile()));
					
					//read the data from the file and place it in a new Job entity
					Job job = new Job();
					//TODO - WRITE CODE HERE
					
					//close the reader
					reader.close();
					
					//add the Job entity to the "accepted list"
					acceptedJobs.add(job);
					
				} catch (Exception ex) {
					// warn the user about loading errors
					// find a way to make him save his current work on the job
					// directory that gave this error
				}
			}
		}
	}
}