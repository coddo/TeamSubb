package com.coddotech.teamsubb.chat.model;

import org.eclipse.swt.graphics.Color;

import com.coddotech.teamsubb.chat.gui.IRCWindow;

/**
 * Class representing a staff member from the server.<br>
 * 
 * This class extends the User class.
 * 
 * @author coddo
 * 
 */
public class StaffMember extends User {

	private boolean online = false;

	public StaffMember() {
		this.setJobsArrayStartIndex(4);
	}

	@Override
	protected void createUserInfo(String[] data) {
		this.setId(Integer.parseInt(data[0]));

		this.setName(data[1]);
		this.setEmail(data[2]);
		this.setRank(User.DEFAULT_USER_RANKS[Integer.parseInt(data[3])]);
	}

	/**
	 * Verify the online status of this staff member
	 * 
	 * @return A logical value
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * Set the online status for this staff member
	 * 
	 * @param online
	 *            A Logical value
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * Get the Color representing a user, based on his/her rank.
	 * 
	 * @param user
	 *            A StaffMember instance
	 * @return A Color instance
	 */
	public static Color getRankColor(StaffMember user) {
		if (user == null)
			return IRCWindow.COLOR_SYSTEM;

		if (user.isFondator())
			return IRCWindow.COLOR_FONDATOR;

		else if (user.isAdmin())
			return IRCWindow.COLOR_ADMIN;

		else if (user.isModerator())
			return IRCWindow.COLOR_MODERATOR;

		else
			return IRCWindow.COLOR_MEMBER;
	}

}
