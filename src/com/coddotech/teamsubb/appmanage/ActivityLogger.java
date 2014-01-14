package com.coddotech.teamsubb.appmanage;

import java.io.File;

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

	private static String LOG_FOLDER_PATH = System.getProperty("user.dir")
			+ File.separator + "DUMP" + File.separator;

	private File logFile;
	private File dumpFile;

	private static ActivityLogger instance = null;

	private ActivityLogger() {
		initializeLogDirectory();
		initializeLogFiles();
	}

	public static ActivityLogger getInstance() {
		if (instance == null)
			instance = new ActivityLogger();

		return instance;
	}

	public void logActivity(String className, String activity) {

	}

	public void logError(String className, String activity, String errorType,
			String message) {

	}

	public void createDump(Exception ex) {

	}

	public void saveLogFile() {

	}

	private void initializeLogDirectory() {
		File logFolder = new File(ActivityLogger.LOG_FOLDER_PATH);

		try {
			if (!logFolder.exists())
				logFolder.mkdir();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	private void initializeLogFiles() {

	}

}
