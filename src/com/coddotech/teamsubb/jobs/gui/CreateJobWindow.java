package com.coddotech.teamsubb.jobs.gui;

import java.io.File;
import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.chat.model.StaffManager;
import com.coddotech.teamsubb.jobs.model.Job;
import com.coddotech.teamsubb.main.CustomWindow;

public class CreateJobWindow extends CustomWindow {

	private CreateJobController controller;

	private Composite panel;

	private Label nameLabel;
	private Label typeLabel;
	private Label commentsLabel;
	private Label nextStaffLabel;
	private Label subLabel;
	private Label fontsLabel;
	private Label torrentLabel;

	private Text name;
	private Text comments;
	private Text sub;
	private Text torrent;

	private Combo type;
	private Combo nextStaff;

	private List fonts;

	private Button browseSubButton;
	private Button browseFontsButton;

	private Button create;
	private Button cancel;

	/**
	 * Class constructor
	 */
	public CreateJobWindow() {
		this.setShell(new Shell(Display.getDefault(), SWT.SHELL_TRIM));

		this.initializeComponents();
	}

	@Override
	public void dispose() {
		try {

			controller.dispose();

			nameLabel.dispose();
			name.dispose();

			typeLabel.dispose();
			type.dispose();

			torrentLabel.dispose();
			torrent.dispose();

			commentsLabel.dispose();
			comments.dispose();

			nextStaffLabel.dispose();
			nextStaff.dispose();

			subLabel.dispose();
			sub.dispose();

			fontsLabel.dispose();
			fonts.dispose();

			browseSubButton.dispose();
			browseFontsButton.dispose();

			create.dispose();
			cancel.dispose();

			panel.dispose();

			this.logDispose();

		}
		catch (Exception ex) {
			this.logDiposeFail(ex);

		}
	}

	/**
	 * Get the job name entered by the user
	 * 
	 * @return A String value
	 */
	public String getName() {
		return this.name.getText();
	}

	/**
	 * Get the job type entered by the user
	 * 
	 * @return An integer value
	 */
	public int getType() {
		return this.type.getSelectionIndex();
	}

	/**
	 * Get the job comments/description entered by the user
	 * 
	 * @return A String value
	 */
	public String getComments() {
		return this.comments.getText();
	}

	/**
	 * Get the next staff member selected by the user
	 * 
	 * @return A String value
	 */
	public String getNextStaff() {
		return this.nextStaff.getText().split(":")[0];
	}

	/**
	 * Get the link to the torrent that is used for the job
	 * 
	 * @return A String value
	 */
	public String getTorrentLink() {
		return this.torrent.getText();
	}

	/**
	 * Get the sub file selected by the user
	 * 
	 * @return A String value representing the absolute path to the file
	 */
	public String getSub() {
		return this.sub.getText();
	}

	/**
	 * Set the sub file to be used
	 * 
	 * @param sub
	 *            The absolute path to the file
	 */
	public void setSub(String sub) {
		this.sub.setText(sub);
	}

	/**
	 * Get the font files to be used for the job
	 * 
	 * @return A String collection containing the absolute paths to the files
	 */
	public String[] getFonts() {
		return this.fonts.getItems();
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
	 * Checks all the fields containing the job data for arguments that are invalid or emprty
	 * 
	 * @return A logical value indicating if the fields are ok or not
	 */
	public boolean verifyFields() {
		MessageBox message = new MessageBox(this.getShell(), SWT.ICON_ERROR);
		message.setText("Empty fields");
		message.setMessage("There cannot be any empty fields except the font list and comments field!");

		boolean empty = false;

		if (name.getText() == null || name.getText().equals(""))
			empty = true;

		if (type.getText() == null || type.getText().equals(""))
			empty = true;

		if (torrent.getText() == null || torrent.getText().equals(""))
			empty = true;

		if (nextStaff.getText() == null || nextStaff.getText().equals(""))
			empty = true;

		if (sub.getText() == null || sub.getText().equals(""))
			empty = true;

		if (empty) {
			message.open();

			return false;
		}

		else if (!new File(this.sub.getText()).exists()) {

			message.setText("Invalid sub file");
			message.setText("The entered sub file doesn't exist or it is corrupted !");
			message.open();

			return false;
		}

		return true;
	}

	/**
	 * Select all the items of the font list
	 */
	public void selectEntireFontList() {
		this.fonts.selectAll();
	}

	/**
	 * Deletes all the fonts that are selected in the list
	 */
	public void deleteSelectedFonts() {
		this.fonts.remove(this.fonts.getSelectionIndices());
	}

	@Override
	protected void updateGUI(final Observable obs, final Object obj) {

		Runnable update = new Runnable() {

			@Override
			public void run() {
				String[] data = obj.toString().split(CustomWindow.NOTIFICATION_SEPARATOR);

				if (data[0].equals("create")) {
					MessageBox message;

					if (Boolean.parseBoolean(data[1])) {
						message = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
						message.setText("Success");
						message.setMessage("The job has been successfully created");

						message.open();

						close();
					}

					else {
						message = new MessageBox(getShell(), SWT.ICON_ERROR);
						message.setText("Error");
						message.setMessage("There was a problem while creating this job");

						message.open();
					}

				}
			}
		};

		Display.getDefault().syncExec(update);

	}

	@Override
	protected void performInitializations() {
		controller = new CreateJobController(this);

		panel = new Composite(this.getShell(), SWT.BORDER);

		nameLabel = new Label(panel, SWT.None);
		name = new Text(this.panel, SWT.BORDER);

		typeLabel = new Label(panel, SWT.None);
		type = new Combo(panel, SWT.READ_ONLY);

		torrentLabel = new Label(panel, SWT.None);
		torrent = new Text(panel, SWT.BORDER);

		commentsLabel = new Label(panel, SWT.None);
		comments = new Text(panel, SWT.BORDER);

		nextStaffLabel = new Label(panel, SWT.None);
		nextStaff = new Combo(panel, SWT.READ_ONLY);

		subLabel = new Label(panel, SWT.None);
		sub = new Text(panel, SWT.BORDER);

		browseSubButton = new Button(panel, SWT.PUSH);

		fontsLabel = new Label(panel, SWT.None);
		fonts = new List(panel, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		browseFontsButton = new Button(panel, SWT.PUSH);

		create = new Button(this.getShell(), SWT.PUSH);
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

		typeLabel.setFont(CustomWindow.DEFAULT_FONT);
		typeLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		typeLabel.setText("Job type:");
		typeLabel.pack();

		type.setFont(CustomWindow.DEFAULT_FONT);
		type.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		for (String jobType : Job.DEFAULT_JOB_TYPES)
			type.add(jobType);
		type.remove(type.getItemCount() - 1); // remove last item ("end" type)
		type.select(0); //make the first item selected by default
		
		torrentLabel.setFont(CustomWindow.DEFAULT_FONT);
		torrentLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		torrentLabel.setText("Torrent link:");
		torrentLabel.pack();

		torrent.setFont(CustomWindow.DEFAULT_FONT);
		torrent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		commentsLabel.setFont(CustomWindow.DEFAULT_FONT);
		commentsLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		commentsLabel.setText("Comments: ");
		commentsLabel.pack();

		comments.setFont(CustomWindow.DEFAULT_FONT);
		comments.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		nextStaffLabel.setFont(CustomWindow.DEFAULT_FONT);
		nextStaffLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		nextStaffLabel.setText("Next staff member:");
		nextStaffLabel.pack();

		nextStaff.setFont(CustomWindow.DEFAULT_FONT);
		nextStaff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		nextStaff.add(Job.DEFAULT_NEXT_STAFF);
		nextStaff.setItems(StaffManager.fetchFormatedStaffList(false));
		nextStaff.add(Job.DEFAULT_NEXT_STAFF, 0);
		nextStaff.select(0);

		subLabel.setFont(CustomWindow.DEFAULT_FONT);
		subLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		subLabel.setText("Sub file:");
		subLabel.pack();

		sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		sub.setFont(CustomWindow.DEFAULT_FONT);

		browseSubButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		browseSubButton.setText("...");
		browseSubButton.pack();

		fontsLabel.setFont(CustomWindow.DEFAULT_FONT);
		fontsLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		fontsLabel.setText("Font files:");
		fontsLabel.pack();

		fonts.setFont(CustomWindow.DEFAULT_FONT);
		fonts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		browseFontsButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		browseFontsButton.setText("...");
		browseFontsButton.pack();

		create.setFont(CustomWindow.DEFAULT_FONT);
		create.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));
		create.setText("Create job");
		create.pack();

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
		this.getShell().setText("Create a new job");
		this.getShell().setSize(800, 600);
		this.placeToCenter();

	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);

		cancel.addSelectionListener(controller.cancelClicked);
		create.addSelectionListener(controller.createClicked);

		browseSubButton.addSelectionListener(controller.browseSubButtonClicked);
		browseFontsButton.addSelectionListener(controller.browseFontsButtonClicked);

		fonts.addKeyListener(controller.fontsKeyListener);
	}
}
