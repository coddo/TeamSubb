package com.coddotech.teamsubb.timers;

import com.coddotech.teamsubb.chat.model.Messaging;

public class MessageTimer extends Thread {

	private static final int refreshInterval = 1000;

	@Override
	public void run() {
		while (!Messaging.getInstance().isDisposed()) {
			Messaging.getInstance().refreshMessages();

			threadPause();
			
		}

	}

	private void threadPause() {
		try {
			Thread.sleep(refreshInterval);

		}

		catch (InterruptedException e) {

		}
	}
}
