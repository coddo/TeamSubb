package com.coddotech.teamsubb.chat.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.main.CustomWindow;

/**
 * Class for managing the lists with the staff (and their details) employed by
 * the fansub.
 * 
 * @author Coddo
 */
public class StaffManager extends Observable {

	private static final int STAFF_REFRESH_INTERVAL = 15000; // 1 sec = 1000 ms => 15 sec = 15000 ms
	
	private StaffMember[] staff = null;
	
	private boolean disposed = false;
	
	public void dispose() {
		this.disposed = true;
		
		staff = null;
	}

	/**
	 * Get the list of staff members
	 * 
	 * @return A StaffMember collection
	 */
	public StaffMember[] getStaff() {
		return this.staff;

	}

	/**
	 * Get the list of staff that are currently online
	 * 
	 * @return A StaffMember collection
	 */
	public StaffMember[] getOnlineStaff() {
		List<StaffMember> list = new ArrayList<StaffMember>();

		for (StaffMember member : staff) {

			if (member.getOnlineStatus())
				list.add(member);

		}

		return list.toArray(new StaffMember[list.size()]);
	}

	/**
	 * Get the list of staff that are currently offline
	 * 
	 * @return A StaffMember collection
	 */
	public StaffMember[] getOfflineStaff() {
		List<StaffMember> list = new ArrayList<StaffMember>();

		for (StaffMember member : staff) {

			if (!member.getOnlineStatus())
				list.add(member);

		}

		return list.toArray(new StaffMember[list.size()]);
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
	
//	private Runnable refreshTimer = new Runnable() {
//		
//		@Override
//		public void run() {
//			if (!disposed) {
//				refreshStaffList();
//				
//				Display.getDefault().timerExec(STAFF_REFRESH_INTERVAL, this);
//			
//		}
//	};

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
