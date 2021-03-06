package com.coddotech.teamsubb.chat.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;
import com.coddotech.teamsubb.timers.StaffRefreshTimer;

/**
 * Class for managing the lists with the staff (and their details) employed by
 * the fansub.
 * 
 * @author Coddo
 */
public class StaffManager extends Observable {

	public static final String ONLINE_STAFF_LIST = "online";

	private static final String STAFF_LIST = "staff";

	private StaffMember[] staff = null;

	private boolean disposed = false;

	public StaffManager() {
		StaffRefreshTimer timer = new StaffRefreshTimer(this);
		timer.start();
	}

	public void dispose() {
		this.disposed = true;

		staff = null;
	}

	public boolean isDisposed() {
		return this.disposed;
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
	 * Refresh the list containing the staff details (fethes the data from the server)
	 */
	public void refreshStaffListAsync() {

		class StaffRefresher extends Thread {

			@Override
			public void run() {
				refreshStaffList();
			}
		}

		StaffRefresher refresher = new StaffRefresher();
		refresher.run();
	}

	public void refreshStaffList() {
		// create the list of staff
		StaffMember[] staff = StaffManager.createStaffArray();

		if (staff != null) {
			this.staff = staff;

			// get the list of online staff
			refreshOnlineStaffList();
		}
	}

	/**
	 * Refresh the staff list in order to have an updated list with who is online and who is not
	 */
	public void refreshOnlineStaffList() {
		// set only the appropriate staff members as online
		String response = ConnectionManager.sendChatDetailsRequest(ONLINE_STAFF_LIST);

		if (response == null)
			return;

		String[] ids = response.split(JobManager.SEPARATOR_DATA);

		for (String id : ids) {
			StaffMember member = getUserByID(id);

			// if an ID is not in the list, then the staff list needs to be refreshed
			if (member == null) {
				refreshStaffListAsync();

				break;
			}

			else
				member.setOnline(true);
		}

		// notify the observers about the new staff list
		notifyStaffList();

	}

	/**
	 * Find a staff member
	 * 
	 * @param id
	 *            The ID for which to search
	 * @return A StaffMember instance
	 */
	public StaffMember getUserByID(String idString) {
		if (staff == null)
			return null;

		int id = -1;

		if (idString.equals(""))
			return null;

		else
			id = Integer.parseInt(idString);

		for (StaffMember member : staff) {

			if (member.getId() == id)
				return member;

		}

		return null;
	}

	/**
	 * Get the list of staff that are currently online
	 * 
	 * @return A StaffMember collection
	 */
	private StaffMember[] getOnlineStaff() {
		List<StaffMember> list = new ArrayList<StaffMember>();

		for (StaffMember member : staff) {

			if (member.isOnline())
				list.add(member);

		}

		return list.toArray(new StaffMember[list.size()]);
	}

	/**
	 * Get the list of staff that are currently offline
	 * 
	 * @return A StaffMember collection
	 */
	private StaffMember[] getOfflineStaff() {
		List<StaffMember> list = new ArrayList<StaffMember>();

		for (StaffMember member : staff) {

			if (!member.isOnline())
				list.add(member);

		}

		return list.toArray(new StaffMember[list.size()]);
	}

	/**
	 * Notify observers about the staff list when updated
	 */
	private void notifyStaffList() {
		this.setChanged();

		// Matrix that contains the 2 rows: online staff members and offline staff members
		StaffMember[][] lists = new StaffMember[2][];

		lists[0] = this.getOnlineStaff();
		lists[1] = this.getOfflineStaff();

		NotificationEntity notif = new NotificationEntity(StaffManager.STAFF_LIST, lists);

		notifyObservers(notif);
	}

	/**
	 * Fetch the staff list from the server.<br>
	 * The list contains both the name of the staff member, and his/her attributions. <br>
	 * <br>
	 * 
	 * Example: NAME: Job1 | Job2 | Job3....
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

				staffStrings[i] = user.getName() + ": ";

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

		String response = ConnectionManager.sendStaffRequest();

		if (response == null)
			return null;

		String[] staffData = response.split(JobManager.SEPARATOR_ENTITY);

		if (staffData == null)
			return null;

		staffArray = new StaffMember[staffData.length];

		for (int i = 0; i < staffArray.length; i++) {
			staffArray[i] = new StaffMember();

			staffArray[i].setUserDetails(staffData[i]);
		}

		return staffArray;
	}

}
