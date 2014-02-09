package com.coddotech.teamsubb.chat.model;

public class StaffMember extends User {

	private boolean online = false;

	public StaffMember() {
		this.setJobsArrayStartIndex(4);
	}

	@Override
	protected void createUserInfo(String[] data) {
		this.setId(Integer.parseInt(data[0]));

		this.setName(data[1]);
		this.setEmail(data[2]);
		this.setRank(User.DEFAULT_USER_RANKS[Integer.parseInt(data[3])]);
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

}
