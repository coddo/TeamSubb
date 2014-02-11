package com.coddotech.teamsubb.chat.gui;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.chat.model.Messaging;

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

			if (!buffer.equals("null"))
				view.chat.openIRCMessages(view.createMessageArray(buffer));

		}
	};

}
