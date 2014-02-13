package com.coddotech.teamsubb.chat.model;

import com.coddotech.teamsubb.jobs.model.JobManager;

public class Message {

	public StaffMember staff;

	public String date;
	public String message;

	public StaffMember dest = null;

	public Message(String rawData, StaffManager manager) throws Exception {
		String[] data = rawData.split(JobManager.SEPARATOR_DATA);

		if (data.length < 3)
			throw new Exception();

		staff = manager.getUserByID(data[0]);

		date = data[1];
		message = data[2];

		if (data.length == 4)
			dest = manager.getUserByID(data[3]);
	}

	public boolean isPrivate() {
		return dest != null;
	}

	public static Message[] createMessageArray(String data, StaffManager manager) throws Exception {
		String[] messages = data.split(JobManager.SEPARATOR_ENTITY);

		Message[] msg = new Message[messages.length];

		for (int i = 0; i < msg.length; i++)
			msg[i] = new Message(messages[i], manager);

		return msg;
	}
}
