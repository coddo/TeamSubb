package com.coddotech.teamsubb.timers;

import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.main.SoundPlayer;

public class JobSoundTimer extends Thread {

	private static final int soundInterval = 300000;

	@Override
	public void run() {
		JobManager jobs = JobManager.getInstance();

		while (!jobs.isDisposed()) {

			try {

				if (!jobs.hasAcceptedJobs())
					if (jobs.hasAcceptableJobs())
						SoundPlayer.playJobSound();

				threadPause();
			}

			catch (Exception ex) {

			}
		}

	}

	private void threadPause() throws InterruptedException {
		Thread.sleep(soundInterval);
	}

}
