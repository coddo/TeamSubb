package com.coddotech.teamsubb.jobs.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.jobs.model.FontsManager;
import com.coddotech.teamsubb.jobs.model.Job;
import com.coddotech.teamsubb.main.CustomController;

/**
 * Class for managing font files
 * 
 * @author Coddo
 * 
 */
public class FontsController extends CustomController {

	private FontsManager model;

	private Job job;

	private FontsWindow view;

	private FileDialog browseFonts;

	private List<File> deleted;

	public boolean applied = false;

	public FontsController(FontsWindow view) {
		this.view = view;

		model = new FontsManager();
		model.addObserver(view);

		browseFonts = new FileDialog(view.getShell(), SWT.OPEN | SWT.MULTI);
		browseFonts.setText("Select font files");

		deleted = new ArrayList<File>();
	}

	public void dispose() {

		model.deleteObserver(view);

		// delete all the marked files from the job's directory if the settings have been applied
		try {

			if (applied) {

				for (int i = 0; i < deleted.size(); i++) {

					if (deleted.get(i).exists())
						deleted.get(i).delete();

				}

			}

			deleted.clear();
			deleted = null;

			browseFonts = null;

			this.logDispose();

		}

		catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	public void setJob(Job job) {
		this.job = job;
	}

	/**
	 * Listener for when the browse fonts button is clicked. This opens the file
	 * dialog for selecting the files that are going to be used
	 */
	public SelectionListener browseClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			browseFonts.open();

			String[] files = browseFonts.getFileNames();

			for (int i = 0; i < files.length; i++) {
				files[i] = browseFonts.getFilterPath() + File.separator + files[i];
			}

			view.appendFonts(files);

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	public SelectionListener doneClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {

			// copy the fonts to the location of the job
			model.addCustomFonts(view.getFonts(), job);

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

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

	/**
	 * Listener for when keys are pressed in the fonts list. If the delete key
	 * is pressed, then remove all the selected items from that list
	 */
	public KeyListener fontsKeyListener = new KeyListener() {

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.DEL) {

				for (String font : view.getSelectedFonts())
					deleted.add(new File(font));

				view.deleteSelectedFonts();
			}

		}
	};

	public Listener shellShownListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.setJobName(job.getName());

			if (job.getAddedFonts() != null) {

				String[] fonts = new String[job.getAddedFonts().length];

				for (int i = 0; i < fonts.length; i++) {
					fonts[i] = job.getAddedFonts()[i].getAbsolutePath();

				}

				view.appendFonts(fonts);
			}

		}
	};

	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.dispose();

		}
	};
}
