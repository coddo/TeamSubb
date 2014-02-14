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

import com.coddotech.teamsubb.chat.gui.IRCWindow;
import com.coddotech.teamsubb.chat.model.Messaging;
import com.coddotech.teamsubb.gadget.model.AnimationRenderer;
import com.coddotech.teamsubb.gadget.model.GadgetProfiler;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;
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
	private MenuItem openChat;
	private MenuItem openSettings;
	private MenuItem exitApp;

	// this lets the app be repositioned with the value stored in the settings file only once
	private boolean firstInstance = true;

	/**
	 * Class constructor
	 */
	public GadgetWindow() {
		super();

		this.setShell(new Shell(Display.getDefault(), SWT.NO_TRIM | SWT.Hide));

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
			openChat.dispose();
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
					NotificationEntity notif = null;

					if (obs instanceof AnimationRenderer) {

						try {
							imageContainer.setBackgroundImage((Image) obj);
						}

						catch (Exception ex) {

						}

					}

					else if (obs instanceof Settings) {
						notif = (NotificationEntity) obj;

						if (notif.getMessage().equals(Settings.LOCATION) && firstInstance) {

							int x = Integer.parseInt(notif.getString().split(",")[0]);
							int y = Integer.parseInt(notif.getString().split(",")[1]);

							getShell().setLocation(x, y);

							getShell().setAlpha(255);

							firstInstance = false;
						}

						else if (notif.getMessage().equals(Settings.GADGET_PROFILE)) {

							controller.ResetAnimationData();

							// force gadget repaint
							controller.redrawGadget();

							imageContainer.setLocation(GadgetProfiler.getInstance().getOffset());

						}
					}

					else if (obs instanceof Messaging) {
						notif = (NotificationEntity) obj;

						if (notif != null)
							if (notif.getMessage().equals(Messaging.PRIVATE))
								if (!notif.getString().isEmpty())
									IRCWindow.openChat(notif);

					}

				}

			}
		};

		Display.getDefault().asyncExec(update);

	}

	@Override
	public void performInitializations() {
		controller = new GadgetController(this);

		imageContainer = new Label(getShell(), SWT.NO_TRIM);

		tray = new TrayItem(Display.getDefault().getSystemTray(), SWT.NONE);

		trayMenu = new Menu(this.getShell(), SWT.POP_UP);

		openJobs = new MenuItem(trayMenu, SWT.PUSH);
		openChat = new MenuItem(trayMenu, SWT.PUSH);
		openSettings = new MenuItem(trayMenu, SWT.PUSH);
		exitApp = new MenuItem(trayMenu, SWT.PUSH);
	}

	@Override
	public void createObjectProperties() {
		imageContainer.setLocation(-10, -11);
		imageContainer.setSize(110, 110);

		tray.setText("TeamSubb");
		tray.setToolTipText("TeamSubb");
		tray.setImage(CustomWindow.APP_ICON);

		openJobs.setText("View Jobs");
		openChat.setText("Open Chat");
		openSettings.setText("Settings");
		exitApp.setText("Quit");
	}

	@Override
	public void createShellProperties() {
		this.getShell().setText("Gadget");
		this.getShell().setSize(200, 200);
		this.getShell().setMenu(trayMenu);
		this.getShell().setAlpha(0);
	}

	@Override
	public void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);
		this.getShell().addPaintListener(controller.shellPaint);

		this.imageContainer.addMouseMoveListener(controller.shellMoved);
		this.imageContainer.addMouseListener(controller.shellClicked);

		this.tray.addSelectionListener(controller.trayClicked);
		this.tray.addMenuDetectListener(controller.trayMenuDetected);

		this.openJobs.addSelectionListener(controller.openJobsClicked);
		this.openChat.addSelectionListener(controller.openChatClicked);
		this.openSettings.addSelectionListener(controller.openSettingsClicked);
		this.exitApp.addSelectionListener(controller.exitAppClicked);
	}
}
