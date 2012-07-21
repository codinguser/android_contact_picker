/*
* Copyright (C) 2011 by Ngewi Fet <ngewif@gmail.com>
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
 */

package com.codinguser.android.contactpicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ContactsPickerActivity extends FragmentActivity implements OnContactSelectedListener {
    public static final String SELECTED_CONTACT_ID 	= "contact_id";
	public static final String KEY_PHONE_NUMBER 	= "phone_number";
	public static final String KEY_CONTACT_NAME 	= "contact_name";

	/**
	 * Starting point
	 * Loads the {@link ContactsListFragment} 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		FragmentManager 		fragmentManager 	= this.getSupportFragmentManager();
		FragmentTransaction 	fragmentTransaction = fragmentManager.beginTransaction();
		ContactsListFragment 	fragment 			= new ContactsListFragment();
		
		fragmentTransaction.add(R.id.fragment_container, fragment);
		fragmentTransaction.commit();
	}

	/** 
	 * Callback when the contact is selected from the list of contacts.
	 * Loads the {@link ContactDetailsFragment} 
	 */
	@Override
	public void onContactNameSelected(long contactId) {
		/* Now that we know which Contact was selected we can go to the details fragment */
		
		Fragment 	detailsFragment = new ContactDetailsFragment();
		Bundle 		args 			= new Bundle();
		args.putLong(ContactsPickerActivity.SELECTED_CONTACT_ID, contactId);
		detailsFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		// Replace whatever is in the fragment_container view with this fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.fragment_container, detailsFragment);
		
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}

	/** 
	 * Callback when the contact number is selected from the contact details view 
	 * Sets the activity result with the contact information and finishes
	 */
	@Override
	public void onContactNumberSelected(String contactNumber, String contactName) {
		Intent intent = new Intent();
		intent.putExtra(KEY_PHONE_NUMBER, contactNumber);
		intent.putExtra(KEY_CONTACT_NAME, contactName);
		
        setResult(RESULT_OK, intent);
        finish();
	}
		
}