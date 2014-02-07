package com.coddotech.teamsubb.chat.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.main.CustomWindow;

/**
 * Class for managing the lists with the staff (and their details) employed by
 * the fansub.
 * 
 * @author Coddo
 */
public class StaffManager {

	List<StaffMember> staff = new ArrayList<StaffMember>();

	public void dispose() {
		staff.clear();

	}

	public StaffMember[] getStaff() {
		return this.staff.toArray(new StaffMember[staff.size()]);

	}

	/**
	 * Refresh the list containing the staff details (fethes the data from the server)
	 */
	public void refreshStaffList() {

		class StaffRefresher extends Thread {

			@Override
			public void run() {

				if (CustomWindow.isConnected(false)) {
					String[] staffData = ConnectionManager.sendStaffRequest().split(JobManager.SEPARATOR_ENTITY);

					if (staffData == null)
						return;

					staff.clear();

					for (String data : staffData) {
						StaffMember member = new StaffMember();

						member.setUserDetails(data);

						staff.add(member);
					}
				}

			}
		}

		StaffRefresher refresher = new StaffRefresher();
		refresher.start();
	}

	/**
	 * Fetch the staff list from the server.<br>
	 * The list contains both the name of the staff member, and his/her attributions. <br>
	 * <br>
	 * 
	 * Example: NAME: Job1 | Job2 | Job3....
	 * 
	 * @param includeNeutral
	 *            Logical value telling the method whether to include the "anyone" item into the
	 *            collection (at the beginning)
	 * 
	 * @return A String collection
	 */
	public static String[] fetchFormatedStaffList(boolean includeID) {
		if (CustomWindow.isConnected(false)) {

			String[] staffData = ConnectionManager.sendStaffRequest().split(JobManager.SEPARATOR_ENTITY);

			for (int i = 0; i < staffData.length; i++) {
				staffData[i] = StaffManager.createStaffString(staffData[i], includeID);

			}

			return staffData;
		}

		return null;
	}

	/**
	 * Create an easier to read String out of the raw message received from the server.<br>
	 * <br>
	 * 
	 * The two possible structures are: <br>
	 * -NAME: Job1 | Job2 | Job3....
	 * -ID: NAME: Job1 | Job2 | Job3....
	 * 
	 * @param staffData
	 *            The raw message from the server
	 * @param includeID
	 *            Indicates whether to include the ID of the user in the String
	 * 
	 * @return A String value
	 */
	private static String createStaffString(String staffData, boolean includeID) {
		staffData = staffData.replaceAll(JobManager.SEPARATOR_DATA, " | ");

		if (!includeID) {
			staffData = staffData.replace(staffData.substring(0, staffData.indexOf(" | ") + 3), "");

		}

		else
			staffData = staffData.replaceFirst(Pattern.quote(" |"), ":");

		staffData = staffData.replaceFirst(Pattern.quote(" |"), ":");

		return staffData;
	}
}
