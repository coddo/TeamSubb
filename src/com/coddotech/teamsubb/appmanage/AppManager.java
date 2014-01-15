package com.coddotech.teamsubb.appmanage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

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
			ActivityLogger.logActivity("Main", "App runtime", "FATAL ERROR !!!");
			
			ActivityLogger.createDump(ex);
			
			AppManager.displayFatalErrorMessage();
			
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
	
	private static void displayFatalErrorMessage() {
		Shell shell = new Shell(Display.getCurrent(), SWT.None);
		
		MessageBox message = new MessageBox(shell, SWT.ICON_ERROR);
		message.setMessage("A FATAL ERROR has occured and the app has stopped working !");
		message.setText("TeamSubb");
		message.open();
		
		shell.dispose();
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
