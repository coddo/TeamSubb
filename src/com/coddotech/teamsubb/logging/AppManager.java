package com.coddotech.teamsubb.logging;

import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.connection.LoginWindow;
import com.coddotech.teamsubb.main.CustomWindow;

public class AppManager {

	public static void startApp() {
		ActivityLogger logger = ActivityLogger.getInstance();
		logger.logActivity("Main", "App initialization");

		try {
			// display the login window
			if (AppManager.isAutoLogin()) {

			} else {
				LoginWindow login = new LoginWindow();
				login.open();
				login = null;
			}

		} catch (Exception ex) {
			logger.logError("Main", "App runtime", "FATAL ERROR",
					ex.getMessage());
			logger.createDump(ex);
		}

		// close the dump files
		logger.saveLogFile();

		// dispose of global resources
		AppManager.disposeGlobalResources();
	}

	private static void disposeGlobalResources() {
		CustomWindow.APP_ICON.dispose();
		CustomWindow.BOLD_FONT.dispose();
		CustomWindow.DEFAULT_FONT.dispose();
		Display.getCurrent().dispose();
	}

	/**
	 * METHOD CREATED IN PREPARATION FOR ADDING NEW FEATURES TO THE SETTINGS
	 * 
	 * NOT IMPLEMENTED YET !
	 * 
	 * @return Always false
	 */
	private static boolean isAutoLogin() {
		return false;
	}
}
