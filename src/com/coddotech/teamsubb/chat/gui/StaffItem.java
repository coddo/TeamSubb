package com.coddotech.teamsubb.chat.gui;

import org.eclipse.swt.widgets.Composite;


public class StaffItem extends Composite {

	StaffContainer parent;
	
	public StaffItem(Composite arg0, int arg1) {
		super(arg0, arg1);

		parent = (StaffContainer) arg0;
	}
	
	@Override
	public void dispose() {
		
		parent.removeItem(this);
		
		super.dispose();
	}

}
