package com.coddotech.teamsubb.chat.model;

import java.util.Observable;

import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;

public class Messaging extends Observable {

	public static final String MESSAGE = "msg";
	public static final String IRC = "irc";
	public static final String PRIVATE = "prv";

	private StaffManager staff = null;

	private static Messaging instance = null;

	private boolean disposed = false;

	private Messaging() {
		staff = new StaffManager();
		staff.refreshStaffList();

	}

	public void dispose() {
		this.disposed = true;

		staff.dispose();
	}

	public boolean isDisposed() {
		return this.disposed;

	}

	public static Messaging getInstance() {
		if (instance == null)
			instance = new Messaging();

		return instance;
	}

	public void sendIRCMessage(String message) {
		boolean result = ConnectionManager.sendChatMessageRequest(0, message);

		this.notifyMessage(result);
	}

	public void sendPrivateMessage(String message, StaffMember receiver) {
		boolean result = ConnectionManager.sendChatMessageRequest(receiver.getId(), message);

		this.notifyMessage(result);
	}

	public void refreshMessages() {
		this.refreshIRCMessages();

		this.refreshPrivateMessages();
	}

	private void refreshIRCMessages() {
		String message = ConnectionManager.sendChatDetailsRequest(IRC);

		NotificationEntity notif = new NotificationEntity(Messaging.IRC, message);

		notifyMessageStatus(notif);
	}

	private void refreshPrivateMessages() {
		String message = ConnectionManager.sendChatDetailsRequest(PRIVATE);

		NotificationEntity notif = new NotificationEntity(Messaging.PRIVATE, message);

		notifyMessageStatus(notif);
	}

	private void notifyMessage(boolean sent) {
		NotificationEntity notif = new NotificationEntity(Messaging.MESSAGE, sent);

		notifyMessageStatus(notif);
	}

	private void notifyMessageStatus(NotificationEntity notif) {
		this.setChanged();
		this.notifyObservers(notif);
	}

}
