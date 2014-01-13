package com.coddotech.teamsubb.logging;

import java.io.File;

/**
 * Class used to log the entire activity of the application during runtime. <br>
 * 
 * If any fata errors or major bugs are found, this class is responsible for
 * creating the dump file containing the error details.
 * 
 * This class is a singleton.
 * @author Coddo
 * 
 */
public class ActivityLogger {
	
	private static File LOG_FILE = new File("");
	private static File DUMP_FILE = new File("");

	private static ActivityLogger instance = null;
	
	private ActivityLogger() {
		
	}
	
	public ActivityLogger getInstance() {
		if (instance == null)
			instance = new ActivityLogger();
		
		return instance;
	}
	
	public void log(String className, String activity, String result) {
		
	}
	
	public void createDump(Exception ex) {
		
	}
	
	public void saveLogFile() {
		
	}
	
}
