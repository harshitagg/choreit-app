package com.pineapps.choreit.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.pineapps.choreit.R;

public class AddUserActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.Data.DATA1,
                    ContactsContract.Data.DATA15
            };
    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Photo.PHOTO
    };
    @SuppressLint("InlinedApi")
    private static final String SELECTION =
            ContactsContract.CommonDataKinds.Email.ADDRESS + " LIKE ? " + "AND " +
                    ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'";

    private String searchString = new String("");
    private String[] selectionArgs = {searchString};

    private final static int[] TO_IDS = {
            R.id.user_entry,
            R.id.user_email,
            R.id.user_thumb
    };

    ListView contactsList;
    private SimpleCursorAdapter simpleCursorAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list_view);
        getLoaderManager().initLoader(0, null, this);
        contactsList = (ListView) this.findViewById(R.id.contacts_list);
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.contacts_list_item, null, FROM_COLUMNS, TO_IDS, 0);
        simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (view.getId() == R.id.user_thumb) {
                    return true;
                }
                return false;
            }
        });
        contactsList.setAdapter(simpleCursorAdapter);
        contactsList.setOnItemClickListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        selectionArgs[0] = "%" + searchString + "%";
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI, PROJECTION, SELECTION, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, android.database.Cursor cursor) {
        simpleCursorAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        simpleCursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View item, int position, long rowID) {
    }
}