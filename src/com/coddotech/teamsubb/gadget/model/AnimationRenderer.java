package com.coddotech.teamsubb.gadget.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;
import com.coddotech.teamsubb.timers.AnimationTimer;

/**
 * Class which has the purpose to guide the gadget's animation. It also observes
 * the changes in the JobManager entity in order to determine which type of
 * animation and alerts to use
 * 
 * There are 3 types of animation:<br>
 * <br>
 * 
 * -> IDLE: animation which looks like a radar scanning something => this is used for when the app
 * doesn't have anything important to report to the user. <br>
 * <br>
 * 
 * -> LOW_PRIORITY: animation used to grab the attention of the user because there are certain jobs
 * available for him.<br>
 * <br>
 * 
 * -> HIGH_PRIORITY: this is a more aggressive animation used to alert the user that there are some
 * jobs which are intended strictly for him. These types of jobs are considered to be urgent ones.
 * 
 * @author Coddo
 * 
 */
public class AnimationRenderer extends Observable implements Observer {

	// used for determining the type of animation that the gadget will perform
	public static final int TYPE_IDLE = 0x001;
	public static final int TYPE_LOW_PRIORITY = 0x002;
	public static final int TYPE_HIGH_PRIORITY = 0x003;

	// paths where the files necessary for the gadget's animation are stored
	private static final String RESOURCE_DIR = System.getProperty("user.dir") + File.separator + "resources";

	private static final String DIR_IDLE = RESOURCE_DIR + File.separator + "idle";
	private static final String DIR_LOW = RESOURCE_DIR + File.separator + "low";
	private static final String DIR_HIGH = RESOURCE_DIR + File.separator + "high";

	private boolean disposed = false;
	private boolean paused = false;

	// the image collections with the help of which the animation is done
	private Image[] idle;
	private Image[] lowPriority;
	private Image[] highPriority;

	private int type;
	private int counter = 0;

	private AnimationTimer timer;


	/**
	 * Class constructor
	 */
	public AnimationRenderer() {

	}

	/**
	 * Clears the memory from this class and its resources
	 */
	public void dispose() {
		try {
			disposed = true;

			timer.interrupt();
			disposeAnimationData();

			idle = null;
			lowPriority = null;
			highPriority = null;

			ActivityLogger.logActivity(this.getClass().getName(), "Dispose");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Dispose", ex);

		}
	}
	
	public boolean isDisposed() {
		return this.disposed;
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	public void disposeAnimationData() {
		if (idle == null)
			return;

		for (int i = 0; i < idle.length; i++)
			idle[i].dispose();

		for (int i = 0; i < lowPriority.length; i++)
			lowPriority[i].dispose();

		for (int i = 0; i < highPriority.length; i++)
			highPriority[i].dispose();

	}

	/**
	 * Set the type of animation that should be done by the gadget. These types are stored in the
	 * static fields TYPE_XXXX of this class
	 * 
	 * @param type
	 *            The animation type that should be set
	 */
	public void setAnimationType(int type) {
		this.type = type;
		counter = 0;
	}

	/**
	 * Start animating the gadget
	 */
	public void startAnimation() {
		timer = new AnimationTimer(this);

		try {
			timer.start();
			
		}
		
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "ANIMATION TIMER", ex);;
		}
	}

	public void pauseAnimation() {
		paused = true;
	}

	public void resumeAnimation() {
		paused = false;
	}

	public void sendAnimationData() {
		// mark this model as being changed
		this.setChanged();
		
		switch (type) {

			case AnimationRenderer.TYPE_IDLE: {
				if (counter == idle.length)
					counter = 0;

				notifyObservers(idle[counter]);
			}
				break;

			case AnimationRenderer.TYPE_LOW_PRIORITY: {
				if (counter == lowPriority.length)
					counter = 0;

				notifyObservers(lowPriority[counter]);
			}
				break;

			case AnimationRenderer.TYPE_HIGH_PRIORITY: {
				if (counter == highPriority.length)
					counter = 0;

				notifyObservers(highPriority[counter]);
			}
				break;
		}
		
		counter++;
	}

	@Override
	public void update(Observable obs, Object obj) {
		NotificationEntity notif = (NotificationEntity) obj;

		// update is done only by the job manager in order to know what type of t needs to
		// be done
		if (notif.getMessage().equals(JobManager.JOB_FIND)) {

			String[] fragments = notif.getString().split(JobManager.SEPARATOR_DATA);

			switch (fragments[fragments.length - 1]) {

				case JobManager.JOB_PRIORITY_NORMAL: {
					this.setAnimationType(TYPE_IDLE);
				}
					break;

				case JobManager.JOB_PRIORITY_ACCEPTABLE: {
					this.setAnimationType(TYPE_LOW_PRIORITY);
				}
					break;

				case JobManager.JOB_PRIORITY_IMPORTANT: {
					this.setAnimationType(AnimationRenderer.TYPE_HIGH_PRIORITY);
				}
					break;

			}
		}
	}

	/**
	 * Reize an image to the given dimensions
	 * 
	 * @param image
	 *            The image to be resized
	 * 
	 * @param width
	 *            The width to be set
	 * 
	 * @param height
	 *            The height to be set
	 * 
	 * @return An Image entity representing the resized image
	 */
	public static Image resizeImage(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);

		GC gc = new GC(scaled);

		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);

		gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, width, height);

		gc.dispose();
		image.dispose();

		return scaled;
	}

	/**
	 * Gets and stores the animation files and data into the local memory of the
	 * app for faster use
	 */
	public void generateAnimationData() {
		try {

			File idleFolder = new File(AnimationRenderer.DIR_IDLE);
			File lowFolder = new File(AnimationRenderer.DIR_LOW);
			File highFolder = new File(AnimationRenderer.DIR_HIGH);

			FilenameFilter filter = new FilenameFilter() {

				@Override
				public boolean accept(File path, String file) {

					return file.split(Pattern.quote("."))[1].equals("png");
				}
			};

			idle = new Image[idleFolder.list(filter).length];
			lowPriority = new Image[lowFolder.list(filter).length];
			highPriority = new Image[highFolder.list(filter).length];

			int imgsize = GadgetProfiler.getInstance().getSizeFactor();

			// idle image sequence
			int k = 0;
			for (String img : idleFolder.list(filter)) {

				idle[k] = resizeImage(
						new Image(Display.getDefault(), AnimationRenderer.DIR_IDLE + File.separator + img), imgsize,
						imgsize);

				k++;
			}

			// low priority image sequence
			k = 0;
			for (String img : lowFolder.list(filter)) {

				lowPriority[k] = resizeImage(new Image(Display.getDefault(), AnimationRenderer.DIR_LOW + File.separator
						+ img), imgsize, imgsize);
				k++;
			}

			// high priority image sequence
			k = 0;
			for (String img : highFolder.list(filter)) {

				highPriority[k] = resizeImage(new Image(Display.getDefault(), AnimationRenderer.DIR_HIGH
						+ File.separator + img), imgsize, imgsize);
				k++;
			}

			idleFolder = null;
			lowFolder = null;
			highFolder = null;

			ActivityLogger.logActivity(this.getClass().getName(), "Initialize animation data");

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Initialize animation data", ex);

		}
	}

}
