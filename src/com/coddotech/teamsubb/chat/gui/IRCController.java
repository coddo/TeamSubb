package com.coddotech.teamsubb.chat.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class IRCController {

	IRCWindow view;

	public IRCController(IRCWindow view) {
		this.view = view;
	}

	public void dispose() {

	}

	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.dispose();

		}
	};

	public Listener keyPressed = new Listener() {

		@Override
		public void handleEvent(Event e) {

			if (e.detail == SWT.TRAVERSE_RETURN) {

			}

		}
	};
}
