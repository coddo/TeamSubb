package com.coddotech.teamsubb.connection;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class LoginController {
	
	Login model;
	LoginWindow view;
	
	public LoginController(LoginWindow view) {
		//set the view and the model for this controller
		model = new Login();
		this.view = view;
		
		//add the view as an observer for the model
		model.addObserver(view);
	}
	
	/**
	 * Clears the memory from this class and its controls
	 */
	public void dispose() {
		model.deleteObserver(view);
		
		model = null;
		view = null;
	}

	/*
	 * Listener for the exit button. When pressed, it quits the application
	 */
	public SelectionListener exitButtonPressed = new SelectionListener() {
	
		@Override
		public void widgetSelected(SelectionEvent e) {
			view.close();
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
	public SelectionListener loginButtonPressed = new SelectionListener() {
	
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (view.isConnected())
				model.doLogin(view.getUserName(), view.getPassword());
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
	public Listener keyPressed = new Listener() {
	
		@Override
		public void handleEvent(Event e) {
			if (e.detail == org.eclipse.swt.SWT.TRAVERSE_RETURN)
				if (view.isConnected())
				model.doLogin(view.getUserName(), view.getPassword());
		}
		
	};
	/**
	 * Listens for when the shell (GUI) closes and clears memory from this class
	 * and its resources
	 */
	public Listener shellClosingListener = new Listener() {
		
		@Override
		public void handleEvent(Event arg0) {
			view.dispose();
		}
		
	};
}
