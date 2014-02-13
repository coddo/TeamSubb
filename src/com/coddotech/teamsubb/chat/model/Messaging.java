package com.coddotech.teamsubb.chat.model;

import java.util.Observable;

import com.coddotech.teamsubb.connection.model.ConnectionManager;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;
import com.coddotech.teamsubb.timers.MessageTimer;

/**
 * Class for relaying, parsing, fetching and posting chat messages between staff members.<br>
 * 
 * This class is a singleton
 * 
 * @author coddo
 * 
 */
public class Messaging extends Observable {

	public static final String MESSAGE = "msg";
	public static final String IRC = "irc";
	public static final String PRIVATE = "prv";
	public static final String OPEN_PRIVATE_CHAT = "op_prv_chat";

	private String buffer = null;

	private static Messaging instance = null;

	private boolean disposed = false;

	/**
	 * Constructor
	 */
	private Messaging() {
		MessageTimer timer = new MessageTimer();
		timer.start();

	}

	/**
	 * Dispose all the components used by this class
	 */
	public void dispose() {
		this.disposed = true;

	}

	/**
	 * Verify if this class has been disposed or not
	 * 
	 * @return A Logical value
	 */
	public boolean isDisposed() {
		return this.disposed;

	}

	/**
	 * Retrieve the public message buffer and nullify it afterwards
	 * 
	 * @return A String value
	 */
	public String flushBuffer() {
		String aux = this.buffer;

		this.buffer = null;

		return aux;
	}

	/**
	 * Get the instance of this class
	 * 
	 * @return A Messaging instance
	 */
	public static Messaging getInstance() {
		if (instance == null)
			instance = new Messaging();

		return instance;
	}

	/**
	 * Notify observers about a new private chat
	 * 
	 * @param user
	 *            The StaffMember instance which represents the new chat
	 */
	public void openPrivateChat(StaffMember user) {
		this.setChanged();

		NotificationEntity notif = new NotificationEntity(OPEN_PRIVATE_CHAT, user);

		notifyObservers(notif);
	}

	/**
	 * Send a chat message
	 * 
	 * @param staff
	 *            The user that will receive the message
	 * @param message
	 *            The message to be sent
	 */
	public void sendChatMessage(final StaffMember staff, final String message) {
		int id = (staff == null) ? 0 : staff.getId();

		boolean result = ConnectionManager.sendChatMessageRequest(id, message);

		notifyMessage(result);
	}

	/**
	 * Retrieve new messages from the server
	 */
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

	/**
	 * Retrieve new public messages from the server
	 */
	private void refreshIRCMessages() {
		String message = ConnectionManager.sendChatDetailsRequest(Messaging.IRC);

		if (buffer == null && !message.isEmpty())
			buffer = message;

		NotificationEntity notif = new NotificationEntity(Messaging.IRC, message);

		notifyMessageStatus(notif);
	}

	/**
	 * Retrieve new private messages from the server
	 */
	private void refreshPrivateMessages() {
		String message = ConnectionManager.sendChatDetailsRequest(Messaging.PRIVATE);

		NotificationEntity notif = new NotificationEntity(Messaging.PRIVATE, message);

		notifyMessageStatus(notif);
	}

	/**
	 * Notify the observers when a message could not be sent
	 * 
	 * @param sent
	 */
	private void notifyMessage(boolean sent) {
		NotificationEntity notif = new NotificationEntity(Messaging.MESSAGE, sent);

		notifyMessageStatus(notif);
	}

	/**
	 * Notify the observers about new messages that have been received
	 * 
	 * @param notif
	 */
	private void notifyMessageStatus(NotificationEntity notif) {
		this.setChanged();
		this.notifyObservers(notif);
	}

}
