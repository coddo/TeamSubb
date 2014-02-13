package com.coddotech.teamsubb.chat.model;

import com.coddotech.teamsubb.jobs.model.JobManager;

/**
 * Class used to transport messages between staff members
 * 
 * @author coddo
 * 
 */
public class Message {

	public StaffMember staff;

	public String date;
	public String message;

	public StaffMember dest = null;

	/**
	 * Main Constructor
	 */
	public Message() {

	}

	/**
	 * Constructor
	 * 
	 * @param rawData
	 *            The string value containing the ID, date, message and destination_id
	 * @param manager
	 *            The staff manager that can parse data for staff members
	 * @throws Exception
	 */
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

	/**
	 * Parse a String data into an array of Message instances
	 * 
	 * @param data
	 *            The raw string data containing the message details
	 * @param manager
	 *            The StaffManager instance used to parse data
	 * @return A Message collection
	 * @throws Exception
	 */
	public static Message[] createMessageArray(String data, StaffManager manager) throws Exception {
		String[] messages = data.split(JobManager.SEPARATOR_ENTITY);

		Message[] msg = new Message[messages.length];

		for (int i = 0; i < msg.length; i++)
			msg[i] = new Message(messages[i], manager);

		return msg;
	}
}
