package com.coddotech.teamsubb.appmanage.model;

import java.io.File;
import java.io.IOException;

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
import com.coddotech.teamsubb.settings.model.Settings;

public class AppManager {

	private static File FILE_LOCK = new File(System.getProperty("user.dir") + File.separator + "lock.coddo");

	public static void startApp() {
		ActivityLogger.performInitializations();

		ActivityLogger.logActivity("Main", "App initialization");

		try {

			if (!AppManager.isRunning()) {

				AppManager.createAppInstanceLock();

				AppManager.performUserLogin();

				AppManager.startMainComponents();

				AppManager.performExitOperations();

				AppManager.deleteAppInstanceLock();
			}

			// display a message telling the user that the app is already running
			else {
				Shell shell = new Shell(Display.getDefault());

				MessageBox message = new MessageBox(shell, SWT.ICON_WARNING);

				message.setText("Already running");
				message.setMessage("TeamSubb is already running !\n Stop the currently active instance in order to start a new one.");

				message.open();

				// dispose the temporary shell
				shell.dispose();

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
		for (Shell shell : Display.getDefault().getShells()) {
			if (shell.getText().equals("Gadget"))
				main = shell;
			else
				shell.close();
		}

		main.close();
	}

	private static void startMainComponents() {
		// if the login process is successful continue with starting the
		// application's main functionalities and close the login window
		if (Login.isLoggedIn()) {

			// start the job searcher timer
			JobSearchTimer timer = JobSearchTimer.getInstance();

			Settings.getInstance().addObserver(timer);

			timer.startTimer();

			final GadgetWindow gadget = new GadgetWindow();
			gadget.open();
		}
	}

	private static void performUserLogin() {
		if (AppManager.isAutoLogin()) {
			// login automatically

		}
		else {

			// display the login window
			LoginWindow loginWindow = new LoginWindow();
			loginWindow.open();
			loginWindow = null;
		}
	}

	private static void performExitOperations() {
		// dispose of global resources
		AppManager.disposeGlobalResources();

		ActivityLogger.logActivity(AppManager.class.getName(), "App exit");

		// close the dump files
		ActivityLogger.dumpLogStack();
	}

	private static void disposeGlobalResources() {

		// user classes (singletons)
		JobSearchTimer.getInstance().dispose();
		JobManager.getInstance().dispose();
		Settings.getInstance().dispose();

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
		Shell shell = new Shell(Display.getDefault(), SWT.None);

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

	private static void createAppInstanceLock() {
		try {
			AppManager.FILE_LOCK.createNewFile();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void deleteAppInstanceLock() {
		AppManager.FILE_LOCK.delete();
	}

	private static boolean isRunning() {
		return AppManager.FILE_LOCK.exists();
	}
}
