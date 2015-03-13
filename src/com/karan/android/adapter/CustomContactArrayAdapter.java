package com.karan.android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.karan.android.R;
import com.karan.android.model.ContactsItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@SuppressLint("DefaultLocale")
public class CustomContactArrayAdapter extends ArrayAdapter<ContactsItem> implements Filterable, SectionIndexer {

	private List<ContactsItem> mContactsItems;
	private List<ContactsItem> mOriginalContactsList;
	private LayoutInflater mInflater;
	private CustomContactFilter mCustomContactFilter;
	private HashMap<String, Integer> mMapIndex;
	private String[] mSections;
	
	String TAG = getClass().getSimpleName();

	public CustomContactArrayAdapter(Context context) {
		super(context, R.layout.contact_item);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMapIndex = new LinkedHashMap<String, Integer>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view;
		ViewHolderItem viewHolderItem;
		Log.i(TAG, "+++ In getView +++");
		if (convertView == null) {
			Log.i(TAG, "+++ Initializing View for the First Time +++");
			view = mInflater.inflate(R.layout.contact_item, parent, false);
			viewHolderItem = new ViewHolderItem();
			viewHolderItem.mContactNameTextView = (TextView) view.findViewById(R.id.nameValueTxtView);
			viewHolderItem.mContactNumberTextView = (TextView) view.findViewById(R.id.contactNumberValueTxtView);
			viewHolderItem.mEmailTextView = (TextView) view.findViewById(R.id.emailValueTxtView);
			viewHolderItem.mProfileImageView = (ImageView) view.findViewById(R.id.profileImageView);
			view.setTag(viewHolderItem);

		} else {
			view = convertView;
			viewHolderItem = (ViewHolderItem) view.getTag();
			//view.setTag(viewHolderItem);
		}

		ContactsItem item = getItem(position);
		viewHolderItem.mContactNameTextView.setText(item.mContactName);
		viewHolderItem.mContactNumberTextView.setText(item.mContactNumber);
		viewHolderItem.mEmailTextView.setText(item.mEmail);

		return view;
	}

	@Override 
	public ContactsItem getItem( int position ) {
		return mOriginalContactsList.get(position);
	}

	@Override
	public int getCount() {
		if( mOriginalContactsList != null ) {
			return mOriginalContactsList.size();
		}
		return 0;
	}

	public class ViewHolderItem {
		TextView mContactNameTextView;
		TextView mContactNumberTextView;
		TextView mEmailTextView;
		ImageView mProfileImageView;
	}

	public void setData( List<ContactsItem> contactsItem ) {
		clear();
		Log.i(TAG, "+++ Initializing Data +++");
		mContactsItems = contactsItem;
		mOriginalContactsList = contactsItem;
		
		String character = null;
		if( mOriginalContactsList == null ) {
			return;
		}
		for( int index = 0; index < mOriginalContactsList.size(); index++ ) {
			character = mOriginalContactsList.get(index).toString().substring(0, 1).toUpperCase(Locale.US);
			mMapIndex.put(character, index);
		}
		
		Set<String> sectionLetters = mMapIndex.keySet();
		 
        // create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
 
        Log.d("sectionList", sectionList.toString());
        Collections.sort(sectionList);
 
        mSections = new String[sectionList.size()];
 
        sectionList.toArray(mSections);
	}

	@Override
	public Filter getFilter() {
		if( mCustomContactFilter == null ) {
			mCustomContactFilter = new CustomContactFilter();
		}
		return mCustomContactFilter;
	}

	/**
	 * Custom Filter implementation
	 * */
	public class CustomContactFilter extends Filter {

		@Override
		protected FilterResults performFiltering( CharSequence searchFilter ) {
			FilterResults filterResults = new FilterResults();
			searchFilter = searchFilter.toString().toLowerCase();
			
			ArrayList<ContactsItem> originalList = (ArrayList<ContactsItem>) mContactsItems;
			if( originalList == null ) 
				return filterResults;
			
			if( searchFilter == null || searchFilter.length() == 0 ) {
				filterResults.values = originalList;
				filterResults.count = originalList.size();
			}
			else {
				ArrayList<ContactsItem> FilteredContactsItems = new ArrayList<ContactsItem>();
				ContactsItem contactsItem;
				for ( int index = 0; index < originalList.size(); index++ ) {
					contactsItem = originalList.get(index);
					if( contactsItem.toString().toLowerCase().contains(searchFilter) ) {
						FilteredContactsItems.add(contactsItem);
					}
				}
				filterResults.values = FilteredContactsItems;
				filterResults.count = FilteredContactsItems.size();
				
				
			}
			return filterResults;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence sequence, FilterResults results) {
			Log.d(TAG, "PublishResults : " + sequence ); 
			mOriginalContactsList = (List<ContactsItem>) results.values;
			if( results.count == 0 ) 
				notifyDataSetInvalidated();
			
			else {
				
				notifyDataSetChanged();
			}
		} 

	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		Log.d(TAG, "Position for Selection : "+ sectionIndex );
		return mMapIndex.get(mSections[sectionIndex]);
	}

	@Override
	public int getSectionForPosition(int position) {
		Log.d(TAG, "Section for Position : "+ position);
		return 0;
	}

	@Override
	public String[] getSections() {
		return mSections;
	}

}
