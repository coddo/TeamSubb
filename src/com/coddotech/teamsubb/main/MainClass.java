package com.coddotech.teamsubb.main;

import java.io.IOException;

import com.coddotech.teamsubb.connection.ConnectionManager;
import com.coddotech.teamsubb.connection.LoginWindow;
import com.coddotech.teamsubb.jobs.Job;
import com.coddotech.teamsubb.jobs.UserInformation;

public class MainClass {

	public static void main(String[] args) throws IOException {
		Job j = new Job();
		/*display the login window as a dialog*/
		LoginWindow login = new LoginWindow();
		login.show(); j.dispose();
		
		//get the login result string
		String mayContinue = login.getMayContinueValue();
		login = null;
		
		//if the login result is "OK", then proceed with
		//opening the gadget 
		if (mayContinue.equals("OK")) {
			DesktopGadget gadget = new DesktopGadget();
			gadget.show();
		}
		
		//reset the user information file to its defaults
		UserInformation det = new UserInformation();
		det.restoreDefaultSettings();
		
		//logout from the server if possible
		if(mayContinue.equals("OK"))
			ConnectionManager.sendLogoutRequest(det.getUserName());
		
		det.dispose();
		det = null;//*/
	}
}