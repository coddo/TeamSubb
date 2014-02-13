package com.coddotech.teamsubb.chat.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.appmanage.model.ActivityLogger;
import com.coddotech.teamsubb.chat.model.Messaging;
import com.coddotech.teamsubb.chat.model.StaffMember;
import com.coddotech.teamsubb.main.CustomWindow;
import com.coddotech.teamsubb.main.Widget;

public class TextFields extends Composite implements Widget {

	private StaffMember staff;

	public StyledText text;
	public Text write;

	private TextFields(Composite arg0, int arg1) {
		super(arg0, arg1);

		initializeComponents();
	}

	public TextFields(Composite c, int style, StaffMember staff) {
		this(c, style);

		this.staff = staff;
	}

	public void dispose() {
		text.dispose();
		write.dispose();

		super.dispose();
	}

	public void append(String text) {
		this.text.append(text);
		this.text.setSelection(this.text.getCharCount());
	}

	public void setStyleRange(StyleRange style) {
		this.text.setStyleRange(style);
	}

	public String getText() {
		return this.text.getText();
	}

	/**
	 * Initializez all the components that are used in this GUI
	 */
	private void initializeComponents() {

		// initializations
		try {
			this.performInitializations();

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "GUI Initialization", ex);

		}

		// object properties
		try {
			this.createObjectProperties();

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Create object properties", ex);

		}

		// shell properties
		try {
			this.createShellProperties();

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Create shell properties", ex);

		}

		// listeners
		try {
			this.createListeners();

		}
		catch (Exception ex) {
			ActivityLogger.logException(this.getClass().getName(), "Create listeners", ex);

		}

	}

	@Override
	public void performInitializations() {
		text = new StyledText(this, SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY | SWT.WRAP);
		write = new Text(this, SWT.BORDER | SWT.SINGLE);

	}

	@Override
	public void createObjectProperties() {
		text.setFont(CustomWindow.DEFAULT_FONT);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6));

		write.setFont(CustomWindow.DEFAULT_FONT);
		write.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
	}

	@Override
	public void createShellProperties() {
		GridLayout layout = new GridLayout();

		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = true;

		this.setLayout(layout);

		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@Override
	public void createListeners() {
		write.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					if (!CustomWindow.isConnected(true))
						return;

					Messaging.getInstance().sendChatMessage(staff, write.getText());
					write.setText("");
				}
			}
		});

	}

}
