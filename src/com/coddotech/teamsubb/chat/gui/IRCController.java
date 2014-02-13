package com.coddotech.teamsubb.chat.gui;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.chat.model.Message;
import com.coddotech.teamsubb.chat.model.Messaging;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;

public class IRCController {

	private Messaging messenger;

	private IRCWindow view;

	public IRCController(IRCWindow view) {
		this.view = view;

		messenger = Messaging.getInstance();
		messenger.addObserver(view);

	}

	public void dispose() {
		messenger.deleteObserver(view);
	}

	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.dispose();

		}
	};

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
