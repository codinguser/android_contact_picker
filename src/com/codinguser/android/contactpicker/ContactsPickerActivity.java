package com.codinguser.android.contactpicker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class ContactsPickerActivity extends FragmentActivity implements OnContactSelectedListener{
    public static final String SELECTED_CONTACT_ID = "contact_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		ContactsListFragment fragment = new ContactsListFragment();
		fragmentTransaction.add(R.id.fragment_container, fragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onContactNameSelected(long contactId) {
		// move to the details fragment
		
		Fragment detailsFragment = new ContactDetailsFragment();
		Bundle args = new Bundle();
		args.putLong(ContactsPickerActivity.SELECTED_CONTACT_ID, contactId);
		detailsFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the fragment_container view with this fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.fragment_container, detailsFragment);
		
		transaction.addToBackStack(null);
		
		transaction.setCustomAnimations(R.anim.slide_in_right, android.R.anim.slide_out_right);
		// Commit the transaction
		transaction.commit();
	}

	@Override
	public void onContactNumberSelected(String contactNumber) {
		// TODO Do what you will with the selected phone number
		Toast.makeText(this, contactNumber + " selected", Toast.LENGTH_SHORT).show();
	}
}