package com.coddotech.teamsubb.jobs;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.jobs.JobWindow;
import com.coddotech.teamsubb.main.GadgetWindow;
import com.coddotech.teamsubb.settings.AppSettings;
import com.coddotech.teamsubb.settings.AppSettingsWindow;

/**
 * Controller class used by the JobWindow in order to complete the job actions
 * that are requested by the user
 * 
 * NOTE: THIS CLASS IS STILL INCOMPLETE. NOT ALL THE COMPONENTS HAVE BEEN
 * CREATED YET
 * 
 * @author Coddo
 * 
 */
public class JobController {

	private JobWindow view;
	private JobManager model;
	
	private AppSettings settings;
	private GadgetWindow mainWindow;

	public JobController(JobWindow view) {
		this.view = view;
	}

	public void dispose() {
		model.deleteObserver(this.view);
	}

	public void setModel(JobManager model) {
		this.model = model;
		this.model.addObserver(this.view);
	}
	
	public void setSettingsModel(AppSettings settings) {
		this.settings = settings;
	}
	
	public void setMainWindow(GadgetWindow gadget) {
		this.mainWindow = gadget;
	}

	public SelectionListener openSettingsClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			AppSettingsWindow set = new AppSettingsWindow();
			set.getController().setModel(settings);
			settings.addObserver(set);
			set.open();
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public SelectionListener closeWindowClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			view.close();
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public SelectionListener exitApplicationClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			view.close();
			mainWindow.close();
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public SelectionListener createJobClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			CreateJobWindow creator = new CreateJobWindow(model);
			model.addObserver(creator);
			creator.open();
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public SelectionListener refreshJobListClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			model.findJobs();
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public SelectionListener acceptJobClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public SelectionListener cancelJobClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public SelectionListener finishJobClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public SelectionListener endJobClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public SelectionListener aboutClicked = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public SelectionListener jobsListItemSelected = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			int ID = view.getSelectedJobID();
			
			if(ID != -1)
				model.notifyJobInformation(ID);
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public Listener shellShownListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			model.findJobs();
			
		}
		
	};
	
	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.dispose();
			
		}
		
	};
	
}
