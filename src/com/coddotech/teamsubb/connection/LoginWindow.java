package com.coddotech.teamsubb.connection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.jobs.UserInformation;
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
public final class LoginWindow extends CustomWindow {

	private Label userLabel;
	private Label passLabel;
	private Text userBox;
	private Text passBox;
	private Button exitButton;
	private Button loginButton;

	private Font defaultFont;

	private UserInformation userDetailsManager;

	private String mayContinue = "NO";

	public String getMayContinueValue() {
		return mayContinue;
	}

	/**
	 * Class constructor
	 */
	public LoginWindow() {
		super();
		initializeComponents();
	}

	/**
	 * Clear the memory from this class and its resources
	 */
	private void dispose() {
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

		userDetailsManager.dispose();
		userDetailsManager = null;
	}

	/**
	 * Set the fetched user information into the XML file containing the
	 * appriopriate data <br>
	 * <br>
	 * data[0] -> true/false <br>
	 * data[1] -> email <br>
	 * data[2] -> rank <br>
	 * data[3 -> n] -> job titles
	 * 
	 * @param data
	 *            A String collection containing the user information
	 */
	private void setData(String[] data) {
		// set the user's name, email and rank
		userDetailsManager.setUserName(userBox.getText());
		userDetailsManager.setUserEmail(data[1]);
		userDetailsManager.setUserRank(UserInformation.DEFAULT_USER_RANKS[Integer
				.parseInt(data[2])]);

		// set the user's jobs
		userDetailsManager.setUserJobs(data);
	}

	/**
	 * Start the login procesure
	 */
	private void doLogin() {
		boolean ok;
		String[] result = null;
		MessageBox message = new MessageBox(getShell(), SWT.ICON_ERROR);
		String resultMessage = ConnectionManager.sendLoginRequest(userBox.getText(),
				passBox.getText());
		
		if (!resultMessage.equals("error")) {
			// on successful connection, check if the login credentials are
			// correct or not
			result = resultMessage.split("&");

			if (Boolean.parseBoolean(result[0])) {
				ok = true;
			} else {
				message.setMessage("The entered username or password is incorrect");
				message.setText("Wrong credentials");
				ok = false;
			}
		} else { // on unsuccessful connection, display an error message
			message.setMessage("A connection error has occured.\nPlease try again later...");
			message.setText("Connection failed");
			ok = false;
		}

		if (!ok) { // if the login was unsuccessful, show the an message
			message.open();
		} else { // otherwise continue with starting the application's main
					// functionalities and close the login window
			setData(result);
			mayContinue = "OK";
			close();
		}
	}

	/**
	 * Listens for when the shell (GUI) closes and clears memory from this class
	 * and its resources
	 */
	Listener shellClosingListener = new Listener() {
		public void handleEvent(Event arg0) {
			dispose();
		}
	};

	/*
	 * Listener for the exit button. When pressed, it quits the application
	 */
	SelectionListener exitButtonPressed = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			close();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {

		}

	};

	/**
	 * Listener for the login button. When pressed, it sends a login request to
	 * the server and waits for a response. The response is then processed and
	 * the appropriate actions are taken
	 */
	SelectionListener loginButtonPressed = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			doLogin();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}

	};

	/**
	 * Listener for the text boxes in this window. Listens for when the "Enter"
	 * key is pressed in either of them and attempts to do a login with the
	 * entered data
	 * 
	 * Helps the user login faster by avoiding the login button push
	 */
	Listener keyPressed = new Listener() {

		public void handleEvent(Event e) {
			if (e.detail == SWT.TRAVERSE_RETURN)
				doLogin();
		}

	};

	/**
	 * Initializez the components for this class
	 */
	private void initializeComponents() {
		// initializations
		userDetailsManager = new UserInformation();

		defaultFont = new Font(Display.getCurrent(), "Calibri", 12, SWT.NORMAL);

		userLabel = new Label(this.getShell(), SWT.None);
		passLabel = new Label(this.getShell(), SWT.None);
		userBox = new Text(this.getShell(), SWT.BORDER);
		passBox = new Text(this.getShell(), SWT.PASSWORD | SWT.BORDER);
		exitButton = new Button(this.getShell(), SWT.PUSH);
		loginButton = new Button(this.getShell(), SWT.PUSH);

		// object properties
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

		// this window's properties
		getShell().setText("Login into your account");
		getShell().setSize(295, 165);
		this.placeToCenter();

		// listeners
		getShell().addListener(SWT.Close, shellClosingListener);
		userBox.addListener(SWT.Traverse, keyPressed);
		passBox.addListener(SWT.Traverse, keyPressed);
		exitButton.addSelectionListener(exitButtonPressed);
		loginButton.addSelectionListener(loginButtonPressed);
	}
}