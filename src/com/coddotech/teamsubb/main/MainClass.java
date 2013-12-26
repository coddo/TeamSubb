package com.coddotech.teamsubb.main;

import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.connection.LoginWindow;
import com.coddotech.teamsubb.settings.AppSettingsWindow;
import com.coddotech.teamsubb.jobs.JobManager;

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
		
		/*
		JobManager jobs = new JobManager("coddo", new String[] {"Traducator", "Verificator", "Encoder"});
		jobs.createJob("NI MA ACI O LOVITURA DE TARAN", 0, "WTF IS THIS SHIT", "Coddo", "Settings.xml", new String[] {"Settings.xml", "README.md"});
		//*/
		
		Display.getCurrent().dispose();
	}
}