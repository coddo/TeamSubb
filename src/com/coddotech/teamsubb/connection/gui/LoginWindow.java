package com.coddotech.teamsubb.connection.gui;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.notifications.gui.PopUpMessages;

/**
 * This class representes the login interface for the application. <br>
 * Here, the user can connect to his own account in order to retrieve his/her
 * user data, communicate with other staff members and perform different jobs
 * based on his/her qualifications
 * 
 * @author Coddo
 */
public class LoginWindow extends CustomWindow {

	private LoginController controller;

	private Label userLabel;
	private Label passLabel;
	private Text userBox;
	private Text passBox;
	private Button exitButton;
	private Button loginButton;
	private Button automaticLogin;

	/**
	 * Class constructor
	 */
	public LoginWindow() {
		super();

		this.initializeComponents();

	}

	@Override
	public void dispose() {
		try {
			// user classes
			controller.dispose();
			controller = null;

			// GUI objects
			userLabel.dispose();
			userLabel = null;

			passLabel.dispose();
			passLabel = null;

			userBox.dispose();
			userBox = null;

			passBox.dispose();
			passBox = null;

			exitButton.dispose();
			exitButton = null;

			loginButton.dispose();
			loginButton = null;

			automaticLogin.dispose();
			automaticLogin = null;

			this.logDispose();

		}
		catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	/**
	 * Retrieve the username that has been entered by the user
	 * 
	 * @return A String representing the user's registered name
	 */
	public String getUserName() {
		return this.userBox.getText();
	}

	/**
	 * Retrieve the password that has been entered by the user
	 * 
	 * @return A String representing the user's login password
	 */
	public String getPassword() {
		return this.passBox.getText();
	}

	public boolean isAutomaticLogin() {
		return this.automaticLogin.getSelection();
	}

	@Override
	protected void updateGUI(final Observable obs, final Object obj) {

		Runnable update = new Runnable() {

			@Override
			public void run() {

				if (!(boolean) obj) {
					PopUpMessages.getInstance().incorrectUserOrPass();

				}

				else {
					// On successful login, close this windows
					close();
				}
			}
		};

		Display.getDefault().syncExec(update);

	}

	@Override
	protected void performInitializations() {
		controller = new LoginController(this);

		userLabel = new Label(this.getShell(), SWT.None);
		passLabel = new Label(this.getShell(), SWT.None);
		userBox = new Text(this.getShell(), SWT.BORDER);
		passBox = new Text(this.getShell(), SWT.PASSWORD | SWT.BORDER | SWT.SINGLE);
		exitButton = new Button(this.getShell(), SWT.PUSH);
		loginButton = new Button(this.getShell(), SWT.PUSH);
		automaticLogin = new Button(this.getShell(), SWT.CHECK);
	}

	@Override
	protected void createObjectProperties() {
		userLabel.setFont(DEFAULT_FONT);
		userLabel.setText("User name:");
		userLabel.setLocation(10, 10);
		userLabel.pack();

		passLabel.setFont(DEFAULT_FONT);
		passLabel.setText("Password:");
		passLabel.setLocation(10, 45);
		passLabel.pack();

		userBox.setFont(DEFAULT_FONT);
		userBox.setLocation(100, 10);
		userBox.setSize(175, 23);

		passBox.setFont(DEFAULT_FONT);
		passBox.setLocation(100, 43);
		passBox.setSize(175, 23);

		automaticLogin.setFont(DEFAULT_FONT);
		automaticLogin.setLocation(35, 75);
		automaticLogin.setText("Automatically login at startup");
		automaticLogin.pack();

		loginButton.setFont(DEFAULT_FONT);
		loginButton.setText("Login");
		loginButton.setLocation(225, 100);
		loginButton.setSize(50, 25);

		exitButton.setFont(DEFAULT_FONT);
		exitButton.setText("Exit");
		exitButton.setLocation(10, 100);
		exitButton.setSize(50, 25);
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("Login into your account");
		this.getShell().setSize(290, 160);
		this.placeToCenter();
	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);

		userBox.addListener(SWT.Traverse, controller.keyPressed);
		passBox.addListener(SWT.Traverse, controller.keyPressed);
		exitButton.addSelectionListener(controller.exitButtonPressed);
		loginButton.addSelectionListener(controller.loginButtonPressed);
	}
}