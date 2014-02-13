package com.coddotech.teamsubb.chat.gui;

import java.util.Date;

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

/**
 * A tab class representing a basic item for the ChatContainer widget. This contains a MultiLine
 * textbox for displaying messages and a single line textbox for writing messages
 * 
 * @author coddo
 * 
 */
public class ChatItem extends CTabItem {

	private static final Color SYSTEM = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);

	private static final String USER_SYSTEM = "[SYSTEM MESSSAGE]";
	private static final String IRCTEXT = "Public chat";

	private ChatContainer parent;

	private StaffMember staff;

	private TextFields text = null;

	/**
	 * Main Constructor
	 * 
	 * @param arg0
	 *            The parent Widget
	 * @param arg1
	 *            The style of the this wirdget
	 */
	private ChatItem(CTabFolder arg0, int arg1) {
		super(arg0, arg1);

		this.parent = (ChatContainer) arg0;

		createListeners();
	}

	/**
	 * Constructor
	 * 
	 * @param arg0
	 *            The parent Widget
	 * @param arg1
	 *            The style of the this wirdget
	 * @param staff
	 *            The StaffMember instance representing the person to chat with
	 */
	public ChatItem(CTabFolder arg0, int arg1, StaffMember staff) {
		this(arg0, arg1);

		this.staff = staff;

		this.setText((staff == null) ? IRCTEXT : staff.getName());

		text = new TextFields(arg0, SWT.BORDER, staff);
		text.setFont(CustomWindow.DEFAULT_FONT);

		this.setControl(text);

		displayUserStatusMessage();
	}

	/**
	 * Get the user that represents this chat
	 * 
	 * @return A StaffMember instance
	 */
	public StaffMember getStaff() {
		return this.staff;
	}

	/**
	 * Set the user that represents this chat
	 * 
	 * @param staff
	 *            A StaffMember instance
	 */
	public void setStaff(StaffMember staff) {
		this.staff = staff;
	}

	/**
	 * Dispose all the resources used by this class and close this tab
	 */
	public void dispose() {
		text.dispose();

		super.dispose();

		parent.closePrivateChats();
	}

	/**
	 * Append messages to this instance
	 * 
	 * @param messages
	 *            A Message instance collection
	 */
	public void appendMessages(Message[] messages) {
		for (Message message : messages)
			this.appendMessage(message);
	}

	/**
	 * Append a message to this instance
	 * 
	 * @param message
	 *            A Message instance
	 */
	public void appendMessage(Message message) {
		this.appendSystemData(message.date + " ");

		this.appendUser(message.staff);

		this.appendMessage(message.message);
	}

	private void appendSystemMessage() {
		this.appendSystemData("[" + new Date() + "] ");

		this.appendUser(null);

		String message = " appears to be offline. This user will receive your message when he/she logs back in :).";

		this.appendSystemData(staff.getName() + message);
	}

	/**
	 * Display a system message about the user
	 */
	private void displayUserStatusMessage() {
		if (staff == null)
			return;

		if (!staff.isOnline()) {
			appendSystemMessage();

		}
	}

	/**
	 * Set the font style for the date and other system messages
	 * 
	 * @param data
	 */
	private void appendSystemData(String data) {
		int start = getStyleStartPoint();

		text.append(data + " ");

		text.setStyleRange(this.getSystemStyle(start, data.length()));
	}

	/**
	 * Set the font style for the chat user (with representing rank color)
	 * 
	 * @param user
	 *            A StaffMember instance
	 */
	private void appendUser(StaffMember user) {
		int start = getStyleStartPoint();

		int length = USER_SYSTEM.length() + 1;
		String name = USER_SYSTEM;

		if (user != null) {
			name = user.getName();
			length = name.length() + 1;
		}

		text.append(name + ": ");

		text.setStyleRange(this.getUserStyle(user, start, length));
	}

	/**
	 * Append text to this chat tab
	 * 
	 * @param msg
	 */
	private void appendMessage(String msg) {
		text.append(msg + "\n");
	}

	/**
	 * Get the font style for system messages
	 * 
	 * @param start
	 *            The start position for this style
	 * @param length
	 *            The length of the text that will have this style
	 * @return A StyleRange entity
	 */
	private StyleRange getSystemStyle(int start, int length) {
		return this.getStyle(start, length, ChatItem.SYSTEM);
	}

	/**
	 * Get the font style for user names
	 * 
	 * @param start
	 *            The start position for this style
	 * @param length
	 *            The length of the text that will have this style
	 * @return A StyleRange entity
	 */
	private StyleRange getUserStyle(StaffMember staff, int start, int length) {
		StyleRange style = this.getStyle(start, length, StaffMember.getRankColor(staff));
		style.fontStyle = SWT.BOLD;

		return style;
	}

	/**
	 * Get the font style with the selected data
	 * 
	 * @param start
	 *            The start position for this style
	 * @param length
	 *            The length of the text that will have this style
	 * @param foreground
	 *            The color for the text which will have this style
	 * @return A StyleRange entity
	 */
	private StyleRange getStyle(int start, int length, Color foreground) {
		StyleRange style = new StyleRange();

		style.start = start;
		style.length = length;
		style.foreground = foreground;

		return style;
	}

	/**
	 * Get the length of the text in this tab, which represents the start point for new styles
	 * 
	 * @return An integer value
	 */
	private int getStyleStartPoint() {
		return text.getText().length();
	}

	/**
	 * Creates listeners for this class
	 */
	private void createListeners() {
		this.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				dispose();

			}
		});

	}
}
