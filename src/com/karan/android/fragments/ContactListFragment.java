package com.karan.android.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.karan.android.R;
import com.karan.android.activity.DetailActivity;
import com.karan.android.adapter.CustomContactArrayAdapter;
import com.karan.android.contactutils.ContactsListLoader;
import com.karan.android.model.ContactsItem;

import java.util.List;

public class ContactListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<ContactsItem>> {

    public static final int SEARCH_QUERY_MODE = 1;
    public static final int SEARCH_ALPHABET_MODE = 2;

    private String TAG = ContactListFragment.class.getSimpleName();

    private CustomContactArrayAdapter mAdapter;
    private String mCurrentFilter;
    private List<ContactsItem> mContactsItems;

    // The Loader's id (this id is specific to the ListFragment's LoaderManager)
    private static final int LOADER_ID = 1;

    ListView mContactsListView;
    String[] mSections;

    /**
     * Default Constructor for Fragment
     */
    public ContactListFragment() {

    }

    ProgressDialog mProgressDialog = null;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        mAdapter = new CustomContactArrayAdapter(getActivity());
        mProgressDialog = new ProgressDialog(getActivity());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ContactsItem clickedContacts = (ContactsItem)
                getListAdapter().getItem(position);
        Intent gotoDetail = new Intent(getActivity(), DetailActivity.class);
        gotoDetail.putExtra("name", clickedContacts.getmContactName());
        gotoDetail.putExtra("phone", clickedContacts.getmContactNumber());
        gotoDetail.putExtra("email", clickedContacts.getmEmail());
        startActivity(gotoDetail);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        setListAdapter(mAdapter);

        Log.i(TAG, "+++ Calling initLoader() +++");
        if (getLoaderManager().getLoader(LOADER_ID) == null) {
            Log.i(TAG, "+++ Initializing the new Loader... +++");
        } else {
            Log.i(TAG, "+++ Reconnecting with existing Loader (id '1')... +++");
        }

        getLoaderManager().initLoader(LOADER_ID, null, this);
        mProgressDialog.setTitle("Contacts App");
        mProgressDialog.setMessage("Fetching Contacts...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View view = inflater.inflate(R.layout.contact_list_fragment, container, false);
        //mContactsListView = (ListView) view.findViewById(android.R.id.list);
        return view;
    }


    int mSearchMode = 0;

    public void searchForContact(int mode, String searchString) {

        mCurrentFilter = searchString;
        if (mCurrentFilter.equals("") || mCurrentFilter.length() == 0)
            resetListViewToDefaultPosition();
        //TODO
        switch (mode) {

            case SEARCH_ALPHABET_MODE:
                mSearchMode = SEARCH_ALPHABET_MODE;
                Log.d(TAG, "Search_Alphabet : " + mCurrentFilter);
                scrollToAlphabet(mCurrentFilter);
                break;

            case SEARCH_QUERY_MODE:
                mSearchMode = SEARCH_QUERY_MODE;
                Log.d(TAG, "Search_Query : " + mCurrentFilter);
                searchByQuery(mCurrentFilter);
                break;

        }
    }

    private void resetListViewToDefaultPosition() {

        mAdapter.setData(mContactsItems);
        getListView().setSelection(0);

    }

    private void scrollToAlphabet(String alphabetFilter) {

        if (alphabetFilter.equals("") == false || alphabetFilter.length() != 0) {
            mSections = mAdapter.getSections();
            for (int index = 0; index < mSections.length; index++) {
                if (mSections[index].equals(alphabetFilter)) {
                    getListView().setSelection(mAdapter.getPositionForSection(index - 1) + 1);
                }
            }
        }

    }

    String mPrevQuery = null;

    private void searchByQuery(String searchQuery) {

        Log.d(TAG, "Initializing Search on : " + searchQuery);

        if (mPrevQuery == null)
            mPrevQuery = searchQuery;

        if (searchQuery.equals("") == false || searchQuery.length() != 0) {

            if (mPrevQuery.equals(searchQuery) == false) {
                mPrevQuery = searchQuery;
                mAdapter.getFilter().filter(searchQuery.trim());
            }
        }

    }

    @Override
    public Loader<List<ContactsItem>> onCreateLoader(int id, Bundle bundle) {
        Log.i(TAG, "+++ onCreateLoader() +++");
        return new ContactsListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<ContactsItem>> loader,
                               List<ContactsItem> contactItems) {
        Log.i(TAG, "+++ onLoadFinished() +++");
        mContactsItems = contactItems;
        mAdapter.setData(contactItems);
        mAdapter.notifyDataSetChanged();
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ContactsItem>> contactItems) {
        Log.i(TAG, "+++ onLoadReset() +++");
        mAdapter.setData(null);
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
