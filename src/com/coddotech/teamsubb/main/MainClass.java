package com.coddotech.teamsubb.main;

import java.io.IOException;

import com.coddotech.teamsubb.connection.ConnectionManager;
import com.coddotech.teamsubb.connection.LoginWindow;
import com.coddotech.teamsubb.jobs.UserDetails;

public class MainClass {

	public static void main(String[] args) throws IOException {
		/*display the login window as a dialog*/
		LoginWindow login = new LoginWindow();
		login.show();
		
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
		UserDetails det = new UserDetails();
		det.restoreDefaultSettings();
		
		//logout from the server if possible
		if(mayContinue.equals("OK"))
			ConnectionManager.sendLogoutRequest(det.getUserName());
		
		det.dispose();
		det = null;//*/
	}
}
