package com.coddotech.teamsubb.chat.gui;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.coddotech.teamsubb.main.CustomWindow;

public class IRCWindow extends CustomWindow {

	StyledText chatBox;
	Text msgBox;
	Group composite;
	IRCController controller;

	public IRCWindow() {

	}

	@Override
	public void dispose() {
		chatBox.dispose();
		msgBox.dispose();
		composite.dispose();
		controller.dispose();

	}

	@Override
	protected void updateGUI(Observable obs, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void performInitializations() {
		controller = new IRCController(this);

		chatBox = new StyledText(this.getShell(), SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.WRAP);
		composite = new Group(getShell(), SWT.BORDER);
		msgBox = new Text(this.getShell(), SWT.BORDER | SWT.H_SCROLL | SWT.SINGLE);

	}

	@Override
	protected void createObjectProperties() {
		chatBox.setFont(CustomWindow.DEFAULT_FONT);
		chatBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		composite.setFont(CustomWindow.DEFAULT_FONT);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		composite.setText("afgasfasf");

		msgBox.setFont(CustomWindow.DEFAULT_FONT);
		msgBox.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1));

	}

	@Override
	protected void createShellProperties() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = true;

		this.getShell().setLayout(layout);
		this.getShell().setSize(800, 600);
		this.placeToCenter();

	}

	@Override
	protected void createListeners() {
		this.getShell().addListener(SWT.Close, controller.shellClosingListener);

		this.msgBox.addListener(SWT.Traverse, controller.keyPressed);

	}

}
