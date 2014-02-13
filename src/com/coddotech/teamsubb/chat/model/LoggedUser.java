package com.coddotech.teamsubb.chat.model;

/**
 * Class representing the User that is currently logged into the app (Extends the User class).
 * 
 * This class is a singleton
 * 
 * @author coddo
 * 
 */
public class LoggedUser extends User {

	private static LoggedUser instance = null;

	private LoggedUser() {

	}

	/**
	 * Get an instance for this class
	 * 
	 * @return A LoggedUser instance
	 */
	public static LoggedUser getInstance() {
		if (instance == null)
			instance = new LoggedUser();

		return instance;
	}

}
