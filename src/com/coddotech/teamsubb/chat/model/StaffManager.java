package com.coddotech.teamsubb.chat.model;

import java.util.regex.Pattern;

import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.main.CustomWindow;

/**
 * Class for managing the lists with the staff (and their details) employed by
 * the fansub.
 * 
 * @author Coddo
 * 
 */
public class StaffManager {

	/**
	 * Fetch the staff list from the server.<br>
	 * The list contains both the name of the staff member, and his/her
	 * attributions.<br>
	 * <br>
	 * 
	 * Example: NAME: Job1 | Job2 | Job3....
	 * 
	 * @param includeNeutral
	 *            Logical value telling the method whether to include the
	 *            "anyone" item into the collection (at the beginning)
	 * @return A String collection
	 */
	public static String[] getStaffList() {
		if (CustomWindow.isConnected(false)) {

			String[] staffData = ConnectionManager.sendStaffRequest().split(
					JobManager.SEPARATOR_JOBS);

			for (int i = 0; i < staffData.length; i++) {
				staffData[i] = StaffManager.createStaffString(staffData[i]);

			}

			return staffData;
		}

		return null;
	}

	public static String[] getStaffDetails()
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public static String[] getStaffJobs() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();

	}

	private static String createStaffString(String staffData) {
		staffData = staffData.replaceAll(JobManager.SEPARATOR_FIELDS, " | ");
		staffData = staffData.replaceFirst(Pattern.quote(" |"), ":");

		return staffData;
	}
}
