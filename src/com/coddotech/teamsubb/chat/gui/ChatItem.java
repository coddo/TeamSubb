package com.coddotech.teamsubb.chat.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.coddotech.teamsubb.chat.model.Message;
import com.coddotech.teamsubb.chat.model.StaffMember;
import com.coddotech.teamsubb.main.CustomWindow;

public class ChatItem extends CTabItem {

	private static final Color SYSTEM = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);

	private static final String USER_SYSTEM = "[SYSTEM MESSSAGE]";
	private static final String IRCTEXT = "Public chat";

	private ChatContainer parent;

	private StaffMember staff;

	private TextFields text = null;

	private ChatItem(CTabFolder arg0, int arg1) {
		super(arg0, arg1);

		this.parent = (ChatContainer) arg0;

		createListeners();
	}

	public ChatItem(CTabFolder arg0, int arg1, StaffMember staff) {
		this(arg0, arg1);

		this.staff = staff;

		this.setText((staff == null) ? IRCTEXT : staff.getName());

		text = new TextFields(arg0, SWT.BORDER, staff);
		text.setFont(CustomWindow.DEFAULT_FONT);

		this.setControl(text);
	}

	public StaffMember getStaff() {
		return this.staff;
	}

	public void dispose() {
		text.dispose();

		super.dispose();

		parent.closePrivateChats();
	}

	public void appendMessages(Message[] messages) {
		for (Message message : messages)
			this.appendMessage(message);
	}

	public void appendMessage(Message message) {
		this.appendSystemData(message.date + " ");

		this.appendUser(message.staff);

		this.appendMessage(message.message);
	}

	private void appendSystemData(String data) {
		int start = getStyleStartPoint();

		text.append(data + " ");

		text.setStyleRange(this.getSystemStyle(start, data.length()));
	}

	private void appendUser(StaffMember user) {
		int start = getStyleStartPoint();

		int length = USER_SYSTEM.length() + 1;
		String name = null;

		if (user != null) {
			name = user.getName();
			length = name.length() + 1;
		}

		text.append(name + ": ");

		text.setStyleRange(this.getUserStyle(user, start, length));
	}

	private void appendMessage(String msg) {
		text.append(msg + "\n");
	}

	private StyleRange getSystemStyle(int start, int length) {
		return this.getStyle(start, length, ChatItem.SYSTEM);
	}

	private StyleRange getUserStyle(StaffMember staff, int start, int length) {
		StyleRange style = this.getStyle(start, length, IRCWindow.getRankColor(staff));
		style.fontStyle = SWT.BOLD;

		return style;
	}

	private StyleRange getStyle(int start, int length, Color foreground) {
		StyleRange style = new StyleRange();

		style.start = start;
		style.length = length;
		style.foreground = foreground;

		return style;
	}

	private int getStyleStartPoint() {
		return text.getText().length();
	}

	private void createListeners() {
		this.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				dispose();

			}
		});
	}
}
