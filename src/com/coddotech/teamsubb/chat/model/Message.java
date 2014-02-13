package com.coddotech.teamsubb.chat.model;

import com.coddotech.teamsubb.jobs.model.JobManager;

public class Message {

	public StaffMember staff;

	public String date;
	public String message;

	public StaffMember dest = null;

	public Message(String rawData, StaffManager manager) {
		String[] data = rawData.split(JobManager.SEPARATOR_DATA);

		staff = manager.getUserByID(data[0]);

		date = data[1];
		message = data[2];

		if (data.length == 4)
			dest = manager.getUserByID(data[3]);
	}

	public boolean isPrivate() {
		return dest != null;
	}
}
