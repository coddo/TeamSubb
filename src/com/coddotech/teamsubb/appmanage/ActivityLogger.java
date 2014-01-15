package com.coddotech.teamsubb.appmanage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

/**
 * Class used to log the entire activity of the application during runtime. <br>
 * 
 * If any fata errors or major bugs are found, this class is responsible for
 * creating the dump file containing the error details.
 * 
 * This class is a singleton.
 * 
 * @author Coddo
 * 
 */
public class ActivityLogger {

	private static String dumpPath = System.getProperty("user.dir")
			+ File.separator + "DUMP" + File.separator;

	private static File logFile;
	private static File dumpFile;

	private static boolean initializationFailed = false;

	private static BufferedWriter logWriter;
	private static BufferedWriter dumpWriter;

	/**
	 * * Log an activity issued by the user
	 * 
	 * @param className
	 *            The name of the class that started the activity
	 * @param activity
	 *            The activity which is invoked
	 * @param msg
	 *            The message received from the completion of the activity
	 */
	public static void logActivity(String className, String activity, String msg) {
		if (!initializationFailed) {

			String message = "(" + className + ") -> " + activity + " -> "
					+ msg;

			try {
				logWriter.write(ActivityLogger.getCurrentTime() + message
						+ "\n");

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

	/**
	 * Log an activity issued by the user and mark it as successful
	 * 
	 * @param className
	 *            The name of the class that started the activity
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
	 * @param activity
	 *            The activity which was active at the time when the exception
	 *            is caught
	 * @param ex
	 *            The exception that was handled
	 */
	public static void logException(String className, String activity,
			Exception ex) {

		ActivityLogger.logActivity(className, activity, ex.getMessage());

	}

	/**
	 * Create the dump file when a fatal error is caught
	 * 
	 * @param ex
	 *            The exception received at app crash
	 */
	public static void createDump(Exception ex) {
		if (!initializationFailed) {

			String message;
			message = ex.getClass().toString() + "\n\n";

			for (StackTraceElement element : ex.getStackTrace()) {
				message += "( " + element.getClassName() + " ) ";
				message += "{ " + element.getMethodName() + " } -> ";
				message += element.toString() + "\n";
			}

			try {
				dumpFile.createNewFile();

				dumpWriter = new BufferedWriter(new FileWriter(
						dumpFile.getAbsoluteFile()));

				dumpWriter.write(message);

				dumpWriter.close();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		}
	}

	/**
	 * Closes the stream for the log file and unlocks it
	 */
	public static void closeLogFile() {
		if (!initializationFailed) {

			try {
				logWriter.close();

			} catch (IOException e) {
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

		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}

	}

	/**
	 * When there are more than 50 files in the directory, it gets cleaned by
	 * deleting all of them
	 * 
	 * @param dumpDirectory
	 *            A File instance
	 */
	private static void cleanDumpDirectory(File dumpDirectory) {
		if (dumpDirectory.list().length >= 50) {

			try {
				FileUtils.cleanDirectory(dumpDirectory);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	/**
	 * Initializes the files in which to dump the app's activity and error
	 * messages.
	 */
	private static void initializeFiles() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

		String date = dateFormat.format(new Date());

		logFile = new File(dumpPath + "LOG - " + date + ".log");
		dumpFile = new File(dumpPath + "DUMP - " + date + ".dmp");

		try {
			logFile.createNewFile();

			logWriter = new BufferedWriter(new FileWriter(
					logFile.getAbsoluteFile()));

		} catch (IOException ex) {
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
}
