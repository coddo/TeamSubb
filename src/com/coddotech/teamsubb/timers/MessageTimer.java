package com.coddotech.teamsubb.timers;

import com.coddotech.teamsubb.chat.model.Messaging;

public class MessageTimer extends Thread {

	private static final int refreshInterval = 500;

	@Override
	public void run() {
		while (!Messaging.getInstance().isDisposed()) {
			try {
				threadPause();

				Messaging.getInstance().refreshMessages();
			}

			catch (Exception ex) {

			}
		}

	}

	private void threadPause() throws InterruptedException {
		Thread.sleep(refreshInterval);
	}
}
