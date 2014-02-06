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

	@SuppressWarnings ("unused")
	private class Staff {

		int ID;
		String name;
		String jobs;

	}

	List<Staff> staff = new ArrayList<Staff>();

	public void dispose() {
		staff.clear();

	}

	public Staff[] getStaff() {
		return this.staff.toArray(new Staff[staff.size()]);
		
	}

	/**
	 * Refresh the list containing the staff details (fethes the data from the server)
	 */
	public void refreshStaffList() {
		String[] staffData = StaffManager.fetchStaffList(true);

		if (staffData == null)
			return;

		staff.clear();

		for (String data : staffData) {
			String[] split = data.split(":");

			Staff member = new Staff();

			member.ID = Integer.parseInt(split[0]);
			member.name = split[1];
			member.jobs = split[2];

			staff.add(member);
		}
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
	public static String[] fetchStaffList(boolean includeID) {
		if (CustomWindow.isConnected(false)) {

			String[] staffData = ConnectionManager.sendStaffRequest().split(JobManager.SEPARATOR_JOBS);

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
		staffData = staffData.replaceAll(JobManager.SEPARATOR_FIELDS, " | ");

		if (!includeID) {
			staffData = staffData.replace(staffData.substring(0, staffData.indexOf(" | ") + 3), "");

		}

		else
			staffData = staffData.replaceFirst(Pattern.quote(" |"), ":");

		staffData = staffData.replaceFirst(Pattern.quote(" |"), ":");

		return staffData;
	}
}
