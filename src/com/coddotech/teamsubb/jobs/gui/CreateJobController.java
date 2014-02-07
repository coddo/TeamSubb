package com.coddotech.teamsubb.jobs.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import com.coddotech.teamsubb.jobs.model.FontsManager;
import com.coddotech.teamsubb.jobs.model.JobManager;
import com.coddotech.teamsubb.main.CustomController;
import com.coddotech.teamsubb.notifications.gui.PopUpMessages;

public class CreateJobController extends CustomController {

	private CreateJobWindow view;
	private JobManager model;

	private FileDialog browseSub;
	private FileDialog browseFonts;

	/**
	 * Class constructor
	 * 
	 * @param view
	 *            The view that uses this controller
	 */
	public CreateJobController(CreateJobWindow view) {
		this.view = view;
		model = JobManager.getInstance();
		model.addObserver(view);

		browseSub = new FileDialog(view.getShell(), SWT.OPEN);
		browseSub.setText("Select sub file");

		browseFonts = new FileDialog(view.getShell(), SWT.OPEN | SWT.MULTI);
		browseFonts.setText("Select font files");
	}

	/**
	 * Clear the memory from this class and its components
	 */
	public void dispose() {
		try {
			model.deleteObserver(this.view);

			browseSub = null;
			browseFonts = null;

			this.logDispose();

		}
		catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	/**
	 * Listener for when the browse sub button is clicked. This opens the file dialog for selecting
	 * a file which is going to be used.
	 */
	public SelectionListener browseSubButtonClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			String result = browseSub.open();

			if (result != null)
				view.setSub(result);

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the browse fonts button is clicked. This opens the file dialog for
	 * selecting the files that are going to be used
	 */
	public SelectionListener browseFontsButtonClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			String result = browseFonts.open();

			if (result != null) {
				String[] files = browseFonts.getFileNames();

				for (int i = 0; i < files.length; i++) {
					files[i] = browseFonts.getFilterPath() + File.separator + files[i];
				}

				view.appendFonts(files);
			}

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when keys are pressed in the fonts list. If the delete key is pressed, then
	 * remove all the selected items from that list
	 */
	public KeyListener fontsKeyListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {

			if (e.keyCode == SWT.DEL)
				view.deleteSelectedFonts();

			if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'a'))
				view.selectEntireFontList();

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the cancel button is clicked. This closes the window.
	 */
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
	 * Listener for when the create button is clicked. This checks for an internet connection, then
	 * it verifies the entered fields for invalid settings. <br>
	 * 
	 * If everything is ok, then it proceeds with creating the job and sending it to the server.
	 */
	public SelectionListener createClicked = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent arg0) {

			if (verifyFields()) {

				String[] fonts = FontsManager.excludeServerFontsAsStrings(view.getFonts());

				model.createJob(view.getName(), view.getType(), view.getComments(), view.getNextStaff(),
						view.getTorrentLink(), view.getSub(), fonts);
			}

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Listener for when the window is closing. This has the duty of disposing
	 * all the components used by this class.
	 */
	public Listener shellClosingListener = new Listener() {

		@Override
		public void handleEvent(Event arg0) {
			view.dispose();

		}
	};

	/**
	 * Checks all the fields containing the job data for arguments that are invalid or emprty
	 * 
	 * @return A logical value indicating if the fields are ok or not
	 */
	private boolean verifyFields() {
		boolean empty = false;

		String name = view.getName();
		String torrent = view.getTorrentLink();
		String sub = view.getSub();

		if (name == null || name.equals(""))
			empty = true;

		if (torrent == null || torrent.equals(""))
			empty = true;

		if (sub == null || sub.equals(""))
			empty = true;

		if (empty) {
			PopUpMessages.getInstance().emptyFields();

			return false;
		}

		else if (!new File(sub).exists()) {
			PopUpMessages.getInstance().invalidSubFile();

			return false;
		}

		return true;
	}
}