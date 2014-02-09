package com.coddotech.teamsubb.timers;

import com.coddotech.teamsubb.chat.model.StaffManager;

public class StaffRefreshTimer extends Thread {

	// 1 sec = 1000 ms => 15 sec = 15000 ms
	private static final int refreshInterval = 15000;

	private StaffManager staff;

	public StaffRefreshTimer(StaffManager staff) {
		this.staff = staff;

	}

	@Override
	public void run() {
		while (!staff.isDisposed()) {
			threadPause();

			staff.refreshOnlineStaffList();
		}
	}

	private void threadPause() {
		try {

			Thread.sleep(refreshInterval);

		}

		catch (InterruptedException e) {

		}
	}
}
