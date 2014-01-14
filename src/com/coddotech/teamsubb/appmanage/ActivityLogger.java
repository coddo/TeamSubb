package com.coddotech.teamsubb.appmanage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public static void logActivity(String className, String activity) {
		if (!initializationFailed) {

			String message = "(" + className + ") -> " + activity + " -> OK!";

			try {
				logWriter.write(ActivityLogger.getCurrentTime() + message
						+ "\n");

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

	public static void logError(String className, String activity, Exception ex) {

		String message = "(" + className + ") -> " + activity + " -> ("
				+ ex.getCause().toString() + ") " + ex.getMessage();

		try {
			logWriter.write(ActivityLogger.getCurrentTime() + message + "\n");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void createDump(Exception ex) {
		if (!initializationFailed) {
			
			String message = "CAUSE: " + ex.getCause().toString() + "\n\n";
			message += ex.getMessage() + "\n\n";
			message += ex.getStackTrace() + "\n\n";
			message += ex.getSuppressed() + "\n\n";
			
			try {
				logWriter.write(message);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void releaseFiles() {
		if (!initializationFailed) {

			try {

				logWriter.close();
				dumpWriter.close();

			} catch (IOException e) {

				e.printStackTrace();

			}
		}
	}

	public static void performInitializations() {
		initializeLogDirectory();
		initializeLogFiles();
	}

	private static void initializeLogDirectory() {
		File logFolder = new File(ActivityLogger.dumpPath);

		try {

			if (!logFolder.exists())
				logFolder.mkdir();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}

	}

	private static void initializeLogFiles() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

		String date = dateFormat.format(new Date());

		logFile = new File(dumpPath + "LOG - " + date + ".log");
		dumpFile = new File(dumpPath + "DUMP - " + date + ".dmp");

		try {
			logFile.createNewFile();
			dumpFile.createNewFile();

			logWriter = new BufferedWriter(new FileWriter(
					logFile.getAbsoluteFile()));

			dumpWriter = new BufferedWriter(new FileWriter(
					dumpFile.getAbsoluteFile()));

		} catch (IOException ex) {
			ex.printStackTrace();
			initializationFailed = true;
		}
	}

	private static String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		String format = "[" + dateFormat.format(new Date()) + "] ";

		return format;
	}

}
