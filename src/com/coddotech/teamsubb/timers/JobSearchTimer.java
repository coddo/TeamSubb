package com.coddotech.teamsubb.timers;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;
import com.coddotech.teamsubb.settings.model.Settings;

public class JobSearchTimer implements Observer {

	// 1 min = 60000 ms
	private int searchInterval = Settings.DEFAULT_SEARCH_INTERVAL * 60000;
	private boolean disposed = false;

	private static JobSearchTimer instance = null;

	private JobSearchTimer() {

	}

	public static JobSearchTimer getInstance() {
		if (instance == null)
			instance = new JobSearchTimer();

		return instance;
	}

	public void dispose() {
		disposed = true;

		try {
			Settings.getInstance().deleteObserver(instance);

			ActivityLogger.logActivity(this.getClass().getName(), "Dispose");
		}

		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Dispose", ex);

		}
	}

	/**
	 * Chek if the controller has been disposed or not
	 * 
	 * @return A logical value
	 */
	public boolean isDisposed() {
		return this.disposed;
	}

	@Override
	public void update(Observable obs, Object obj) {
		NotificationEntity notif = (NotificationEntity) obj;

		if (notif.getMessage().equals(Settings.SEARCH_INTERVAL))
			this.searchInterval = notif.getInteger() * 60000;

	}

	/**
	 * Start the timer in order for it to search for new jobs
	 */
	public void startTimer() {
		Display.getDefault().timerExec(100, timer);
	}

	/**
	 * Timer used to search for new jobs. This method checks if there is an internet connection
	 * available before sending the request
	 */
	private Runnable timer = new Runnable() {

		@Override
		public void run() {
			if (!disposed) {

				if (CustomWindow.isConnected(false))
					JobManager.getInstance().findJobs();

				Display.getDefault().timerExec(searchInterval, this);
			}
		}

	};

}
