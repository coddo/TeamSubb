package com.coddotech.teamsubb.connection.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.chat.model.LoggedUser;
import com.coddotech.teamsubb.crypting.Cypher;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.settings.model.Settings;

public class Login extends Observable {

	public static final File loginDataFile = new File(System.getProperty("user.dir") + File.separator
			+ "login.lin");

	private static boolean loginSuccess = false;

	/**
	 * Start the login procedure
	 */
	public void doLogin(String user, String pass, boolean automaticLogin) {

		try {
			String response = ConnectionManager.sendLoginRequest(user, pass);

			String responseValue = response.substring(0, response.indexOf(JobManager.SEPARATOR_DATA));

			Login.loginSuccess = Boolean.parseBoolean(responseValue);

			// notify the views about the success or failure of the login
			// process that took place
			this.setChanged();
			notifyObservers(Login.loginSuccess);

			ActivityLogger.logActivity(this.getClass().getName(), "User login");

			if (automaticLogin) {
				Login.createLoginDataFile(user, pass);

				Settings.getInstance().setAutomaticLogin(true);
				Settings.getInstance().saveSettings();
			}

			if (Login.loginSuccess) {
				String userDetails = response.replace(responseValue, "");
				userDetails = userDetails.replaceFirst(JobManager.SEPARATOR_DATA, "");

				LoggedUser.getInstance().setUserDetails(userDetails);

			}

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "User login", ex);

		}
	}

	public static boolean isLoggedIn() {
		return Login.loginSuccess;
	}

	/**
	 * Read the login details from the data file
	 * 
	 * @return A String collection: (user, pass)
	 */
	public static String[] readLoginDetails() {
		try {

			if (!Login.loginDataFile.exists())
				throw new Exception();

			FileInputStream fis = new FileInputStream(Login.loginDataFile);
			
			byte[] data = new byte[(int) Login.loginDataFile.length()];
			
			fis.read(data);
			fis.close();

			return Cypher.decrypt(new String(data, "UTF-8")).split("\n");
		}

		catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Create the data file which contain the details for this user in order to be able to do the
	 * login automatically on next program start
	 * 
	 * @param user
	 *            The username for login
	 * @param pass
	 *            The password for login
	 * 
	 * @throws IOException
	 */
	private static void createLoginDataFile(String user, String pass) throws IOException {
		if (!Login.loginDataFile.exists())
			Login.loginDataFile.createNewFile();

		BufferedWriter writer = new BufferedWriter(new FileWriter(Login.loginDataFile.getAbsoluteFile()));

		writer.write(Cypher.encrypt(user + "\n"));
		writer.write(Cypher.encrypt(pass));

		writer.close();
	}

}
