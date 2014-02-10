package com.coddotech.teamsubb.chat.gui;

import org.eclipse.swt.widgets.Composite;

import com.coddotech.teamsubb.main.Widget;


public class StaffItem extends Composite implements Widget {

	private StaffContainer parent;
	
	public StaffItem(Composite arg0, int arg1) {
		super(arg0, arg1);
	
		parent = (StaffContainer) arg0;
	}

	@Override
	public void dispose() {
		
		parent.removeItem(this);
		
		super.dispose();
	}

	@Override
	public void performInitializations() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createObjectProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createShellProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createListeners() {
		// TODO Auto-generated method stub
		
	}

}
