package com.coddotech.teamsubb.connection;

public class ConnectionManager {
	
	private String loginResult; //contains user data
	
	public String getLoginResult() {
		return loginResult;
	}
	
	public void setLoginResult(String loginResult) {
		this.loginResult = loginResult;
	}
	
	public static boolean sendLoginRequest(String user, String pass) {
		return false;
	}
	
	public static boolean sendLogoutRequest(String user) {
		return true;
	}

}
