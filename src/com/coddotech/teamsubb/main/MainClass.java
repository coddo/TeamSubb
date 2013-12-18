package com.coddotech.teamsubb.main;

import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.connection.LoginWindow;

public class MainClass {

	public static void main(String[] args) {
		/*display the login window as a dialog*/
		LoginWindow login = new LoginWindow();
		login.open();
		login = null;
		
		Display.getCurrent().dispose();//*/
	}
}