package com.coddotech.teamsubb.main;

import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.connection.LoginWindow;
import com.coddotech.teamsubb.settings.AppSettingsWindow;

@SuppressWarnings("unused")
public class MainClass {

	public static void main(String[] args) {
		/*display the login window as a dialog*/
		LoginWindow login = new LoginWindow();
		login.open();
		login = null;//*/
		
		/*AppSettingsWindow settings = new AppSettingsWindow();
		settings.open();//*/
		
		/*
		String[] ui = new String[] {"a", "b", "c"};
		boolean[] uj = new boolean[] {true, true, true, true, true, true, true};
		GadgetWindow gadget = new GadgetWindow(ui, uj);
		gadget.open();//*/
		
		Display.getCurrent().dispose();
	}
}