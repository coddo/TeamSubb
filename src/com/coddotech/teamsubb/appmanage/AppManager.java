package com.coddotech.teamsubb.appmanage;

import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.connection.gui.LoginWindow;
import com.coddotech.teamsubb.main.CustomWindow;

public class AppManager {

	public static void startApp() {
		ActivityLogger.performInitializations();
		
		ActivityLogger.logActivity("Main", "App initialization");

		try {
			if (AppManager.isAutoLogin()) {
				// login automatically

			} else {
				// display the login window
				LoginWindow login = new LoginWindow();
				login.open();
				login = null;
			}

		} catch (Exception ex) {
			ActivityLogger.logError("Main", "App runtime", ex);
			
			ActivityLogger.createDump(ex);
		}

		// close the dump files
		ActivityLogger.releaseFiles();

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
