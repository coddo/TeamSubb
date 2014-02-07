package com.coddotech.teamsubb.chat.model;

public class LoggedUser extends User {

	private static LoggedUser instance = null;

	private LoggedUser() {

	}

	public static LoggedUser getInstance() {
		if (instance == null)
			instance = new LoggedUser();

		return instance;
	}

}
