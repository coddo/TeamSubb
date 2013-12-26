package com.coddotech.teamsubb.main;

import java.io.File;
import java.util.Observable;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class AnimationRenderer extends Observable {

	public static final int TYPE_IDLE = 0x001;
	public static final int TYPE_LOW_PRIORITY = 0x002;
	public static final int TYPE_HIGH_PRIORITY = 0x003;

	private static final String DIR_IDLE = "resources" + File.separator
			+ "idle";
	private static final String DIR_LOW = "resources" + File.separator + "low";
	private static final String DIR_HIGH = "resources" + File.separator
			+ "high";

	private int imageInterval = 100;
	private Image[] idle = new Image[23];
	private Image[] lowPriority = new Image[2];
	private Image[] highPriority = new Image[2];

	private int type;
	private int counter = 0;
	private boolean disposed = false;

	public AnimationRenderer() {
		initializeAnimationData();
	}

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

	public void setAnimationType(int type) {
		this.type = type;
		counter = 0;
	}

	public void startAnimations() {
		Display.getCurrent().timerExec(this.imageInterval, animationRenderer);
	}

	private Runnable animationRenderer = new Runnable() {

		@Override
		public void run() {
			if (!disposed) {
				setChanged();

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

				Display.getCurrent().timerExec(imageInterval, this);
			}
		}
	};

	private Image resize(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, image.getBounds().width,
				image.getBounds().height, 0, 0, width, height);
		gc.dispose();
		image.dispose(); // don't forget about me!
		return scaled;
	}

	private void initializeAnimationData() {
		// idle image sequence - taken directly in a sorted (ascending) order
		for (String img : new File(AnimationRenderer.DIR_IDLE).list()) {
			
			idle[Integer.parseInt(img.split(".png")[0])] = resize(new Image(Display.getCurrent(),
					AnimationRenderer.DIR_IDLE + File.separator + img), 170,
					170);
		}
		// sort these images as they are not retrieved in a preferable order

		// low priority image sequence
		int k = 0;
		for (String img : new File(AnimationRenderer.DIR_LOW).list()) {
			lowPriority[k] = resize(new Image(Display.getCurrent(),
					AnimationRenderer.DIR_LOW + File.separator + img), 170, 170);
			k++;
		}

		// high priority image sequence
		k = 0;
		for (String img : new File(AnimationRenderer.DIR_HIGH).list()) {
			highPriority[k] = resize(new Image(Display.getCurrent(),
					AnimationRenderer.DIR_HIGH + File.separator + img), 170,
					170);
			k++;
		}
	}

}
