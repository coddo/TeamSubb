package com.coddotech.teamsubb.appmanage.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Class used to log the entire activity of the application during runtime. <br>
 * If any fata errors or major bugs are found, this class is responsible for
 * creating the dump file containing the error details.
 * This class is a singleton.
 * 
 * @author Coddo
 */
public class ActivityLogger {

	private static String dumpPath = System.getProperty("user.dir") + File.separator + "DUMP" + File.separator;

	private static File logFile;
	private static File dumpFile;

	private static boolean initializationFailed = false;

	private static Stack<String> logStack = new Stack<String>();

	/**
	 * * Log an activity issued by the user
	 * 
	 * @param className
	 *            The name of the class that started the activity
	 * 
	 * @param activity
	 *            The activity which is invoked
	 * 
	 * @param msg
	 *            The message received from the completion of the activity
	 */
	public static void logActivity(String className, String activity, String msg) {
		if (!initializationFailed) {

			String message = "(" + className + ") -> " + activity + " -> " + msg;

			logStack.add(ActivityLogger.getCurrentTime() + message + "\n");

		}
	}

	/**
	 * Log an activity issued by the user and mark it as successful
	 * 
	 * @param className
	 *            The name of the class that started the activity
	 * 
	 * @param activity
	 *            The activity which is invoked
	 */
	public static void logActivity(String className, String activity) {

		ActivityLogger.logActivity(className, activity, "OK!");
	}

	/**
	 * Log an exception that has been handled by the app
	 * 
	 * @param className
	 *            The name of the class that caused the exception
	 * 
	 * @param activity
	 *            The activity which was active at the time when the exception is caught
	 * 
	 * @param ex
	 *            The exception that was handled
	 */
	public static void logException(String className, String activity, Exception ex) {

		String message = "(" + className + ") -> " + activity + " -> " + ex.toString();

		logStack.add("[!] " + ActivityLogger.getCurrentTime() + message + "\n");

		// save the contents of the log file
		ActivityLogger.dumpLogStack();

	}

	/**
	 * Create the dump file when a fatal error is caught
	 * 
	 * @param ex
	 *            The exception received at app crash
	 */
	public static void dumpAppErrorStack(Exception ex) {
		if (!initializationFailed) {

			String message;
			message = ex.toString() + "\n\n";

			for (StackTraceElement element : ex.getStackTrace()) {
				message += "( " + element.getClassName() + " ) ";

				message += "{ " + element.getMethodName() + " } -> ";

				message += element.toString() + "\n";
			}

			try {
				dumpFile.createNewFile();

				BufferedWriter dumpWriter = new BufferedWriter(new FileWriter(dumpFile.getAbsoluteFile()));

				dumpWriter.write(message);

				dumpWriter.close();

				ActivityLogger.logStack.clear();

			}
			catch (IOException e) {
				e.printStackTrace();

			}
		}
	}

	/**
	 * Closes the stream for the log file and unlocks it
	 */
	public static void dumpLogStack() {
		if (!initializationFailed) {

			try {
				BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile(), true));

				while (ActivityLogger.logStack.size() > 0)
					logWriter.write(ActivityLogger.logStack.remove(0));

				logWriter.close();

				// empty the stack
				ActivityLogger.logStack.clear();

			}
			catch (IOException e) {
				e.printStackTrace();

			}
		}
	}

	/**
	 * Starts the initializations for the dump directory and its files
	 */
	public static void performInitializations() {
		initializeLogDirectory();

		initializeFiles();
	}

	/**
	 * Initializes the directory in which all the log and dump files are created
	 */
	private static void initializeLogDirectory() {
		File dumpDirectory = new File(ActivityLogger.dumpPath);

		try {

			if (!dumpDirectory.exists())
				dumpDirectory.mkdir();

			else
				cleanDumpDirectory(dumpDirectory);

		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());

		}

	}

	/**
	 * When there are more than 5 files of each type (log or dump) in the directory, it gets cleaned
	 * by deleting the oldest one.
	 * 
	 * @param dumpDirectory
	 *            A File instance
	 */
	private static void cleanDumpDirectory(File dumpDirectory) {
		File[] logFiles = dumpDirectory.listFiles(ActivityLogger.getFileTypeFilter("log"));

		File[] dumpFiles = dumpDirectory.listFiles(ActivityLogger.getFileTypeFilter("dmp"));

		// log files cleaning
		if (logFiles.length >= 5) {

			// sort the files by "date modified"
			Arrays.sort(logFiles, new FileDateCompare());

			// delete the oldest files
			logFiles[0].delete();

		}

		// dump files cleaning
		if (dumpFiles.length >= 5) {
			Arrays.sort(dumpFiles, new FileDateCompare());

			dumpFiles[0].delete();
		}
	}

	/**
	 * Initializes the files in which to dump the app's activity and error messages.
	 */
	private static void initializeFiles() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

		String date = dateFormat.format(new Date());

		logFile = new File(dumpPath + "LOG - " + date + ".log");

		dumpFile = new File(dumpPath + "DUMP - " + date + ".dmp");

		try {
			logFile.createNewFile();

		}
		catch (IOException ex) {
			ex.printStackTrace();

			initializationFailed = true;
		}
	}

	/**
	 * Retrueve the current date and time
	 * 
	 * @return A String value
	 */
	private static String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		String format = "[" + dateFormat.format(new Date()) + "] ";

		return format;
	}

	private static FilenameFilter getFileTypeFilter(final String type) {
		FilenameFilter filter = new FilenameFilter() {

			@Override
			public boolean accept(File path, String file) {

				return file.split(Pattern.quote("."))[1].equals(type);
			}
		};

		return filter;
	}

	/**
	 * Class the implements Comparator<T> and compares the dates in which 2 files were modified.
	 */
	private static class FileDateCompare implements Comparator<File> {

		public FileDateCompare() {

		}

		@Override
		public int compare(File f1, File f2) {
			return this.compareDates(f1.lastModified(), f2.lastModified());
		}

		private int compareDates(long date1, long date2) {
			return (date1 <= date2) ? -1 : 1;
		}

	}
}
