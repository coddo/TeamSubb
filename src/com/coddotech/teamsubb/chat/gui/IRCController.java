package com.coddotech.teamsubb.chat.gui;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.chat.model.Message;
import com.coddotech.teamsubb.chat.model.Messaging;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;

/**
 * Controller for the IRCWindow GUI class
 * 
 * @author coddo
 * 
 */
public class IRCController {

	private Messaging messenger;

	private IRCWindow view;

	/**
	 * Constructor
	 * 
	 * @param view
	 *            The IRCWindow class that uses this controller
	 */
	public IRCController(IRCWindow view) {
		this.view = view;

		messenger = Messaging.getInstance();
		messenger.addObserver(view);

	}

	/**
	 * Dispose all the components used by this class
	 */
	public void dispose() {
		messenger.deleteObserver(view);
	}

	/**
	 * Listener for when the view class is closing
	 */
	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.dispose();

		}
	};

	/**
	 * Listener for when the shell is displayed. At the view startup, all the public and private
	 * messages that have not been seen by the user are displayed
	 */
	public Listener shellShownListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			String buffer = Messaging.getInstance().flushBuffer();

			try {
				if (buffer != null)
					view.chat.openIRCMessages(Message.createMessageArray(buffer, view.manager));

				NotificationEntity privateBuffer = view.flushPrivateBuffer();

				if (privateBuffer != null)
					view.chat.openPrivateMessages(Message.createMessageArray(privateBuffer.getString(), view.manager));
			}

			catch (Exception ex) {

			}

		}
	};

}
