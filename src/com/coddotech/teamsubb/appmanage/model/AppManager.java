package com.coddotech.teamsubb.appmanage.model;

import java.io.IOException;
import java.net.ServerSocket;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.coddotech.teamsubb.chat.gui.StaffItem;
import com.coddotech.teamsubb.chat.model.Messaging;
import com.coddotech.teamsubb.connection.gui.LoginWindow;
import com.coddotech.teamsubb.connection.model.Login;
import com.coddotech.teamsubb.gadget.gui.GadgetWindow;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.notifications.gui.PopUpMessages;
import com.coddotech.teamsubb.settings.model.Settings;
import com.coddotech.teamsubb.timers.JobSearchTimer;

public class AppManager {

	private static ServerSocket instanceLock = null;

	public static void startApp() {
		ActivityLogger.performInitializations();

		ActivityLogger.logActivity("Main", "App initialization");

		try {

			if (AppManager.createAppInstanceLock()) {

				AppManager.performUserLogin(); // locks the main thread

				AppManager.startMainComponents(); // locks the main thread

				AppManager.deleteAppInstanceLock();
			}

			// display a message telling the user that the app is already running
			else {
				PopUpMessages.getInstance().areadyRunning();

			}

		}

		catch (Exception ex) {
			ActivityLogger.logActivity("Main", "App runtime", "FATAL ERROR !!!");

			ActivityLogger.dumpAppErrorStack(ex);

			PopUpMessages.getInstance().fatalError();

			AppManager.deleteAppInstanceLock();

		}

		finally {
			AppManager.performExitOperations();

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

			// start the chat modules
			Messaging.getInstance();

			// open the gadget
			GadgetWindow gadget = new GadgetWindow();
			gadget.open();
		}
	}

	private static void performUserLogin() {
		// read the settings for the first time
		Settings.getInstance().readSettings();

		boolean displayLoginWindow = false;

		if (AppManager.isAutoLogin()) {

			// login automatically
			String[] data;

			if ((data = Login.readLoginDetails()) == null)
				displayLoginWindow = true;

			else {
				new Login().doLogin(data[0], data[1], false);

			}

		}

		else {
			displayLoginWindow = true;
		}

		if (displayLoginWindow) {

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
		Messaging.getInstance().dispose();

		// resources
		CustomWindow.APP_ICON.dispose();
		CustomWindow.BOLD_FONT.dispose();
		CustomWindow.DEFAULT_FONT.dispose();

		StaffItem.OFFLINE.dispose();
		StaffItem.ONLINE.dispose();
		StaffItem.FONT_RANK.dispose();
		StaffItem.FONT_USER.dispose();

		try {
			Display.getDefault().dispose();

		}

		catch (Exception ex) {

		}
	}

	private static boolean createAppInstanceLock() {

		try {

			instanceLock = new ServerSocket(1993);

			return true;
		}

		catch (Exception ex) {
			return false;

		}
	}

	private static void deleteAppInstanceLock() {
		if (instanceLock != null) {

			try {
				instanceLock.close();

			}

			catch (IOException e) {

			}
		}
	}

	/**
	 * Get the logical value indicating is the application should automatically login with the saved
	 * user data.
	 * 
	 * @return Always false
	 */
	private static boolean isAutoLogin() {
		return Settings.getInstance().isAutomaticLogin();
	}

}
