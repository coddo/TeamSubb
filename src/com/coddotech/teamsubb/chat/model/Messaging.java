package com.coddotech.teamsubb.chat.model;

import java.util.Observable;

import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;
import com.coddotech.teamsubb.timers.MessageTimer;

public class Messaging extends Observable {

	public static final String MESSAGE = "msg";
	public static final String IRC = "irc";
	public static final String PRIVATE = "prv";
	public static final String OPEN_PRIVATE_CHAT = "op_prv_chat";

	private String buffer = "null";

	private static Messaging instance = null;

	private boolean disposed = false;

	private Messaging() {
		MessageTimer timer = new MessageTimer();
		timer.start();

	}

	public void dispose() {
		this.disposed = true;

	}

	public boolean isDisposed() {
		return this.disposed;

	}

	public String flushBuffer() {
		String aux = this.buffer;

		this.buffer = null;

		return aux;
	}

	public static Messaging getInstance() {
		if (instance == null)
			instance = new Messaging();

		return instance;
	}

	public void openPrivateChat(StaffMember user) {
		this.setChanged();

		NotificationEntity notif = new NotificationEntity(OPEN_PRIVATE_CHAT, user);

		notifyObservers(notif);
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

	public void sendChatMessage(final StaffMember staff, final String message) {
		class MessageSender extends Thread {

			@Override
			public void run() {
				int id = (staff == null) ? 0 : staff.getId();

				boolean result = ConnectionManager.sendChatMessageRequest(id, message);

				notifyMessage(result);
			}
		}

		MessageSender sender = new MessageSender();
		sender.run();
	}

	private void refreshIRCMessages() {
		String message = ConnectionManager.sendChatDetailsRequest(Messaging.IRC);

		buffer = message;

		NotificationEntity notif = new NotificationEntity(Messaging.IRC, message);

		notifyMessageStatus(notif);
	}

	private void refreshPrivateMessages() {
		String message = ConnectionManager.sendChatDetailsRequest(Messaging.PRIVATE);

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
