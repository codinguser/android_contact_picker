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

package com.codinguser.android.contactpicker.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.codinguser.android.contactpicker.ContactsPickerActivity;
import com.codinguser.android.contactpicker.OnContactSelectedListener;

/**
 * Extends the ContactsPickerActivity class from the library for demonstration purposes.
 * Instead of delivering an intent with the selected contact info, a toast is displayed
 */
public class ContactPickerSampleActivity extends ContactsPickerActivity implements OnContactSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getIntent().putExtra(ARG_PICKER_MODE, REQUST_CONTACT_EMAIL);
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null){
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
	}

	/**
	 * Callback when the contact number is selected from the contact details view 
	 * Sets the activity result with the contact information and finishes
	 */
	@Override
	 public void onContactNumberSelected(String contactNumber, String contactName) {
		Toast.makeText(this, String.format("Selected:\n %s: %s\nAn intent would be delivered to your app",
						contactName, contactNumber),
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onContactEmailSelected(String email, String contactName) {
		Toast.makeText(this, String.format("Selected:\n %s: <%s>\nAn intent would be delivered to your app",
						contactName, email),
				Toast.LENGTH_LONG).show();
	}
}