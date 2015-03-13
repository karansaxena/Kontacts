package com.karan.android.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.karan.android.fragments.ContactListFragment;
import com.karan.android.R;
import com.karan.android.fragments.SearchFragment;

public class MainActivity extends FragmentActivity implements SearchFragment.onSearchListener {

	ContactListFragment mContactsListFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContactsListFragment = getContactListFragment();
	}

	private ContactListFragment getContactListFragment() {
		
		if( mContactsListFragment == null ) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			mContactsListFragment = (ContactListFragment)
	                fragmentManager.findFragmentById(R.id.contactListFragment);
			
		}
		return mContactsListFragment;
	}

	/**
	 * SearchFragment.onSearchListener
	 * */
	@Override
	public void queryByString(String searchString) {
		
		mContactsListFragment = getContactListFragment();
		/**
		 * One pane fragment - 2 Fragments are in same screen
		 * */
		if( mContactsListFragment != null ) {
			mContactsListFragment.searchForContact(ContactListFragment.SEARCH_QUERY_MODE, searchString);
		}
			
	}
	
	/**
	 * SearchFragment.onSearchListener
	 * */
	@Override
	public void queryByAlphabet(String alphabet) {
		
		mContactsListFragment = getContactListFragment();
		/**
		 * One pane fragment - 2 Fragments are in same screen
		 * */
		if( mContactsListFragment != null ) {
			mContactsListFragment.searchForContact(ContactListFragment.SEARCH_ALPHABET_MODE, alphabet);
		}
		
	}

	
}