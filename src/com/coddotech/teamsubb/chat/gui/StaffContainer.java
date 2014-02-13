package com.coddotech.teamsubb.chat.gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.chat.model.StaffMember;

/**
 * Widget for keeping a list of staff members that are currently registered on the server
 * 
 * @author coddo
 * 
 */
public class StaffContainer extends ScrolledComposite {

	private Composite content = null;

	private List<StaffItem> items = new ArrayList<StaffItem>();

	/**
	 * Constructor
	 * 
	 * @param arg0
	 *            The parent widget
	 * @param arg1
	 *            The style for this widget
	 */
	public StaffContainer(Composite arg0, int arg1) {
		super(arg0, arg1);

		this.setExpandHorizontal(true);
		this.setExpandVertical(true);
	}

	@Override
	public void dispose() {
		this.clearList();

		super.dispose();
	}

	/**
	 * Refresh the staff list by setting the appropriate data
	 * 
	 * @param staff
	 *            The StaffMember collection reprezenting the staff
	 */
	public void refreshStaff(final StaffMember[] staff) {
		Runnable updater = new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < staff.length; i++)
					items.get(i).changeStaff(staff[i]);

			}
		};

		Display.getDefault().asyncExec(updater);

	}

	/**
	 * Verify if the staff list size corresponds with the size of the list that is fetched from the
	 * server
	 * 
	 * @param size
	 *            The size of the list fetched from the server
	 * @return A Logical value
	 */
	public boolean checkStaffList(int size) {
		return size == items.size();

	}

	/**
	 * Deselect all the items from the list
	 * 
	 * @param item
	 */
	public void deselectAll(StaffItem item) {
		for (StaffItem it : items) {

			if (!it.equals(item))
				it.deselect();

		}
	}

	/**
	 * Generate the entire staff list from scratch
	 * 
	 * @param staff
	 *            The StaffMember collection representing the staff list
	 */
	public void generateList(final StaffMember[] staff) {
		Runnable updater = new Runnable() {

			@Override
			public void run() {
				clearList();

				createContent();

				populateList(staff);

			}
		};

		Display.getDefault().asyncExec(updater);
	}

	/**
	 * Empty the list
	 */
	private void clearList() {
		for (int i = 0; i < items.size(); i++) {
			items.get(i).dispose();

		}

		items.clear();
	}

	/**
	 * Redraw the content for this widget
	 */
	private void createContent() {
		if (content != null)
			content.dispose();

		content = new Composite(getThis(), SWT.None);
		content.setLayout(new GridLayout(1, true));

		setContent(content);

	}

	/**
	 * Fill the list with staff data
	 * 
	 * @param staff
	 *            A StaffMember collection
	 */
	private void populateList(StaffMember[] staff) {

		for (int i = 0; i < staff.length; i++) {
			createStaffItem(staff[i]);

		}

		this.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	/**
	 * Create a new staff item that boxes a staff member
	 * 
	 * @param staff
	 *            A StaffMember value
	 */
	private void createStaffItem(StaffMember staff) {
		StaffItem item = new StaffItem(content, getThis(), SWT.BORDER, staff);

		items.add(item);
	}

	/**
	 * Get an instance of this class
	 * 
	 * @return A StaffContainer instance
	 */
	private StaffContainer getThis() {
		return this;
	}

}
