package com.coddotech.teamsubb.chat.gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.coddotech.teamsubb.chat.model.StaffMember;

public class StaffContainer extends ScrolledComposite {

	private Composite content;

	private List<StaffItem> items = new ArrayList<StaffItem>();

	public StaffContainer(Composite arg0, int arg1) {
		super(arg0, arg1);

		initializeContent();
	}

	@Override
	public void dispose() {
		this.clearList();

		super.dispose();
	}

	public void setItems(StaffMember[] staff) {
		for (int i = 0; i < staff.length; i++) {
			StaffItem item = new StaffItem(content, this, SWT.BORDER, staff[i]);

			items.add(item);

			this.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}

	}

	public void clearList() {
		for (int i = 0; i < items.size(); i++) {

			try {
				items.get(i).dispose();
			}

			catch (Exception ex) {

			}

		}

		items.clear();
	}

	public void resizeItems() {

		for (StaffItem item : items)
			item.resize();

	}

	public void deselectAll(StaffItem item) {
		for (StaffItem it : items) {
			if (!it.equals(item))
				it.deselect();
		}
	}

	private void initializeContent() {
		content = new Composite(this, SWT.None);
		content.setLayout(new GridLayout(1, true));

		this.setExpandHorizontal(true);
		this.setExpandVertical(true);

		this.setContent(content);
	}

}
