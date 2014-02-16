package com.coddotech.teamsubb.chat.gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.coddotech.teamsubb.chat.model.LoggedUser;
import com.coddotech.teamsubb.chat.model.Message;
import com.coddotech.teamsubb.chat.model.StaffMember;
import com.coddotech.teamsubb.main.SoundPlayer;

/**
 * A Tabbed pane used to distinguish between the public chat and private chats with different staff
 * members
 * 
 * @author coddo
 * 
 */
public class ChatContainer extends CTabFolder {

	private ChatItem irc = null;

	private List<ChatItem> chats = new ArrayList<ChatItem>();

	/**
	 * Constructor
	 * 
	 * @param arg0
	 *            The parent Widget
	 * @param arg1
	 *            The style of the this wirdget
	 */
	public ChatContainer(Composite arg0, int arg1) {
		super(arg0, arg1);

		irc = new ChatItem(this, SWT.None, null);

		this.createShellProperties();
	}

	/**
	 * Dispose all the resources used by this class
	 */
	public void dispose() {
		irc.dispose();

		for (int i = 0; i < chats.size(); i++)
			chats.get(i).dispose();

		super.dispose();

	}

	/**
	 * Display messages on the public chat
	 * 
	 * @param msg
	 *            The Messages to be displayed
	 */
	public void openIRCMessages(Message[] msg) {
		irc.appendMessages(msg);
	}

	/**
	 * Display messages in separate tabs as private chats
	 * 
	 * @param msg
	 *            The messages to be displayed
	 */
	public void openPrivateMessages(Message[] msg) {
		for (int i = 0; i < msg.length; i++) {
			ChatItem item;

			item = (msg[i].staff.getId() == LoggedUser.getInstance().getId()) ? openPrivateChat(msg[i].dest, false)
					: openPrivateChat(msg[i].staff, true);

			item.appendMessage(msg[i]);
		}
	}

	/**
	 * Get the private chat tab for a staff member, or open a new one if it is not opened
	 * 
	 * @param staff
	 *            The staff member with who to chat
	 * @return A ChatItem instance
	 */
	public ChatItem openPrivateChat(StaffMember staff, boolean playSound) {
		if (playSound)
			SoundPlayer.playChatSound();

		for (ChatItem item : chats) {

			if (item.getStaff().getId() == staff.getId()) {

				if (!item.getStaff().equals(staff))
					item.setStaff(staff);

				return item;
			}

		}

		// open a new chat in case no item is found for the selected staff member
		ChatItem item = new ChatItem(this, SWT.BORDER | SWT.CLOSE, staff);
		this.setSelection(item);

		chats.add(item);

		return item;
	}

	/**
	 * Close all the private chats that have been disposed
	 */
	public void closePrivateChats() {
		for (int i = 0; i < chats.size(); i++)
			if (chats.get(i).isDisposed())
				chats.remove(i);
	}

	/**
	 * Set the properties for this widget
	 */
	private void createShellProperties() {
		GridLayout layout = new GridLayout();

		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = true;

		this.setLayout(layout);

	}

}
