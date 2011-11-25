package com.codinguser.android.contactpicker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;

public class ContactsListFragment extends ListFragment {

	private Cursor mCursor;
	private OnContactSelectedListener mContactsListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contacts_list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ContentResolver resolver = getActivity().getContentResolver();
		
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		
		String[] projection = new String[] {
				Contacts._ID, 
				Contacts.DISPLAY_NAME, 
				Contacts.HAS_PHONE_NUMBER
		};
		
		String selection = Contacts.HAS_PHONE_NUMBER + "= '1'";
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		mCursor = resolver.query(uri, 
				projection, 
				selection,
				null, 
				sortOrder);
		
		getActivity().startManagingCursor(mCursor);
		
		ListAdapter adapter = new IndexedListAdapter(
				this.getActivity(),
				R.layout.list_item_contacts,
				mCursor,
				new String[] {ContactsContract.Contacts.DISPLAY_NAME},
				new int[] {R.id.display_name});
		
		setListAdapter(adapter);
		getListView().setFastScrollEnabled(true);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mContactsListener.onContactNameSelected(id);
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

	class IndexedListAdapter extends SimpleCursorAdapter implements SectionIndexer{

		AlphabetIndexer alphaIndexer;
		
		public IndexedListAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			alphaIndexer = new AlphabetIndexer(c, c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME), "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
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
			return alphaIndexer.getSections();
		}
		
		
		
		
	}
}
