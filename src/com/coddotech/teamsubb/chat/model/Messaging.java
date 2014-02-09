package com.coddotech.teamsubb.chat.model;

import java.util.Observable;

import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;
import com.coddotech.teamsubb.timers.MessageTimer;

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

		this.refreshMessages();

		MessageTimer timer = new MessageTimer();
		timer.run();

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

	public void sendIRCMessage(final String message) {
		this.sendChatMessage(0, message);

	}

	public void sendPrivateMessage(final String message, final StaffMember receiver) {
		this.sendChatMessage(receiver.getId(), message);

	}

	public void refreshMessages() {
		class Refresher extends Thread {

			@Override
			public void run() {
				refreshIRCMessages();

				refreshPrivateMessages();
			}
		};

		Refresher refresher = new Refresher();
		refresher.run();

	}

	private void sendChatMessage(final int id, final String message) {
		class MessageSender extends Thread {

			@Override
			public void run() {
				boolean result = ConnectionManager.sendChatMessageRequest(id, message);

				notifyMessage(result);
			}
		}

		MessageSender sender = new MessageSender();
		sender.run();
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
