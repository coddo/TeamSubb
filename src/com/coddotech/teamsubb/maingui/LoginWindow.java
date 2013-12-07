package com.coddotech.teamsubb.maingui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.connection.ConnectionManager;

/**
 * 
 * 
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
	
	/**
	 * Class constructor
	 */
	public LoginWindow(){
		createContents();
	}
	
	/**
	 * Initializez the components for this class
	 */
	private void createContents(){
		// initializations
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
		userBox.setSize(175, 21);
		
		passBox.setFont(defaultFont);
		passBox.setLocation(100, 60);
		passBox.setSize(userBox.getSize());
		
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
		exitButton.addSelectionListener(exitButtonPressed);
		loginButton.addSelectionListener(loginButtonPressed);
	}
	
	/**
	 * Listens for when the shell (GUI) closes and
	 * clears memory from this class and its resources
	 */
	Listener shellClosingListener = new Listener(){
		public void handleEvent(Event arg0) {
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
	};
	
	/*
	 * Listener for the exit button.
	 * When pressed, it quits the application
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
	 * Listener for the login button.
	 * When pressed, it sends a login request to the server
	 * and waits for a response
	 */
	SelectionListener loginButtonPressed = new SelectionListener() {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			if (!ConnectionManager.sendLoginRequest("", "")) {
				//show message box
			} else {
				
			}
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			
		}
		
	};
	
}
