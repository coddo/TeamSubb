package com.coddotech.teamsubb.chat.model;

public class StaffMember extends User {

	public StaffMember() {
		this.jobsArrayStartIndex = 2;
	}

	@Override
	protected void createUserInfo(String[] data) {
		this.id = Integer.parseInt(data[0]);
		
		this.name = data[1];
	}
	
}
