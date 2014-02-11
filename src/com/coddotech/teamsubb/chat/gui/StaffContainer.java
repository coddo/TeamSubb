package com.coddotech.teamsubb.chat.gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.chat.model.StaffMember;

public class StaffContainer extends ScrolledComposite {

	private Composite content = null;

	private List<StaffItem> items = new ArrayList<StaffItem>();

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

	public void refreshStaff(final StaffMember[] staff) {
		Runnable updater = new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < staff.length; i++)
					items.get(i).changeStaff(staff[i]);

			}
		};

		Display.getDefault().syncExec(updater);

	}

	public boolean checkStaffList(int size) {
		return size == items.size();

	}

	public void deselectAll(StaffItem item) {
		for (StaffItem it : items) {

			if (!it.equals(item))
				it.deselect();

		}
	}

	public void generateList(final StaffMember[] staff) {
		Runnable updater = new Runnable() {

			@Override
			public void run() {
				clearList();

				createContent();

				populateList(staff);

			}
		};

		Display.getDefault().syncExec(updater);
	}

	private void clearList() {
		for (int i = 0; i < items.size(); i++) {
			items.get(i).dispose();

		}

		items.clear();
	}

	private void createContent() {
		if (content != null)
			content.dispose();

		content = new Composite(getThis(), SWT.None);
		content.setLayout(new GridLayout(1, true));

		setContent(content);

	}

	private void populateList(StaffMember[] staff) {

		for (int i = 0; i < staff.length; i++) {
			createStaffItem(staff[i]);

		}

		this.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	private void createStaffItem(StaffMember staff) {
		StaffItem item = new StaffItem(content, getThis(), SWT.BORDER, staff);

		items.add(item);
	}

	private StaffContainer getThis() {
		return this;
	}

}
