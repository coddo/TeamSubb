package com.coddotech.teamsubb.main;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;

public class CustomController {

	public CustomController() {
		ActivityLogger.logActivity(this.getClass().getName(), "Controller initialization", "START");
	}

	protected void logDispose() {
		ActivityLogger.logActivity(this.getClass().getName(), "Controller dispose");

	}

	protected void logDiposeFail(Exception ex) {
		ActivityLogger.logException(this.getClass().getName(), "Controller dispose", ex);
	}
}
