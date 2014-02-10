package com.coddotech.teamsubb.chat.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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

	public static final Color SELECTED = Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION);
	public static final Color DESELECTED = Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND);

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

	public void resize() {
		// this.setSize(length, this.getSize().y);

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
		image.setImage(StaffItem.OFFLINE);
		image.setSize(60, 40);

		name.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		name.setFont(CustomWindow.BOLD_FONT);
		name.setText(staff.getName());
		name.pack();

		rank.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		rank.setFont(CustomWindow.DEFAULT_FONT);
		rank.setText(staff.getRank());
		rank.pack();
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
		this.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				deselect();

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				select();

			}
		});

		this.addMouseListener(new MouseListener() {

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
		});

	}

	private void setMemberData() {
		if (staff.isOnline()) {
			this.image.setImage(StaffItem.ONLINE);

		}
	}

	private StaffItem getThis() {
		return this;
	}

}
