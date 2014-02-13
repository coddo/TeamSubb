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

public class ChatContainer extends CTabFolder {

	private ChatItem irc = null;

	private List<ChatItem> chats = new ArrayList<ChatItem>();

	public ChatContainer(Composite arg0, int arg1) {
		super(arg0, arg1);

		irc = new ChatItem(this, SWT.None, null);

		this.createShellProperties();
	}

	public void dispose() {
		irc.dispose();

		for (int i = 0; i < chats.size(); i++)
			chats.get(i).dispose();

		super.dispose();

	}

	public void openIRCMessages(Message[] msg) {
		irc.appendMessages(msg);
	}

	public void openPrivateMessages(Message[] msg) {
		for (int i = 0; i < msg.length; i++) {
			ChatItem item;

			item = (msg[i].staff.getId() == LoggedUser.getInstance().getId()) ? openPrivateChat(msg[i].dest)
					: openPrivateChat(msg[i].staff);

			item.appendMessage(msg[i]);
		}
	}

	public ChatItem openPrivateChat(StaffMember staff) {
		for (ChatItem item : chats) {

			if (item.getStaff().equals(staff)) {
				this.setSelection(item);

				return item;
			}

		}

		ChatItem item = new ChatItem(this, SWT.BORDER | SWT.CLOSE, staff);
		this.setSelection(item);

		chats.add(item);

		return item;
	}

	public void closePrivateChats() {
		for (int i = 0; i < chats.size(); i++)
			if (chats.get(i).isDisposed())
				chats.remove(i);
	}

	private void createShellProperties() {
		GridLayout layout = new GridLayout();

		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = true;

		this.setLayout(layout);

	}

}
