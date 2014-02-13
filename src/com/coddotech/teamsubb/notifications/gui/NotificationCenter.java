package com.coddotech.teamsubb.notifications.gui;

public interface NotificationCenter {

	abstract void dispplayMessage(String title, String text, int ICON);

	abstract boolean displayYesNoQuestion(String title, String text);

	public abstract void areadyRunning();

	public abstract void fatalError();

	public abstract void incorrectUserOrPass();

	public abstract void emptyFields();

	public abstract void jobPushSuccess();

	public abstract void jobPushError();

	public abstract void jobEndError();

	public abstract void jobAcceptSuccess();

	public abstract void jobAcceptError();

	public abstract void jobCancelError();

	public abstract void jobCreateSuccess();

	public abstract void jobCreateError();

	public abstract void fontsAddSuccess();

	public abstract void fontsAddError();

	public abstract void invalidSubFile();

	public abstract void numberOutOfBounds();

	public abstract void numberFormatError();

	public abstract void saveSettingsSuccess();

	public abstract void saveSettingsError();

	public abstract void connectionError();

	public abstract void messageSendError();

	public abstract boolean unsavedChanges();

}
