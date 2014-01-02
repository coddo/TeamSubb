package com.coddotech.teamsubb.jobs;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

public class CreateJobController {

	private CreateJobWindow view;
	private JobManager model;
	
	private FileDialog browseSub;
	private FileDialog browseFonts;

	public CreateJobController(CreateJobWindow view) {
		this.view = view;
		
		browseSub = new FileDialog(view.getShell(), SWT.OPEN);
		browseSub.setText("Select sub file");
		
		browseFonts = new FileDialog(view.getShell(), SWT.OPEN | SWT.MULTI);
		browseFonts.setText("Select font files");
	}

	public void dispose() {
		model.deleteObserver(this.view);
		
		browseSub = null;
		browseFonts = null;
	}

	public void setModel(JobManager model) {
		this.model = model;
	}
	
	public SelectionListener browseSubButtonClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			browseSub.open();
			view.setSub(browseSub.getFilterPath() + File.separator + browseSub.getFileName());
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public SelectionListener browseFontsButtonClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			browseFonts.open();
			
			String[] files =browseFonts.getFileNames();
			
			for(int i = 0; i < files.length; i++) {
				files[i] = browseFonts.getFilterPath() + File.separator + files[i];
			}
			
			view.setFonts(files);
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public KeyListener fontsKeyListener = new KeyListener() {
		
		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.DEL)
				view.deleteSelectedFonts();
			
		}
	};
	
	public SelectionListener cancelClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			view.close();

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	public SelectionListener createClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			if (view.isConnected(true)) {
				if (view.verifFields())
					
					model.createJob(view.getName(), view.getType(),
							view.getComments(), view.getNextStaff(),
							view.getSub(), view.getFonts());
			}

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.dispose();

		}
	};

}