package com.coddotech.teamsubb.notifications.model;

import com.coddotech.teamsubb.chat.model.StaffMember;
import com.coddotech.teamsubb.jobs.model.Job;

public final class NotificationEntity {

	private String message;

	private Object argument;

	private Object[] extraArguments;

	public NotificationEntity(String message, Object argument) {
		this.message = message;
		this.argument = argument;
	}

	public NotificationEntity(String message, Object argument, Object[] extraArguments) {
		this(message, argument);

		this.extraArguments = extraArguments;
	}

	public String getString() {
		return argument.toString();
	}

	public String getMessage() {
		return this.message;
	}

	public Boolean getBoolean() {
		return Boolean.parseBoolean(argument.toString());
	}
	
	public int getInteger() {
		return Integer.parseInt(argument.toString());
	}

	public Job getJob() {
		return (Job) argument;
	}

	public Job[] getJobList() {
		return (Job[]) argument;
	}
	
	public StaffMember[][] getStaff() {
		return (StaffMember[][]) argument;
	}

	public Object[] getExtraArguments() {
		return extraArguments;
	}

}
