package com.coddotech.teamsubb.appmanage.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.coddotech.teamsubb.connection.gui.LoginWindow;
import com.coddotech.teamsubb.connection.model.Login;
import com.coddotech.teamsubb.gadget.gui.GadgetWindow;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.jobs.model.JobSearchTimer;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.settings.model.AppSettings;

public class AppManager {

	public static void startApp() {
		ActivityLogger.performInitializations();

		ActivityLogger.logActivity("Main", "App initialization");

		try {
			if (AppManager.isAutoLogin()) {
				// login automatically

			}
			else {

				// display the login window
				LoginWindow loginWindow = new LoginWindow();
				loginWindow.open();
				loginWindow = null;
			}

			// if the login process is successful continue with starting the
			// application's main functionalities and close the login window
			if (Login.isLoggedIn()) {

				// start the job searcher timer
				JobSearchTimer timer = JobSearchTimer.getInstance();

				AppSettings.getInstance().addObserver(timer);

				timer.startTimer();

				// display the gadget
				GadgetWindow gadget = new GadgetWindow();
				gadget.open();

			}

		}
		catch (Exception ex) {
			ActivityLogger.logActivity("Main", "App runtime", "FATAL ERROR !!!");

			ActivityLogger.dumpAppErrorStack(ex);

			AppManager.displayFatalErrorMessage();

		}
	}

	public static void exitApp() {
		// Close all the shells (the main shell is closed last)
		Shell main = null;
		for (Shell shell : Display.getCurrent().getShells()) {
			if (shell.getText().equals("Gadget"))
				main = shell;
			else
				shell.close();
		}

		main.close();

		// close the dump files
		ActivityLogger.createLogFile();

		// dispose of global resources
		AppManager.disposeGlobalResources();

		ActivityLogger.logActivity(AppManager.class.getName(), "App exit");
	}

	private static void disposeGlobalResources() {

		// user classes (singletons)
		JobSearchTimer.getInstance().dispose();
		JobManager.getInstance().dispose();
		AppSettings.getInstance().dispose();

		// resources
		CustomWindow.APP_ICON.dispose();
		CustomWindow.BOLD_FONT.dispose();
		CustomWindow.DEFAULT_FONT.dispose();

		try {
			Display.getDefault().dispose();
		}
		catch (Exception ex) {

		}
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
	 * METHOD CREATED IN PREPARATION FOR ADDING NEW FEATURES TO THE SETTINGS NOT IMPLEMENTED YET !
	 * 
	 * @return Always false
	 */
	private static boolean isAutoLogin() {
		return false;
	}
}
