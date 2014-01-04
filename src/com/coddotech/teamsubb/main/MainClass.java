package com.coddotech.teamsubb.main;

import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.connection.LoginWindow;

public class MainClass {

	public static void main(String[] args) {

		// display the login window as a dialog
		LoginWindow login = new LoginWindow();
		login.open();
		login = null;

		//dispose of global resources
		CustomWindow.APP_ICON.dispose();
		CustomWindow.BOLD_FONT.dispose();
		CustomWindow.DEFAULT_FONT.dispose();
		Display.getCurrent().dispose();
	}
}