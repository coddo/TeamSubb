package com.coddotech.teamsubb.chat.gui;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.chat.model.StaffManager;
import com.coddotech.teamsubb.chat.model.StaffMember;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.notifications.model.NotificationEntity;

public class IRCWindow extends CustomWindow {

	private StaffContainer staff;

	private IRCController controller;

	public IRCWindow() {
		initializeComponents();
	}

	@Override
	public void dispose() {
		controller.dispose();

		staff.dispose();
	}

	@Override
	protected void updateGUI(Observable obs, Object obj) {
		if (obs instanceof StaffManager) {
			NotificationEntity notif = (NotificationEntity) obj;

			staff.clearList();

			StaffMember[][] members = (StaffMember[][]) notif.getStaff();

			StaffMember[] items = new StaffMember[members[0].length + members[1].length];
			int index = 0;

			for (int i = 0; i < members[0].length; i++, index++)
				items[index] = members[0][i];

			for (int i = 0; i < members[1].length; i++, index++)
				items[index] = members[1][i];

			staff.setItems(items);
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

		staff = new StaffContainer(this.getShell(), SWT.BORDER | SWT.V_SCROLL);
	}

	@Override
	public void createObjectProperties() {
		staff.setFont(CustomWindow.DEFAULT_FONT);
		staff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@Override
	public void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
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
