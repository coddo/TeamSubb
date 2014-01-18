package com.coddotech.teamsubb.jobs.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.jobs.model.Job;
import com.coddotech.teamsubb.main.CustomWindow;

public class FontsWindow extends CustomWindow {

	private FontsController controller;

	private Composite panel;

	private Label nameLabel;
	private Text name;
	private Label fontsLabel;
	private List fonts;
	private Button done;
	private Button browse;
	private Button cancel;

	public FontsWindow(Job job) {
		this.setShell(new Shell(Display.getCurrent(), SWT.APPLICATION_MODAL
				| SWT.SHELL_TRIM));

		this.initializeComponents();

		controller.setJob(job);
	}

	@Override
	public void dispose() {
		try {
			controller.dispose();

			nameLabel.dispose();
			name.dispose();

			fontsLabel.dispose();
			fonts.dispose();

			done.dispose();
			browse.dispose();
			cancel.dispose();

			panel.dispose();

			this.logDispose();

		} catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	/**
	 * Get the font files to be used for the job
	 * 
	 * @return A String collection containing the absolute paths to the files
	 */
	public String[] getFonts() {
		return this.fonts.getItems();
	}

	public String[] getSelectedFonts() {
		return this.fonts.getSelection();
	}

	/**
	 * Append the font files to be used for the job to the list
	 * 
	 * @param fonts
	 *            A String collection with the absolute paths to the files
	 */
	public void appendFonts(String[] fonts) {
		for (String font : fonts) {
			this.fonts.add(font);
		}
	}

	/**
	 * Set the name to be displayed for this job instance
	 * 
	 * @param name
	 *            A String value
	 */
	public void setJobName(String name) {
		this.name.setText(name);
	}

	/**
	 * Deletes all the fonts that are selected in the list
	 */
	public void deleteSelectedFonts() {
		this.fonts.remove(this.fonts.getSelectionIndices());
	}

	public void notifyUser(boolean jobCompletion) {
		MessageBox message;

		if (jobCompletion) {
			message = new MessageBox(this.getShell(), SWT.ICON_INFORMATION);
			message.setText("Success");
			message.setMessage("The fonts have been added to the job !");

			message.open();

			this.close();
		} else {
			message = new MessageBox(this.getShell(), SWT.ICON_ERROR);
			message.setText("Error");
			message.setMessage("An error has occured while adding the fonts to the job");

			message.open();
		}
	}

	@Override
	protected void performInitializations() {
		controller = new FontsController(this);

		panel = new Composite(this.getShell(), SWT.BORDER);

		nameLabel = new Label(panel, SWT.None);
		name = new Text(this.panel, SWT.READ_ONLY);

		fontsLabel = new Label(panel, SWT.None);
		fonts = new List(panel, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
				| SWT.H_SCROLL);

		browse = new Button(panel, SWT.PUSH);

		done = new Button(this.getShell(), SWT.PUSH);
		cancel = new Button(this.getShell(), SWT.PUSH);
	}

	@Override
	protected void createObjectProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;

		panel.setLayout(layout);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		panel.pack();

		nameLabel.setFont(CustomWindow.DEFAULT_FONT);
		nameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		nameLabel.setText("Job name:");
		nameLabel.pack();

		name.setFont(CustomWindow.DEFAULT_FONT);
		name.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		fontsLabel.setFont(CustomWindow.DEFAULT_FONT);
		fontsLabel
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		fontsLabel.setText("Font files:");
		fontsLabel.pack();

		fonts.setFont(CustomWindow.DEFAULT_FONT);
		fonts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		browse.setFont(CustomWindow.DEFAULT_FONT);
		browse.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1,
				1));
		browse.setText("...");
		browse.pack();

		done.setFont(CustomWindow.DEFAULT_FONT);
		done.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));
		done.setText("Set fonts");
		done.pack();

		cancel.setFont(CustomWindow.DEFAULT_FONT);
		cancel.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		cancel.setText("Cancel");
		cancel.pack();
	}

	@Override
	protected void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;

		this.getShell().setLayout(layout);
		this.getShell().setText("Edit fonts");
		this.getShell().setSize(500, 350);
		this.placeToCenter();

	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);
		this.getShell().addListener(SWT.Show, controller.shellShownListener);

		fonts.addKeyListener(controller.fontsKeyListener);

		browse.addSelectionListener(controller.browseClicked);
		done.addSelectionListener(controller.doneClicked);
		cancel.addSelectionListener(controller.cancelClicked);
	}
}
