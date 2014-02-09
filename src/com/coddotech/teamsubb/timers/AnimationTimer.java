package com.coddotech.teamsubb.timers;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.gadget.model.AnimationRenderer;

public class AnimationTimer extends Thread {

	private static final int imageInterval = 300;

	private AnimationRenderer renderer;

	public AnimationTimer(AnimationRenderer renderer) {
		this.renderer = renderer;

	}

	@Override
	public void run() {

		while (!renderer.isDisposed()) {

			threadPause();

			try {

				if (!renderer.isPaused() && !renderer.isDisposed()) {

					// send the animation data to the gadget based on the animation type
					// currently selected
					renderer.sendAnimationData();

				}
			}

			catch (Exception ex) {
				ActivityLogger.logException(this.getClass().getName(), "Animation rendering", ex);

			}

		}

	}

	private void threadPause() {
		try {

			Thread.sleep(imageInterval);

		}

		catch (InterruptedException e) {

		}
	}
}
