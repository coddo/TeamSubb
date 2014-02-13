package com.coddotech.teamsubb.timers;

import com.coddotech.teamsubb.chat.model.StaffManager;
import com.coddotech.teamsubb.main.CustomWindow;

public class StaffRefreshTimer extends Thread {

	// 1 sec = 1000 ms => 15 sec = 15000 ms
	private static final int refreshInterval = 10000;

	private StaffManager staff;

	public StaffRefreshTimer(StaffManager staff) {
		this.staff = staff;

	}

	@Override
	public void run() {
		while (!staff.isDisposed()) {
			if (!CustomWindow.isConnected(false))
				return;

			staff.refreshOnlineStaffList();

			threadPause();
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
