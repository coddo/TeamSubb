package com.coddotech.teamsubb.maingui;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.coddotech.teamsubb.connection.ConnectionManager;
import com.coddotech.teamsubb.jobs.UserDetails;

public class MainClass {

	public static void main(String[] args) throws IOException {
		/*display the login window as a dialog*/
		LoginWindow login = new LoginWindow();
		login.setShell(new Shell(Display.getCurrent(), SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL));
		login.show();
		
		//get the login result string
		String mayContinue = login.getMayContinueValue();
		
		//dispose of the login window class
		login.dispose();
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
