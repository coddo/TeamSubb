package com.coddotech.teamsubb.chat.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.chat.model.Messaging;
import com.coddotech.teamsubb.chat.model.StaffMember;
import com.coddotech.teamsubb.main.Widget;

/**
 * Items used by the StaffContainer class, representing a certain staff member within the chat
 * 
 * @author coddo
 * 
 */
public class StaffItem extends Composite implements Widget {

	private static final String ICODIR = System.getProperty("user.dir") + File.separator + "resources" + File.separator
			+ "staff" + File.separator;

	private static final Color SELECTED = Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION);
	private static final Color DESELECTED = Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND);

	public static final Image ONLINE = new Image(Display.getDefault(), ICODIR + "online.ico");
	public static final Image OFFLINE = new Image(Display.getDefault(), ICODIR + "offline.ico");

	public static final Font FONT_RANK = new Font(Display.getDefault(), "Calibri", 10, SWT.NORMAL);
	public static final Font FONT_USER = new Font(Display.getDefault(), "Calibri", 10, SWT.BOLD);

	private CLabel image;
	private Label name;
	private Label rank;

	int length = 0;

	private StaffMember staff = null;

	private StaffContainer parent = null;

	/**
	 * Main Constructor
	 * 
	 * @param c
	 *            The parent widget
	 * @param style
	 *            The style for this widget
	 */
	private StaffItem(Composite c, int style) {
		super(c, style);

	}

	/**
	 * Main Constructor
	 * 
	 * @param c
	 *            The parent widget
	 * @param style
	 *            The style for this widget
	 * @param staff
	 *            The StaffMember instance reprezenting this item
	 */
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

	/**
	 * Get the representing staff member for this item
	 * 
	 * @return A StaffMember instance
	 */
	public StaffMember getStaff() {
		return this.staff;
	}

	/**
	 * Make this widget visible as selected by setting the appropriate colors
	 */
	public void select() {
		this.setBackground(SELECTED);
		image.setBackground(SELECTED);
		name.setBackground(SELECTED);
		rank.setBackground(SELECTED);
	}

	/**
	 * Make this widget visible as deselected by setting the appropriate colors
	 */
	public void deselect() {
		this.setBackground(DESELECTED);
		image.setBackground(DESELECTED);
		name.setBackground(DESELECTED);
		rank.setBackground(DESELECTED);
	}

	/**
	 * Change the representing staff member for this widget
	 * 
	 * @param staff
	 *            A StaffMember instance
	 */
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
		image.setSize(40, 20);

		name.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		name.setFont(FONT_USER);

		rank.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		rank.setFont(FONT_RANK);
	}

	@Override
	public void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		this.setLayout(layout);
		this.setSize(length, 15);

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

	/**
	 * Display the appropriate data in the GUI, according to the representing staff details
	 */
	private void setMemberData() {
		this.image.setImage(staff.isOnline() ? StaffItem.ONLINE : StaffItem.OFFLINE);

		this.name.setText(this.staff.getName());
		this.rank.setText(this.staff.getRank());

		this.name.pack();
		this.rank.pack();

		this.rank.setForeground(StaffMember.getRankColor(staff));

	}

	/**
	 * Get an instance for this class
	 * 
	 * @return A StaffItem instance
	 */
	private StaffItem getThis() {
		return this;
	}

}
