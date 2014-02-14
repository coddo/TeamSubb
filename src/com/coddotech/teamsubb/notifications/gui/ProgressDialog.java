package com.coddotech.teamsubb.notifications.gui;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.coddotech.teamsubb.main.CustomWindow;

/**
 * Pop-up window containing a progress bar that tells the user about the state
 * of a long action
 * 
 * @author Coddo
 */
public class ProgressDialog extends CustomWindow {

	private boolean disposed = false;

	private Label title;
	private Label dots;

	private int dotCount = 0;

	public ProgressDialog(String message) {
		this.setShell(new Shell(Display.getDefault(), SWT.ON_TOP | SWT.NO_TRIM));

		this.initializeComponents();

		title.setText(message);
		title.pack();
		title.setLocation(this.getShell().getSize().x / 2 - title.getSize().x / 2, title.getSize().y);
	}

	@Override
	public void dispose() {
		disposed = true;

		title.dispose();
		dots.dispose();
	}

	@Override
	protected void updateGUI(final Observable obs, final Object obj) {
		Runnable updater = new Runnable() {

			@Override
			public void run() {
				obs.deleteObserver(getThis());

				dispose();

			}
		};

		Display.getDefault().asyncExec(updater);
	}

	@Override
	public void performInitializations() {
		title = new Label(this.getShell(), SWT.WRAP);
		dots = new Label(this.getShell(), SWT.None);

	}

	@Override
	public void createObjectProperties() {
		title.setFont(CustomWindow.DEFAULT_FONT);
		title.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		title.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));

		dots.setFont(CustomWindow.DEFAULT_FONT);
		dots.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		dots.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		dots.setLocation(80, 30);

	}

	@Override
	public void createShellProperties() {
		this.getShell().setSize(200, 50);
		this.getShell().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		this.getShell().setAlpha(200);

		this.placeToCenter();

	}

	@Override
	public void createListeners() {
		this.getShell().addListener(SWT.Show, new Listener() {

			@Override
			public void handleEvent(Event e) {
				animation.run();

			}
		});

	}

	Runnable animation = new Runnable() {

		@Override
		public void run() {

			if (disposed) {
				close();

			}

			else {

				if (dotCount == 7) {
					dotCount = 1;
					dots.setText(".");
				}

				else {
					dots.setText("." + dots.getText());
					dots.pack();

					dotCount++;
				}

				Display.getCurrent().timerExec(50, this);
			}

		}
	};

	private ProgressDialog getThis() {
		return this;
	}

}
