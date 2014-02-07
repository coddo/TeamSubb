package com.coddotech.teamsubb.chat.model;

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

	private StaffMember[] staff = null;

	public StaffMember[] getStaff() {
		return this.staff;

	}

	/**
	 * Refresh the list containing the staff details (fethes the data from the server)
	 */
	public void refreshStaffList() {

		class StaffRefresher extends Thread {

			@Override
			public void run() {
				staff = StaffManager.createStaffArray();

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
	public static String[] fetchFormatedStaffList() {
		StaffMember[] staffArray = StaffManager.createStaffArray();

		if (staffArray == null)
			return null;

		String[] staffStrings = new String[staffArray.length];

		for (int i = 0; i < staffArray.length; i++) {

			try {
				StaffMember user = staffArray[i];

				String[] jobs = user.getJobNames();

				staffStrings[i] = user.name + ": ";

				for (int j = 0; j < jobs.length - 1; j++)
					staffStrings[i] += jobs[j] + " | ";

				staffStrings[i] += jobs[jobs.length - 1];
			}

			finally {
				// do nothing on error
			}

		}

		return staffStrings;
	}

	/**
	 * Gets the staff from the server and creates an array with the staff members
	 * 
	 * @return A StaffMember colletion
	 */
	private static StaffMember[] createStaffArray() {
		StaffMember[] staffArray = null;

		if (CustomWindow.isConnected(false)) {
			String[] staffData = ConnectionManager.sendStaffRequest().split(JobManager.SEPARATOR_ENTITY);

			if (staffData == null)
				return null;

			staffArray = new StaffMember[staffData.length];

			for (int i = 0; i < staffArray.length; i++) {
				staffArray[i] = new StaffMember();

				staffArray[i].setUserDetails(staffData[i]);
			}
		}

		return staffArray;
	}

}
