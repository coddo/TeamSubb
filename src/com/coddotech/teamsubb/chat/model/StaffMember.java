package com.coddotech.teamsubb.chat.model;

public class StaffMember extends User {
	
	private boolean online = false;

	public StaffMember() {
		this.jobsArrayStartIndex = 4;
	}
	
	@Override
	protected void createUserInfo(String[] data) {
		this.id = Integer.parseInt(data[0]);

		this.name = data[1];
		this.email = data[2];
		this.rank = User.DEFAULT_USER_RANKS[Integer.parseInt(data[3])];
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

}
