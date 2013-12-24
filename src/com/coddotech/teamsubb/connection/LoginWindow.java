package com.coddotech.teamsubb.connection;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.main.CustomWindow;

/**
 * This class representes the login interface for the application. <br>
 * Here, the user can connect to his own account in order to retrieve his/her
 * user data, communicate with other staff members and perform different jobs
 * based on his/her qualifications
 * 
 * @author Coddo
 * 
 */
public class LoginWindow extends CustomWindow implements Observer {

	private Font defaultFont;

	private LoginController controller;

	private Label userLabel;
	private Label passLabel;
	private Text userBox;
	private Text passBox;
	private Button exitButton;
	private Button loginButton;

	/**
	 * Class constructor
	 */
	public LoginWindow() {
		super();
		
		this.initializeComponents();
	}

	/**
	 * Clear the memory from this class and its resources
	 */
	void dispose() {
		//user classes
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

		defaultFont.dispose();
		defaultFont = null;
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

	/**
	 * Update the GUI based on the modifications that took place in the
	 * registered models
	 */
	@Override
	public void update(Observable obs, Object obj) {
		if (!(boolean) obj) {
			// On failed login, show the "wrong credentials" message
			MessageBox message = new MessageBox(getShell(), SWT.ICON_ERROR);
			message.setMessage("The entered username or password is incorrect");
			message.setText("Wrong credentials");
			message.open();
		} else {
			// On successful login, close this windows
			this.close();
		}
	}

	@Override
	protected void performInitializations() {
		controller = new LoginController(this);

		defaultFont = new Font(Display.getCurrent(), "Calibri", 12, SWT.NORMAL);

		userLabel = new Label(this.getShell(), SWT.None);
		passLabel = new Label(this.getShell(), SWT.None);
		userBox = new Text(this.getShell(), SWT.BORDER);
		passBox = new Text(this.getShell(), SWT.PASSWORD | SWT.BORDER
				| SWT.SINGLE);
		exitButton = new Button(this.getShell(), SWT.PUSH);
		loginButton = new Button(this.getShell(), SWT.PUSH);
	}

	@Override
	protected void createObjectProperties() {
		userLabel.setFont(defaultFont);
		userLabel.setText("User name:");
		userLabel.setLocation(10, 10);
		userLabel.pack();

		passLabel.setFont(defaultFont);
		passLabel.setText("Password:");
		passLabel.setLocation(10, 60);
		passLabel.pack();

		userBox.setFont(defaultFont);
		userBox.setLocation(100, 10);
		userBox.setSize(175, 23);

		passBox.setFont(defaultFont);
		passBox.setLocation(100, 60);
		passBox.setSize(175, 23);

		loginButton.setFont(defaultFont);
		loginButton.setText("Login");
		loginButton.setLocation(225, 100);
		loginButton.setSize(50, 25);

		exitButton.setFont(defaultFont);
		exitButton.setText("Exit");
		exitButton.setLocation(10, 100);
		exitButton.setSize(50, 25);
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("Login into your account");
		this.getShell().setSize(295, 165);
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