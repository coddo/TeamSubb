package com.coddotech.teamsubb.gadget.gui;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;

import com.coddotech.teamsubb.gadget.model.AnimationRenderer;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.settings.model.Settings;

/**
 * A window similar to a gadget that stays stationary on the workspace of the
 * user and is responsible to transmit announcements to the user and inform him
 * about different jobs that become available for him.<br>
 * 
 * The JobWindow and SettingsWindow can be opened from here.
 * 
 * @author Coddo
 * 
 */
public class GadgetWindow extends CustomWindow {

	private GadgetController controller;

	public Label imageContainer;

	private TrayItem tray;

	private Menu trayMenu;
	private MenuItem openJobs;
	private MenuItem openSettings;
	private MenuItem exitApp;

	// this lets the app be repositioned with the value stored in the settings
	// file only once
	private boolean first = true;

	/**
	 * Class constructor
	 */
	public GadgetWindow() {
		super();

		this.setShell(new Shell(Display.getDefault(), SWT.NO_TRIM | SWT.ON_TOP));

		this.initializeComponents();
	}

	@Override
	public void dispose() {
		try {

			// user classes
			controller.dispose();

			// fields
			imageContainer.dispose();

			openJobs.dispose();
			openSettings.dispose();
			exitApp.dispose();
			trayMenu.dispose();

			tray.dispose();

			this.logDispose();

		}
		catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	public void showMenu() {
		trayMenu.setVisible(true);
	}

	@Override
	protected void updateGUI(final Observable obs, final Object obj) {

		Runnable update = new Runnable() {

			@Override
			public void run() {

				if (!controller.isDisposed()) {

					if (obs instanceof AnimationRenderer) {
						imageContainer.setBackgroundImage((Image) obj);
						
					}

					else if (obs instanceof Settings) {

						String[] data = ((String) obj).split(CustomWindow.NOTIFICATION_SEPARATOR);

						if (data[0].equals(Settings.MESSAGE_LOCATION) && first) {
							int x = Integer.parseInt(data[1].split(",")[0]);
							int y = Integer.parseInt(data[1].split(",")[1]);
							getShell().setAlpha(255);

							getShell().setLocation(x, y);
							first = false;

						}
					}

				}

			}
		};

		Display.getDefault().asyncExec(update);

	}

	@Override
	protected void performInitializations() {
		controller = new GadgetController(this);

		imageContainer = new Label(getShell(), SWT.NO_TRIM);

		tray = new TrayItem(Display.getDefault().getSystemTray(), SWT.NONE);

		trayMenu = new Menu(this.getShell(), SWT.POP_UP);
		openJobs = new MenuItem(trayMenu, SWT.PUSH);
		openSettings = new MenuItem(trayMenu, SWT.PUSH);
		exitApp = new MenuItem(trayMenu, SWT.PUSH);
	}

	@Override
	protected void createObjectProperties() {
		imageContainer.setLocation(-10, -11);
		imageContainer.setSize(110, 110);

		tray.setText("TeamSubb");
		tray.setToolTipText("TeamSubb");
		tray.setImage(CustomWindow.APP_ICON);

		exitApp.setText("Exit TeamSubb");
		openJobs.setText("Open jobs window");
		openSettings.setText("Open settings window");
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("Gadget");
		this.getShell().setSize(200, 200);
		this.getShell().setMenu(trayMenu);
		this.getShell().setAlpha(0);
	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);
		this.getShell().addPaintListener(controller.shellPaint);

		this.imageContainer.addMouseMoveListener(controller.shellMoved);
		this.imageContainer.addMouseListener(controller.shellClicked);

		this.tray.addSelectionListener(controller.trayClicked);
		this.tray.addMenuDetectListener(controller.trayMenuDetected);

		this.openJobs.addSelectionListener(controller.openJobsClicked);
		this.openSettings.addSelectionListener(controller.openSettingsClicked);
		this.exitApp.addSelectionListener(controller.exitAppClicked);
	}
}
