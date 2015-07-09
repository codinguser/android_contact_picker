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
 
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class ContactsListFragment extends ListFragment implements 
	LoaderCallbacks<Cursor>{

	private OnContactSelectedListener mContactsListener;
	private SimpleCursorAdapter mAdapter;
	private String mCurrentFilter = null;
	private EditText mSearchEditText;
	
	private static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
			Contacts._ID, 
			Contacts.DISPLAY_NAME, 
			Contacts.HAS_PHONE_NUMBER,
			Contacts.LOOKUP_KEY
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contacts_list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);
		
		getLoaderManager().initLoader(0, null, this);
		
		mAdapter = new IndexedListAdapter(
				this.getActivity(),
				R.layout.list_item_contacts,
				null,
				new String[] {ContactsContract.Contacts.DISPLAY_NAME},
				new int[] {R.id.display_name});
		
		setListAdapter(mAdapter);
		getListView().setFastScrollEnabled(true);
		
		mSearchEditText = (EditText) getActivity().findViewById(R.id.search_edittext);
		mSearchEditText.addTextChangedListener(mtextWatcher);
	}
	
	private TextWatcher mtextWatcher = new TextWatcher() {  
         
	        @Override    
	        public void afterTextChanged(Editable s) {     
	        }   
	          
	        @Override 
	        public void beforeTextChanged(CharSequence s, int start, int count,  
	                int after) {  
	        }  
	 
	         @Override    
	        public void onTextChanged(CharSequence s, int start, int before,     
	                int count) {
	        	
	        	 search();          
	        }                    
	    }; 
	    
	 private void search() {
		Bundle bundle = new Bundle(1);
		bundle.putString("keyword", mSearchEditText.getText().toString());
		getLoaderManager().restartLoader(0, bundle, this);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		/* Retrieving the phone numbers in order to see if we have more than one */
		String phoneNumber = null;
		String name = null;
		
		String[] projection = new String[] {Phone.DISPLAY_NAME, Phone.NUMBER};
    	final Cursor phoneCursor = getActivity().getContentResolver().query(
			Phone.CONTENT_URI,
			projection,
			Data.CONTACT_ID + "=?",
			new String[]{String.valueOf(id)},
			null);
    	
    	if(phoneCursor.moveToFirst() && phoneCursor.isLast()) {
    		final int contactNumberColumnIndex 	= phoneCursor.getColumnIndex(Phone.NUMBER);    			
   			phoneNumber = phoneCursor.getString(contactNumberColumnIndex);
   			name = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.DISPLAY_NAME));
    	}
		
    	if (phoneNumber != null){    		  		
    		mContactsListener.onContactNumberSelected(phoneNumber, name);
    	}
    	else {
    		mContactsListener.onContactNameSelected(id);
    	}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {			
			mContactsListener = (OnContactSelectedListener) activity;
		} catch (ClassCastException	e) {
			throw new ClassCastException(activity.toString() + " must implement OnContactSelectedListener");
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Uri baseUri;
		
		if (mCurrentFilter != null) {
            baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
                    Uri.encode(mCurrentFilter));
        } else {
            baseUri = Contacts.CONTENT_URI;
        }
		
	String selection = null;
		String[] selectionArgs = null;
		if (null == arg1) {
			selection = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
					+ Contacts.HAS_PHONE_NUMBER + "=1) AND ("
					+ Contacts.DISPLAY_NAME + " != '' ))";
		} else {
			selection = "(sort_key LIKE ? OR " + Contacts.DISPLAY_NAME
					+ " LIKE ? ) AND " + "((" + Contacts.DISPLAY_NAME
					+ " NOTNULL) AND (" + Contacts.HAS_PHONE_NUMBER
					+ "=1) AND (" + Contacts.DISPLAY_NAME + " != '' ))";
			selectionArgs = new String[] {
					"%" + arg1.getString("keyword") + "%",
					"%" + arg1.getString("keyword") + "%" };
		}
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		return new CursorLoader(getActivity(), baseUri,
				CONTACTS_SUMMARY_PROJECTION, selection, selectionArgs,
				sortOrder);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	class IndexedListAdapter extends SimpleCursorAdapter implements SectionIndexer{

		AlphabetIndexer alphaIndexer;
		
		public IndexedListAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to, 0);		
		}

		@Override
		public Cursor swapCursor(Cursor c) {
			if (c != null) {
				alphaIndexer = new AlphabetIndexer(c,
						c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME),
						" ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			}

			return super.swapCursor(c);
		}
		
		@Override
		public int getPositionForSection(int section) {
			return alphaIndexer.getPositionForSection(section);
		}

		@Override
		public int getSectionForPosition(int position) {
			return alphaIndexer.getSectionForPosition(position);
		}

		@Override
		public Object[] getSections() {
			return alphaIndexer == null ? null : alphaIndexer.getSections();
		}
	
	}



}
