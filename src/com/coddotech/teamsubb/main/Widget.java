package com.coddotech.teamsubb.main;


public interface Widget {

	/**
	 * Dispose all the components for this class
	 */
	public abstract void dispose();

	/**
	 * Object initializations and instance creation
	 */
	abstract void performInitializations();

	/**
	 * Set the properties for all the components used in this GUI instance
	 */
	abstract void createObjectProperties();

	/**
	 * Set the shell properties for this GUI instance
	 */
	abstract void createShellProperties();

	/**
	 * Set the listeners that will be used in this GUI instance
	 */
	abstract void createListeners();
	
}
