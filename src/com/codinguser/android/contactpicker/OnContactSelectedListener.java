package com.codinguser.android.contactpicker;

public interface OnContactSelectedListener {

	/**
	 * Callback when the contact is selected from the list of contacts
	 * @param contactId Long ID of the contact which was selected. 
	 */
	public void onContactNameSelected(long contactId);
	
	/**
	 * Callback when the contact number is selected from the contact details view
	 * @param contactNumber String representation of the number which was selected
	 */
	public void onContactNumberSelected(String contactNumber);
}
