package com.pineapps.choreit.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.pineapps.choreit.ChoreItContext;
import com.pineapps.choreit.R;
import com.pineapps.choreit.service.UserService;

public class AddUserActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private UserService userService;
    private Context curContext;
    private LayoutInflater inflater;
    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1,
            ContactsContract.CommonDataKinds.Photo.PHOTO_URI
    };
    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Photo.PHOTO_URI
    };
    @SuppressLint("InlinedApi")
    private static final String SELECTION =
            "( " + ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ? " +
                    "OR " + ContactsContract.CommonDataKinds.Email.ADDRESS + " LIKE ? )" +
                    "AND " + ContactsContract.Data.MIMETYPE + " = '" +
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'";
    private final static int[] TO_IDS = {
            R.id.user_entry,
            R.id.user_email,
            R.id.user_thumb
    };

    private String searchString = "";
    private String[] selectionArgs = {searchString};

    private ListView contactsList;
    private SearchView userSearch;
    private SimpleCursorAdapter userListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curContext = this;
        inflater = this.getLayoutInflater();

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.action_add_user);
        setContentView(R.layout.contacts_list_view);

        ChoreItContext choreItContext = ChoreItContext.getInstance();
        userService = choreItContext.userService();

        getLoaderManager().initLoader(0, null, this);

        contactsList = (ListView) this.findViewById(R.id.contacts_list);
        userSearch = (SearchView) this.findViewById(R.id.user_search);

        initUserListAdapter();
        initContactList();
        initUserSearch();

    }

    private void initUserSearch() {
        userSearch.setIconifiedByDefault(false);
        userSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    contactsList.clearTextFilter();
                } else {
                    userListAdapter.getFilter().filter(s);
                    View emptyView = contactsList.getEmptyView();
                    TextView emptyUserName = (TextView) emptyView.findViewById(R.id.empty_user_entry);
                    TextView emptyUserEmail = (TextView) emptyView.findViewById(R.id.email_hint);
                    emptyUserName.setText(s);
                    emptyUserEmail.setText("Tap to add email for \"" + s + "\"");
                    emptyUserEmail.setTextColor(Color.GRAY);
                }
                return true;
            }
        });
    }

    private void initUserListAdapter() {
        userListAdapter = new SimpleCursorAdapter(this, R.layout.contacts_list_item, null, FROM_COLUMNS, TO_IDS, 0);
        userListAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnId) {
                if (view.getId() == R.id.user_thumb) {
                    ImageView image = (ImageView) view;
                    String str = cursor.getString(columnId);
                    if (str != null) {
                        image.setImageURI(Uri.parse(str));
                    }
                    return true;
                }
                return false;
            }
        });
        userListAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                searchString = charSequence.toString();
                selectionArgs[0] = "%" + searchString + "%";
                return getContentResolver().query(ContactsContract.Data.CONTENT_URI, PROJECTION, SELECTION, selectionArgs, null);
            }
        });
    }

    private void initContactList() {
        final Activity activity = this;
        contactsList.setEmptyView(findViewById(R.id.empty_user));
        View emptyView = contactsList.getEmptyView();
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView userNameView = (TextView) view.findViewById(R.id.empty_user_entry);
                final String newUserName = userNameView.getText().toString();
                View dialogView = inflater.inflate(R.layout.dialog_email, null);
                final EditText userEmailView = (EditText) dialogView.findViewById(R.id.empty_user_email);
                AlertDialog.Builder confirmationPrompt = new AlertDialog.Builder(curContext);
                confirmationPrompt.setTitle("Enter email for \"" + newUserName + "\"");
                confirmationPrompt.setView(dialogView);
                confirmationPrompt.setPositiveButton("Add User", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newUserEmail = userEmailView.getText().toString();
                        userService.addUser(newUserName, newUserEmail);
                        Toast.makeText(getApplicationContext(), "New User Added", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }

                });
                confirmationPrompt.setNegativeButton("Discard", null);
                AlertDialog dialog = confirmationPrompt.create();
                dialog.show();
            }
        });
        contactsList.setAdapter(userListAdapter);
        contactsList.setTextFilterEnabled(true);
        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView userNameView = (TextView) view.findViewById(R.id.user_entry);
                TextView userEmailView = (TextView) view.findViewById(R.id.user_email);
                final String userName = userNameView.getText().toString();
                final String userEmail = userEmailView.getText().toString();
                AlertDialog.Builder confirmationPrompt = new AlertDialog.Builder(curContext);
                confirmationPrompt.setTitle(R.string.action_add_user_confirmation);
                confirmationPrompt.setMessage(R.string.confirm_add_user);
                confirmationPrompt.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userService.addUser(userName, userEmail);
                        Toast.makeText(getApplicationContext(), "New User Added", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }

                });
                confirmationPrompt.setNegativeButton(R.string.no, null);
                AlertDialog dialog = confirmationPrompt.create();
                dialog.show();
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        selectionArgs[0] = "%" + searchString + "%";
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI, PROJECTION, SELECTION, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, android.database.Cursor cursor) {
        userListAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        userListAdapter.swapCursor(null);
    }
}