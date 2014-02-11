package com.coddotech.teamsubb.chat.gui;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.chat.model.Message;
import com.coddotech.teamsubb.chat.model.Messaging;
import com.coddotech.teamsubb.chat.model.StaffManager;
import com.coddotech.teamsubb.chat.model.StaffMember;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.notifications.gui.PopUpMessages;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;

public class IRCWindow extends CustomWindow {

	private static final Color COLOR_ADMIN = Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE);
	private static final Color COLOR_MODERATOR = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
	private static final Color COLOR_FONDATOR = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private static final Color COLOR_MEMBER = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private static final Color COLOR_SYSTEM = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);

	private StaffContainer staff;

	private StaffManager manager;

	private ChatContainer chat;

	private IRCController controller;

	public IRCWindow() {
		this.setShell(new Shell(Display.getDefault(), SWT.SHELL_TRIM));

		initializeComponents();
	}

	@Override
	public void dispose() {
		controller.dispose();

		staff.dispose();
		chat.dispose();

		manager.deleteObserver(this);
		manager.dispose();
	}

	public static Color getRankColor(StaffMember user) {
		if (user == null)
			return COLOR_SYSTEM;

		if (user.isFondator())
			return COLOR_FONDATOR;

		else if (user.isAdmin())
			return COLOR_ADMIN;

		else if (user.isModerator())
			return COLOR_MODERATOR;

		else
			return COLOR_MEMBER;
	}

	@Override
	protected void updateGUI(final Observable obs, final Object obj) {
		Runnable updater = new Runnable() {

			@Override
			public void run() {
				NotificationEntity notif = (NotificationEntity) obj;

				if (obs instanceof StaffManager) {

					StaffMember[][] members = (StaffMember[][]) notif.getStaff();

					int size = members[0].length + members[1].length;

					StaffMember[] items = new StaffMember[size];
					int index = 0;

					for (int i = 0; i < members[0].length; i++, index++)
						items[index] = members[0][i];

					for (int i = 0; i < members[1].length; i++, index++)
						items[index] = members[1][i];

					if (!staff.checkStaffList(size))
						staff.generateList(items);

					else
						staff.refreshStaff(items);

				}

				if (obs instanceof Messaging) {

					switch (notif.getMessage()) {
						case Messaging.MESSAGE: {

							if (!notif.getBoolean())
								PopUpMessages.getInstance().messageSendError();

						}
							break;

						case Messaging.IRC: {
							if (!notif.getString().equals("null"))
								chat.openIRCMessages(createMessageArray(notif.getString()));
						}
							break;

						case Messaging.PRIVATE: {
							if (!notif.getString().equals("null"))
								chat.openPrivateMessages(createMessageArray(notif.getString()));
						}
							break;

						case Messaging.OPEN_PRIVATE_CHAT: {
							chat.openPrivateChat(notif.getStaffMember());

						}
							break;
					}

				}

			}
		};

		Display.getDefault().asyncExec(updater);
	}

	private Message[] createMessageArray(String data) {
		String[] messages = data.split(JobManager.SEPARATOR_ENTITY);

		Message[] msg = new Message[messages.length];

		for (int i = 0; i < msg.length; i++)
			msg[i] = new Message(messages[i], manager);

		return msg;
	}

	/**
	 * Initializez all the components that are used in this GUI
	 */
	protected void initializeComponents() {

		// initializations
		try {
			this.performInitializations();

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "GUI Initialization", ex);

		}

		// object properties
		try {
			this.createObjectProperties();

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Create object properties", ex);

		}

		// shell properties
		try {
			this.createShellProperties();

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Create shell properties", ex);

		}

		// listeners
		try {
			this.createListeners();

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Create listeners", ex);

		}

	}

	@Override
	public void performInitializations() {
		controller = new IRCController(this);
		manager = new StaffManager();

		chat = new ChatContainer(this.getShell(), SWT.BORDER);
		staff = new StaffContainer(this.getShell(), SWT.BORDER | SWT.V_SCROLL);
	}

	@Override
	public void createObjectProperties() {
		manager.addObserver(this);

		staff.setFont(CustomWindow.DEFAULT_FONT);
		staff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

		chat.setFont(CustomWindow.DEFAULT_FONT);
		chat.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	}

	@Override
	public void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = true;

		this.getShell().setLayout(layout);
		this.getShell().setSize(800, 600);
		this.placeToCenter();

	}

	@Override
	public void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);
	}

}
