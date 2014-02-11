package com.coddotech.teamsubb.chat.gui;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.chat.model.StaffManager;
import com.coddotech.teamsubb.chat.model.StaffMember;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;

public class IRCWindow extends CustomWindow {

	private static final Color COLOR_ADMIN = Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE);
	private static final Color COLOR_MODERATOR = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
	private static final Color COLOR_FONDATOR = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private static final Color COLOR_MEMBER = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

	private StaffContainer staff;

	private ChatContainer chat;

	private IRCController controller;

	public IRCWindow() {
		initializeComponents();
	}

	@Override
	public void dispose() {
		controller.dispose();

		staff.dispose();
		chat.dispose();
	}

	public static Color getRankColor(StaffMember user) {
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
	protected void updateGUI(Observable obs, Object obj) {
		if (obs instanceof StaffManager) {
			NotificationEntity notif = (NotificationEntity) obj;

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

		chat = new ChatContainer(this.getShell(), SWT.BORDER);
		staff = new StaffContainer(this.getShell(), SWT.BORDER | SWT.V_SCROLL);
	}

	@Override
	public void createObjectProperties() {
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
	}

}
