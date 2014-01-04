package com.coddotech.teamsubb.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ApplicationInformation extends CustomWindow {

	private Font font;
	
	private Label applicationName;
	private Label type;
	private Label server;
	private Link serverLink;
	private Label author;
	private Label copyWright;
	
	public ApplicationInformation() {
		this.setShell(new Shell(Display.getCurrent(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM));
		
		this.initializeComponents();
	}
	
	public void dispose() {
		applicationName.dispose();
		type.dispose();
		server.dispose();
		serverLink.dispose();
		author.dispose();
		copyWright.dispose();
		
		font.dispose();
	}
	
	@Override
	protected void performInitializations() {
		font = new Font(Display.getCurrent(), "Calibri", 15, SWT.BOLD);
		
		applicationName = new Label(this.getShell(), SWT.None);
		type = new Label(this.getShell(), SWT.None);
		server = new Label(this.getShell(), SWT.None);
		serverLink = new Link(this.getShell(), SWT.Selection);
		author = new Label(this.getShell(), SWT.None);
		copyWright = new Label(this.getShell(), SWT.None);
		
	}

	@Override
	protected void createObjectProperties() {
		applicationName.setFont(font);
		applicationName.setLocation(100, 10);
		applicationName.setText("TeamSubb");
		applicationName.pack();
		
		type.setFont(CustomWindow.DEFAULT_FONT);
		type.setLocation(10, 40);
		type.setText("Type: Client application");
		type.pack();
		
		server.setFont(CustomWindow.DEFAULT_FONT);
		server.setLocation(10, 70);
		server.setText("Server: ");
		server.pack();
		
		author.setFont(CustomWindow.DEFAULT_FONT);
		author.setLocation(10, 100);
		author.setText("Author: Coddo");
		author.pack();
		
		copyWright.setFont(CustomWindow.DEFAULT_FONT);
		copyWright.setLocation(10, 130);
		copyWright.setText("Copywright: © CoddoTechnologies 2014");
		copyWright.pack();
		
		serverLink.setFont(CustomWindow.DEFAULT_FONT);
		serverLink.setLocation(60, 70);
		serverLink.setText("<a>www.anime4fun.ro</a>");
		serverLink.pack();
		
		serverLink.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Program.launch("www.anime4fun.ro");
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	protected void createShellProperties() {
		this.getShell().setText("About");
		this.getShell().setSize(300, 200);
		this.placeToCenter();
		
	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				dispose();
				
			}
		});
		
	}
	
}
