package com.coddotech.teamsubb.connection.model;

import java.util.Observable;

import com.coddotech.teamsubb.gadget.gui.GadgetWindow;
import com.coddotech.teamsubb.jobs.gui.JobWindow;

public class Login extends Observable {

	private static final String[] DEFAULT_USER_RANKS = { "Membru", "Moderator",
			"Administrator", "Fondator" };

	/**
	 * Start the login procedure
	 */
	public void doLogin(String user, String pass) {

		String response = ConnectionManager.sendLoginRequest(user, pass);

		String[] result = response.split("&");

		// notify the views about the success or failure of the login
		// process that took place
		this.setChanged();
		notifyObservers(Boolean.parseBoolean(result[0]));

		if (Boolean.parseBoolean(result[0])) {
			// if the login process is successful continue with starting the
			// application's main functionalities and close the login window
			GadgetWindow gadget = new GadgetWindow(this.getUserInfo(result), this.getJobsInfo(result));

			gadget.open();
		}
	}

	/**
	 * Extract the jobs data into a boolean array indicating the jobs types that
	 * this user can work on <br>
	 * <br>
	 * data[0] -> true/false <br>
	 * data[1] -> email <br>
	 * data[2] -> rank <br>
	 * data[3 -> n] -> job titles
	 * 
	 * @param data
	 *            A String collection containing the server's response
	 * @return A boolean array containing the possible jobs for this user
	 */
	private boolean[] getJobsInfo(String[] data) {
		boolean[] jobsData = { false, false, false, false, false, false, false };

		for (int i = 3; i < data.length; i++) {

			for (int j = 0; j < JobWindow.DEFAULT_JOBS_INFO_HEADERS.length; j++) {

				if (data[i].equals(JobWindow.DEFAULT_JOBS_INFO_HEADERS[j])) {
					jobsData[j] = true;
					break;
				}
			}
		}

		return jobsData;
	}

	/**
	 * Extract the user information into a String array. Information contains:
	 * user name, email and rank <br>
	 * <br>
	 * data[0] -> true/false <br>
	 * data[1] -> email <br>
	 * data[2] -> rank (integer)<br>
	 * data[3 -> n] -> job titles
	 * 
	 * @param data
	 *            A String collection containing the server's response
	 * @return A String array containing the user's information
	 */
	private String[] getUserInfo(String[] data) {
		String[] info = new String[3];

		info[0] = data[1];
		info[1] = data[2];

		/*
		 * The rank is stored as an integer, indicating on of the following
		 * ranks: 0 - Membru; 1 - Moderator; 2 - Administrator; 3 - Fondator;
		 */
		info[2] = Login.DEFAULT_USER_RANKS[Integer.parseInt(data[3])];

		return info;
	}

}
