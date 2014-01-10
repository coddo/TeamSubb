package com.coddotech.teamsubb.main;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Class which has the purpose to guide the gadget's animation. It also observes
 * the changes in the JobManager entity in order to determine which type of
 * animation and alerts to use
 * 
 * There are 3 types of animation:<br>
 * -> IDLE: animation which looks like a radar scanning something => this is
 * used for when the app doesn't have anything important to report to the user. <br>
 * -> LOW_PRIORITY: animation used to grab the attention of the user because
 * there are certain jobs available for him.<br>
 * -> HIGH_PRIORITY: this is a more aggressive animation used to alert the user
 * that there are some jobs which are intended strictly for him. These types of
 * jobs are considered to be urgent ones.
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
	private static final String DIR_IDLE = System.getProperty("user.dir")
			+ File.separator + "resources" + File.separator + "idle";
	private static final String DIR_LOW = System.getProperty("user.dir")
			+ File.separator + "resources" + File.separator + "low";
	private static final String DIR_HIGH = System.getProperty("user.dir")
			+ File.separator + "resources" + File.separator + "high";

	// interval used to
	private int imageInterval = 300;

	// the image collections with the help of which the animation is done
	private Image[] idle;
	private Image[] lowPriority;
	private Image[] highPriority;

	private int type;
	private int counter = 0;
	private boolean disposed = false;

	/**
	 * Class constructor
	 */
	public AnimationRenderer() {
		initializeAnimationData();
	}

	/**
	 * Clears the memory from this class and its resources
	 */
	public void dispose() {
		this.disposed = true;

		for (int i = 0; i < idle.length; i++)
			idle[i].dispose();

		for (int i = 0; i < lowPriority.length; i++)
			lowPriority[i].dispose();

		for (int i = 0; i < highPriority.length; i++)
			highPriority[i].dispose();

		idle = null;
		lowPriority = null;
		highPriority = null;
	}

	/**
	 * Set the type of animation that should be done by the gadget. These types
	 * are stored in the static fields TYPE_XXXX of this class
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
	public void startAnimations() {
		Display.getCurrent().timerExec(this.imageInterval, animationTimer);
	}

	/**
	 * Timer used for changing the resources between them at set intervals in
	 * order to perform the animation for the gadget
	 */
	private Runnable animationTimer = new Runnable() {

		@Override
		public void run() {
			if (!disposed) {
				// mark this model as being changed
				setChanged();

				// send the animation data to the gadget based on the animation
				// type currently selected
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

				// recursive method calling in order to mimic a real timer
				Display.getCurrent().timerExec(imageInterval, this);
			}
		}
	};

	@Override
	public void update(Observable obs, Object obj) {
		// update is done only by the job manager in order to know what type of
		// animation needs to be done
		if (obj instanceof String) {

			switch (obj.toString().split(CustomWindow.NOTIFICATION_SEPARATOR)[1]) {
			case "normal": {
				this.setAnimationType(TYPE_IDLE);
			}
				break;
			case "acceptable": {
				this.setAnimationType(TYPE_LOW_PRIORITY);
			}
				break;
			case "important": {
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
	 * @param width
	 *            The width to be set
	 * @param height
	 *            The height to be set
	 * @return An Image entity representing the resized image
	 */
	private Image resizeImage(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, image.getBounds().width,
				image.getBounds().height, 0, 0, width, height);
		gc.dispose();
		image.dispose();
		return scaled;
	}

	/**
	 * Gets and stores the animation files and data into the local memory of the
	 * app for faster use
	 */
	private void initializeAnimationData() {
		File idleFolder = new File(AnimationRenderer.DIR_IDLE);
		File lowFolder = new File(AnimationRenderer.DIR_LOW);
		File highFolder = new File(AnimationRenderer.DIR_HIGH);

		FilenameFilter filter = new FilenameFilter() {

			@Override
			public boolean accept(File path, String file) {

				if (file.split(Pattern.quote("."))[1].equals("png"))
					return true;
				else
					return false;
			}
		};

		idle = new Image[idleFolder.list(filter).length];
		lowPriority = new Image[lowFolder.list(filter).length];
		highPriority = new Image[highFolder.list(filter).length];

		int size = 88;
		
		// idle image sequence
		int k = 0;
		for (String img : idleFolder.list(filter)) {

			idle[k] = resizeImage(new Image(Display.getCurrent(),
					AnimationRenderer.DIR_IDLE + File.separator + img), size,
					size);

			k++;
		}

		// low priority image sequence
		k = 0;
		for (String img : lowFolder.list(filter)) {

			lowPriority[k] = resizeImage(new Image(Display.getCurrent(),
					AnimationRenderer.DIR_LOW + File.separator + img), size, size);
			k++;
		}

		// high priority image sequence
		k = 0;
		for (String img : highFolder.list(filter)) {

			highPriority[k] = resizeImage(new Image(Display.getCurrent(),
					AnimationRenderer.DIR_HIGH + File.separator + img), size,
					size);
			k++;
		}

		idleFolder = null;
		lowFolder = null;
		highFolder = null;
	}

}
