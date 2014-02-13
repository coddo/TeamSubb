package com.coddotech.teamsubb.chat.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.chat.model.Messaging;
import com.coddotech.teamsubb.chat.model.StaffMember;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.main.Widget;

public class StaffItem extends Composite implements Widget {

	private static final String ICODIR = System.getProperty("user.dir") + File.separator + "resources" + File.separator
			+ "staff" + File.separator;

	private static final Color SELECTED = Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION);
	private static final Color DESELECTED = Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND);

	public static final Image ONLINE = new Image(Display.getDefault(), ICODIR + "online.ico");
	public static final Image OFFLINE = new Image(Display.getDefault(), ICODIR + "offline.ico");

	private CLabel image;
	private Label name;
	private Label rank;

	int length = 0;

	private StaffMember staff = null;

	private StaffContainer parent = null;

	private StaffItem(Composite c, int type) {
		super(c, type);

	}

	public StaffItem(Composite c, StaffContainer parent, int type, StaffMember staff) {
		this(c, type);

		this.staff = staff;
		this.parent = parent;

		initializeComponents();

		deselect();
	}

	@Override
	public void dispose() {
		image.dispose();
		name.dispose();
		rank.dispose();

		super.dispose();
	}

	public StaffMember getStaff() {
		return this.staff;
	}

	public void select() {
		this.setBackground(SELECTED);
		image.setBackground(SELECTED);
		name.setBackground(SELECTED);
		rank.setBackground(SELECTED);
	}

	public void deselect() {
		this.setBackground(DESELECTED);
		image.setBackground(DESELECTED);
		name.setBackground(DESELECTED);
		rank.setBackground(DESELECTED);
	}

	public void changeStaff(StaffMember staff) {
		this.staff = staff;

		this.setMemberData();
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

		// member data
		try {
			this.setMemberData();

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Set member data", ex);

		}

	}

	@Override
	public void performInitializations() {
		image = new CLabel(this, SWT.None);
		name = new Label(this, SWT.None);
		rank = new Label(this, SWT.None);
	}

	@Override
	public void createObjectProperties() {
		image.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 2));
		image.setSize(60, 40);

		name.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		name.setFont(CustomWindow.BOLD_FONT);

		rank.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		rank.setFont(CustomWindow.DEFAULT_FONT);
	}

	@Override
	public void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		this.setLayout(layout);
		this.setSize(length, 50);

		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		this.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

	}

	@Override
	public void createListeners() {
		MouseListener itemClicked = new MouseListener() {

			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				select();

				parent.deselectAll(getThis());
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				Messaging.getInstance().openPrivateChat(staff);

			}

		};

		this.addMouseListener(itemClicked);
		this.name.addMouseListener(itemClicked);
		this.rank.addMouseListener(itemClicked);
		this.image.addMouseListener(itemClicked);

	}

	private void setMemberData() {
		this.image.setImage(staff.isOnline() ? StaffItem.ONLINE : StaffItem.OFFLINE);

		this.name.setText(this.staff.getName());
		this.rank.setText(this.staff.getRank());

		this.name.pack();
		this.rank.pack();

		this.rank.setForeground(StaffMember.getRankColor(staff));

	}

	private StaffItem getThis() {
		return this;
	}

}
