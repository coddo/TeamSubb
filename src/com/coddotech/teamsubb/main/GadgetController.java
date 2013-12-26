package com.coddotech.teamsubb.main;

import java.awt.MouseInfo;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

import com.coddotech.teamsubb.jobs.JobManager;
import com.coddotech.teamsubb.jobs.JobWindow;
import com.coddotech.teamsubb.settings.AppSettings;
import com.coddotech.teamsubb.settings.AppSettingsWindow;

/**
 * Controller class for the GadgetWindow.<br>
 * The vast majority of models used for the workflow intersect here.
 * 
 * @author Coddo
 * 
 */
public class GadgetController {

	private GadgetWindow gadget;

	private JobManager jobs;
	private JobWindow jobsWindow;

	private AppSettings settings;
	private AppSettingsWindow settingsWindow;

	private AnimationRenderer animations;

	// 1 min = 60000 ms
	int searchInterval = AppSettings.DEFAULT_SEARCH_INTERVAL * 60000;

	boolean disposed = false;

	// data used for moving the form around
	boolean move = false;
	int x;
	int y;

	/**
	 * Class constructor
	 * 
	 * @param view
	 *            The view which uses this controller
	 */
	public GadgetController(GadgetWindow view) {
		this.gadget = view;

		// initialize the components
		this.initializeController();

		// start the timer in order for it to search for new jobs
		Display.getCurrent().timerExec(50, timer);
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		this.disposed = true;

		animations.dispose();
		animations = null;

		timer = null;
		gadget = null;

		jobsWindow = null;
		jobs.dispose();
		jobs = null;

		settingsWindow = null;
		settings.dispose();
		settings = null;
	}

	/**
	 * Set the interval in which
	 * 
	 * @param mins
	 */
	public void setSearchInterval(int mins) {
		this.searchInterval = mins * 60000;
	}

	/**
	 * Chek if the controller has been disposed or not
	 * 
	 * @return A logical value
	 */
	public boolean isDisposed() {
		return this.disposed;
	}

	/**
	 * Timer used to search for new jobs. This method checks if there is an
	 * internet connection available before sending the request
	 */
	Runnable timer = new Runnable() {

		@Override
		public void run() {
			if (!disposed) {
				if (gadget.isConnected(false))
					jobs.findJobs();

				Display.getCurrent().timerExec(searchInterval, this);
			}
		}

	};

	/**
	 * Listener for when the gadget is clicked. <br>
	 * Left double-click opens the JobsWindow while left double-click opens the
	 * ApplicationSettings. The left click is used for moving the window on the
	 * screen
	 */
	public MouseListener shellClicked = new MouseListener() {

		@Override
		public void mouseUp(MouseEvent e) {
			if (e.button == 1)
				move = false;
		}

		@Override
		public void mouseDown(MouseEvent e) {
			if (e.button == 1) {
				move = true;

				x = MouseInfo.getPointerInfo().getLocation().x;
				y = MouseInfo.getPointerInfo().getLocation().y;
			}
		}

		// opening of the JobWindow class is done in such a way to prevent
		// overflowing the workspace with instances of the same type of view
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			if (e.button == 1) {
				boolean visible = false;
				
				try {
					visible = jobsWindow.getShell().isVisible();
				} catch (Exception ex) {
					jobsWindow = new JobWindow(gadget.getUserInfo(),
							gadget.getUserJobs());
				} finally {
					jobsWindow.getController().setModel(jobs);
					jobs.addObserver(jobsWindow);
					
					if(!visible)
						jobsWindow.open();
					
				}
			} else if (e.button == 3) {
				settingsWindow = new AppSettingsWindow();
				settingsWindow.getController().setModel(settings);
				settings.addObserver(settingsWindow);
				settingsWindow.open();
			}

			// don't let the window be moved because it was a double-click
			move = false;
		}
	};

	/**
	 * Detects when the mouse is moved over the shell. <br>
	 * This helps with the repositioning of the application by the user.
	 */
	public MouseMoveListener shellMoved = new MouseMoveListener() {

		@Override
		public void mouseMove(MouseEvent arg0) {
			if (move) {
				int difx = MouseInfo.getPointerInfo().getLocation().x - x;
				int dify = MouseInfo.getPointerInfo().getLocation().y - y;

				int curx = gadget.getShell().getLocation().x;
				int cury = gadget.getShell().getLocation().y;

				gadget.getShell().setLocation(curx + difx, cury + dify);

				x = MouseInfo.getPointerInfo().getLocation().x;
				y = MouseInfo.getPointerInfo().getLocation().y;
			}
		}
	};

	/**
	 * Listener for when the window is shown. <br>
	 * Reads the settings and updates the interface.
	 */
	public Listener shellShownListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			// read the app settings to get the position for the gadget
			settings.readSettings();

			animations.setAnimationType(AnimationRenderer.TYPE_IDLE);
			animations.startAnimations();
		}

	};

	/**
	 * Listener for when the window is closed. <br>
	 * Saves the current position for the app and disposes of all the window's
	 * components
	 */
	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			// save the position of the gadget is necessary
			if (settings.isGadgetAutosaveLocation()) {
				settings.setGadgetLocation(gadget.getShell().getLocation());
				settings.commitChangesToFile();
			}

			gadget.dispose();
		}
	};

	/**
	 * Draws the shell into a circle form
	 */
	public PaintListener shellPaint = new PaintListener() {

		@Override
		public void paintControl(PaintEvent arg0) {
			// create the region defining the gadget
			Region region = new Region();

			// set the circle data to the region
			region.add(circle(45, 45, 45));

			// define the shape of the shell
			gadget.getShell().setRegion(region);
			Rectangle size = region.getBounds();
			gadget.getShell().setSize(size.width, size.height);
		}
	};

	/**
	 * Get the ecuation defining a circle with the set radius
	 * 
	 * @param r
	 *            The radius of the circle
	 * @param offsetX
	 *            Horizontal offset
	 * @param offsetY
	 *            Vertical offset
	 * @return The polygon ecuation for the circle
	 */
	private int[] circle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r + 4];
		// x^2 + y^2 = r^2
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int) Math.sqrt(r * r - x * x);
			polygon[2 * i] = offsetX + x;
			polygon[2 * i + 1] = offsetY + y;
			polygon[8 * r - 2 * i - 2] = offsetX + x;
			polygon[8 * r - 2 * i - 1] = offsetY - y;
		}
		return polygon;
	}

	/**
	 * Creates all the components for this class
	 */
	private void initializeController() {
		// create the models
		jobs = new JobManager(this.gadget.getUserName(),
				this.gadget.getUserJobs());
		settings = new AppSettings();
		animations = new AnimationRenderer();

		// set the observers for the models
		settings.addObserver(this.gadget);
		jobs.addObserver(animations);
		animations.addObserver(this.gadget);
	}

}
