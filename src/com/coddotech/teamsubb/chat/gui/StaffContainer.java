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

		refreshContent();
	}

	@Override
	public void dispose() {
		this.clearList();

		super.dispose();
	}

	public void setItems(final StaffMember[] staff) {
		Runnable updater = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				for (int i = 0; i < staff.length; i++) {
					StaffItem item = new StaffItem(content, getThis(), SWT.BORDER, staff[i]);

					items.add(item);
				}

				setContent(content);
				setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		};

		Display.getDefault().syncExec(updater);

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

		refreshContent();
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

	private void refreshContent() {
		Runnable contentUpdate = new Runnable() {

			@Override
			public void run() {
				if (content != null)
					content.dispose();

				content = new Composite(getThis(), SWT.None);
				content.setLayout(new GridLayout(1, true));

			}
		};

		Display.getDefault().syncExec(contentUpdate);
		// contentUpdate.run();

	}

	private StaffContainer getThis() {
		return this;
	}

}
