package com.coddotech.teamsubb.notifications.model;

import com.coddotech.teamsubb.jobs.model.Job;


public final class NotificationEntity {

	public static final String MESSAGE_JOB_FIND = "job find";
	public static final String MESSAGE_JOB_INFORMATION = "job information";
	public static final String MESSAGE_JOB_END = "job end";
	public static final String MESSAGE_JOB_ACCEPT = "job accept";
	public static final String MESSAGE_JOB_CANCEL = "job cancel";
	
	public String message;
	
	public Job job;
	
	public boolean actionSuccess;
	
}
