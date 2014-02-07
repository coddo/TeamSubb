package com.coddotech.teamsubb.notifications.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class PopUpMessages implements NotificationCenter {

	private static PopUpMessages instance = null;

	public static PopUpMessages getInstance() {
		if (instance == null)
			instance = new PopUpMessages();

		return instance;
	}

	@Override
	public void dispplayMessage(String title, String text, int ICON) {
		Shell shell = new Shell(Display.getDefault(), ICON);

		MessageBox message = new MessageBox(shell);

		message.setText(title);
		message.setMessage(text);

		message.open();

		shell.dispose();

	}

	@Override
	public boolean displayYesNoQuestion(String title, String text) {
		Shell shell = new Shell(Display.getDefault(), SWT.ICON_QUESTION);

		MessageBox message = new MessageBox(shell);

		message.setText(title);
		message.setMessage(text);

		boolean result = message.open() == SWT.YES;

		shell.dispose();

		return result;
	}

	@Override
	public void areadyRunning() {
		String title = "Already running";
		String text = "TeamSubb is already running !\n\n Stop the currently active instance in order to start a new one.";

		this.dispplayMessage(title, text, SWT.ICON_CANCEL);
	}

	@Override
	public void fatalError() {
		String title = "TeamSubb";
		String text = "A FATAL ERROR has occured and the app has stopped working !";

		this.dispplayMessage(title, text, SWT.ICON_ERROR);

	}

	@Override
	public void incorrectUserOrPass() {
		String title = "Wrong credentials";
		String text = "The entered username or password is incorrect";

		this.dispplayMessage(title, text, SWT.ICON_CANCEL);

	}

	@Override
	public void emptyFields() {
		String title = "Empty fields";
		String text = "Starred fields cannot be empty !";

		this.dispplayMessage(title, text, SWT.ICON_CANCEL);

	}

	@Override
	public void jobPushSuccess() {
		String title = "Success";
		String text = "The job has been successfully sent back to the server !";

		this.dispplayMessage(title, text, SWT.ICON_INFORMATION);

	}

	@Override
	public void jobPushError() {
		String title = "Error";
		String text = "The job could not be finished !\n\n The server may have refused your request...";

		this.dispplayMessage(title, text, SWT.ICON_ERROR);

	}

	@Override
	public void jobEndError() {
		String title = "Error";
		String text = "There was an error while ending the job";

		this.dispplayMessage(title, text, SWT.ICON_ERROR);

	}

	@Override
	public void jobAcceptSuccess() {
		String title = "Success";
		String text = "The job has been successfully accepted !";

		this.dispplayMessage(title, text, SWT.ICON_INFORMATION);

	}

	@Override
	public void jobAcceptError() {
		String title = "Error";
		String text = "There was an error while accepting the job !";

		this.dispplayMessage(title, text, SWT.ICON_ERROR);

	}

	@Override
	public void jobCancelError() {
		String title = "Error";
		String text = "There was an error while cancelling the job !";

		this.dispplayMessage(title, text, SWT.ICON_ERROR);

	}

	@Override
	public void jobCreateSuccess() {
		String title = "Success";
		String text = "The job has been successfully created !";

		this.dispplayMessage(title, text, SWT.ICON_INFORMATION);

	}

	@Override
	public void jobCreateError() {
		String title = "Error";
		String text = "There was a problem while creating the job";

		this.dispplayMessage(title, text, SWT.ICON_ERROR);

	}

	@Override
	public void fontsAddSuccess() {
		String title = "Success";
		String text = "The fonts have been added to the job !";

		this.dispplayMessage(title, text, SWT.ICON_INFORMATION);

	}

	@Override
	public void fontsAddError() {
		String title = "Error";
		String text = "An error has occured while adding the fonts to the job";

		this.dispplayMessage(title, text, SWT.ICON_ERROR);

	}

	@Override
	public void invalidSubFile() {
		String title = "Invalid sub file";
		String text = "The entered sub file doesn't exist or it is corrupted !";

		this.dispplayMessage(title, text, SWT.ICON_CANCEL);

	}

	@Override
	public void numberOutOfBounds() {
		String title = "Number error";
		String text = "The entered number is out of bounds. The search interval must be an integer between 1 and 60";

		this.dispplayMessage(title, text, SWT.ICON_CANCEL);

	}

	@Override
	public void numberFormatError() {
		String title = "Format error";
		String text = "The search interval must be an INTEGER between 1 and 60 !";

		this.dispplayMessage(title, text, SWT.ICON_ERROR);

	}

	@Override
	public void saveSettingsSuccess() {
		String title = "Success";
		String text = "The settings have been successfully applied !";

		this.dispplayMessage(title, text, SWT.ICON_INFORMATION);

	}

	@Override
	public void saveSettingsError() {
		String title = "Error";
		String text = "An error has been encountered while saving the changes !";

		this.dispplayMessage(title, text, SWT.ICON_ERROR);

	}

	@Override
	public void connectionError() {
		String title = "Connection failed";
		String text = "A connection error has occured.\n\nPlease try again later...";
		
		this.dispplayMessage(title, text, SWT.ICON_ERROR);
		
	}

	@Override
	public boolean unsavedChanges() {
		String title = "Unsaved changes";
		String text = "The are unsaved changes !\n\n Do you want to save them now ?";

		return this.displayYesNoQuestion(title, text);
	}

}
