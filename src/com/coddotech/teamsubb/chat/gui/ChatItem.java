package com.coddotech.teamsubb.chat.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;

import com.coddotech.teamsubb.chat.model.StaffMember;

public class ChatItem extends CTabItem {

	private StaffMember staff = null;

	private StyledText text = null;

	private ChatItem(CTabFolder arg0, int arg1) {
		super(arg0, arg1);

		text = new StyledText(arg0, SWT.BORDER);
	}

	public ChatItem(CTabFolder arg0, int arg1, StaffMember staff) {
		super(arg0, arg1);

		this.staff = staff;
	}

}
