package com.coddotech.teamsubb.chat.gui;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.chat.model.Messaging;
import com.coddotech.teamsubb.chat.model.StaffManager;

public class IRCController {

	private Messaging messenger;
	private StaffManager staff;

	private IRCWindow view;

	public IRCController(IRCWindow view) {
		this.view = view;

		messenger = Messaging.getInstance();
		messenger.addObserver(view);

		staff = new StaffManager();
		staff.addObserver(view);
	}

	public void dispose() {
		messenger.deleteObserver(view);
		staff.deleteObserver(view);

		staff.dispose();
	}

	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.dispose();

		}
	};

}
